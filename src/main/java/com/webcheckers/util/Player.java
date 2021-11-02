package com.webcheckers.util;

import java.util.ArrayList;
import java.util.List;

import com.webcheckers.ui.WebServer;

public class Player {
    
    private Player waitingOnAccept;
    /**
     * TEMP
     * 0 = not in game
     * 1 = searching for game
     * 2 = in game
     */
    private int playerStatus;

    private final String name;
    private final int id;
    private Player opponent;

    private List<DisappearingMessage> promptMessages = new ArrayList<>();
    private List<DisappearingMessage> disappearingMessages = new ArrayList<>();

    public Player(String name){
        this.name = name;
        this.id = name.hashCode();
        this.playerStatus = 0;
        this.opponent = null;
    }

    //Getters and Setters

    public int getPlayerStatus() {
        return this.playerStatus;
    }

    public void setPlayerStatus(int stat) {
        playerStatus = stat;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Player getOppoent() {
        return this.opponent;
    }

    public String getName(){
        return this.name;
    }

    public int getId(){
        return this.id;
    }

    /**
     * @param p the player to wait on (to accept or timeout a prompt)
     */
    public void setWaitingOn(Player p){
        this.waitingOnAccept = p;
    }

    public Player getWaitingOn(){
        return this.waitingOnAccept;
    }

    /**
     * Remove the player that is being waited on
     */
    public void clearWaitingOn(){
        this.waitingOnAccept = null;
    }

    /**
     * @return all of a user's messages
     */
    public List<DisappearingMessage> getDisappearingMessages(){
        return this.disappearingMessages;
    }

    /**
     * @return game requests
     */
    public List<DisappearingMessage> getPrompts(){
        return this.promptMessages;
    }

    //Other functions    

    @Override
    public String toString(){
        return this.name;
    }

    public void startGame(Player player, Player opponent) {
        Game game = new Game(player, opponent);
        WebServer.GLOBAL_GAME_CONTROLLER.addGame(game);
    }

    /**
     * @param promptingPlayer the player requesting a game
     */
    public void promptForGame(String promptingPlayer){
        String promptString = promptingPlayer + " wants to play you in a game. [Click to accept]";
        promptMessages.add(DisappearingMessage.info(promptString, 6));
    }

    /**
     * @param promptingPlayer the player whose prompt should be removed
     */
    public void removePrompt(String promptingPlayer){
        synchronized(this){
            for(Message m : promptMessages){
                if(m.toString().contains(promptingPlayer)){
                    promptMessages.remove(m);
                }
            }
        }
    }

    public void removeOldPrompts(List<DisappearingMessage> removePromptList){
        promptMessages.removeAll(removePromptList);
    }

    /**
     * 
     * @param m the DisappearingMessage to add to the user's home screen
     */
    public void addDisappearingMessage(DisappearingMessage m){
        this.disappearingMessages.add(m);
    }

    /**
     * @param removeDMList The list of DisappearingMessage (s) to remove from the user's home screen
     */
    public void removeDisappearingMessages(List<DisappearingMessage> removeDMList){
        disappearingMessages.removeAll(removeDMList);
    }

    /**
     * @return whether or not the user is currently in a game
     */
    public boolean isPlaying(){
        return WebServer.GLOBAL_GAME_CONTROLLER.isPlayerPlaying(this.toString());
    }

}
