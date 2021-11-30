package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.util.Game;
import com.webcheckers.util.Message;
import com.webcheckers.util.Player;

/**
 * The UI Controller to GET the queue page.
 *
 * @author David Authur Cole
 */
public class GetQueueRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetQueueRoute.class.getName()); 

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetQueueRoute(final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    LOG.config("GetQueueRoute is initialized.");
  }

  /**
   * Render the WebCheckers Queue page.
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
    //Invoking not logged due to console spamming

    //Create the view model
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Queue");  

    //put necessary info into page.
    String userName = request.session().attribute("currentUser").toString();
    Player currentUser = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(userName);
    vm.put("currentUser", currentUser);

    //Adds the user to the queue if they are not already in it
    if(WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(currentUser) == null){
      WebServer.GLOBAL_QUEUE.addPlayer(currentUser);
    }

    //Display a message to the user
    vm.put("message", Message.info("Welcome, " + userName +  ", there are " + Integer.toString((WebServer.GLOBAL_QUEUE.getNumberOfPlayers() - 1))  + " other players in the queue..."));

    if(WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(currentUser) != null){
      Game g = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(currentUser);
      Player opponent = g.getPlayers()[0] == currentUser ? g.getPlayers()[1] : g.getPlayers()[0];
      response.redirect("/game?otherUser=" + opponent.toString());
    }
    else{
      Game g = WebServer.GLOBAL_QUEUE.createNewGame();
      if(g != null){
          Player opponent = g.getPlayers()[0] == currentUser ? g.getPlayers()[1] : g.getPlayers()[0];
          response.redirect("/game?otherUser=" + opponent.toString());
      }
    }

    // render the View

    return templateEngine.render(new ModelAndView(vm , "queue.ftl"));
  }
}
