package com.webcheckers.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.webcheckers.ui.WebServer;
import com.webcheckers.util.Piece.Color;

public class Game {
   
    private Player redPlayer;
    private Player whitePlayer;
    private Color activeColor;

    //Positions of captured pieces
    private Position capture;

    private static final Logger LOG = Logger.getLogger(Game.class.getName());

    private BoardView gameBoard;
    
    private int id;

    private boolean backingUp = false;

    private List<Position> toRemove = new ArrayList<>();

    private Deque<Move> lastMoves = new ArrayDeque<>();
    private Deque<Boolean> lastMovesCapture = new ArrayDeque<>();
    private Deque<Boolean> lastMovePromotion = new ArrayDeque<>();

    private List<Player> exited;

    private int nonCaptureMoves = 0;

    public Game(Player redPlayer, Player whitePlayer){

        this.id = new Random().nextInt(Integer.MAX_VALUE);
        this.exited = new ArrayList<>();
        
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.activeColor = Color.RED;
        this.gameBoard = new BoardView(new ArrayList<>());
    }

    /**
     * Increment playersExited
     */
    public void playerExited(Player p){
        if(!exited.contains(p)){
            exited.add(p);
        }
    }

    /**
     * @return the number of players who have exited the game
     */
    public List<Player> getPlayersExited(){
        return exited;
    }

    /**
     * @return the id of the game
     */
    public int getId(){
        return this.id;
    }

    /**
     * Handles setting a new id for the game when the first one was already taken
     */
    public void setNewId(){
        this.id = new Random().nextInt(Integer.MAX_VALUE);
    }

    /** 
     * @return the board object of the game
     */
    public BoardView getBoard(){
        return this.gameBoard;
    }

    /**
     * @return the two Players playing the game
     */
    public Player[] getPlayers(){
        return new Player[]{redPlayer, whitePlayer};
    }

    /**
     * If the activecolor is red, set it to white, and vice versa
     */
    public void swapActiveColor(){
        if(this.activeColor == Color.RED){
            this.activeColor = Color.WHITE;
        }
        else{
            this.activeColor = Color.RED;
        }
        nonCaptureMoves = 0;
    }

    /**
     * @return the active color of the game
     */
    public Color getActiveColor(){
        return activeColor;
    }

    public Color getNonActiveColor(){
        return activeColor == Color.RED ? Color.WHITE : Color.RED;
    }

    /**
     * Remove all pieces that are queued, only called when a turn is submitted
     */
    public void removeDeadPieces(){
        for(Position pos : toRemove){
            this.gameBoard.getRow(pos.getRow()).getSpace(pos.getCell()).setPiece(null);
        }
        toRemove.clear();
    }

    public void removeDeadPieceFromList(Position p){

        if (WebServer.DEBUG_FLAG) LOG.info("Dead piece list before removal: " + toRemove);

        for(Position dp : toRemove){
            if(dp.equals(p)){
                toRemove.remove(dp);
                break;
            }
        }

        if (WebServer.DEBUG_FLAG) LOG.info("Dead piece list after removal: " + toRemove);
    }

    /**
     * Undo the last move, including putting back the captured piece (if there was one)
     */
    public void undoLastMove(){

        Move lastMove = lastMoves.removeFirst();

        Position lastMoveStart = lastMove.getStart();
        Position lastMoveEnd = lastMove.getEnd();

        Move inverseLastMove = new Move(lastMoveEnd, lastMoveStart);

        backingUp = true;
        executeMove(inverseLastMove);
        backingUp = false;

        if (!lastMovesCapture.removeFirst().booleanValue()) {
            nonCaptureMoves--;
            System.out.println("reach 1");
        }
        else{
            Position middle = new Position((lastMoveStart.getRow() + lastMoveEnd.getRow()) / 2, (lastMoveStart.getCell() + lastMoveEnd.getCell()) / 2);
            removeDeadPieceFromList(middle);
            System.out.println("reach 2");
        }
    }

    /**
     * @return true if the game is won by a player
     */
    public boolean isOver(){
        
        int redPieces = 0;
        int whitePieces = 0;

        for(Row r : gameBoard.getRows()){
            for(Space s : r.getSpaces()){
                if(s.getPiece() != null){
                    if(s.getPiece().getColor() == Color.RED) redPieces++;
                    else whitePieces++;
                }
            }
        }

        return(redPieces == 0 || whitePieces == 0);
    }

    /** 
     * There is a lot of complex logic in here to determine if a move is valid
     * @return true if the move is valid, false otherwise
    */
    public Message isMoveValid(Move move, Piece piece){

        if(nonCaptureMoves > 0) return Message.error("You have already made a non-capturing move this turn.");

        //Start and end positions of the piece
        Position start = move.getStart();
        Position end = move.getEnd();

        int changeY = end.getRow() - start.getRow();
        int changeX = end.getCell() - start.getCell();

        int distance = move.getDistance();

        if(WebServer.DEBUG_FLAG){
            LOG.info("MoveDistance: " + distance);
        }

        if(distance == 4) capture = new Position((start.getRow() + (changeY / 2)), (start.getCell() + (changeX / 2)));
        else capture = null;

        //Log captures if debug flag exists
        if (WebServer.DEBUG_FLAG) {
            LOG.info("capture: " + capture);
        }

        Piece midPiece1 = capture != null ? gameBoard.getRow(capture.getRow()).getSpace(capture.getCell()).getPiece() : null;


        //Int for inversion - since vertical moves are inverted for White/Red respectively, we can use a simple inversion ( * -1) to combine the statements
        //  -1 for White, 1 for Red
        int inversion = piece.getColor() == Piece.Color.RED ? 1 : -1;

        //Yeah this is one return statement....
        if( (changeX == 1 || changeX == -1 )  && ( (changeY == -1*inversion) || ( (changeY == 1*inversion) && (piece.getType() == Piece.Type.KING) ) ) //Check for simple, non capturing moves
           || ( (changeX == 2 || changeX == -2) && ( (changeY == -2*inversion) || ( (changeY == 2*inversion) && (piece.getType() == Piece.Type.KING) ) ) && midPiece1 != null)) //Check for single capturing move
        {
            return Message.info("Welcome to the world of online Checkers.");
        }
        return Message.error("Invalid move.");
    }

    /**
     * If a piece moves to the 7th or 0th row, it should be made a king
     */
    public void checkForPromotion(Position end, Piece p){
        if(p.getType() == Piece.Type.SINGLE && (end.getRow() == 0 || end.getRow() == 7)){
            p.setType(Piece.Type.KING);
            lastMovePromotion.offerFirst(true);
        }
        lastMovePromotion.offerFirst(false);
    }

    /**
     * @param move The desired move
     * @param player The current player
     */
    public void executeMove(Move move){ 
        
        // variables for move indices
        int startColumn = move.getStart().getCell();
        int startRow = move.getStart().getRow();
        int endColumn = move.getEnd().getCell();
        int endRow = move.getEnd().getRow();

        Piece piece = gameBoard.getRow(startRow).getSpace(startColumn).getPiece();

        if(!backingUp) lastMoves.offerFirst(move);
        else{
            boolean undoPromotion = lastMovePromotion.removeFirst();
            if(undoPromotion) piece.setType(Piece.Type.SINGLE);
        }
        
        //Set the end piece
        gameBoard.getRow(move.getEnd().getRow()).getSpace(move.getEnd().getCell()).setPiece(gameBoard.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).getPiece());

        //Remove the start piece
        gameBoard.getRow(move.getStart().getRow()).getSpace(move.getStart().getCell()).setPiece(null);

        if(!backingUp){
            //Remove captured pieces
            if(capture != null) {
                toRemove.add(new Position(capture.getRow(), capture.getCell()));
                lastMovesCapture.offerFirst(true);   
            }
            else{
                lastMovesCapture.offerFirst(false);
                nonCaptureMoves++;
            }
        }

        checkForPromotion(new Position(endRow, endColumn), piece);
    }

}
