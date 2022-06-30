package woolwars.woolwars.game.states;

import org.bukkit.Bukkit;
import woolwars.woolwars.WoolWarsPlugin;
import woolwars.woolwars.enums.TeamType;
import woolwars.woolwars.game.GamePlayer;
import woolwars.woolwars.game.GameState;
import woolwars.woolwars.utils.Colorize;

public class EndState extends GameState {

    private TeamType winnerTeam;

    public EndState(WoolWarsPlugin plugin) {
        super(plugin, "End");
    }

    @Override
    public void onEnable(){
        super.onEnable();

        getGame().getPlayerList().stream().map(Bukkit::getPlayer).forEach(player -> {
            GamePlayer gamePlayer = GamePlayer.getGamePlayer(player).get();

            if(getGame().getBlueTeam().getScore()>getGame().getRedTeam().getScore()){
                winnerTeam = TeamType.BLUE;
            }else {
                winnerTeam = TeamType.RED;
            }

            if(winnerTeam == gamePlayer.getTeam().getTeamType()){
                player.sendTitle(Colorize.format("&aVICTORY!"),Colorize.format("LEZ GOOOO!"));
            }else {
                player.sendTitle(Colorize.format("&cLOSE!"),Colorize.format("NT"));
            }

        });

    }

    @Override
    public void onDisable(){
        super.onDisable();
    }


}
