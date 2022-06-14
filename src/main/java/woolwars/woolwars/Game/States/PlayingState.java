package woolwars.woolwars.Game.States;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import woolwars.woolwars.Enums.Items;
import woolwars.woolwars.Game.GamePlayer;
import woolwars.woolwars.Game.GameState;
import woolwars.woolwars.Game.ItemArmorStand;
import woolwars.woolwars.Utils.Colorize;
import woolwars.woolwars.WoolWarsPlugin;

import java.util.HashMap;

public class PlayingState extends GameState {

    private HashMap<Location, Items> itemsLocations = new HashMap<>();
    private HashMap<Location, ItemArmorStand> itemsLocationswith = new HashMap<>();

    public PlayingState(WoolWarsPlugin plugin) {
        super(plugin, "playing");

        itemsLocations.clear();
        itemsLocationswith.clear();

        getGame().getItemLList().forEach(itemArmorStand ->{
                itemsLocations.put(itemArmorStand.getLocation(),itemArmorStand.getType());
                itemsLocationswith.put(itemArmorStand.getLocation(),itemArmorStand);
        });

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

            if(damagerGamePlayer.getTeam() == victimGamePlayer.getTeam()){
                event.setCancelled(true);
            }
        } else if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) return;
            Player damager = (Player) arrow.getShooter();
            GamePlayer damagerGamePlayer = GamePlayer.getGamePlayer(damager).get();

            if(damagerGamePlayer.getTeam() == victimGamePlayer.getTeam()){
                event.setCancelled(true);
            }
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

        itemsLocations.forEach((location ,type) -> {
            if(getDistance(event.getTo().getX(),location.getBlockX()) < 1.5 && getDistance(event.getTo().getZ(),location.getBlockZ()) < 1.5){

                switch (type){
                    case bow : player.getInventory().addItem(new ItemStack(Material.BOW)); player.getInventory().addItem(new ItemStack(Material.ARROW,2)); break;
                    case sword: player.getInventory().addItem(new ItemStack(Material.STONE_SWORD)); break;
                    case pickaxe: player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE)); break;
                    case heal: if(player.getHealth()<20){player.setHealth(20);} break;
                    case jump: player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,100,2)); break;
                    case speed:player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100,2)); break;
                    case strength: player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,40,1)); break;
                }

                itemsLocationswith.get(location).delArmorstand();
                itemsLocations.remove(location);
            }
        });



    }

    private static double getDistance(double source, double target){
        return Math.abs(source-target);
    }
}
