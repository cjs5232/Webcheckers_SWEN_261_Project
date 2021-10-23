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
import spark.TemplateEngine;

import com.webcheckers.util.Message;
import com.webcheckers.util.Player;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final TemplateEngine templateEngine) {
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
    LOG.finer("GetHomeRoute is invoked.");
    //
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    List<Player> loggedInPlayers = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayers();

    //If the user is logged in
    if(request.session().attributes().contains("currentUser")){
      vm.put("currentUser", request.session().attribute("currentUser"));

      List<Message> gamePrompts = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(vm.get("currentUser").toString()).getPrompts();
      if(!gamePrompts.isEmpty()){
        vm.put("activePrompts", gamePrompts);
      }

      if(loggedInPlayers != null && (loggedInPlayers.size() != 1)) {

        //Get a list of all players EXCEPT FOR the currentUser
        List<Player> allButCurrentUser = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayersExcept(vm.get("currentUser").toString());
        vm.put("otherUsers", allButCurrentUser);
      }
    }
    if(loggedInPlayers != null){
      vm.put("otherUsersQuantity", loggedInPlayers.size());
    }
    else{
      vm.put("otherUsersQuantity", 0);
    }
  
    // display a user message in the Home page
    vm.put("message", WELCOME_MSG);

    //If the user did not load home from "/", redirect them on the next refresh cycle
    if(!request.url().equals("http://localhost:4567/")){
      response.redirect("/");
    }
    
    // render the View
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }
}
