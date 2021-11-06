package com.webcheckers.util;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.webcheckers.ui.WebServer;
import com.webcheckers.util.Piece.Color;

public class Game {
   
    private Player redPlayer;
    private Player whitePlayer;
    private Color activeColor;

    //Positions of captured pieces
    private Position capture1;
    private Position capture2;
    private Position capture3;

    private static final Logger LOG = Logger.getLogger(Game.class.getName());

    private BoardView gameBoard;

    /**
     * Set by AJax/JS code, used to store the potential move
     */
    private Move pendingMove;

    public Game(Player redPlayer, Player whitePlayer){

        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.activeColor = Color.RED;
        this.gameBoard = new BoardView(new ArrayList<>());

        this.pendingMove = null;
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
    }

    /**
     * @return the active color of the game
     */
    public Color getActiveColor(){
        return activeColor;
    }

    public void setPendingMove(Move move){
        this.pendingMove = move;
    }

    public Move getPendingMove(){
        return this.pendingMove;
    }

    public void executeMove(Move move){
        
        Position start = move.getStart();
        Position end = move.getEnd();
        
        //Set the end piece
        gameBoard.getRow(end.getRow()).getSpace(end.getCell()).setPiece(gameBoard.getRow(start.getRow()).getSpace(start.getCell()).getPiece());

        //Remove the start piece
        gameBoard.getRow(start.getRow()).getSpace(start.getCell()).setPiece(null);

        //Remove captured pieces
        if(capture1 != null) gameBoard.getRow(capture1.getRow()).getSpace(capture1.getCell()).setPiece(null);
        if(capture2 != null) gameBoard.getRow(capture2.getRow()).getSpace(capture2.getCell()).setPiece(null);
        if(capture3 != null) gameBoard.getRow(capture3.getRow()).getSpace(capture3.getCell()).setPiece(null);
    }

    /** 
     * There is a lot of complex logic in here to determine if a move is valid
     * @return true if the move is valid, false otherwise
    */
    public boolean isMoveValid(Move move, Piece piece){

        //Start and end positions of the piece
        Position start = move.getStart();
        Position end = move.getEnd();

        int distance = move.getDistance();

        //Determine captures if any
        switch(distance){
            //Single captures
            case 4:
                capture1 = new Position((start.getRow() + end.getRow()) / 2, (start.getCell() + end.getCell()) / 2);
                capture2 = null;
                capture3 = null;
                break;

            //Double capture
            case 8:
                Position capturingPieceMiddle1 = new Position((start.getRow() + end.getRow()) / 2, (start.getCell() + end.getCell()) / 2);

                capture1 = new Position((start.getRow() + capturingPieceMiddle1.getRow()) / 2, (start.getCell() + capturingPieceMiddle1.getCell()) / 2);
                capture2 = new Position((capturingPieceMiddle1.getRow() + end.getRow()) / 2, (capturingPieceMiddle1.getCell() + end.getCell()) / 2);
                capture3 = null;
                break;

            //Triple capture
            case 12:
                //The direct center of a triple jump will be a capture
                Position capturingPieceMiddle2 = new Position((start.getRow() + end.getRow()) / 2, (start.getCell() + end.getCell()) / 2);

                capture2 = capturingPieceMiddle2;

                int xInversion = (end.getCell() - start.getCell()) > 0 ? 1 : -1;
                int yInversion = (end.getRow() - start.getRow()) > 0 ? 1 : -1;

                capture1 = new Position((capturingPieceMiddle2.getRow() - (yInversion * 2)), (capturingPieceMiddle2.getCell() - (xInversion * 2)));
                capture3 = new Position((capturingPieceMiddle2.getRow() + (yInversion * 2)), (capturingPieceMiddle2.getCell() + (xInversion * 2))); 
                break;

            //No capture
            default:
                capture1 = null;
                capture2 = null;
                capture3 = null;
                break;
        }

        //Log captures if debug flag exists
        if (WebServer.DEBUG_FLAG) {
            LOG.info("capture1: " + capture1);
            LOG.info("capture2: " + capture2);
            LOG.info("capture3: " + capture3);
        }

        Piece midPiece1 = capture1 != null ? gameBoard.getRow(capture1.getRow()).getSpace(capture1.getCell()).getPiece() : null;
        Piece midPiece2 = capture2 != null ? gameBoard.getRow(capture2.getRow()).getSpace(capture2.getCell()).getPiece() : null;
        Piece midPiece3 = capture3 != null ? gameBoard.getRow(capture3.getRow()).getSpace(capture3.getCell()).getPiece() : null;

        //The positions between caputures, aka jump positions
        Position inBetweenCaptures1 = capture2 != null ? new Position((capture1.getRow() + capture2.getRow()) / 2, (capture1.getCell() + capture2.getCell()) / 2) : null;
        Position inBetweenCaptures2 = capture3 != null ? new Position((capture2.getRow() + capture3.getRow()) / 2, (capture2.getCell() + capture3.getCell()) / 2) : null;

        Piece inBetween1 = inBetweenCaptures1 != null ? gameBoard.getRow(inBetweenCaptures1.getRow()).getSpace(inBetweenCaptures1.getCell()).getPiece() : null;
        Piece inBetween2 = inBetweenCaptures2 != null ? gameBoard.getRow(inBetweenCaptures2.getRow()).getSpace(inBetweenCaptures2.getCell()).getPiece() : null;

        int changeY = end.getRow() - start.getRow();
        int changeX = end.getCell() - start.getCell();

        //Int for inversion - since vertical moves are inverted for White/Red respectively, we can use a simple inversion ( * -1) to combine the statements
        //  -1 for White, 1 for Red
        int inversion = piece.getColor() == Piece.Color.RED ? 1 : -1;

        //Yeah this is one return statement....
        return( (changeX == 1 || changeX == -1 )  && ( (changeY == -1*inversion) || ( (changeY == 1*inversion) && (piece.getType() == Piece.Type.KING) ) ) //Check for simple, non capturing moves
           || ( (changeX == 2 || changeX == -2) && ( (changeY == -2*inversion) || ( (changeY == 2*inversion) && (piece.getType() == Piece.Type.KING) ) ) && midPiece1 != null) //Check for single capturing move
           || ( (changeX == 4 || changeX == -4) && ( ( changeY == -4*inversion) || ( (changeY == 4*inversion) && (piece.getType() == Piece.Type.KING)) ) && midPiece1 != null && midPiece2 != null && inBetween1 == null) //Check for double capturing move
           || ( (changeX == 6 || changeX == -6) && ( ( changeY == -6*inversion) || ( (changeY == 6*inversion) && (piece.getType() == Piece.Type.KING)) ) && midPiece1 != null && midPiece2 != null && midPiece3 != null && inBetween1 == null && inBetween2 == null) //Check for triple capturing move
           );

    }

    //If a piece moves to the 7th or 0th row, it should be made a king
    public void checkForPromotion(Position end, Piece p){
        if(p.getType() == Piece.Type.SINGLE && (end.getRow() == 0 || end.getRow() == 7)){
            p.setType(Piece.Type.KING);
        }
    }

    /**
     * @param move The desired move
     * @param player The current player
     *
     * @return Whether the move is valid or not
     */
    public boolean validateAndMove(Move move, Player player){
        // variables for move indices
        int startColumn = move.getStart().getCell();
        int startRow = move.getStart().getRow();
        int endColumn = move.getEnd().getCell();
        int endRow = move.getEnd().getRow();

        // determining team
        Piece.Color team;
        if(player.equals(redPlayer)) team = Piece.Color.RED;
        else team = Piece.Color.WHITE;
        Piece piece = gameBoard.getRow(startRow).getSpace(startColumn).getPiece();

        // getting pieces on the tiles
        Piece start = gameBoard.getRow(startRow).getSpace(startColumn).getPiece();
        Piece end = gameBoard.getRow(endRow).getSpace(endColumn).getPiece();

        // must be your piece and placed on a black empty square
        if(start.getColor() != team && endColumn + endRow % 2 == 0 && end != null){ return false; }

        if(isMoveValid(move, piece)){
            if(WebServer.DEBUG_FLAG) LOG.info("Move is valid");
            executeMove(move);
            checkForPromotion(new Position(endRow, endColumn), piece);
            swapActiveColor();
            setPendingMove(null);
            return true;
        }
        else{
            if(WebServer.DEBUG_FLAG) LOG.info("Move is not valid");
            return false;
        }
    }

}
