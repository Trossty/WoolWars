package woolwars.woolwars.game.classes;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import woolwars.woolwars.WoolWarsPlugin;
import woolwars.woolwars.enums.ClassType;
import woolwars.woolwars.utils.ItemBuilder;

public class EngineerClass extends AbstractClass implements Listener {

    public EngineerClass(WoolWarsPlugin plugin) {
        super("Engineer",  new ItemBuilder(Material.REDSTONE_BLOCK).withDisplayName("&7Engineer").getItemStack(), "woolwars.Engineer", "&7Engineer", ClassType.Engineer, plugin);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
