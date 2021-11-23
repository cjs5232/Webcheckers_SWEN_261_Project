package com.webcheckers.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

import com.google.gson.JsonObject;
import com.webcheckers.util.BoardView;
import com.webcheckers.util.Game;
import com.webcheckers.util.Message;
import com.webcheckers.util.Player;
import com.webcheckers.util.Piece.Color;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetReplayRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetReplayRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetReplayRoute(final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    
    if (WebServer.DEBUG_FLAG) LOG.config("GetReplayRoute is initialized.");
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
    if (WebServer.DEBUG_FLAG) LOG.info("GetReplayRoute is invoked.");

    //Create the view model
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Replay Browser");

    // display a user message in the Home page
    vm.put("message", WELCOME_MSG);

    String currentUser = request.session().attribute("currentUser").toString();
    Player currentUserPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);
    vm.put("currentUser", currentUser);

    int id = Integer.parseInt(request.queryParams("id"));
    currentUserPlayer.setReplayGameID(id);
    Game refGame = null;

    //Get the reference game from the game ID and the list of completed games
    List<Game> games = WebServer.GLOBAL_GAME_CONTROLLER.getCompletedGameListForUser(currentUserPlayer);
    for(Game g : games){
        if(g.getId() == id){
            refGame = g;
            break;
        }
    }

    //This should never be reached, but is here for safety
    if(refGame == null){
        response.redirect("/");
        return null;
    }

    //Set the starting board for the game - only triggers once
    if(!refGame.getReplayBoardSet()){
        refGame.setBoard(new BoardView(new ArrayList<>()));
        refGame.setOverrideOverFlag(false);
        refGame.setActiveColor(Color.RED);
    }

    currentUserPlayer.setLastKnownTurnColor(refGame.getActiveColor());
    currentUserPlayer.setSpectating(true);
    currentUserPlayer.setSpectatingGame(refGame);

    vm.put("redPlayer", refGame.getPlayers()[0]);
    vm.put("whitePlayer", refGame.getPlayers()[1]);
    vm.put("currentPlayer", currentUserPlayer);
    vm.put("activeColor", refGame.getActiveColor());

    //Add game ID to the view model
    vm.put("gameID", Integer.toString(id));
    
    vm.put("viewMode", "REPLAY");

    //Place the board from the created game in the view model
    vm.put("board", refGame.getBoard());

    JsonObject modeOptions = new JsonObject();
    if(refGame.replayHasNext()){
      modeOptions.addProperty("hasNext", "true" );
    }
    else{
      modeOptions.addProperty("gameOverMessage", refGame.getGameOverMessage().toString());
      modeOptions.addProperty("isGameOver", "true");
    }
    if(refGame.replayHasPrevious()) modeOptions.addProperty("hasPrevious", "false");
    if(refGame.isOver()) modeOptions.addProperty("isGameOver", "true");
    vm.put("modeOptionsAsJSON", modeOptions);

    Session session = request.session();
    session.attribute("currentUser", currentUser);
    
    // render the View
    
    return templateEngine.render(new ModelAndView(vm , "game.ftl"));
  }
}
