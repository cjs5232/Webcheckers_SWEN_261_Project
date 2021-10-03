package com.webcheckers.util;

public class Player {
    
    public final String name;
    public final int id;

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

}
