package com.example.draft;

public class Player {
    public String name;
    public int image;

    public Player(){
        super();
    }

    public Player(String name){
        super();
        this.name = name;
    }

    public Player(int image, String name){
        super();
        this.image = image;
        this.name = name;
    }
}
