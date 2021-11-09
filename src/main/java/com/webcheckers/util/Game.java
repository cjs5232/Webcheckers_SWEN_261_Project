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

    //Gets the logger object
    private static final Logger LOG = Logger.getLogger(Game.class.getName());

    //Stores player objects and the active color whos turn it is
    private Player redPlayer;
    private Player whitePlayer;
    private Color activeColor;

    //Positions of captured pieces
    private Position capture;

    //Stores the board
    private BoardView gameBoard;
    
    //Game ID, different for each game
    private int id;

    //Flag for if a move is being reversed or not - controls that captures will not be performed
    private boolean backingUp = false;

    //Overrides if the game is over, used in resignation
    private boolean overrideOverFlag = false;

    //A list of positions to remove pieces from when the call is made
    private List<Position> toRemove = new ArrayList<>();

    //A list of last moves in order to be undone
    private Deque<Move> lastMoves = new ArrayDeque<>();

    //Stores boolean values that reflect whether or not the last move was a capture or not
    private Deque<Boolean> lastMovesCapture = new ArrayDeque<>();

    //Stores boolean values that reflect whether or not the last move was a promotion or not
    private Deque<Boolean> lastMovePromotion = new ArrayDeque<>();

    //Stores which players have exited the game
    private List<Player> exited;

    //Allows only one non-capturing move to be made in one turn
    private int nonCaptureMoves = 0;

    /**
     * Constructor for the game class
     * @param redPlayer The red player
     * @param whitePlayer The white player
     */
    public Game(Player redPlayer, Player whitePlayer){

        this.id = new Random().nextInt(Integer.MAX_VALUE);
        this.exited = new ArrayList<>();
        
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.activeColor = Color.RED;
        this.gameBoard = new BoardView(new ArrayList<>());
    }

    /**
     * @return The piece at position P
     * @param p The position to get the piece from
     */
    public Piece getPieceAtPosition(Position p){
        return gameBoard.getRow(p.getRow()).getSpace(p.getCell()).getPiece();
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
     * @return the end position of the last move the player made
     */
    public Position getLastMoveEndPosition(){
        Deque<Move> lastMoveCopy = new ArrayDeque<>(lastMoves);
        Move lastMove = lastMoveCopy.removeFirst();
        return(lastMove.getEnd());
    }

    /**
     * @return the number of non capturing moves already made in a given turn
     */
    public int getNonCapturingMoves(){
        return nonCaptureMoves;
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

        //Resets the non-capturing move counter at change of turn
        nonCaptureMoves = 0;
        //Resets the last-move vars at change of turn
        lastMoves = new ArrayDeque<>();
        lastMovesCapture = new ArrayDeque<>();
        lastMovePromotion = new ArrayDeque<>();
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

    /**
     * Remove a position from the list: it will not be killed when the call is made
     */
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
     * @param p The position to check
     * @return True if the piece at position p can make further moves, false otherwise
     */
    public boolean positionHasOtherMoves(Position p){

        //If the user has made a non capturing move, they cannot make any more moves
        if(nonCaptureMoves >0) return false;

        int startRowP = p.getRow();
        int startColP = p.getCell();

        //Check if the piece is a king, if it can, there will be more moves
        boolean isPieceKing = this.gameBoard.getRow(startRowP).getSpace(startColP).getPiece().getType() == Piece.Type.KING;

        //Get the piece's color
        Piece.Color pieceColor = this.gameBoard.getRow(startRowP).getSpace(startColP).getPiece().getColor();
        Piece.Color otherColor;

        if(pieceColor == Piece.Color.RED) otherColor = Piece.Color.WHITE;
        else otherColor = Piece.Color.RED;

        Position northWest = (startRowP - 1 >= 0 && startColP -1 >=0) ? new Position(startRowP - 1, startColP - 1) : null;
        Position northWest2 = (startRowP - 2 >= 0 && startColP -2 >=0) ? new Position(startRowP - 2, startColP - 2) : null;

        Position northEast = (startRowP - 1 >= 0 && startColP + 1 < 8) ? new Position(startRowP - 1, startColP + 1) : null;
        Position northEast2 = (startRowP - 2 >= 0 && startColP + 2 < 8) ? new Position(startRowP - 2, startColP + 2) : null;
        
        Position southWest = (startRowP + 1 < 8 && startColP - 1 >= 0) ? new Position(startRowP + 1, startColP - 1) : null;
        Position southWest2 = (startRowP + 2 < 8 && startColP - 2 >= 0) ? new Position(startRowP + 2, startColP - 2) : null;

        Position southEast = (startRowP + 1 < 8 && startColP + 1 < 8) ? new Position(startRowP + 1, startColP + 1) : null;
        Position southEast2 = (startRowP + 2 < 8 && startColP + 2 < 8) ? new Position(startRowP + 2, startColP + 2) : null;

        //Red piece, north moves as single, white piece, south moves as single
        return(
            ((pieceColor == Piece.Color.WHITE || isPieceKing) && southEast != null && getPieceAtPosition(southEast) != null && getPieceAtPosition(southEast).getColor().equals(otherColor) && getPieceAtPosition(southEast2) == null) ||
            ((pieceColor == Piece.Color.WHITE || isPieceKing) && southWest != null && getPieceAtPosition(southWest) != null && getPieceAtPosition(southWest).getColor().equals(otherColor) && getPieceAtPosition(southWest2) == null) ||
            ((pieceColor == Piece.Color.RED || isPieceKing) && northEast != null && getPieceAtPosition(northEast) != null && getPieceAtPosition(northEast).getColor().equals(otherColor) && getPieceAtPosition(northEast2) == null) ||
            ((pieceColor == Piece.Color.RED || isPieceKing) && northWest != null && getPieceAtPosition(northWest) != null && getPieceAtPosition(northWest).getColor().equals(otherColor) && getPieceAtPosition(northWest2) == null)
        );
    }

    /**
     * Undo the last move, including putting back the captured piece (if there was one)
     */
    public void undoLastMove(){

        //Moves should be undone 'last first'
        Move lastMove = lastMoves.removeFirst();

        //Get the positions involved in the move 
        Position lastMoveStart = lastMove.getStart();
        Position lastMoveEnd = lastMove.getEnd();

        //Create a new Move with the opposite start and end positions
        Move inverseLastMove = new Move(lastMoveEnd, lastMoveStart);

        //Set the backing up flag temporarily to true
        backingUp = true;
        //Execute the reverse move
        executeMove(inverseLastMove);
        //Reset the backing up flag
        backingUp = false;

        //If a player is undoing their only non-capturing move, the count should be reset
        if (!lastMovesCapture.removeFirst().booleanValue()) nonCaptureMoves--;
        else{
            Position middle = new Position((lastMoveStart.getRow() + lastMoveEnd.getRow()) / 2, (lastMoveStart.getCell() + lastMoveEnd.getCell()) / 2);
            removeDeadPieceFromList(middle);
        }
    }

    /**
     * @return true if the game is won by a player
     */
    public boolean isOver(){
        
        //Count of pieces still on the board
        int redPieces = 0;
        int whitePieces = 0;

        //Iterate through the board and count the number of each color of pieces
        for(Row r : gameBoard.getRows()){
            for(Space s : r.getSpaces()){
                if(s.getPiece() != null){
                    if(s.getPiece().getColor() == Color.RED) redPieces++;
                    else whitePieces++;
                }
            }
        }

        //If either player has no pieces left, or the override flag is active, the game is over
        return(redPieces == 0 || whitePieces == 0 || overrideOverFlag);
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

        if(distance == 2 && positionHasOtherMoves(start)) return Message.error("You cannot make a capturing move from a position with capturing moves available.");

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

        //A bit of a mess, but functional
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

        //Get the piece at the start position
        Piece piece = gameBoard.getRow(startRow).getSpace(startColumn).getPiece();

        //If the move is an 'undo', do not add the move to the list
        if(!backingUp) lastMoves.offerFirst(move);
        else{
            //Gets the boolean value as to whether or not the last move was a promotion
            boolean undoPromotion = lastMovePromotion.removeFirst();
            //If it was, set it back to a single piece
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

        //Check for promotion at the end of every turn
        checkForPromotion(new Position(endRow, endColumn), piece);
    }

    /**
     * Sets the resignation flag to true
     */
    public void resign(){
        overrideOverFlag = true;   
    }
}
