package game;

import java.util.HashMap;
import java.util.Map;


// CUrrent state of the game.
public class GameState {

    boolean isOver;
    String Winner;

    public GameState(boolean isOver, String Winner){
        this.isOver = isOver;
        this.Winner = Winner;
    }

    public boolean isOver(){
        return this.isOver;
    }

    public String getWinner() {
        return this.Winner;
    }
    public Map<String,String> showResult(){
        Map<String,String> result = new HashMap<>();
        result.put("isOver", isOver ? "true" : "false");
        result.put("Winner", Winner);
        return result;
    }
}
