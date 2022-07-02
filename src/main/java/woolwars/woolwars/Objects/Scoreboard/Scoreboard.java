package woolwars.woolwars.Objects.Scoreboard;

import org.bukkit.entity.Player;
import woolwars.woolwars.api.assemble.AssembleAdapter;
import woolwars.woolwars.game.GamePlayer;
import woolwars.woolwars.utils.Colorize;
import woolwars.woolwars.WoolWarsPlugin;

import java.util.ArrayList;
import java.util.List;

public class Scoreboard implements AssembleAdapter {

    private WoolWarsPlugin plugin;

    public Scoreboard(WoolWarsPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public String getTitle(Player player) {
        return Colorize.format("&cWOOL&9WARS");
    }

    @Override
    public List<String> getLines(Player player) {

        ArrayList<String> lines = new ArrayList<>();
        GamePlayer gamePlayer = GamePlayer.getGamePlayer(player).orElse(null);

        if(getPlugin().getGameManager().getGame()==null){
            lines.clear();
            lines.add("");
            lines.add("&cSet Locations!");
            lines.add("/ww set location");
            lines.add("");

            return lines;
        }

        int min = 0;
        int sec = 0;

        int allPlayers = getPlugin().getGameManager().getGame().getPlayerList().size();

        if(getPlugin().getGameManager().getGame().getTime() > 0){
            min = getPlugin().getGameManager().getGame().getTime() / 60;
            sec = getPlugin().getGameManager().getGame().getTime() % 60;
        }


        if(getPlugin().getGameManager().getGame().getGameState().getName().equalsIgnoreCase("lobby")){
            lines.clear();
            lines.add("");
            lines.add(Colorize.format("&6Players: &7"+ allPlayers+"/8"));
            lines.add(Colorize.format("&6State:&7 Lobby"));
            lines.add("");
            lines.add(Colorize.format(time()));
            lines.add("");
            return lines;
        }else if(getPlugin().getGameManager().getGame().getGameState().getName().equalsIgnoreCase("preround")){
            lines.clear();
            lines.add("");
            lines.add(Colorize.format("&6State:&7 Pre round"));
            lines.add(Colorize.format("&6Round:&7 "+getPlugin().getGameManager().getGame().getRound()));
            lines.add("");
            lines.add(Colorize.format("&c[R] "+getPlugin().getGameManager().getGame().redScore()));
            lines.add(Colorize.format("&9[B] "+getPlugin().getGameManager().getGame().blueScore()));
            lines.add("");
            lines.add(Colorize.format("&6Team: "+gamePlayer.getTeam().getTeamType().getColoredName()));
            lines.add("");
            lines.add(Colorize.format("&6Time: &7"+min+":"+sec));
            return lines;
        }else if(getPlugin().getGameManager().getGame().getGameState().getName().equalsIgnoreCase("playing")){
            lines.clear();
            lines.add("");
            lines.add(Colorize.format("&6State:&7 Playing"));
            lines.add(Colorize.format("&6Round:&7 "+getPlugin().getGameManager().getGame().getRound()));
            lines.add("");
            lines.add(Colorize.format("&6Score:"));
            lines.add(Colorize.format("&c[R] "+getPlugin().getGameManager().getGame().redScore()));
            lines.add(Colorize.format("&9[B] "+getPlugin().getGameManager().getGame().blueScore()));
            lines.add("");
            lines.add(Colorize.format("&cRed Player: "+ getPlugin().getGameManager().getGame().getRedTeam().getRemaningPlayerCount()));
            lines.add(Colorize.format("&9Blue Player: "+ getPlugin().getGameManager().getGame().getBlueTeam().getRemaningPlayerCount()));
            lines.add("");
            lines.add(Colorize.format("&6Time: &7"+min+":"+sec));
            return lines;
        }else if(getPlugin().getGameManager().getGame().getGameState().getName().equalsIgnoreCase("end")){
            lines.clear();
            lines.add("");
            lines.add(Colorize.format("&6State:&7 Game Over!"));
            lines.add(Colorize.format("&6Round:&7 "+getPlugin().getGameManager().getGame().getRound()));
            lines.add("");
            lines.add(Colorize.format("&6Score:"));
            lines.add(Colorize.format("&c[R] "+getPlugin().getGameManager().getGame().redScore()));
            lines.add(Colorize.format("&9[B] "+getPlugin().getGameManager().getGame().blueScore()));
            lines.add("");
            lines.add(Colorize.format("&6WINNER:"));
            lines.add(Colorize.format((getPlugin().getGameManager().getGame().getBlueTeam().getScore() == 3)?"&9BLUE":"&4RED"));
            lines.add("");
            lines.add(Colorize.format("&6Time: &7"+min+":"+sec));
            return lines;
        }

        return null;
    }

    public WoolWarsPlugin getPlugin() {
        return plugin;
    }

    public String time() {

        int min = 0;
        int sec = 0;

        if(getPlugin().getGameManager().getGame().getTime() > 0){
            min = getPlugin().getGameManager().getGame().getTime() / 60;
            sec = getPlugin().getGameManager().getGame().getTime() % 60;
        }else{
            return "&fWaiting...";
        }

        if(getPlugin().getGameManager().getGame().getPlayerList().size()<8){
            return "&fWaiting...";
        }

        return ("&6Time: &7"+min+":"+sec);
    }
}
