package com.oose2017.jlee562.hareandhounds;

/*
 * Game Class
 *
 */
public class Game{

    private String gameId;
    private Player player1;
    private Player player2;
    private Board board;

    public Game(String gameId, String pieceType) {
        this.gameId = gameId;
        this.player1 = new Player(gameId, gameId + "_p1", pieceType);
        this.player2 = new Player(gameId, gameId + "_p2", "unknown");
        this.board = new Board();
    }

    public Player getPlayer1() {
        return this.player1;
    }

    public Player getPlayer2() {
        return this.player2;
    }

    public Board getBoard(){ return this.board; }

    @Override
    public String toString() {
        return "Game {" +
                "gameId='" + this.gameId + '\'' +
                ", player1='" + this.player1.toString() + '\'' +
                ", player2=" + this.player2.toString() + '\'' +
                ", board=" + this.board.toString() +
                '}';
    }
}
