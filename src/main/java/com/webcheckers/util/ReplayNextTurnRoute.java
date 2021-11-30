package com.webcheckers.util;

import java.util.List;
import java.util.logging.Logger;

import spark.Request;
import spark.Response;
import spark.Route;

import com.google.gson.Gson;
import com.webcheckers.application.*;
import com.webcheckers.model.Game;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;

/**
 * The UI Controller to replay the next turn.
 *
 * @author David Authur Cole
 */
public class ReplayNextTurnRoute implements Route {
  private static final Logger LOG = Logger.getLogger(ReplayNextTurnRoute.class.getName());
  private static final Gson gson = new Gson();

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   */
  public ReplayNextTurnRoute() {
    //
    
    if (WebServer.DEBUG_FLAG) LOG.config("GetReplayRoute is initialized.");
  }

  /**
   * Render the updated WebCheckers game page by replaying the next turn
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

    //Get user
    String currentUser = request.session().attribute("currentUser").toString();
    Player currentUserPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);

    int id = currentUserPlayer.getReplayGameID();
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

    refGame.replayNext();
    
    return gson.toJson(Message.info("true"));
  }
}
