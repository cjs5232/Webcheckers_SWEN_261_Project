package com.webcheckers.util;

import java.util.ArrayList;

public class Game {
   
    private Player p1;
    private Player p2;

    private BoardView gameBoard;

    public Game(Player p1, Player p2){

        this.p1 = p1;
        this.p2 = p2;

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
        return new Player[]{p1, p2};
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
        if(player.equals(p1)) team = Piece.Color.RED;
        else team = Piece.Color.WHITE;
        Piece.Type type = gameBoard.getRow(y1).getSpace(x1).getPiece().getType();

        // getting pieces on the tiles
        Piece start = gameBoard.getRow(y1).getSpace(x1).getPiece();
        Piece end = gameBoard.getRow(y2).getSpace(x2).getPiece();

        // must be your piece and placed on a black empty square
        if(start.getColor() != team && x2 + y2 % 2 == 0 && end != null){ return false; }

        // basic move
        if(team == Piece.Color.RED){
            if(changeX == 1 || changeX == -1){
                if(changeY == -1 || (changeY == 1 && type == Piece.Type.KING)){
                    // moves the piece
                    gameBoard.getRow(y2).getSpace(x2).setPiece(gameBoard.getRow(y1).getSpace(x1).getPiece());
                    gameBoard.getRow(y1).getSpace(x1).setPiece(null);
                    return true;
                }
            }
        } else {
            if(changeX == 1 || changeX == -1){
                if(changeY == 1 || (changeY == -1 && type == Piece.Type.KING)){
                    // moves the piece
                    gameBoard.getRow(y2).getSpace(x2).setPiece(gameBoard.getRow(y1).getSpace(x1).getPiece());
                    gameBoard.getRow(y1).getSpace(x1).setPiece(null);
                    return true;
                }
            }
        }
        return false;
    }

}
