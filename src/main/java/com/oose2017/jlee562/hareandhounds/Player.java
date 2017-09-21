package com.oose2017.jlee562.hareandhounds;

public class Player {

    private String playerId;
    private String gameId;
    private String pieceType;

    public Player(String gameId, String playerId, String pieceType) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.pieceType = pieceType;
    }

    public String getPlayerId(){
        return this.playerId;
    }

    public String getPieceType(){
        return this.pieceType;
    }

    public void setGameId(String gameId){ this.gameId = gameId;}

    public void setPlayerId(String playerId){ this.playerId = playerId;}

    public void setPieceType(String pieceType){
        this.pieceType = pieceType;
    }

    @Override
    public String toString() {
        return "Player = { playerId: " + this.playerId + ", gameId: "+ this.gameId + ", pieceType: " + this.pieceType+" }";
    }
}
