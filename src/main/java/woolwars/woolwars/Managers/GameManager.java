package woolwars.woolwars.Managers;

import woolwars.woolwars.Game.Game;

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
