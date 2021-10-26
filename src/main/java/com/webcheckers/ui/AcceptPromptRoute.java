package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.util.Game;
import com.webcheckers.util.Message;
import com.webcheckers.util.Player;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

public class AcceptPromptRoute implements Route {

    private static final Logger LOG = Logger.getLogger(AcceptPromptRoute.class.getName());
  
  
    private final TemplateEngine templateEngine;
  
    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public AcceptPromptRoute(final TemplateEngine templateEngine) {
      this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
      //
      LOG.config("RemovePlayerRoute is initialized.");
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
    public Object handle(Request request, Response response) throws Exception {
      LOG.finer("GetAcceptRoute is invoked.");
      //

      


      Map<String, Object> vm = new HashMap<>();
      vm.put("title", "Match");

      String currentUser = request.session().attribute("currentUser").toString();
      Player refPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);

      //Find the opponent from the "user" passed from the hyperlink generated in home.ftl
      String prompt = request.queryParams("prompt");
      String[] words = prompt.split(" ");
      Player opponent = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(words[0]);

      //Game game = new Game(refPlayer, opponent);
      //WebServer.GLOBAL_GAME_CONTROLLER.addGame(game);
      
      // render the View
      
      response.redirect("/game?otherUser=" + words[0]);
      return null;
     
    }
    
}
