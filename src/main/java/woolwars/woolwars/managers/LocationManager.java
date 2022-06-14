package woolwars.woolwars.managers;

import org.bukkit.Location;
import woolwars.woolwars.enums.Locations;
import woolwars.woolwars.WoolWarsPlugin;

import java.util.ArrayList;
import java.util.List;


public class LocationManager {

    private WoolWarsPlugin plugin;

    private List<Location> itemList = new ArrayList();

    public LocationManager(WoolWarsPlugin plugin){
        this.plugin = plugin;
    }

    public void setLocation(Locations type, org.bukkit.Location location) {
        switch (type){
            case redSpawn:
                plugin.getLocations().getConfiguration().set("redSpawn",location);
                break;
            case blueSpawn:
                plugin.getLocations().getConfiguration().set("blueSpawn",location);
                break;
            case lobbySpawn:
                plugin.getLocations().getConfiguration().set("lobbySpawn",location);
                break;
            case centerWool:
                plugin.getLocations().getConfiguration().set("centerWool",location);
                break;
        }
    }

    public Location getLocations(Locations type){
        switch (type){
            case redSpawn:
                return plugin.getLocations().getConfiguration().getLocation("redSpawn");
            case blueSpawn:
                return plugin.getLocations().getConfiguration().getLocation("blueSpawn");
            case lobbySpawn:
                return plugin.getLocations().getConfiguration().getLocation("lobbySpawn");
            case centerWool:
                return plugin.getLocations().getConfiguration().getLocation("centerWool");
        }
        return null;
    }

    public List getItemList(){
        return plugin.getLocations().getConfiguration().getList("itemList");
    }

    public void setReady(boolean bool){
        plugin.getLocations().getConfiguration().set("Locations.Ready",bool);
    }

    public boolean getReady(){
        return plugin.getLocations().getConfiguration().getBoolean("Locations.Ready");
    }

    public void save(){
        plugin.getLocations().save();
    }

}
