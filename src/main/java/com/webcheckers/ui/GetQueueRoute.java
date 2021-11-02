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

import com.webcheckers.util.Message;
import com.webcheckers.util.Player;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetQueueRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetQueueRoute.class.getName());

  private static final Message WAIT_MSG = Message.info("Please wait until someone else wants to play.");

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
    LOG.info("GetQueueRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Queue");

    // display a user message in the Home page
    vm.put("message", WAIT_MSG);

    //put necessary info into page.
    String userName = request.session().attribute("currentUser").toString();
    Player currentUser = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(userName);
    vm.put("currentUser", currentUser);
    vm.put("gameController", WebServer.GLOBAL_GAME_CONTROLLER);

    //update info
    WebServer.GLOBAL_GAME_CONTROLLER.putInQueue(currentUser);
    // render the View

    return templateEngine.render(new ModelAndView(vm , "queue.ftl"));
  }
}
