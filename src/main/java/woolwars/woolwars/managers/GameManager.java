package woolwars.woolwars.managers;

import woolwars.woolwars.game.Game;

public class GameManager {

    private Game game;

    public Game getGame(){
        return this.game;
    }

    public Game setGame(Game game){
        this.game = game;
        return this.game;
    }

}
