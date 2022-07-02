package woolwars.woolwars.Objects.gui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import woolwars.woolwars.WoolWarsPlugin;
import woolwars.woolwars.api.GUI.GUI;
import woolwars.woolwars.game.GamePlayer;
import woolwars.woolwars.game.classes.AbstractClass;
import woolwars.woolwars.utils.Colorize;
import woolwars.woolwars.utils.ItemBuilder;

public class ClassGUI extends GUI<WoolWarsPlugin> {

    private final Player player;
    private final GamePlayer gamePlayer;

    public ClassGUI(WoolWarsPlugin plugin, Player player) {
        super(plugin);
        this.player = player;
        gamePlayer = GamePlayer.getGamePlayer(player).get();

        createInventory();
    }

    private void createInventory() {

        int i = 10;

        for(AbstractClass kit: getPlugin().getClassManager().getClasses()){

            if(i==18){
                i+=10;
            }

            ItemBuilder itemBuilder = new ItemBuilder(kit.getGuiItem().getType()).withDisplayName(kit.getColoredName());
            if(!player.hasPermission(kit.getPermission())){
                itemBuilder = new ItemBuilder(Material.BLACK_STAINED_GLASS);
                itemBuilder.withDisplayName("&cLOCKED");
            }
            if(gamePlayer.getAbstractClass()==kit){
                itemBuilder.withEnchant(Enchantment.DURABILITY,10,true);
                itemBuilder.withFlags(ItemFlag.HIDE_ENCHANTS);
            }
            set(i,itemBuilder.getItemStack(),((whoClicked, clickedItem) -> {
                if(gamePlayer.getAbstractClass()==kit){
                    whoClicked.playSound(whoClicked.getLocation(), Sound.ENTITY_VILLAGER_NO,.5f,1.0f);
                    whoClicked.sendMessage(Colorize.format("&cAlready using this class!"));
                    return ButtonAction.CANCEL;
                }
                if(!whoClicked.hasPermission(kit.getPermission())){
                    whoClicked.playSound(whoClicked.getLocation(), Sound.ENTITY_VILLAGER_NO,.5f,1.0f);
                    whoClicked.sendMessage(Colorize.format("&cThis class locked!"));
                    return ButtonAction.CANCEL;
                }
                gamePlayer.setAbstractClass(kit);
                getPlugin().getGameManager().getGame().giveClass(player,kit.getClassType());
                whoClicked.playSound(whoClicked.getLocation(),Sound.ENTITY_PLAYER_LEVELUP,.5f,.5f);
                return ButtonAction.CLOSE_GUI;

            }));
            i+=2;
        }

    }

    @Override
    public int getSize() {
        return 54;
    }

    @Override
    public String getTitle() {
        return Colorize.format("&6CHOOSE CLASS");
    }

    @Override
    public boolean canClose(Player player) {
        return true;
    }
}
