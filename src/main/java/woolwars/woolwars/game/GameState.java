package woolwars.woolwars.game;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import woolwars.woolwars.WoolWarsPlugin;

public class GameState implements Listener {

    private final WoolWarsPlugin plugin;

    private final String name;

    public GameState(final WoolWarsPlugin plugin, String name){
        this.plugin = plugin;
        this.name = name;
    }

    public void onEnable(){
        Bukkit.getPluginManager().registerEvents(this,getPlugin());
    }

    public void onDisable(){
        HandlerList.unregisterAll(this);
    }

    protected Game getGame(){
        return plugin.getGameManager().getGame();
    }

    protected WoolWarsPlugin getPlugin(){
        return this.plugin;
    }

    public String getName() {
        return name;
    }

}
