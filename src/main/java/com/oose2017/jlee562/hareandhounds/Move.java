package com.oose2017.jlee562.hareandhounds;


/* to contain data from PlayGame body response */
public class Move {

    public String playerId;
    public String fromX;
    public String fromY;
    public String toX;
    public String toY;

    @Override
    public String toString(){
        return "Move = { playerId: " + this.playerId + "}";
    }
}
