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
      LOG.config("AddPlayerRoute is initialized.");
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
      String successfulAddPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.addPlayer(new Player(newPlayerName));

      if(successfulAddPlayer.equals("")){
          LOG.log(Level.INFO, "Successfully added a new player with name: {0}", newPlayerName);
          session.attribute("currentUser", WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(newPlayerName));

          //REACH
          System.out.println(session.attribute("currentUser").toString());

          vm.put("currentUser", newPlayerName);
          // Return the user to the home page
          return templateEngine.render(new ModelAndView(vm , "home.ftl"));
        }
      else{
          LOG.log(Level.WARNING, "Did not create player with name: {0}", newPlayerName);
          LOG.log(Level.INFO, "User error: {0}", successfulAddPlayer);

          vm.put("addUserError", successfulAddPlayer);

          // Return the user to the login page
          return templateEngine.render(new ModelAndView(vm , "login.ftl"));
      }
    }
  }
