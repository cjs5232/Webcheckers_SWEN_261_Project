package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.util.Player;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

public class StopWatchingRoute implements Route {
    
    private static final Logger LOG = Logger.getLogger(GetLoginRoute.class.getName());
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public StopWatchingRoute(final TemplateEngine templateEngine) {
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

        //When a user reloads the homepage after spectating a game, reset the fields
        currentUserPlayer.setSpectating(false);
        currentUserPlayer.setLastKnownTurnColor(null);
        currentUserPlayer.setSpectatingGame(null);
        currentUserPlayer.setAnnounceSpectatorWinOnNextRefresh(false);

        response.redirect(WebServer.HOME_URL);
        return templateEngine.render(new ModelAndView(vm , "home.ftl"));
    }

}
