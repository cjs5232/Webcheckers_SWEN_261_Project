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

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetLoginRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetLoginRoute.class.getName());

  private static final Message LOGIN_MSG = Message.info("Please enter a username");

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetLoginRoute(final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    LOG.config("GetLoginRoute is initialized.");
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
    LOG.finer("GetLoginRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Login");

    //Pass error message to the view if it exists
    vm.put("addUserError", request.session().attribute("addUserError"));

    // display a user message in the Home page
    vm.put("message", LOGIN_MSG);

    // render the View
    return templateEngine.render(new ModelAndView(vm , "login.ftl"));
  }
}
