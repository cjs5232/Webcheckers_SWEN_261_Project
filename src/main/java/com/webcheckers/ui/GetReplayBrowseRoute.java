package com.webcheckers.ui;

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

import com.webcheckers.util.Game;
import com.webcheckers.util.Player;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetReplayBrowseRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetReplayBrowseRoute.class.getName());

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetReplayBrowseRoute(final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    
    if (WebServer.DEBUG_FLAG) LOG.config("GetReplayBrowseRoute is initialized.");
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
    //Invoking not logged due to console spam
    
    //Create the view model
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Replay");

    String currentUser = request.session().attribute("currentUser").toString();
    Player currentUserPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);
    vm.put("currentUser", currentUser);

    List<Game> games = WebServer.GLOBAL_GAME_CONTROLLER.getCompletedGameListForUser(currentUserPlayer);

    vm.put("pastGames", games);

    Session session = request.session();
    session.attribute("currentUser", currentUser);
    
    // render the View
    
    return templateEngine.render(new ModelAndView(vm , "replay.ftl"));
  }
}
