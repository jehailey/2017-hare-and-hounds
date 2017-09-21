package com.oose2017.jlee562.hareandhounds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;

public class Board{

    private Piece p1;
    private Piece p2;
    private Piece p3;
    private Piece p4;
    private State state;
    private HashMap<ArrayList<Piece>, Integer> snapShots = new HashMap<>();
    private ArrayList<Piece> pieces = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(Board.class);

    public Board() {    // initialize board with four pieces and state of waiting for second player
        this.p1 = new Piece("HOUND", 1,0);
        this.p2 = new Piece("HOUND", 0,1 );
        this.p3 = new Piece("HOUND", 1, 2);
        this.p4 = new Piece("HARE", 4, 1);
        this.state = new State("WAITING_FOR_SECOND_PLAYER");
        ArrayList<Piece> first = createPieceList();
        this.snapShots.put(first, 1);
    }

    /* get the state of Board */
    public State getState(){
        return this.state;
    }

    /**
     * check whether the piece exists at (to_x, to_y) position
     * @param to_x  the x position to move to
     * @param to_y  the y position to move to
     * @return  true if exists, false if doesn't exist
     */
    public Boolean presentPiece(int to_x, int to_y){
        if (to_x == this.p1.getX() && to_y == this.p1.getY()){ return true;}
        if (to_x == this.p2.getX() && to_y == this.p2.getY()){ return true;}
        if (to_x == this.p3.getX() && to_y == this.p3.getY()){ return true;}
        if (to_x == this.p4.getX() && to_y == this.p4.getY()){ return true;}
        return false;
    }

    /**
     * find piece with (x,y) position and return piece
     * @param x the x position of piece
     * @param y the y position of piece
     * @return  the Piece, null if doesn't exist
     */
    public Piece findPiece(int x, int y){
        if (x == this.p1.getX() && y == this.p1.getY()){ return p1;}
        if (x == this.p2.getX() && y == this.p2.getY()){ return p2;}
        if (x == this.p3.getX() && y == this.p3.getY()){ return p3;}
        if (x == this.p4.getX() && y == this.p4.getY()){ return p4;}
        return null;
    }


    /* change the state of Board */
    public void changeState(String state){ this.state.setState(state);}


    /**
     * change the turn of Hound to Hare and vice versa
     *        update the snapshot of board for changes
     *        also check for the win, and change to WIN state
     */
    public void changeTurn(){
        // store board / snapshots
        updateSnapshots();
        if (this.state.toString().equals("TURN_HARE")){
            if(winHareEscape()){
                this.state.setState("WIN_HARE_BY_ESCAPE");
            }else{
                this.state.changeTurn();
            }
        }else if (this.state.toString().equals("TURN_HOUND")){
            if(winHound()){
                this.state.setState("WIN_HOUND");
            }else if (winHareStall()){
                this.state.setState("WIN_HARE_BY_STALLING");
            }else{
                this.state.changeTurn();
            }
        }
    }

    /**
     * return list of current pieces as array list
     * @return  list of pieces
     */
    public ArrayList<Piece> allPieces(){
        this.pieces.add(this.p1);
        this.pieces.add(this.p2);
        this.pieces.add(this.p3);
        this.pieces.add(this.p4);
        return pieces;
    }


    //-----------------------------------------------------------------------------//
    // Helper Classes and Methods
    //-----------------------------------------------------------------------------//

    /**
     * update Snapshot of board  Every Turn
     */
    private void updateSnapshots(){     // get and update current position of the board after turn
        logger.info("Updating Snapshots");
        ArrayList<Piece> current = createPieceList();
        checkPositionExists(current);
    }

    /**
     * Create new piece list to store the snapshot of board
     *
     * @return a new snapshot of board
     */
    private ArrayList<Piece> createPieceList(){
        ArrayList<Piece> tempList = new ArrayList<>();
        Piece temp1 = new Piece(this.p1.getPieceType(), this.p1.getX(), this.p1.getY());
        Piece temp2 = new Piece(this.p2.getPieceType(), this.p2.getX(), this.p2.getY());
        Piece temp3 = new Piece(this.p3.getPieceType(), this.p3.getX(), this.p3.getY());
        Piece temp4 = new Piece(this.p4.getPieceType(), this.p4.getX(), this.p4.getY());
        tempList.add(temp1);
        tempList.add(temp2);
        tempList.add(temp3);
        tempList.add(temp4);
        return tempList;
    }

    /**
     * check whether Position exists in snapShots
     *
     * @param current current Array list of pieces
     */
    private void checkPositionExists(ArrayList<Piece> current){
        // Iterate over snapShots and get the value set.
        for (HashMap.Entry<ArrayList<Piece>, Integer> entry : this.snapShots.entrySet()) {
            ArrayList<Piece> positions = entry.getKey();
            // if same position occurred, then increment frequency
            if (equalPieceList(positions, current)) {
                int freq = entry.getValue() + 1;
                logger.info("Updating frequency to " + freq);
                snapShots.put(positions, freq);
                return;
            }
        }
        // add new position to the board if doesn't exist
        logger.info("New position of board " + pieceListString(current));
        snapShots.put(current, 1);
    }

    /**
     * compare two pieceLists and if all four of the pieces are at the same position
     *
     * @param firstList
     * @param secondList
     * @return  return true if 4 equal positions, return false if not.
     */
    private boolean equalPieceList(ArrayList<Piece> firstList, ArrayList<Piece> secondList){
        int equalPosition = 0;

        for (Piece piece1: firstList){
            for(Piece piece2: secondList){
                if (piece1.equals(piece2)){ // if same pieceType at the same position
                    equalPosition++;
                }
            }
        }
        if(equalPosition == 4){    // if all 4 pieces are at the same position
            logger.info("Equal pieces exist " + pieceListString(firstList));
            logger.info("Equal pieces exist " + pieceListString(secondList));
            return true;
        }
        return false;
    }

    /**
     * Print piece list
     *
     * @param list
     * @return String of all pieces
     */
    private String pieceListString(ArrayList<Piece> list){
        String s = "";
        for (Piece p: list){
            s += "piece " + p.getPieceType() + " :(" + p.getX() + "," + p.getY() + ")  ";
        }
        return s;
    }

    /**
     * check if Hound won the game, check whether the hare can have adjacent spaces to move
     *
     * @return true if Hound won, false if not
     */
    private boolean winHound(){
        int possible_moves = 0;
        int hare_x = this.p4.getX();
        int hare_y = this.p4.getY();
        logger.info("checking for winHound");

        // check all possible moves of Hare if they are legal
        for (int x = hare_x - 1; x < hare_x + 2; x = x + 1){
            for (int y = hare_y - 1; y < hare_y + 2; y = y + 1){
                if (this.p4.isLegal(x,y) && !presentPiece(x, y)){
                    possible_moves++;
                    logger.info("possible moves " + x  + ", " + y);
                }
            }
        }
        if (possible_moves == 0){ return true;}
        else{ return false; }
    }

    /**
     * Check if Hare won the game by sneak passing
     *
     * @return true if Hare escaped
     */
    private boolean winHareEscape(){
        int hareX = this.p4.getX();
        int hound1X = this.p1.getX();
        int hound2x = this.p2.getX();
        int hound3x = this.p3.getX();

        // if all the hounds are at the same x coordinate or greater x coordinate than hare
        //      then, Hare can sneak passing.
        return ((hareX <= hound1X) && (hareX <= hound2x) && (hareX <= hound3x));
    }

    /* check if Hare won the game by stall */

    /**
     * Check if same positions occur three times or move, then Hare win by stall
     *
     * @return  true if Hare win by stall
     */
    private boolean winHareStall(){
        // Iterate over all possible positions
        for (HashMap.Entry<ArrayList<Piece>, Integer> entry : snapShots.entrySet()) {
            Integer freq = entry.getValue();
            if (freq >= 3){     // if appeared more than 3 times, then stall
                return true;
            }
        }
        return false;
    }


}
