package com.defty.movie.enums;

import java.util.Map;
import java.util.TreeMap;

public enum Reaction {
    HaHa("HaHa"),
    Sad("Sad"),
    Passion("Passion"),
    Like("Like"),
    Fondness("Fondness");

    private final String reactionName;

    Reaction(String reactionName){
        this.reactionName=reactionName;
    }
    public static Map<String,String> type(){
        Map<String,String> reactions=new TreeMap<>();
        for(Reaction it : Reaction.values()){
            reactions.put(it.toString(),it.reactionName);
        }
        return reactions;
    }
}



