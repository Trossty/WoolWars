package woolwars.woolwars;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import woolwars.woolwars.Api.assemble.Assemble;
import woolwars.woolwars.Api.assemble.AssembleStyle;
import woolwars.woolwars.Commands.MainCommand;
import woolwars.woolwars.Game.Game;
import woolwars.woolwars.Game.States.LobbyState;
import woolwars.woolwars.Managers.GameManager;
import woolwars.woolwars.Managers.LocationManager;
import woolwars.woolwars.Objects.Scoreboard.Scoreboard;
import woolwars.woolwars.Utils.Colorize;
import woolwars.woolwars.Utils.ConfigurationFile;

public final class WoolWarsPlugin extends JavaPlugin {

    private GameManager gameManager;
    private LocationManager locationManager;
    private ConfigurationFile locations;
    @Override
    public void onEnable() {

        locationManager = new LocationManager(this);
        locations = new ConfigurationFile(this,"locations");
        gameManager = new GameManager();

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
}
