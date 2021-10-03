package com.webcheckers.util;

public class Player {
    
    private final String name;
    private final int id;

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

}
