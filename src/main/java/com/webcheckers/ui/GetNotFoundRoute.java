package com.webcheckers.ui;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI Controller to GET the not found page.
 *
 * @author David Authur Cole
 */
public class GetNotFoundRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetNotFoundRoute.class.getName());

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetNotFoundRoute(final TemplateEngine templateEngine) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    LOG.config("GetNotFoundRoute is initialized.");
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

    //Create the view model
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "404");

    // render the View
    return templateEngine.render(new ModelAndView(vm , "not-found.ftl"));
  }
}
