package com.oose2017.jlee562.hareandhounds;

import static spark.Spark.*;


public class GameController{

    private static final String API_CONTEXT = "/hareandhounds/api/";

    private final GameService gameService;

    /**
     * set up Endpoints for hare and hounds game
     *
     * @param gameService
     */
    public GameController(GameService gameService) {
        this.gameService = gameService;
        setupEndpoints();
    }

    /**
     * Set up all essential Endpoints here
     *
     */
    private void setupEndpoints() {

        /* Start of the Game */
        post(API_CONTEXT + "/games", "application/json", (request, response) -> {
            Render render;
            render =  gameService.createNewGame(request.body());
            response.status(render.Code);
            return render.Json();});

        /* Joining the Game */
        put(API_CONTEXT + "/games/:gameId", "application/json", (request, response) -> {
            Render render;
            render =  gameService.joinGame(request.params(":gameId"));
            response.status(render.Code);
            return render.Json();});

        /* Playing the Game */
        post(API_CONTEXT + "/games/:gameId/turns", "application/json", (request, response) -> {
            Render render;
            render =  gameService.playGame(request.params(":gameId"), request.body());
            response.status(render.Code);
            return render.Json();});

        /* Describe the board */
        get(API_CONTEXT + "/games/:gameId/board", "application/json", (request, response)-> {
            Render render;
            render =  gameService.describeBoard(request.params(":gameId"));
            response.status(render.Code);
            return render.Json();});

        /* Describe the game state*/
        get(API_CONTEXT + "/games/:gameId/state", "application/json", (request, response)-> {
            Render render;
            render =  gameService.describeState(request.params(":gameId"));
            response.status(render.Code);
            return render.Json();});
    }



}
