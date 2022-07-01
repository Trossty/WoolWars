package woolwars.woolwars.game.classes;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import woolwars.woolwars.WoolWarsPlugin;
import woolwars.woolwars.enums.ClassType;
import woolwars.woolwars.utils.ItemBuilder;

public class AssaultClass extends AbstractClass implements Listener {

    public AssaultClass(WoolWarsPlugin plugin) {
        super("Assault", new ItemBuilder(Material.SHEARS).withDisplayName("&7Assault").getItemStack(), "woolwars.Assault", "&7Assault", ClassType.Assault, plugin);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
