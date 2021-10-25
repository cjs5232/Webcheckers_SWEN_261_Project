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


public class GetAcceptRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetAcceptRoute.class.getName());
    private final TemplateEngine templateEngine;
  
    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetAcceptRoute(final TemplateEngine templateEngine) {
      this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
      //
      LOG.config("GetAcceptRoute is initialized.");
    }
  
    /**
     * Render the WebCheckers Accept Game page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Accept Game page
     */
    @Override
    public Object handle(Request request, Response response) {
      LOG.finer("GetAcceptRoute is invoked.");
      //
      Map<String, Object> vm = new HashMap<>();
      vm.put("title", "Welcome!");
  
      // render the View
      return templateEngine.render(new ModelAndView(vm , "acceptPrompt.ftl"));
    }
}