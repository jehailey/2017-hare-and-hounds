package com.oose2017.jlee562.hareandhounds;

public class State {

    private String state;

    public State(String state){
        this.state = state;
    }

    /**
     * Set a state to the parameter
     *
     * @param state the state to change current state
     */
    public void setState(String state){
        if (validState(state)) {
            this.state = state;
        }
    }

    /**
     * Change the state from TURN_HARE to TURN_HOUND or vice versa
     */
    public void changeTurn(){
        if (this.state.equals("TURN_HARE")) {
            this.state = "TURN_HOUND";
        }else if (this.state.equals("TURN_HOUND")){
            this.state = "TURN_HARE";
        }
    }

    /**
     * Check if the state is valid
     *
     * @param state the state to check for validity
     * @return true for the valid state, false for invalid state string
     */
    private boolean validState(String state){
        switch (state) {
            case "WAITING_FOR_SECOND_PLAYER":
            case "TURN_HARE":
            case "TURN_HOUND":
            case "WIN_HARE_BY_ESCAPE":
            case "WIN_HARE_BY_STALLING":
            case "WIN_HOUND":
                return true;
            default:
                return false;
        }
    }

    @Override
    public String toString(){
        return state;
    }
}
