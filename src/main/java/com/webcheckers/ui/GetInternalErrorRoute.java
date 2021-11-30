package com.webcheckers.ui;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI Controller to GET the internal error page.
 *
 * @author David Authur Cole
 */
public class GetInternalErrorRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetInternalErrorRoute.class.getName());

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetInternalErrorRoute(final TemplateEngine templateEngine) {
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
    LOG.finer("GetInternalErrorRoute is invoked (oh no).");

    //Create the view model
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "500");

    // render the View
    return templateEngine.render(new ModelAndView(vm , "internal-error.ftl"));
  }
}
