package woolwars.woolwars.game.classes;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import woolwars.woolwars.WoolWarsPlugin;
import woolwars.woolwars.enums.ClassType;
import woolwars.woolwars.utils.ItemBuilder;

public class TankClass extends AbstractClass implements Listener {


    public TankClass(WoolWarsPlugin plugin) {
        super("Tank", new ItemBuilder(Material.IRON_BLOCK).withDisplayName("&7Tank").getItemStack(), "woolwars.Tank", "&7Tank", ClassType.Tank, plugin);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
