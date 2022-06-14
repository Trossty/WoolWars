package woolwars.woolwars.game.states;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import woolwars.woolwars.enums.Locations;
import woolwars.woolwars.game.GamePlayer;
import woolwars.woolwars.game.GameState;
import woolwars.woolwars.utils.Colorize;
import woolwars.woolwars.WoolWarsPlugin;

public class LobbyState extends GameState {
    public LobbyState(WoolWarsPlugin plugin) {
        super(plugin, "lobby");
        getGame().setTime(10);
    }

    @Override
    public void onEnable(){
        super.onEnable();
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        getGame().getPlayerList().add(player.getUniqueId());

        player.teleport(getPlugin().getLocationManager().getLocations(Locations.lobbySpawn));

        event.setJoinMessage(Colorize.format("&7[&a+&7] &f"+player.getName()+" &7("+getGame().getPlayerList().size()+"/8)"));

        if(getGame().getPlayerList().size()>=8){
            (new BukkitRunnable() {
                @Override
                public void run() {
                    getGame().setTime(getGame().getTime()-1);

                    if(getGame().getTime()<1) {
                        getGame().setGameState(new PreRoundState(getPlugin()));
                    }

                    getGame().shout("&eThe game starts in &c"+getGame().getTime()+" &eseconds!");

                }
            }).runTaskTimer(getPlugin(),0,20);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        getGame().getPlayerList().remove(player.getUniqueId());

        event.setQuitMessage(Colorize.format("&7[&4-&7] &f"+player.getName()+" &7("+getGame().getPlayerList().size()+"/8)"));

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
