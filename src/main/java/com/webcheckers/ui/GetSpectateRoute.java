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

import com.webcheckers.util.Game;
import com.webcheckers.util.Message;
import com.webcheckers.util.Player;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetSpectateRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetSpectateRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetSpectateRoute(final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    
    if (WebServer.DEBUG_FLAG) LOG.config("GetSpectateRoute is initialized.");
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
    if (WebServer.DEBUG_FLAG) LOG.info("GetSpectateRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Game");

    // display a user message in the Home page
    vm.put("message", WELCOME_MSG);

    String currentUser = request.session().attribute("currentUser").toString();
    Player currentUserPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);
    vm.put("currentUser", currentUser);

    int id = Integer.parseInt(request.queryParams("id"));
    Game refGame = WebServer.GLOBAL_GAME_CONTROLLER.getGameById(id);

    currentUserPlayer.setLastKnownTurnColor(refGame.getActiveColor());
    currentUserPlayer.setSpectating(true);
    currentUserPlayer.setSpectatingGame(refGame);

    vm.put("redPlayer", refGame.getPlayers()[0]);
    vm.put("whitePlayer", refGame.getPlayers()[1]);
    vm.put("currentPlayer", currentUserPlayer);
    vm.put("activeColor", refGame.getActiveColor());

    //Add game ID to the view model
    vm.put("gameID", Integer.toString(id));
    
    vm.put("viewMode", "SPECTATOR");

    //Place the board from the created game in the view model
    vm.put("board", refGame.getBoard());

    Session session = request.session();
    session.attribute("currentUser", currentUser);
    
    // render the View
    
    return templateEngine.render(new ModelAndView(vm , "game.ftl"));
  }
}
