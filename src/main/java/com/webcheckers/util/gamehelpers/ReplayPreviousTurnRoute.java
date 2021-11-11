package com.webcheckers.util.gamehelpers;

import java.util.List;
import java.util.logging.Logger;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import com.google.gson.Gson;
import com.webcheckers.ui.WebServer;
import com.webcheckers.util.Game;
import com.webcheckers.util.Message;
import com.webcheckers.util.Player;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class ReplayPreviousTurnRoute implements Route {
  private static final Logger LOG = Logger.getLogger(ReplayPreviousTurnRoute.class.getName());
  private static final Gson gson = new Gson();

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public ReplayPreviousTurnRoute() {
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

    refGame.replayPrevious();

    Session session = request.session();
    session.attribute("currentUser", currentUser);
    
    // render the View
    
    return gson.toJson(Message.info("true"));
  }
}
