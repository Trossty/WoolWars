package woolwars.woolwars.Game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import woolwars.woolwars.Enums.Items;
import woolwars.woolwars.Enums.Locations;
import woolwars.woolwars.Enums.TeamType;
import woolwars.woolwars.Utils.Colorize;
import woolwars.woolwars.WoolWarsPlugin;

import java.util.*;

public class Game {
    private WoolWarsPlugin plugin;

    private GameState gameState;

    private List<UUID> playerList;

    private List<ItemArmorStand> itemLList;

    private GameTeam red;
    private GameTeam blue;

    private int time = 0;

    public Game(WoolWarsPlugin plugin){

        this.plugin = plugin;

        red = new GameTeam(TeamType.RED);
        blue = new GameTeam(TeamType.BLUE);

        playerList = new ArrayList();

        itemLList = new LinkedList<>();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        if(this.gameState!=null){
            if(this.gameState.getClass() == gameState.getClass()){
                return;
            }
            this.gameState.onDisable();
        }
        gameState.onEnable();
        this.gameState = gameState;
    }

    public GameTeam getBlueTeam() {
        return blue;
    }

    public GameTeam getRedTeam() {
        return red;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<UUID> getPlayerList() {
        return playerList;
    }

    public void shout(String text){
        playerList.stream().map(Bukkit::getPlayer).forEach(player -> {
            player.sendMessage(Colorize.format(text));
        });
    }

    public void titleShout(String title,String text2,int fadeIn,int stay,int fadeOut){
        playerList.stream().map(Bukkit::getPlayer).forEach(player -> {
            player.sendTitle(Colorize.format(title),Colorize.format(text2),fadeIn,stay,fadeOut);
        });
    }

    public boolean isintheArea(Location location){

        Location centerWoolLoc = plugin.getLocationManager().getLocations(Locations.centerWool);

        if(centerWoolLoc.getBlockX()+2 > location.getBlockX() && location.getBlockX() > centerWoolLoc.getBlockX()-2){
            if(centerWoolLoc.getBlockZ()+2 > location.getBlockZ() && location.getBlockZ() > centerWoolLoc.getBlockZ()-2){
                if(centerWoolLoc.getBlockY()==location.getBlockY()){
                    return true;
                }
            }
        }

        return false;
    }


    //Items:
    //strength
    //speed
    //jump boost
    //heal
    //pick
    //sword
    //bow

    public void spawnItem(){
        Random random = new Random();

        for (String locationName: plugin.getLocations().getConfiguration().getConfigurationSection("ItemSpawn").getKeys(false)){
            Location loc = plugin.getLocations().getConfiguration().getLocation("ItemSpawn."+locationName);
            int randomInt = random.nextInt(Items.values.length);
            shout(Items.values[randomInt].getName());
            ItemArmorStand itemArmorStand = new ItemArmorStand(loc,Items.values[randomInt]);
            itemLList.add(itemArmorStand);
        }
    }

    public List<ItemArmorStand> getItemLList() {
        return itemLList;
    }
}
