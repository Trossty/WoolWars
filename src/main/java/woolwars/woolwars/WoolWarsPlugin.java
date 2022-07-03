package woolwars.woolwars;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import woolwars.woolwars.api.GUI.GUIAPI;
import woolwars.woolwars.api.assemble.Assemble;
import woolwars.woolwars.api.assemble.AssembleStyle;
import woolwars.woolwars.commands.MainCommand;
import woolwars.woolwars.enums.Locations;
import woolwars.woolwars.game.Game;
import woolwars.woolwars.game.states.LobbyState;
import woolwars.woolwars.managers.ClassManager;
import woolwars.woolwars.managers.GameManager;
import woolwars.woolwars.managers.LocationManager;
import woolwars.woolwars.Objects.Scoreboard.Scoreboard;
import woolwars.woolwars.utils.Colorize;
import woolwars.woolwars.utils.ConfigurationFile;

public final class WoolWarsPlugin extends JavaPlugin {

    private GameManager gameManager;
    private LocationManager locationManager;
    private ConfigurationFile locations;

    private GUIAPI<WoolWarsPlugin> guiapi;

    private ClassManager classManager;
    @Override
    public void onEnable() {

        locationManager = new LocationManager(this);
        locations = new ConfigurationFile(this,"locations");
        gameManager = new GameManager();
        classManager = new ClassManager(this);

        guiapi = new GUIAPI<>(this);

        loadGamerules();

        Assemble assemble = new Assemble(this, new Scoreboard(this));
        assemble.setAssembleStyle(AssembleStyle.MODERN);
        assemble.setTicks(2);

        if(getLocationManager().getReady()){
            gameManager.setGame(new Game(this));
            gameManager.getGame().setGameState(new LobbyState(this));
        }else{
            Bukkit.getConsoleSender().sendMessage(Colorize.format("&7[WoolWars] &cPlease Set Locations!"));
        }

        getCommand("woolwars").setExecutor(new MainCommand(this));
        getCommand("woolwars").setTabCompleter((TabCompleter) new MainCommand(this));

    }

    private void loadGamerules() {
        World world = Bukkit.getWorld("world");
        world.setGameRule(GameRule.DO_WEATHER_CYCLE,false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS,false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false);
        world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS,false);
        world.setGameRule(GameRule.DO_MOB_SPAWNING,false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
    }

    @Override
    public void onDisable() {
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ConfigurationFile getLocations() {
        return locations;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    public GUIAPI<WoolWarsPlugin> getGuiapi() {
        return guiapi;
    }
}
