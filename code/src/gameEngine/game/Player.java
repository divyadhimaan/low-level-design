package game;

import java.util.Objects;

public class Player {
//    private User id;
    private int TimeUsedInMillis;
    private final String playerSymbol;

    public Player(String symbol){
        this.playerSymbol = symbol;
    }

    public String symbol(){
        return playerSymbol;
    }

    public Player playerFlip(){
        return new Player(playerSymbol.equals("X")?"O":"X");
    }

    public void setTimeTaken(int timeInMillis){
        this.TimeUsedInMillis += timeInMillis;
    }
    public int getTimeUsedInMillis(){
        return TimeUsedInMillis;
    }
}
