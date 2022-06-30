package woolwars.woolwars.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import woolwars.woolwars.enums.Items;
import woolwars.woolwars.enums.Locations;
import woolwars.woolwars.enums.TeamType;
import woolwars.woolwars.utils.Colorize;
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

    private ArmorStand armorStand;
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
            Location aLoc = plugin.getLocations().getConfiguration().getLocation("ItemSpawn."+locationName);
            Location loc = new Location(aLoc.getWorld(),aLoc.getBlockX(),aLoc.getBlockY(),aLoc.getBlockZ());

            int randomInt = random.nextInt(Items.values.length);
            ItemArmorStand itemArmorStand = new ItemArmorStand(loc,Items.values[randomInt]);
            itemArmorStand.runTaskTimer(plugin,0,1);
            itemLList.add(itemArmorStand);

        }
    }

    public List<ItemArmorStand> getItemLList() {
        return itemLList;
    }
}
