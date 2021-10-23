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
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetGameRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetGameRoute(final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    LOG.config("GetGameRoute is initialized.");
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
    LOG.info("GetGameRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Game");

    // display a user message in the Home page
    vm.put("message", WELCOME_MSG);

    //If the player does not already have an active game
    
    //Create a new Game object
    Game refGame = new Game(new Player("foo"), new Player("bar"));

    String otherPlayerName = request.queryParams("user");
    if(otherPlayerName != null){
      LOG.info("user param pulled with value: " + otherPlayerName);
      vm.put("otherUser", otherPlayerName);

      //Get the other player by reference of their name
      Player otherPlayer =  WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(otherPlayerName);
      
      //If the other player accepts, create a new game
      refGame = new Game(request.session().attribute("currentUser"), otherPlayer);
    }
    else{
      LOG.info("user param did not pull");
      vm.put("otherUser", "Other Player");
    }

    vm.put("currentUser", request.session().attribute("currentUser"));

    // Should not always be play, should determine from input
    vm.put("viewMode", "play");

    //Place the board from the created game in the view model
    vm.put("board", refGame.getBoard()); 
    
    // render the View
    return templateEngine.render(new ModelAndView(vm , "game.ftl"));
  }
}
