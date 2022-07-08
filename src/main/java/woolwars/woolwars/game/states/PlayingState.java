package woolwars.woolwars.game.states;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
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
import woolwars.woolwars.utils.ItemBuilder;

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
                        cancel();
                    }else if(bPlayer==0){
                        if(getGame().getRedTeam().getScore()==3){
                            getGame().setGameState(new EndState(getPlugin()));
                            return;
                        }
                        getGame().getRedTeam().setScore(getGame().getRedTeam().getScore()+1);
                        restart(getGame().getRedTeam());
                        cancel();
                    }else{
                        getGame().setTime(30);
                    }
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
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayer.getGamePlayer(player).get();
        if(player.getInventory().getItemInMainHand().getType()==Material.BLAZE_POWDER){
            if(event.getAction()==Action.RIGHT_CLICK_AIR || event.getAction()==Action.RIGHT_CLICK_BLOCK){
                if(!gamePlayer.getIsItUsed()){
                    gamePlayer.setItUsed(true);
                    switch (gamePlayer.getAbstractClass().getClassType()){
                        case Swordsman:
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20,4));
                            break;
                        case Engineer:

                            final int[] i = {6};

                            getGame().setCanBreakPlace(false);
                            (new BukkitRunnable(){

                                @Override
                                public void run() {

                                    getGame().getPlayerList().stream().map(Bukkit::getPlayer).forEach(all -> all.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Colorize.format("&6The Center Unlocks In "+i[0]))));

                                    if(i[0]==0){
                                        getGame().setCanBreakPlace(true);
                                        cancel();
                                    }

                                    i[0]--;
                                }
                            }).runTaskTimer(getPlugin(),0,20);

                            break;
                        case Assault:
                            player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
                            break;
                        case Archer:
                            player.setVelocity(player.getLocation().getDirection().multiply(-1).setY(1));
                            break;
                        case Golem:

                            Material head;
                            Material chest;
                            Material leggings;
                            Material boots;

                            if(player.getInventory().getHelmet()==null){
                                head = Material.AIR;
                            }else {
                                head = player.getInventory().getHelmet().getType();
                            }

                            if(player.getInventory().getChestplate()==null){
                                chest = Material.AIR;
                            }else {
                                chest = player.getInventory().getChestplate().getType();
                            }

                            if(player.getInventory().getLeggings()==null){
                                leggings = Material.AIR;
                            }else {
                                leggings = player.getInventory().getLeggings().getType();
                            }

                            if(player.getInventory().getBoots()==null){
                                boots = Material.AIR;
                            }else {
                                boots = player.getInventory().getBoots().getType();
                            }

                            player.getInventory().setHelmet(new ItemStack(Material.AIR));
                            player.getInventory().setChestplate(new ItemBuilder(Material.GOLDEN_CHESTPLATE).withEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,4,true).getItemStack());
                            player.getInventory().setLeggings(new ItemBuilder(Material.GOLDEN_LEGGINGS).withEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,4,true).getItemStack());
                            player.getInventory().setBoots(new ItemBuilder(Material.GOLDEN_BOOTS).withEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,4,true).getItemStack());

                            (new BukkitRunnable(){
                                @Override
                                public void run() {
                                    player.getInventory().setHelmet(new ItemStack(head));
                                    player.getInventory().setChestplate(new ItemStack(chest));
                                    player.getInventory().setLeggings(new ItemStack(leggings));
                                    player.getInventory().setBoots(new ItemStack(boots));
                                }
                            }).runTaskLater(getPlugin(),100);

                            break;
                        case Tank:
                            player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST,50,4));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL,5,100));
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTNTExplode(EntityExplodeEvent event){
        event.setCancelled(true);
        Entity entity = event.getEntity();

        if(!(entity instanceof TNTPrimed)) return;

        Location location = event.getLocation();

        location.getWorld().getNearbyEntities(location,3,3,3).forEach(allentity -> {
            allentity.setVelocity(entity.getLocation().getDirection().multiply(-4).setY(0.5));
        });

    }

    @EventHandler
    public void onBreakingBlocks(BlockBreakEvent event){

        if(!getGame().isCanBreakPlace()){
            event.setCancelled(true);
        }

        Location breakingBlockLoc = event.getBlock().getLocation();

        if(!getGame().isintheArea(breakingBlockLoc)){
            event.setCancelled(true);
        }else {
            GamePlayer.getGamePlayer(event.getPlayer()).get().setBlocksBreaked(GamePlayer.getGamePlayer(event.getPlayer()).get().getBlocksBreaked()+1);
        }

        event.getBlock().getDrops().clear();

    }

    @EventHandler
    public void onPlacingBlocks(BlockPlaceEvent event){

        if(!getGame().isCanBreakPlace()){
            event.setCancelled(true);
        }

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
        if(event.getDamager() instanceof TNTPrimed) event.setCancelled(true);
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
        if(!(event.getEntity().getKiller() instanceof Player)) return;
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
                    }else if(!player.getInventory().contains(Material.STONE_SWORD)){
                        player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                    }
                    break;
                case pickaxe:
                    if(player.getInventory().contains(Material.WOODEN_PICKAXE)){
                        replace(player,Material.WOODEN_PICKAXE,Material.STONE_PICKAXE);
                    }else if(!player.getInventory().contains(Material.STONE_PICKAXE)){
                        player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
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

    @EventHandler
    public void onClicking(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();

        if(player.getInventory()==inventory){
            event.setCancelled(true);
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
            if(stack == null )
                continue;
            if(stack.getType() == replacingItem)
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
