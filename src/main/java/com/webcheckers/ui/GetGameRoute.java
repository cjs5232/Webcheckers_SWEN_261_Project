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

    String currentUser = request.session().attribute("currentUser").toString();
    Player currentUserPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);
    vm.put("currentUser", currentUser);

    //Create a dummy Game object, this will be filled in later
    Game refGame = new Game(new Player("foo"), new Player("bar"));

    //If the player does not already have an active game
    if(!WebServer.GLOBAL_GAME_CONTROLLER.isPlayerPlaying(currentUserPlayer)){
      LOG.info(currentUser + " USER NOT IN GAME");
      //Grab the other player's name
      String otherPlayerName = request.queryParams("otherUser");
      if(otherPlayerName != null){
        LOG.info("user param pulled with value: " + otherPlayerName);

        //Get the other player by reference of their name
        Player otherPlayer =  WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(otherPlayerName);

        refGame = new Game(currentUserPlayer, otherPlayer);
        WebServer.GLOBAL_GAME_CONTROLLER.addGame(refGame);
        otherPlayer.promptForGame(currentUser);

        LOG.info("PUTTING CURRENT USER");
        vm.put("redPlayer", refGame.getPlayers()[0]);
        vm.put("whitePlayer", refGame.getPlayers()[1]);
        vm.put("currentPlayer", currentUserPlayer);
        //If the other player accepts, create a new game
        refGame = new Game(currentUserPlayer, otherPlayer);
      }
      else{
        LOG.info("user param did not pull");
        vm.put("player2", "Other Player");
      }
    }
    else{
      String otherPlayerName = request.queryParams("otherUser");
      Player otherPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(otherPlayerName);
      refGame = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(otherPlayer);
      LOG.info("USER IN GAME");

      vm.put("redPlayer", refGame.getPlayers()[0]);
      vm.put("whitePlayer", refGame.getPlayers()[1]);
      vm.put("currentPlayer", currentUserPlayer);
      
      refGame = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(currentUserPlayer);
    }
    
    //TODO: Should not always be play, should determine from input
    vm.put("viewMode", "PLAY");

    //Place the board from the created game in the view model
    vm.put("board", refGame.getBoard()); 
    
    // render the View
    
    return templateEngine.render(new ModelAndView(vm , "game.ftl"));
  }
}
