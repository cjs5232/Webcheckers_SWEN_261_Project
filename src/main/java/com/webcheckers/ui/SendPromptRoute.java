package com.webcheckers.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.util.DisappearingMessage;
import com.webcheckers.util.Message;
import com.webcheckers.util.Player;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

public class SendPromptRoute implements Route {

    private static final Logger LOG = Logger.getLogger(SendPromptRoute.class.getName());
    private final TemplateEngine templateEngine;

    public SendPromptRoute(final TemplateEngine templateEngine){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("SendPromptRoute is initialized.");
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
        
        LOG.info("SendPromptRoute is invoked.");

        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Send Prompt");

        String currentUser = request.session().attribute("currentUser").toString();
        Player currentUserPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);
        vm.put("currentUser", currentUser);

        String opponent = request.queryParams("user");
        Player opponentPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(opponent);

        //Feedback, keeping attributes between redirects
        List<Message> gamePrompts = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(vm.get("currentUser").toString()).getPrompts();
        if(!gamePrompts.isEmpty()){
            vm.put("activePrompts", gamePrompts);
        }

        List<Player> loggedInPlayers = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayers();
        if(loggedInPlayers != null && (loggedInPlayers.size() != 1)) {

            //Get a list of all players EXCEPT FOR the currentUser
            List<Player> allButCurrentUser = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayersExcept(vm.get("currentUser").toString());
            vm.put("otherUsers", allButCurrentUser);
        }

        if(loggedInPlayers != null){
        vm.put("otherUsersQuantity", loggedInPlayers.size());
        }
        else{
            vm.put("otherUsersQuantity", 0);
        }

        boolean alreadyPromptedUser = false;
        List<Message> existingPrompts = opponentPlayer.getPrompts();
        for(Message m : existingPrompts){
            if(m.toString().contains(currentUser)){
                alreadyPromptedUser = true;
            }
        }

        if(opponentPlayer.isPlaying()){

            currentUserPlayer.addDisappearingMessage(DisappearingMessage.info(opponent + " is currently playing. Try again later."));

            response.redirect("/");
            return templateEngine.render(new ModelAndView(vm , "home.ftl"));
        }
        else if(alreadyPromptedUser){
            currentUserPlayer.addDisappearingMessage(DisappearingMessage.info("You have already sent a request to " + opponent));

            response.redirect("/");
            return templateEngine.render(new ModelAndView(vm , "home.ftl"));
        }
        else{

            currentUserPlayer.addDisappearingMessage(DisappearingMessage.info("Prompt sent to " + opponent));

            opponentPlayer.promptForGame(currentUser);
            response.redirect("/");
            return templateEngine.render(new ModelAndView(vm , "home.ftl"));
        }
        

    }
    
}