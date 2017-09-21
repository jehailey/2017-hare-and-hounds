package com.oose2017.jlee562.hareandhounds;


public class Piece {
    private String pieceType;
    private int x;
    private int y;

    public Piece(String pieceType, int x, int y) {
        this.pieceType = pieceType;
        this.x = x;
        this.y = y;
    }

    public String getPieceType() {
        return this.pieceType;
    }

    public boolean isEqual(int x, int y){
        return this.x == x && this.y == y;
    }

    /**
     * Check whether the move is legal
     *
     * @param to_x the x position
     * @param to_y the y position
     * @return true for legal move, false for illegal move
     */
    public boolean isLegal(int to_x, int to_y){
        if (outOfBound(to_x, to_y)){ return false; }    // need to be on bound
        if (isEqual(to_x, to_y)){ return false; }   // need to move at least one step
        if (!isValidStep(to_x, to_y)){return false; } // need to be a valid step

        if (this.pieceType.equals("HARE")) {    // must move only one step ahead
            if (Math.abs(to_x - this.x) > 1) return false;
            if (Math.abs(to_y - this.y) > 1) return false;
        }else if (this.pieceType.equals("HOUND")){
            if (Math.abs(to_y - this.y) > 2) return false;    // can move up or down but only one step
            if (this.x > to_x) return false;                  // must move forward but only one step
            if (to_x > (this.x + 2)) return false;
        }
        return true;
    }

    /**
     * Move Piece to (to_x, to_y)
     * @param to_x  x position to be moved to
     * @param to_y  y position to be moved to
     */
    public void move(int to_x, int to_y){
        this.x = to_x;
        this.y = to_y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    /**
     * Check whether the step is valid by description of board
     *
     * @param to_x x position to move to
     * @param to_y y position to move to
     */
    private boolean isValidStep(int to_x, int to_y){
        if (this.x == 1 && this.y == 1){
            if (to_x == 2 && to_y == 0){
                return false;
            }else if (to_x == 2 && to_y == 2){
                return false;
            }
        }else if (this.x == 3 && this.y == 1){
            if (to_x == 2 && to_y == 0){
                return false;
            }else if (to_x == 2 && to_y == 2){
                return false;
            }
        }else if (this.x == 2 && this.y == 0){
            if (to_x == 1 && to_y == 1){
                return false;
            }else if (to_x == 3 && to_y == 1){
                return false;
            }
        }else if (this.x == 2 && this.y == 2){
            if (to_x == 1 && to_y == 1){
                return false;
            }else if (to_x == 3 && to_y == 1){
                return false;
            }
        }
        return true;
    }

    /**
     * Check for the bounds of to_x, to_y
     * @param to_x  the x position to move to
     * @param to_y  the y position to move to
     * @return  true for in bound, false for out of bound
     */
    private boolean outOfBound(int to_x, int to_y){
        if ((to_x == 0 && to_y == 2) || (to_x == 0 && to_y == 0) ||
                (to_x == 4 && to_y == 2) || (to_x == 4 && to_y == 0)){
            return true;   // invalid x,y coordinates on the board
        }
        if (to_x > 4 || to_x < 0){
            return true;
        }
        if (to_y > 2 || to_y < 0){
            return true;
        } // invalid x, y coordinates on the board
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Piece piece = (Piece) o;

        if (this.x != piece.getX()) return false;
        if (this.y != piece.getY()) return false;
        return this.pieceType.equals(piece.pieceType);
    }

    @Override
    public String toString() {
        return "piece " + this.pieceType + " :(" + this.x + "," + this.y + ")";
    }
}
