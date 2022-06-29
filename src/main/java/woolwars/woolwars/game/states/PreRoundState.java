package woolwars.woolwars.game.states;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import woolwars.woolwars.enums.Locations;
import woolwars.woolwars.game.GamePlayer;
import woolwars.woolwars.game.GameState;
import woolwars.woolwars.utils.Colorize;
import woolwars.woolwars.WoolWarsPlugin;

import java.util.Random;

public class PreRoundState extends GameState {
    public PreRoundState(WoolWarsPlugin plugin) {
        super(plugin, "preround");
    }

    @Override
    public void onEnable(){
        super.onEnable();

        getGame().getPlayerList().stream().map(Bukkit::getPlayer).forEach(player -> {
            GamePlayer gamePlayer = GamePlayer.getGamePlayer(player).get();

            if(getGame().getRedTeam().getPlayerList().size() > getGame().getBlueTeam().getPlayerList().size()){
                gamePlayer.setTeam(getGame().getBlueTeam());
                player.teleport(getPlugin().getLocationManager().getLocations(Locations.blueSpawn));
            }

            gamePlayer.setTeam(getGame().getRedTeam());
            player.teleport(getPlugin().getLocationManager().getLocations(Locations.redSpawn));

        });

        getGame().titleShout("&ePRE ROUND","&7Select Your Class!",10,10,10);

        getGame().setTime(15);

        (new BukkitRunnable() {
            @Override
            public void run() {

                if(getGame().getTime() == 10){
                    getGame().shout("&eThe game starts in &c"+getGame().getTime()+"&e!");
                }   else

                if(getGame().getTime()<1){
                    getGame().setGameState(new PlayingState(getPlugin()));
                }   else
                if(getGame().getTime()==5){
                    getGame().spawnItem();
                }   else
                if(getGame().getTime() < 6){
                    getGame().shout("&eThe game starts in &c"+getGame().getTime()+"&e!");
                }

                getGame().setTime(getGame().getTime()-1);
            }
        }).runTaskTimer(getPlugin(),0,20);



    }

    @Override
    public void onDisable(){
        super.onDisable();

        int maxLocX = getPlugin().getLocationManager().getLocations(Locations.centerWool).getBlockX() + 1;
        int maxLocZ = getPlugin().getLocationManager().getLocations(Locations.centerWool).getBlockZ() + 1;
        int minLocX = getPlugin().getLocationManager().getLocations(Locations.centerWool).getBlockX() - 1;
        int minLocZ = getPlugin().getLocationManager().getLocations(Locations.centerWool).getBlockZ() - 1;
        int LocY = getPlugin().getLocationManager().getLocations(Locations.centerWool).getBlockY();
        World world = getPlugin().getLocationManager().getLocations(Locations.centerWool).getWorld();

        for(int x=minLocX;x<=maxLocX;x++){
            for(int z=minLocZ;z<=maxLocZ;z++){
                Location location = new Location(world,x,LocY,z);

                Material quartz = Material.QUARTZ_BLOCK;
                Material concrete = Material.WHITE_CONCRETE;
                Material bone = Material.BONE_BLOCK;
                Material snow = Material.SNOW_BLOCK;

                int upper = 3;
                Random random = new Random();
                random.nextInt(1 +(upper));

                switch (random.nextInt(upper)) {
                    case 0:
                        location.getBlock().setType(quartz);
                        break;
                    case 1:
                        location.getBlock().setType(concrete);
                        break;
                    case 2:
                        location.getBlock().setType(bone);
                        break;
                    case 3:
                        location.getBlock().setType(snow);
                        break;
                }
            }
        }

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
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) event.setCancelled(true);
    }

    @EventHandler
    public void onPlacingBlocks(BlockPlaceEvent event){
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

}
