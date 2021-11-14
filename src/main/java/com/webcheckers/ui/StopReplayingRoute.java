package com.webcheckers.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.util.BoardView;
import com.webcheckers.util.Game;
import com.webcheckers.util.Player;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

public class StopReplayingRoute implements Route {
    
    private static final Logger LOG = Logger.getLogger(StopReplayingRoute.class.getName());
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public StopReplayingRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("StopWatchingRoute is initialized.");
        }

    /**
     * Render the WebCheckers Home page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("StopWatchingRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Welcome!");

        //Get the current user
        String currentUser = request.session().attribute("currentUser").toString();
        vm.put("currentUser", currentUser);

        //Get the player object from the name
        Player currentUserPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);

        //When a user reloads the homepage after replaying a game, reset the fields
        int oldReplayId = currentUserPlayer.getReplayGameID();
        List<Game> completedGames = WebServer.GLOBAL_GAME_CONTROLLER.getCompletedGameList();
        Game refGame = null;
        for(Game g : completedGames){
            if(g.getId() == oldReplayId){
                refGame = g;
            }
        }
        if(refGame == null) return templateEngine.render(new ModelAndView(vm , "home.ftl"));

        currentUserPlayer.setReplayGameID(0);
        //Reset the board
        refGame.setBoard(new BoardView(new ArrayList<>()));
        refGame.setReplayBoardSet(false);
        refGame.setReplayMoveIndex(0);

        response.redirect(WebServer.HOME_URL);
        return templateEngine.render(new ModelAndView(vm , "home.ftl"));
    }

}
