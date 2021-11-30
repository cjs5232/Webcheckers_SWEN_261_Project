package com.webcheckers.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
/**
 * The player class handles checkers players.
 *
 * @author David Authur Cole
 */
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

    private boolean isPlaying;
    private Player opponent;

    //Used for spectating, will store the last turn's color to check against current turn's color
    private Piece.Color lastKnownTurn;

    private boolean isSpectating;
    private Game spectatingGame;

    private List<DisappearingMessage> promptMessages = new ArrayList<>();
    private List<DisappearingMessage> disappearingMessages = new ArrayList<>();

    //Stores the move history for a game being replayed
    private Deque<Move> moveHistoryForReplay = new ArrayDeque<>();
    private int replayGameID;

    public Player(String name){
        this.name = name;
        this.id = name.hashCode();
        this.playerStatus = 0;
        this.opponent = null;
        this.isPlaying = false;

        //Spectating functionality
        this.lastKnownTurn = null;
        this.isSpectating = false;
        this.spectatingGame = null;

        //Replay functionality
        this.moveHistoryForReplay = null;
        this.replayGameID = 0;
    }

    //Getters and Setters

    /**
     * @param id the id of the game that is being replayed
     */
    public void setReplayGameID(int id){
        this.replayGameID = id;
    }

    /**
     * @return the id of the game that is being replayed
     */
    public int getReplayGameID(){
        return this.replayGameID;
    }

    /**
     * @return the list of moves to be made in a replay
     */
    public Deque<Move> getMoveHistoryForReplay() {
        return moveHistoryForReplay;
    }

    /**
     * Sets the list of moves that should be made in a replay
     * @param moveHistoryForReplay the list of moves to copy in construction
     */
    public void setMoveHistoryForReplay(Deque<Move> moveHistoryForReplay) {
        this.moveHistoryForReplay = new ArrayDeque<>(moveHistoryForReplay);
    }

    /**
     * Sets the game that is currently being spectated
     */
    public void setSpectatingGame(Game game){
        this.spectatingGame = game;
    }

    /**
     * Returns the game that is currently being spectated
     * @return Game
     */
    public Game getSpectatingGame(){
        return this.spectatingGame;
    }

    /**
     * Sets whether or not the user is spectating a game
     */
    public void setSpectating(boolean spectating) {
        isSpectating = spectating;
    }

    /**
     * Gets whether or not the user is spectating a game
     * @return if the user is spectating or not
     */
    public boolean isSpectating() {
        return isSpectating;
    }

    /**
     * Sets the last known turn color - used in spectating to wait for turn to change
     */
    public void setLastKnownTurnColor(Piece.Color color){
        lastKnownTurn = color;
    }

    /**
     * Gets the last known turn color
     * @return the last known turn color
     */
    public Piece.Color getLastKnownTurnColor(){
        return lastKnownTurn;
    }

    public int getPlayerStatus() {
        return this.playerStatus;
    }

    public void setPlayerStatus(int stat) {
        playerStatus = stat;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Player getOpponent() {
        return this.opponent;
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

    /**
     * @param promptingPlayer the player requesting a game
     */
    public void promptForGame(Player promptingPlayer){
        promptingPlayer.setWaitingOn(this);
        String promptString = promptingPlayer + " wants to play you in a game. [Click to accept]";
        promptMessages.add(DisappearingMessage.info(promptString, 6));
    }
    
    /**
     * @param promptingPlayer the player whose prompt should be removed
     */
    public void removePrompt(Player promptingPlayer){
        Message promptToRemove = null;
        synchronized(this){
            for(Message m : promptMessages){
                if(m.toString().contains(promptingPlayer.toString())){
                    promptToRemove = m;
                }
            }
            promptMessages.remove(promptToRemove);
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
     * @param isPlaying the flag to set whether or not the user is playing
     */
    public void setIsPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
    }

    /**
     * @return whether or not the user is currently in a game
     */
    public boolean getIsPlaying(){
        return isPlaying;
    }

}
