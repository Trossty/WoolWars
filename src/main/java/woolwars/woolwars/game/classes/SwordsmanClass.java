package woolwars.woolwars.game.classes;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import woolwars.woolwars.WoolWarsPlugin;
import woolwars.woolwars.enums.ClassType;
import woolwars.woolwars.utils.ItemBuilder;

public class SwordsmanClass extends AbstractClass implements Listener {

    public SwordsmanClass(WoolWarsPlugin plugin) {
        super("Swordsman", new ItemBuilder(Material.STONE_SWORD).withDisplayName("&7Swordsman").getItemStack(), "woolwars.Swordsman", "&7Swordsman", ClassType.Swordsman, plugin);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
