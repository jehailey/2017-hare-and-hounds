package com.oose2017.jlee562.hareandhounds;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GameService {

    private final Logger logger = LoggerFactory.getLogger(GameService.class);
    private HashMap<String, Game> gameList = new HashMap<>();
    private int numGames = 1;

    /**
     * Initialize HashMap of Games
     *
     * @param gameList the list of games initialized by Game Controller
     */
    public GameService(HashMap<String, Game> gameList) throws GameServiceException{
        this.gameList = gameList;
    }

    /**
     * Create a new Game entry.
     *
     * @param body the contents sent from the user's front end.
     *              It contains the starting player's piece type.
     * @return a Render object with the response Code and message
     */
    public Render createNewGame(String body) throws GameServiceException {

        Render render = new Render();

        // when there exists empty body request
        if (body.isEmpty()){
            render.Code = 400;
            render.Message = Collections.emptyMap();
            return render;
        }

        // create a player to contain information about pieceType
        Player player = new Gson().fromJson(body, Player.class);

        // check for invalid pieceType input and return
        if (!(player.getPieceType().equals("HOUND") || player.getPieceType().equals("HARE"))){
            render.Code = 400;
            render.Message = Collections.emptyMap();
            return render;
        }

        // set corresponding gameID and playerID
        String gameId =  Integer.toString(this.numGames);
        player.setGameId(gameId);
        player.setPlayerId(String.valueOf(this.numGames) + "_p1");

        // create new game
        Game game = new Game(gameId, player.getPieceType());
        logger.info("New Game has successfully created! ");
        this.gameList.put(gameId, game);
        this.numGames++;

        // return Render object of SUCCESS
        render.Code = 201;
        render.Message = player;
        return render;
    }

    /**
     * Join an existing Game
     *
     * @param gameId the unique ID of which game to Join
     * @return a Render object with the response Code and message
     */
    public Render joinGame(String gameId) {

        Render render = new Render();

        // if game does not EXIST, join invalid game, return ERROR code
        if (! gameList.containsKey(gameId)){
            render.Code = 404;
            render.Message = Collections.emptyMap();
            return render;
        }

        Game game = gameList.get(gameId);

        // check if player 2 has already joined the game
        if (game.getPlayer2().getPieceType().equals("unknown")){
            // Knowing which piece player1 has taken, set corresponding piece type for player 2
            if (game.getPlayer1().getPieceType().equals("HARE")){
                game.getPlayer2().setPieceType("HOUND");
            }else if(game.getPlayer1().getPieceType().equals("HOUND")) {
                game.getPlayer2().setPieceType("HARE");
            }
            render.Code = 200;
            render.Message = game.getPlayer2();
            logger.info("Friend Joined me on the Game! ");
            game.getBoard().changeState("TURN_HOUND");      // set the first state of game as "TURN_HOUND"
        }else{
            render.Code = 410;
            render.Message = Collections.emptyMap();
        }
        return render;
    }


    /**
     * Describe the board using Pieces
     *
     * @param gameId the ID of board to give descriptions of pieces
     * @return  a Render object with the response Code and message
     */
    public Render describeBoard(String gameId){

        Render render = new Render();

        // if game does not EXIST, join invalid game, return ERROR code
        if (! gameList.containsKey(gameId)){
            render.Code = 404;                                      // response code 404, fail
            render.Message = Collections.emptyMap();
            return render;
        }

        Game game = gameList.get(gameId);

        // get current piece list of the board
        Board board = game.getBoard();
        ArrayList <Piece> pieceList = board.allPieces();
        render.Code = 200;
        render.Message = pieceList;
        return render;
    }


    /**
     * Describe the state
     *
     * @param gameId the ID of board to give current state
     * @return a Render obejct with the response Code and message
     */
    public Render describeState(String gameId){

        Render render = new Render();

        // if game does not EXIST, join invalid game, return ERROR code
        if (! gameList.containsKey(gameId)){
            render.Code = 404;                                      // response code 404, fail
            render.Message = Collections.emptyMap();
            return render;
        }

        Game game = gameList.get(gameId);

        // get current state of board
        Board board = game.getBoard();
        State state = board.getState();
        render.Code = 200;
        render.Message = state;
        logger.info("Current state for game " + gameId + " is "+ state);
        return render;
    }


    /**
     * Play the Game
     *
     * @param gameId body of response that carries information of which piece to move where
     * @return a Render object with the response Code and message
     */
    public Render playGame(String gameId, String body){

        Render render = new Render();
        Move move = new Gson().fromJson(body, Move.class);

        // If game does not EXIST, join invalid game, return INVALID_GAME_ID
        if (!gameList.containsKey(gameId)){
            render.Code = 404;                                      // response code 404, fail
            render.Message = "INVALID_GAME_ID";
            return render;
        }
        Game game = gameList.get(gameId);

        // If playerID is invalid, return INVALID_PLAYER_ID
        Board board = game.getBoard();
        Player player;
        player = findPlayer(move.playerId, game);
        if(player == null){
            render.Code = 404;
            render.Message = "INVALID_PLAYER_ID";
            return render;
        }

        // If the turn is invalid, return INVALID_TURN
        boolean  valid = checkValidTurn(player, board.getState());
        if (!valid){
            render.Code = 422;
            render.Message = "INCORRECT_TURN";
            logger.info("PlayerID is " + move.playerId + ", but State is " + board.getState());
            return render;
        }

        // Make all move positions integers
        int fromX = Integer.valueOf(move.fromX);
        int fromY = Integer.valueOf(move.fromY);
        int toX = Integer.valueOf(move.toX);
        int toY = Integer.valueOf(move.toY);

        // If try to move a piece that is not hound nor hare
        if (board.findPiece(fromX, fromY) == null){
            render.Code = 422;
            render.Message = "ILLEGAL_MOVE";

        // If the piece is legal to move to (toX, toY), move the piece, change the Turn, and return SUCCESS
        } else if(board.findPiece(fromX, fromY).isLegal(toX, toY) && !board.presentPiece(toX, toY)){
            board.findPiece(fromX, fromY).move(toX, toY);
            logger.info(board.findPiece(toX, toY).getPieceType() + " moved to (" + board.findPiece(toX, toY).getX()
                    + "," + board.findPiece(toX, toY).getY() + ")");
            render.Code = 200;
            render.Message = move.playerId;
            board.changeTurn();
        }else{   // If the piece is illegal to move, return ILLEGAL MOVE
            render.Code = 422;
            render.Message = "ILLEGAL_MOVE";
        }

        return render;
    }


    //-----------------------------------------------------------------------------//
    // Helper Classes and Methods
    //-----------------------------------------------------------------------------//

    /**
     * find the player with playerId, and game info.
     *
     * @param playerId  the player ID
     * @param game  the information of game
     *
     * @return a Render object with the response Code and message
     */
    private Player findPlayer(String playerId, Game game){
        // check playerID and return the correct player
        if(game.getPlayer1().getPlayerId().equals(playerId)){
            return game.getPlayer1();
        }else if (game.getPlayer2().getPlayerId().equals(playerId)){
            return game.getPlayer2();
        }else{
            return null;
        }
    }

    /**
     * check whether the turn is a valid one based on state
     *
     * @param player  player object containing information
     * @param state  state object containing information
     *
     * @return validity of turn
     */
    private boolean checkValidTurn(Player player, State state){

        logger.info("State = "+ state.toString() + " piece = " + player.getPieceType());

        // if pieceType and TURN_PIECETYPE does not match, then return false
        if (state.toString().equals("TURN_HOUND")){
            if (!player.getPieceType().equals("HOUND")){
                return false;
            }
        }else if (state.toString().equals("TURN_HARE")) {
            if (!player.getPieceType().equals("HARE")) {
                return false;
            }
        }else{      // if the state is not TURN, then return false
            return false;
        }
        return true;
    }

    public static class GameServiceException extends Exception {
        public GameServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }


}