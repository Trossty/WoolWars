package woolwars.woolwars.game.classes;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import woolwars.woolwars.WoolWarsPlugin;
import woolwars.woolwars.enums.ClassType;
import woolwars.woolwars.utils.ItemBuilder;

public class ArcherClass extends AbstractClass implements Listener {

    public ArcherClass(WoolWarsPlugin plugin) {
        super("Archer", new ItemBuilder(Material.BOW).withDisplayName("&7Archer").getItemStack(), "woolwars.Archer", "&7Archer", ClassType.Archer, plugin);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
