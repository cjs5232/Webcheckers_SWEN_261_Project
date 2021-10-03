package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
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
public class RemovePlayerRoute implements Route {
    private static final Logger LOG = Logger.getLogger(RemovePlayerRoute.class.getName());
  
    private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  
    private final TemplateEngine templateEngine;
  
    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public RemovePlayerRoute(final TemplateEngine templateEngine) {
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
    public Object handle(Request request, Response response) {

      LOG.finer("RemovePlayerRoute is invoked.");
      //
      Map<String, Object> vm = new HashMap<>();
      vm.put("title", "Welcome!");
  
      // display a user message in the Home page
      vm.put("message", WELCOME_MSG);

      String remPlayerName = request.session().attribute("currentUser");

      boolean successfulRemPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.removePlayer(remPlayerName);
      if(successfulRemPlayer){
        LOG.log(Level.INFO, "Player with name {} removed", remPlayerName);
        request.session().removeAttribute("currentUser");
      }
      else{
        LOG.log(Level.INFO, "Player with name {} not found, therefore not removed", remPlayerName);
      }
  
      // render the View
      return templateEngine.render(new ModelAndView(vm , "home.ftl"));
    }
  }