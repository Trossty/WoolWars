package woolwars.woolwars.Objects.Scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import woolwars.woolwars.Api.assemble.AssembleAdapter;
import woolwars.woolwars.Game.GamePlayer;
import woolwars.woolwars.Utils.Colorize;
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

        int min = 0;
        int sec = 0;

        int allPlayers = getPlugin().getGameManager().getGame().getPlayerList().size();

        if(getPlugin().getGameManager().getGame().getTime() > 0){
            min = getPlugin().getGameManager().getGame().getTime() / 60;
            sec = getPlugin().getGameManager().getGame().getTime() % 60;
        }

        if(getPlugin().getGameManager().getGame()==null){
            lines.clear();
            lines.add("");
            lines.add("&cSet Locations!");
            lines.add("/ww set location");
            lines.add("");

            return lines;
        }

        if(getPlugin().getGameManager().getGame().getGameState().getName().equalsIgnoreCase("lobby")){
            lines.clear();
            lines.add("");
            lines.add(Colorize.format("&6Players:"));
            lines.add(Colorize.format(" &7"+ allPlayers));
            lines.add("");
            lines.add(Colorize.format("&6Time:"));
            lines.add(Colorize.format(" &7"+min+":"+sec));
            lines.add("");
            return lines;
        }else if(getPlugin().getGameManager().getGame().getGameState().getName().equalsIgnoreCase("preround")){
            lines.clear();
            lines.add("");
            lines.add(Colorize.format("&6Players:"));
            lines.add(Colorize.format(" &7"+ allPlayers));
            lines.add("");
            lines.add(Colorize.format("&6Time:"));
            lines.add(Colorize.format(" &7"+min+":"+sec));
            lines.add("");
            lines.add(Colorize.format("&6Team:"));
            lines.add(Colorize.format(gamePlayer.getTeam().getTeamType().getColoredName()));
            lines.add("");
            return lines;
        }

        return null;
    }

    public WoolWarsPlugin getPlugin() {
        return plugin;
    }

}