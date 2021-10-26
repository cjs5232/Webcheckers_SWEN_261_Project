package com.webcheckers.util;

import java.util.ArrayList;
import java.util.List;

import com.webcheckers.ui.WebServer;

public class Player {
    
    private final String name;
    private final int id;

    private List<Message> promptMessages = new ArrayList<>();
    private List<DisappearingMessage> disappearingMessages = new ArrayList<>();

    public Player(String name){
        this.name = name;
        this.id = name.hashCode();
    }

    public String getName(){
        return this.name;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public void startGame (Player player, Player opponent) {
        Game game = new Game(player, opponent);
        WebServer.GLOBAL_GAME_CONTROLLER.addGame(game);
    }

    /**
     * @param promptingPlayer the player requesting a game
     */
    public void promptForGame(String promptingPlayer){
        String promptString = promptingPlayer + " wants to play you in a game. [Click to accept]";
        promptMessages.add(Message.info(promptString));
    }

    /**
     * @param promptingPlayer the player whose prompt should be removed
     */
    public void removePrompt(String promptingPlayer){
        for(Message m : promptMessages){
            if(m.toString().contains(promptingPlayer)){
                promptMessages.remove(m);
            }
        }
    }

    /**
     * @return
     */
    public List<Message> getPrompts(){
        return this.promptMessages;
    }

    public void addDisappearingMessage(DisappearingMessage m){
        this.disappearingMessages.add(m);
    }

    public void removeDisappearingMessages(List<DisappearingMessage> removeDMList){
        disappearingMessages.removeAll(removeDMList);
    }

    public List<DisappearingMessage> getDisappearingMessages(){
        return this.disappearingMessages;
    }

    public boolean isPlaying(){
        return WebServer.GLOBAL_GAME_CONTROLLER.isPlayerPlaying(this.toString());
    }

}
