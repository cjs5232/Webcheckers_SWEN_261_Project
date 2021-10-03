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
import spark.Session;
import spark.TemplateEngine;

import com.webcheckers.util.Message;
import com.webcheckers.util.Player;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class AddPlayerRoute implements Route {
    private static final Logger LOG = Logger.getLogger(AddPlayerRoute.class.getName());
  
    private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
  
    private final TemplateEngine templateEngine;
  
    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public AddPlayerRoute(final TemplateEngine templateEngine) {
      this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
      //
      LOG.config("GetHomeRoute is initialized.");
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

      final Session session = request.session();

      LOG.finer("AddPlayerRoute is invoked.");
      //
      Map<String, Object> vm = new HashMap<>();
      vm.put("title", "Welcome!");
  
      // display a user message in the Home page
      vm.put("message", WELCOME_MSG);

      String newPlayerName = request.queryParams("name");

      boolean successfulAddPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.addPlayer(new Player(newPlayerName));
      if(successfulAddPlayer){
          LOG.log(Level.INFO, "Successfully added a new player with name: {0}", newPlayerName);
          session.attribute("currentUser", newPlayerName);

          vm.put("currentUser", newPlayerName);

          LOG.log(Level.INFO, "currentUser: {0}", session.attribute("currentUser").toString());
        }
      else{
          LOG.log(Level.WARNING, "Did not create player with name: {0}", newPlayerName);
      }
  
      // render the View
      return templateEngine.render(new ModelAndView(vm , "home.ftl"));
    }
  }
