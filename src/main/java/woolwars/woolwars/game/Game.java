package woolwars.woolwars.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import woolwars.woolwars.enums.ClassType;
import woolwars.woolwars.enums.Items;
import woolwars.woolwars.enums.Locations;
import woolwars.woolwars.enums.TeamType;
import woolwars.woolwars.utils.Colorize;
import woolwars.woolwars.WoolWarsPlugin;
import woolwars.woolwars.utils.ItemBuilder;

import java.util.*;

public class Game {
    private WoolWarsPlugin plugin;

    private GameState gameState;

    private List<UUID> playerList;

    private List<ItemArmorStand> itemLList;

    private GameTeam red;
    private GameTeam blue;

    private int time = 0;

    private int round = 0;

    private boolean canBreakPlace;

    public Game(WoolWarsPlugin plugin){

        this.plugin = plugin;

        red = new GameTeam(TeamType.RED);
        blue = new GameTeam(TeamType.BLUE);

        playerList = new ArrayList();

        itemLList = new LinkedList<>();

        canBreakPlace = true;
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

    public String blueScore(){
        String score = "";

        int bScore = blue.getScore();

        switch (bScore){
            case 0:
                score = "&l&7● ● ●";
                break;
            case 1:
                score = "&l&9● &7● ●";
                break;
            case 2:
                score = "&l&9● ● &7●";
                break;
            case 3:
                score = "&l&9● ● ●";
                break;
        }

        return score;
    }

    public String redScore(){
        String score = "";

        int bScore = red.getScore();

        switch (bScore){
            case 0:
                score = "&l&7● ● ●";
                break;
            case 1:
                score = "&l&c● &7● ●";
                break;
            case 2:
                score = "&l&c● ● &7●";
                break;
            case 3:
                score = "&l&c● ● ●";
                break;
        }

        return score;
    }

    public void giveClass(Player player, ClassType classType){

        player.getInventory().clear();

        GamePlayer gamePlayer = GamePlayer.getGamePlayer(player).get();

        switch (classType){
            case Tank:
                player.getInventory().setItem(0,new ItemStack(Material.WOODEN_SWORD));
                player.getInventory().setItem(1,new ItemStack(Material.WOODEN_PICKAXE));
                player.getInventory().setItem(8,new ItemBuilder(Material.BLAZE_POWDER).withDisplayName("&6GIGAHEALTH").getItemStack());
                break;
            case Golem:
                player.getInventory().setItem(0,new ItemStack(Material.STONE_SWORD));
                player.getInventory().setItem(8,new ItemBuilder(Material.BLAZE_POWDER).withDisplayName("&6GOLDEN SHELL").getItemStack());
                break;
            case Archer:
                player.getInventory().setItem(0,new ItemStack(Material.BOW));
                player.getInventory().setItem(1,new ItemStack(Material.WOODEN_PICKAXE));
                player.getInventory().setItem(2,new ItemStack(Material.ARROW,6));
                player.getInventory().setItem(8,new ItemBuilder(Material.BLAZE_POWDER).withDisplayName("&6STEP BACK").getItemStack());
                break;
            case Assault:
                player.getInventory().setItem(0,new ItemStack(Material.WOODEN_SWORD));
                player.getInventory().setItem(1,new ItemStack(Material.IRON_PICKAXE));
                player.getInventory().setItem(2,new ItemStack(Material.STONE_SHOVEL));

                /*
                ItemStack healPot = new ItemStack(Material.POTION);
                Potion heal = new Potion(PotionType.INSTANT_HEAL);
                heal.setSplash(true);
                heal.setLevel(2);
                heal.apply(healPot);

                ItemStack damagePot = new ItemStack(Material.POTION);
                Potion damage = new Potion(PotionType.INSTANT_DAMAGE);
                damage.setSplash(true);
                damage.setLevel(1);
                damage.apply(damagePot);

                player.getInventory().setItem(3,healPot);
                player.getInventory().setItem(4,damagePot);

                 */
                player.getInventory().setItem(8,new ItemBuilder(Material.BLAZE_POWDER).withDisplayName("&6KNOCKBACK TNT").getItemStack());
                break;
            case Engineer:
                player.getInventory().setItem(0,new ItemStack(Material.WOODEN_SWORD));
                player.getInventory().setItem(1,new ItemStack(Material.BOW));
                player.getInventory().setItem(2,new ItemStack(Material.ARROW,4));
                player.getInventory().setItem(3,new ItemStack(Material.STONE_PICKAXE));
                player.getInventory().setItem(8,new ItemBuilder(Material.BLAZE_POWDER).withDisplayName("&6HACK").getItemStack());
                break;
            case Swordsman:
                player.getInventory().setItem(1,new ItemStack(Material.STONE_SWORD));
                player.getInventory().setItem(2,new ItemStack(Material.WOODEN_PICKAXE));
                /*
                ItemStack healPot1 = new ItemStack(Material.POTION);
                Potion heal1 = new Potion(PotionType.INSTANT_HEAL);
                heal1.setSplash(true);
                heal1.setLevel(2);
                heal1.apply(healPot1);
                player.getInventory().setItem(3,healPot1);

                 */
                player.getInventory().setItem(8,new ItemBuilder(Material.BLAZE_POWDER).withDisplayName("&6SPRINT").getItemStack());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + classType);
        }

        player.getInventory().addItem(new ItemBuilder(Material.SHEARS).setUnbreakable(true).getItemStack());

        switch (gamePlayer.getTeam().getTeamType()){
            case RED -> player.getInventory().addItem(new ItemBuilder(Material.RED_WOOL,64).getItemStack());
            case BLUE -> player.getInventory().addItem(new ItemBuilder(Material.BLUE_WOOL,64).getItemStack());
        }

    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void spawnNPCs(){
        Location rNPC = plugin.getLocationManager().getLocations(Locations.rNpc);
        Location bNPC = plugin.getLocationManager().getLocations(Locations.bNpc);
        World world = rNPC.getWorld();

        Villager rVillager = (Villager) world.spawnEntity(rNPC, EntityType.VILLAGER);
        Villager bVillager = (Villager) world.spawnEntity(bNPC, EntityType.VILLAGER);

        rVillager.setCustomName(Colorize.format("&6SELECT YOUR CLASS"));
        bVillager.setCustomName(Colorize.format("&6SELECT YOUR CLASS"));

        rVillager.setAI(false);
        bVillager.setAI(false);
    }

    public void removeNPCs(){
        Location bNPC = plugin.getLocationManager().getLocations(Locations.bNpc);
        World world = bNPC.getWorld();
        for(Villager villager: world.getEntitiesByClass(Villager.class)){
            villager.remove();
        }
    }

    public boolean isCanBreakPlace() {
        return canBreakPlace;
    }

    public void setCanBreakPlace(boolean canBreakPlace) {
        this.canBreakPlace = canBreakPlace;
    }
}
