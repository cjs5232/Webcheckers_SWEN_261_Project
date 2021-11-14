package com.webcheckers.ui;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.webcheckers.util.Player;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

public class AcceptPromptRoute implements Route {

    private static final Logger LOG = Logger.getLogger(AcceptPromptRoute.class.getName());
  
    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public AcceptPromptRoute(final TemplateEngine templateEngine) {
      if (WebServer.DEBUG_FLAG) LOG.config("RemovePlayerRoute is initialized.");
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
      if (WebServer.DEBUG_FLAG) LOG.finer("GetAcceptRoute is invoked.");

      Map<String, Object> vm = new HashMap<>();
      vm.put("title", "Match");

      String currentUser = request.session().attribute("currentUser").toString();
      System.out.println("Current user: " + currentUser);
      Player refPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);

      //Notify player that their prompt is accepted
      synchronized(refPlayer){
        refPlayer.notifyAll();
      }

      //Store the currentUser in the vm
      vm.put("currentUser", currentUser);

      //Find the opponent from the "user" passed from the hyperlink generated in home.ftl
      String prompt = request.queryParams("prompt");
      String[] promptSplit = prompt.split(" ");
      Player opponent = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(promptSplit[0]);
      
      //Render the View
      response.redirect("/game?otherUser=" + opponent.toString());

      //Remove the request from the user's list
      try{
        refPlayer.removePrompt(opponent.toString());
      }
      catch(ConcurrentModificationException ex){
        LOG.finer("ConcurrentModificationException caught from " + AcceptPromptRoute.class.getName() + ". Stacktrace: " + ex.getStackTrace());
      }
      return null;
    }
}
