package woolwars.woolwars.game.classes;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import woolwars.woolwars.WoolWarsPlugin;
import woolwars.woolwars.enums.ClassType;
import woolwars.woolwars.utils.ItemBuilder;

public class GolemClass extends AbstractClass implements Listener {

    public GolemClass(WoolWarsPlugin plugin) {
        super("Golem", new ItemBuilder(Material.GOLDEN_CHESTPLATE).withDisplayName("&7Golem").getItemStack(), "woolwars.Golem", "&7Golem", ClassType.Golem, plugin);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
