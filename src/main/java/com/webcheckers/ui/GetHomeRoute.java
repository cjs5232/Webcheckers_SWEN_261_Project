package com.webcheckers.ui;

import java.util.ArrayList;
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

import com.webcheckers.util.DisappearingMessage;
import com.webcheckers.util.Game;
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

    //If error messages exist - add them
    String errorMsg = request.session().attribute("sendPromptErrorMessage");
    if(errorMsg != null && !errorMsg.isBlank()) vm.put("sendPromptErrorMessage", errorMsg);

    //If the user is logged in
    if(request.session().attributes().contains("currentUser")){

      //Get the current user
      String currentUser = request.session().attribute("currentUser").toString();
      vm.put("currentUser", currentUser);

      //Get the player object from the name
      Player currentUserPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);

      //Get the game of the user
      Game refGame = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(currentUserPlayer);

      if(currentUserPlayer.getOpponent() != null && currentUserPlayer.getOpponent().getIsPlaying()) vm.put("activeGame", refGame);
      else currentUserPlayer.setIsPlaying(false);

      //If the user has an active game, and they are on the home page, they exited/resigned
      if(refGame != null && refGame.isOver()){
        WebServer.GLOBAL_GAME_CONTROLLER.handlePlayerExitGame(refGame.getId(), currentUserPlayer);
        WebServer.GLOBAL_GAME_CONTROLLER.addCompletedGame(refGame);
      }

      waitOnPrompt(currentUserPlayer, response);

      //Add Disappearing Messages to the view model
      vm.put("disappearingMessages", handleDisappearingMessages(currentUserPlayer, currentUserPlayer.getDisappearingMessages()));

      //Add prompts to the view model
      vm.put("activePrompts", handleDisappearingMessages(currentUserPlayer, currentUserPlayer.getPrompts()));

      if(loggedInPlayers != null && (loggedInPlayers.size() != 1)) {

        //Get a list of all players EXCEPT FOR the currentUser
        List<Player> allButCurrentUser = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayersExcept(currentUser);
        //Add the list of other players to the view model
        vm.put("otherUsers", allButCurrentUser);
      }
    }

    //Add the quantity of other players to the view model
    if(loggedInPlayers != null){
      vm.put("otherUsersQuantity", loggedInPlayers.size());
    }
    else{
      vm.put("otherUsersQuantity", 0);
    }
  
    // display a user message in the Home page
    vm.put("message", WELCOME_MSG);
    
    // render the View
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }

  public void waitOnPrompt(Player currentUserPlayer, Response response){
    //If the user has a pending outgoing prompt
    if(currentUserPlayer.getWaitingOn() != null){
        //The player who the prompt was sent to
      Player waitingOn = currentUserPlayer.getWaitingOn();

      Thread t = new Thread(() -> {
        try{
          synchronized(waitingOn){
            while(!waitingOn.getIsPlaying()){
              waitingOn.wait(500);
            }
          }
          //Find the active prompt 'from' the other user
          for(Message m : currentUserPlayer.getPrompts()){
            if(m.toString().contains(waitingOn.toString())){
              currentUserPlayer.addDisappearingMessage(DisappearingMessage.info( waitingOn + " has accepted your request.", 1));
              response.redirect("/acceptPrompt?prompt=" + m);
              return;
            }
          }
        }
        catch(InterruptedException ex){
          LOG.warning("Thread was interrupted. Stacktrace: " + ex.getStackTrace());
          Thread.currentThread().interrupt();
        }
      });

      //Start the waiting thread
      t.start();
    }
    
  }

  public List<DisappearingMessage> handleDisappearingMessages(Player currentUserPlayer, List<DisappearingMessage> toSort){
    //Create a new list of prompts to display
    List<DisappearingMessage> toDisplay = new ArrayList<>();
    //Create a list to store the prompts to remove
    List<DisappearingMessage> toRemovePrompts = new ArrayList<>();

    //If the user has any prompts
    if(!toSort.isEmpty()){
      //Loop through the prompts
      for(DisappearingMessage dm : toSort){

        //If the prompt is not expired, add it to be displayed
        if(dm.getRemainingDisplays() != 0){
          toDisplay.add(dm);
        }
        //If it is expired, add it to the list of items to be removed.
        else{
          toRemovePrompts.add(dm);
        }
      }
      //Remove the expired prompts
      currentUserPlayer.removeOldPrompts(toRemovePrompts);
    }

    return toDisplay;
  }

}
