package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

import com.webcheckers.application.*;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Game;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import com.google.gson.JsonObject;

/**
 * The UI Controller to GET the game page.
 *
 * @author David Authur Cole
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
    
    if (WebServer.DEBUG_FLAG) LOG.config("GetGameRoute is initialized.");
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
    if (WebServer.DEBUG_FLAG) LOG.info("GetGameRoute is invoked.");

    //Create the view model
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Game");

    // display a user message in the Home page
    vm.put("message", WELCOME_MSG);

    String currentUser = request.session().attribute("currentUser").toString();
    Player currentUserPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);
    vm.put("currentUser", currentUser);
    currentUserPlayer.setIsPlaying(true);
    currentUserPlayer.setWaitingOn(null);

    //Create a dummy Game object, this will be filled in later
    Game refGame = new Game(new Player("foo"), new Player("bar"));

    //If the player does not already have an active game
    if(!WebServer.GLOBAL_GAME_CONTROLLER.isPlayerPlaying(currentUserPlayer)){
      if (WebServer.DEBUG_FLAG) LOG.info(currentUser + " USER NOT IN GAME");
      //Grab the other player's name
      String otherPlayerName = request.queryParams("otherUser");
      if(otherPlayerName != null){
        if (WebServer.DEBUG_FLAG) LOG.info("user param pulled with value: " + otherPlayerName);

        //Get the other player by reference of their name
        Player otherPlayer =  WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(otherPlayerName);

        refGame = new Game(currentUserPlayer, otherPlayer);
        WebServer.GLOBAL_GAME_CONTROLLER.addGame(refGame);
        otherPlayer.promptForGame(currentUserPlayer);

        if (WebServer.DEBUG_FLAG) LOG.info("PUTTING CURRENT USER");
        vm.put("redPlayer", refGame.getPlayers()[0]);
        vm.put("whitePlayer", refGame.getPlayers()[1]);
        vm.put("currentPlayer", currentUserPlayer);
        vm.put("activeColor", refGame.getActiveColor());

        //New game created, let's see what tf this looks like...
        if (WebServer.DEBUG_FLAG) LOG.info("NEW GAME CREATED");
        if (WebServer.DEBUG_FLAG) System.out.println(refGame.getBoard().printBoardPretty());

      }
      else{
        if (WebServer.DEBUG_FLAG) LOG.info("user param did not pull");
        vm.put("player2", "Other Player");
      }
    }
    else{
      refGame = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(currentUserPlayer);
      
      Player[] players = refGame.getPlayers();
      Player otherPlayer = null;
      for(Player p : players){
        if(!p.toString().equals(currentUser)){
          otherPlayer = p;
        }
      }
      currentUserPlayer.setOpponent(otherPlayer);

      if (WebServer.DEBUG_FLAG) LOG.info("USER IN GAME");

      vm.put("redPlayer", refGame.getPlayers()[0]);
      vm.put("whitePlayer", refGame.getPlayers()[1]);
      vm.put("currentPlayer", currentUserPlayer);
      synchronized(refGame){
        vm.put("activeColor", refGame.getActiveColor());
      }
    }

    //Add game ID to the view model
    vm.put("gameID", String.format("%010d", refGame.getId()));
    
    vm.put("viewMode", "PLAY");

    //If the game is over, tell the server to end the game
    if(refGame.isOver()){
      if (WebServer.DEBUG_FLAG) LOG.info("GAME IS OVER");

      JsonObject modeOptions = new JsonObject();
      modeOptions.addProperty("isGameOver", "true");
      modeOptions.addProperty("gameOverMessage", refGame.getGameOverMessage().toString());
      vm.put("modeOptionsAsJSON", modeOptions);
    }
    else{
      vm.put("modeOptionsAsJSON", "{}");
    }

    refGame.resetTurnOccupied();

    //Place the board from the created game in the view model
    BoardView inverseBoard = new BoardView(refGame.getBoard()).inverseForWhite();
    if(refGame.getWhitePlayer().equals(currentUserPlayer)) vm.put("board", inverseBoard);
    else vm.put("board", refGame.getBoard());

    Session session = request.session();
    session.attribute("currentUser", currentUser);
    
    // render the View
    
    return templateEngine.render(new ModelAndView(vm , "game.ftl"));
  }
}
