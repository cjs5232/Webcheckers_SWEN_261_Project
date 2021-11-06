package com.webcheckers.util;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.webcheckers.util.Piece.Color;

public class Game {
   
    private Player redPlayer;
    private Player whitePlayer;
    private Color activeColor;

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
        
        //Basic move
        if(move.getDistance() == 2){
            gameBoard.getRow(end.getRow()).getSpace(end.getCell()).setPiece(gameBoard.getRow(start.getRow()).getSpace(start.getCell()).getPiece());
            gameBoard.getRow(start.getRow()).getSpace(start.getCell()).setPiece(null);
        }
        //Capturing move
        else{
            //Set the piece in the end position
            gameBoard.getRow(end.getRow()).getSpace(end.getCell()).setPiece(gameBoard.getRow(start.getRow()).getSpace(start.getCell()).getPiece());

            //Clear the space that was captured
            int midRow = (start.getRow() + end.getRow()) / 2;
            int midCell = (start.getCell() + end.getCell()) / 2;
            gameBoard.getRow(midRow).getSpace(midCell).setPiece(null);

            //Clear the space that was moved from
            gameBoard.getRow(start.getRow()).getSpace(start.getCell()).setPiece(null);
        }
    }

    public boolean isMoveValid(Move move, Piece piece){
        Position start = move.getStart();
        Position end = move.getEnd();
        Position middle;

        int distance = move.getDistance();
        middle = distance > 2 ? new Position((start.getRow() + end.getRow()) / 2, (start.getCell() + end.getCell()) / 2) : null;

        Piece midPiece = middle != null ? gameBoard.getRow(middle.getRow()).getSpace(middle.getCell()).getPiece() : null;

        int changeY = end.getRow() - start.getRow();
        int changeX = end.getCell() - start.getCell();

        //Int for inversion - since vertical moves are inverted for White/Red respectively, we can use a simple inversion ( * -1) to combine the statements
        //  -1 for White, 1 for Red
        int inversion = piece.getColor() == Piece.Color.RED ? 1 : -1;

        
        if( (changeX == 1 || changeX == -1 )  && ( (changeY == -1*inversion) || ( (changeY == 1*inversion) && (piece.getType() == Piece.Type.KING) ) ) //Check for simple, non capturing moves
           || ( (changeX == 2 || changeX == -2) && ( (changeY == -2*inversion) || ( (changeY == 2*inversion) && (piece.getType() == Piece.Type.KING) ) ) && midPiece != null)//Check for capturing moves
        ){
            return true;
        }
        return false;

    }

    /**
     * @param move The desired move
     * @param player The current player
     *
     * @return Whether the move is valid or not
     */
    public boolean validateAndMove(Move move, Player player){
        // variables for move indices
        int x1 = move.getStart().getCell();
        int y1 = move.getStart().getRow();
        int x2 = move.getEnd().getCell();
        int y2 = move.getEnd().getRow();

        // determining team
        Piece.Color team;
        if(player.equals(redPlayer)) team = Piece.Color.RED;
        else team = Piece.Color.WHITE;
        Piece piece = gameBoard.getRow(y1).getSpace(x1).getPiece();

        // getting pieces on the tiles
        Piece start = gameBoard.getRow(y1).getSpace(x1).getPiece();
        Piece end = gameBoard.getRow(y2).getSpace(x2).getPiece();

        // must be your piece and placed on a black empty square
        if(start.getColor() != team && x2 + y2 % 2 == 0 && end != null){ return false; }

        if(isMoveValid(move, piece)){
            LOG.info("Move is valid");
            executeMove(move);
            swapActiveColor();
            setPendingMove(null);
            return true;
        }
        else{
            LOG.info("Move is not valid");
            return false;
        }
    }

}
