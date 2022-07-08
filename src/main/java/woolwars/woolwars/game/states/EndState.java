package woolwars.woolwars.game.states;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import woolwars.woolwars.WoolWarsPlugin;
import woolwars.woolwars.enums.TeamType;
import woolwars.woolwars.game.GamePlayer;
import woolwars.woolwars.game.GameState;
import woolwars.woolwars.utils.Colorize;

public class EndState extends GameState {

    private TeamType winnerTeam;

    public EndState(WoolWarsPlugin plugin) {
        super(plugin, "End");
    }

    @Override
    public void onEnable(){
        super.onEnable();

        getGame().getPlayerList().stream().map(Bukkit::getPlayer).forEach(player -> {
            GamePlayer gamePlayer = GamePlayer.getGamePlayer(player).get();

            if(getGame().getBlueTeam().getScore()>getGame().getRedTeam().getScore()){
                winnerTeam = TeamType.BLUE;
            }else {
                winnerTeam = TeamType.RED;
            }

            if(winnerTeam == gamePlayer.getTeam().getTeamType()){
                player.sendTitle(Colorize.format("&aVICTORY!"),Colorize.format("LEZ GOOOO!"));
            }else {
                player.sendTitle(Colorize.format("&cLOSE!"),Colorize.format("NT"));
            }

        });

    }

    @Override
    public void onDisable(){
        super.onDisable();
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
