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

        //Grab the currentUser attribute, and find the Player object from it
        String currentUser = request.session().attribute("currentUser").toString();
        Player currentUserPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(currentUser);
        vm.put("player1", currentUser);

        //Find the opponent from the "user" passed from the hyperlink generated in home.ftl
        String opponent = request.queryParams("user");
        Player opponentPlayer = WebServer.GLOBAL_PLAYER_CONTROLLER.getPlayerByName(opponent);

        //Check if the user has already sent a request to this specific opponent
        boolean alreadyPromptedUser = false;
        List<DisappearingMessage> existingPrompts = opponentPlayer.getPrompts();
        for(Message m : existingPrompts){
            if(m.toString().contains(currentUser)){
                alreadyPromptedUser = true;
                LOG.info("User has already been prompted.");
            }
        }

        //We do not want to send the opponent a prompt if they are currently playing
        if(opponentPlayer.getIsPlaying()){

            if (currentUserPlayer.getIsPlaying() && !WebServer.GLOBAL_GAME_CONTROLLER.getGameOfPlayer(currentUserPlayer).getPlayersExited().contains(currentUserPlayer)) {
                response.redirect("/game");
            }

            currentUserPlayer.addDisappearingMessage(DisappearingMessage.info(opponent + " is currently playing. Try again later.", 2));

            response.redirect("/");
            return templateEngine.render(new ModelAndView(vm , "home.ftl"));
        }
        //Only can send 1 prompt
        else if(alreadyPromptedUser){
            currentUserPlayer.addDisappearingMessage(DisappearingMessage.info("You have already sent a request to " + opponent, 2));

            response.redirect("/");
            return templateEngine.render(new ModelAndView(vm , "home.ftl"));
        }
        //Send the prompt
        else{

            currentUserPlayer.addDisappearingMessage(DisappearingMessage.info("Prompt sent to " + opponent, 2));

            opponentPlayer.promptForGame(currentUserPlayer);
            response.redirect("/");
            return templateEngine.render(new ModelAndView(vm , "home.ftl"));
        }
        

    }
    
}
