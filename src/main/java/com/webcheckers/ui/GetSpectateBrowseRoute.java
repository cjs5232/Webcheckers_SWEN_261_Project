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

/**
 * The UI Controller to GET to spectate browse page.
 *
 * @author David Authur Cole
 */
public class GetSpectateBrowseRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetSpectateBrowseRoute.class.getName());

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetSpectateBrowseRoute(final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    
    if (WebServer.DEBUG_FLAG) LOG.config("GetSpectateBrowseRoute is initialized.");
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
    vm.put("title", "Spectate");

    String currentUser = request.session().attribute("currentUser").toString();
    vm.put("currentUser", currentUser);

    List<Game> games = WebServer.GLOBAL_GAME_CONTROLLER.getGames();

    vm.put("activeGames", games);

    Session session = request.session();
    session.attribute("currentUser", currentUser);
    
    // render the View
    
    return templateEngine.render(new ModelAndView(vm , "spectate.ftl"));
  }
}
