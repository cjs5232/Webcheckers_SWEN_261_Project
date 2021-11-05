package com.webcheckers.util;

import java.util.ArrayList;

import com.webcheckers.util.Piece.Color;

public class Game {
   
    private Player redPlayer;
    private Player whitePlayer;
    private Color activeColor;

    private BoardView gameBoard;

    public Game(Player redPlayer, Player whitePlayer){

        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.activeColor = Color.RED;
        this.gameBoard = new BoardView(new ArrayList<>());

        
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
        activeColor = activeColor == Color.RED ? Color.WHITE : Color.RED;
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
        int changeX = x2 - x1;
        int changeY = y2 - y1;

        // determining team
        Piece.Color team;
        if(player.equals(redPlayer)) team = Piece.Color.RED;
        else team = Piece.Color.WHITE;
        Piece.Type type = gameBoard.getRow(y1).getSpace(x1).getPiece().getType();

        // getting pieces on the tiles
        Piece start = gameBoard.getRow(y1).getSpace(x1).getPiece();
        Piece end = gameBoard.getRow(y2).getSpace(x2).getPiece();

        // must be your piece and placed on a black empty square
        if(start.getColor() != team && x2 + y2 % 2 == 0 && end != null){ return false; }

        
        //Int for inversion - since vertical moves are inverted for White/Red respectively, we can use a simple inversion ( * -1) to combine the statements
        //  -1 for White, 1 for Red
        int inversion = team == Piece.Color.RED ? 1 : -1;

        if( (changeX == 1 || changeX == -1 )  && ( (changeY == -1*inversion) || ( (changeY == 1*inversion) && (type == Piece.Type.KING) ) )){
            gameBoard.getRow(y2).getSpace(x2).setPiece(gameBoard.getRow(y1).getSpace(x1).getPiece());
            gameBoard.getRow(y1).getSpace(x1).setPiece(null);
            return true;
        }
        return false;
    }

}
