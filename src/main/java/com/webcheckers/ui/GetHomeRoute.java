package com.webcheckers.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
      String currentUser = request.session().attribute("currentUser").toString();
      vm.put("currentUser", currentUser);

      Player currentUserPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);
      List<DisappearingMessage> disappearingMessages = currentUserPlayer.getDisappearingMessages();
      List<DisappearingMessage> disappearingMessagesToShow = new ArrayList<>();

      Game refGame = WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(currentUserPlayer);

      if(refGame != null && refGame.isOver()){
        WebServer.GLOBAL_GAME_CONTROLLER.handlePlayerExitGame(refGame.getId(), currentUserPlayer);
      }

      List<DisappearingMessage> toRemove = new ArrayList<>();

      if(currentUserPlayer.getWaitingOn() != null){

        Player waitingOn = currentUserPlayer.getWaitingOn();

        long start = System.currentTimeMillis();
        long end = start+10000;

        Thread t = new Thread(() -> {
          while(!waitingOn.isPlaying() && System.currentTimeMillis() < end){
            //Do nothing
          }
          //Have to figure out which one killed the while loop
          if(waitingOn.isPlaying()){
            
            //Find the active prompt 'from' the other user
            for(Message m : currentUserPlayer.getPrompts()){
              if(m.toString().contains(waitingOn.toString())){
                response.redirect("/acceptPrompt?prompt=" + m);
                return;
              }
            }
          }
        });

        //Start the waiting thread
        t.start();
      }

      //If there are any disappearing messages
      if(!disappearingMessages.isEmpty()){
      
        //Loop through the messages
        for(Iterator<DisappearingMessage> iterator = disappearingMessages.iterator(); iterator.hasNext();){
          DisappearingMessage dm = iterator.next();

          //If the message is not expired
          if(dm.getRemainingDisplays() != 0){
            disappearingMessagesToShow.add(dm);
          }
          else{
            //If it is expired, add it to the list of items to be removed. 
            // To note - We do not do direct removal here as it will almost certainly cause a ConcurrentModificationException 
            toRemove.add(dm);
          }
        }

        //Remove the expired messages
        currentUserPlayer.removeDisappearingMessages(toRemove);

        //Only add messsages if they exist
        if(!disappearingMessagesToShow.isEmpty()){
          LOG.info("Disappearing messages for user \" " + currentUser + "\":" + disappearingMessagesToShow.toString());
          vm.put("disappearingMessages", disappearingMessagesToShow);
        }
        else{
          //Place a null if no messages exist so there is no ghosting (Messages: text appears when there are no messages otherwise)
          vm.put("disappearingMessages", null);
        }
        
      }

      //Create a new list of prompts to display
      List<DisappearingMessage> toDisplay = new ArrayList<>();
      //Create a list to store the prompts to remove
      List<DisappearingMessage> toRemovePrompts = new ArrayList<>();
      //Get the user's list of prompts
      List<DisappearingMessage> gamePrompts = currentUserPlayer.getPrompts();

      //If the user has any prompts
      if(!gamePrompts.isEmpty()){

        //Loop through the prompts
        for(DisappearingMessage dm : gamePrompts){

          //If the prompt is not expired, add it to be displayed
          if(dm.getRemainingDisplays() != 0){
            toDisplay.add(dm);
          }
          //If it is expired, add it to the list of items to be removed.
          else{
            toRemovePrompts.add(dm);
          }
        } 
      }

      //Null the list so that there is no ghosting (Prompts: text appears when there are no prompts otherwise)
      if(toDisplay.isEmpty()) toDisplay = null;
      //Remove the expired prompts
      currentUserPlayer.removeOldPrompts(toRemovePrompts);
      //Add the user's prompts to the view model
      vm.put("activePrompts", toDisplay);

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

    //If the user did not load home from "/", redirect them on the next refresh cycle
    if(!request.url().equals("http://localhost:4567/")){
      response.redirect("/");
    }
    
    // render the View
    return templateEngine.render(new ModelAndView(vm , "home.ftl"));
  }
}
