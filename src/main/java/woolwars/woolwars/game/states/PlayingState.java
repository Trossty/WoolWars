package woolwars.woolwars.game.states;

import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import woolwars.woolwars.enums.Items;
import woolwars.woolwars.enums.Locations;
import woolwars.woolwars.enums.TeamType;
import woolwars.woolwars.game.*;
import woolwars.woolwars.utils.Colorize;
import woolwars.woolwars.WoolWarsPlugin;

import java.util.HashMap;
import java.util.LinkedList;

public class PlayingState extends GameState {

    private HashMap<Location, Items> itemsLocations = new HashMap<>();
    private HashMap<Location, ItemArmorStand> itemsLocationswith = new HashMap<>();

    private BukkitRunnable bukkitRunnable;

    private int rPlayer = 0;

    private int bPlayer= 0;

    public PlayingState(WoolWarsPlugin plugin) {
        super(plugin, "playing");
    }

    @Override
    public void onEnable(){
        super.onEnable();

        getGame().setRound(getGame().getRound()+1);

        itemsLocations.clear();
        itemsLocationswith.clear();

        getGame().getItemLList().forEach(itemArmorStand ->{
            itemsLocations.put(itemArmorStand.getLocation(),itemArmorStand.getType());
            itemsLocationswith.put(itemArmorStand.getLocation(),itemArmorStand);
        });

        getGame().getPlayerList().stream().map(Bukkit::getPlayer).forEach(player -> {
            GamePlayer gamePlayer = GamePlayer.getGamePlayer(player).get();
            switch (gamePlayer.getTeam().getTeamType()){
                case RED -> rPlayer++;
                case BLUE -> bPlayer++;
            }
        });

        getGame().getRedTeam().setRemaningPlayerCount(rPlayer);
        getGame().getBlueTeam().setRemaningPlayerCount(bPlayer);

        getGame().setTime(60);

        bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {

                if(getGame().getTime()==0){


                    getGame().getPlayerList().stream().map(Bukkit::getPlayer).forEach(player -> {
                        GamePlayer gamePlayer = GamePlayer.getGamePlayer(player).get();
                        switch (gamePlayer.getTeam().getTeamType()){
                            case RED -> rPlayer++;
                            case BLUE -> bPlayer++;
                        }
                    });

                    getGame().getRedTeam().setRemaningPlayerCount(rPlayer);
                    getGame().getBlueTeam().setRemaningPlayerCount(bPlayer);

                    if(rPlayer==0){
                        if(getGame().getBlueTeam().getScore()==3){
                            getGame().setGameState(new EndState(getPlugin()));
                            return;
                        }
                        getGame().getBlueTeam().setScore(getGame().getBlueTeam().getScore()+1);
                        restart(getGame().getBlueTeam());
                    }else if(bPlayer==0){
                        if(getGame().getRedTeam().getScore()==3){
                            getGame().setGameState(new EndState(getPlugin()));
                            return;
                        }
                        getGame().getRedTeam().setScore(getGame().getRedTeam().getScore()+1);
                        restart(getGame().getRedTeam());
                    }

                    cancel();
                }

                getGame().setTime(getGame().getTime()-1);
            }
        };

        bukkitRunnable.runTaskTimer(getPlugin(),0,20);
    }

    @Override
    public void onDisable(){
        super.onDisable();
        bukkitRunnable.cancel();
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        event.setQuitMessage(Colorize.format("&7[&4-&7] &f"+player.getName()));

        getGame().getPlayerList().remove(player.getUniqueId());

        GamePlayer.removePlayer(player.getUniqueId());
    }

    @EventHandler
    public void onBreakingBlocks(BlockBreakEvent event){
        Location breakingBlockLoc = event.getBlock().getLocation();

        if(!getGame().isintheArea(breakingBlockLoc)){
            event.setCancelled(true);
        }else {
            GamePlayer.getGamePlayer(event.getPlayer()).get().setBlocksBreaked(GamePlayer.getGamePlayer(event.getPlayer()).get().getBlocksBreaked()+1);
        }

    }

    @EventHandler
    public void onPlacingBlocks(BlockPlaceEvent event){
        Location placingBlockLoc = event.getBlock().getLocation();

        if(!getGame().isintheArea(placingBlockLoc)){
            event.setCancelled(true);
        }else{
            if(!(event.getBlock().getType()==Material.BLUE_WOOL) && !(event.getBlock().getType()==Material.RED_WOOL)){
                event.setCancelled(true);
            }else {
                int maxLocX = getPlugin().getLocationManager().getLocations(Locations.centerWool).getBlockX() + 1;
                int maxLocZ = getPlugin().getLocationManager().getLocations(Locations.centerWool).getBlockZ() + 1;
                int minLocX = getPlugin().getLocationManager().getLocations(Locations.centerWool).getBlockX() - 1;
                int minLocZ = getPlugin().getLocationManager().getLocations(Locations.centerWool).getBlockZ() - 1;
                int LocY = getPlugin().getLocationManager().getLocations(Locations.centerWool).getBlockY();
                World world = getPlugin().getLocationManager().getLocations(Locations.centerWool).getWorld();

                int bBlocks = 0;
                int rBlocks = 0;
                for(int x=minLocX;x<=maxLocX;x++){
                    for(int z=minLocZ;z<=maxLocZ;z++){
                        Location location = new Location(world,x,LocY,z);

                        if(location.getBlock().getType()==Material.BLUE_WOOL){
                            bBlocks++;

                        }else if(location.getBlock().getType()==Material.RED_WOOL){
                            rBlocks++;

                        }
                    }
                }

                if(rBlocks==9){
                    restart(getGame().getRedTeam());
                }else if(bBlocks==9){
                    restart(getGame().getBlueTeam());
                }

                GamePlayer.getGamePlayer(event.getPlayer()).get().setBlocksPlaced(GamePlayer.getGamePlayer(event.getPlayer()).get().getBlocksPlaced()+1);

            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player) || !(event.getDamager() instanceof Arrow)) return;

        Player victim = (Player) event.getEntity();
        GamePlayer victimGamePlayer = GamePlayer.getGamePlayer(victim).get();

        if (event.getDamager() instanceof Player){
            Player damager = (Player) event.getDamager();
            GamePlayer damagerGamePlayer = GamePlayer.getGamePlayer(damager).get();

            if(damager.getGameMode()==GameMode.ADVENTURE){
                event.setCancelled(true);
            }

            if(damagerGamePlayer.getTeam().getTeamType() == victimGamePlayer.getTeam().getTeamType()){
                event.setCancelled(true);
                return;
            }
        } else if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) return;
            Player damager = (Player) arrow.getShooter();
            GamePlayer damagerGamePlayer = GamePlayer.getGamePlayer(damager).get();

            if(damagerGamePlayer.getTeam().getTeamType() == victimGamePlayer.getTeam().getTeamType()){
                event.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){

        event.getDrops().clear();

        Player victim = event.getEntity();
        Player killer = event.getEntity().getKiller();

        GamePlayer victimGamePlayer = GamePlayer.getGamePlayer(victim).get();
        GamePlayer killerGamePlayer = GamePlayer.getGamePlayer(killer).get();

        gogoSpec(victim);

        victim.teleport(killer.getLocation());

        killerGamePlayer.setKillCount(killerGamePlayer.getKillCount()+1);

        if(victimGamePlayer.getTeam().getTeamType()==TeamType.BLUE){
            bPlayer--;
            getGame().getBlueTeam().setRemaningPlayerCount(bPlayer);
        }else{
            rPlayer--;
            getGame().getRedTeam().setRemaningPlayerCount(rPlayer);
        }

    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(event.getTo().getX() == event.getFrom().getX() && event.getTo().getZ() == event.getFrom().getZ()){
            return;
        }

        Player player = event.getPlayer();

        for(Entity entity: player.getNearbyEntities(1.5,3,1.5)){

            LinkedList<String> linkedList = new LinkedList<>(entity.getScoreboardTags());
            if(!linkedList.contains("type")) continue;
            linkedList.remove("type");
            Items type = Items.valueOf(linkedList.get(0));

            switch (type){
                case bow :
                    if(!player.getInventory().contains(Material.BOW)){
                        player.getInventory().addItem(new ItemStack(Material.BOW));
                    }
                    player.getInventory().addItem(new ItemStack(Material.ARROW,2));
                    break;
                case sword:
                    if(player.getInventory().contains(Material.WOODEN_SWORD)){
                        replace(player,Material.WOODEN_SWORD,Material.STONE_SWORD);
                        return;
                    }
                    player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                    break;
                case pickaxe:
                    if(player.getInventory().contains(Material.WOODEN_PICKAXE)){
                        replace(player,Material.WOODEN_PICKAXE,Material.STONE_PICKAXE);
                    }
                    break;
                case heal:
                    if(player.getHealth()<20){
                        player.setHealth(20);
                    }
                    break;
                case jump:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,100,2));
                    break;
                case speed:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100,2));
                    break;
                case strength:
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,40,1));
                    break;
                case boots:
                    player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
                    break;
                case leggings:
                    player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                    break;
                case chestplate:
                    player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                    break;
            }

            entity.remove();
        }

    }

    private void restart(GameTeam gameTeam){
        gameTeam.setScore(gameTeam.getScore()+1);
        if(gameTeam.getScore()>=3){
            getGame().setGameState(new EndState(getPlugin()));
            return;
        }

        int bScore = getGame().getBlueTeam().getScore();
        int rScore = getGame().getRedTeam().getScore();

        getGame().getPlayerList().stream().map(Bukkit::getPlayer).forEach(player -> {
            GamePlayer gamePlayer = GamePlayer.getGamePlayer(player).get();
            player.sendTitle(Colorize.format((bScore>rScore) ? "&9"+bScore+"&f - &4"+rScore : "&4"+rScore+"&f - &9"+bScore),Colorize.format((gamePlayer.getTeam().getTeamType()==gameTeam.getTeamType())?"&aROUND WON!":"&cROUND LOSE!"),5,10,5);
            if(gamePlayer.getTeam().getTeamType()== TeamType.BLUE){
                player.teleport(getPlugin().getLocationManager().getLocations(Locations.blueSpawn));
            }else {
                player.teleport(getPlugin().getLocationManager().getLocations(Locations.redSpawn));
            }
        });

        (new BukkitRunnable() {
            @Override
            public void run() {
                onDisable();
                getGame().setGameState(new FakePreRound(getPlugin()));
            }
        }).runTaskLater(getPlugin(),20);
    }

    private void lowerGlass(){
        //TODO: Lower The Glass
    }

    private void replace(Player p, Material replacingItem, Material replaceToItem)
    {
        ItemStack[] stacks = p.getInventory().getContents();
        for(ItemStack stack : stacks)
        {
            if(stack == null ) // just in case it is empty (and yes other people, it is save to check)
                continue;
            if(stack.getType() == replacingItem) // not sure about the material
            {
                stack.setType(replaceToItem);
            }
        }
    }

    private void gogoSpec(Player player){

        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);

        getGame().getPlayerList().stream().map(Bukkit::getPlayer).forEach(players -> players.hidePlayer(player));
    }

}
