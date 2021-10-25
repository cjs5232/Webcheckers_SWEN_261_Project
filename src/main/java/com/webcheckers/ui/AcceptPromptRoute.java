package com.webcheckers.ui;

import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.util.Message;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

public class AcceptPromptRoute implements Route {

    private static final Logger LOG = Logger.getLogger(AcceptPromptRoute.class.getName());
  
  
    private final TemplateEngine templateEngine;
  
    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public AcceptPromptRoute(final TemplateEngine templateEngine) {
      this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
      //
      LOG.config("RemovePlayerRoute is initialized.");
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
    public Object handle(Request request, Response response) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
    
}
