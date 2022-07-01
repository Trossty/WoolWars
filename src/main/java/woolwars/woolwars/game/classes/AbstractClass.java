package woolwars.woolwars.game.classes;

import org.bukkit.inventory.ItemStack;
import woolwars.woolwars.WoolWarsPlugin;
import woolwars.woolwars.enums.ClassType;
import woolwars.woolwars.game.Game;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractClass {
    private final String name;
    private final ItemStack guiItem;
    private final String permission;
    private final String coloredName;
    private final WoolWarsPlugin plugin;
    private final Set<UUID> playerList;
    private final ClassType classType;

    public AbstractClass(String name, ItemStack guiItem, String permission, String coloredName, ClassType classType, WoolWarsPlugin plugin){
        this.name = name;
        this.guiItem = guiItem;
        this.permission = permission;
        this.plugin = plugin;
        this.playerList = new HashSet<>();
        this.coloredName = coloredName;
        this.classType = classType;
    }

    public ItemStack getGuiItem() {
        return guiItem;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public void addPlayer(UUID name){
        this.playerList.add(name);
    }

    public void removePlayer(UUID name){
        this.playerList.remove(name);
    }

    public boolean containsPlayer(UUID name){
        return this.playerList.contains(name);
    }

    public Set<UUID> getPlayerList() {
        return playerList;
    }

    protected WoolWarsPlugin getPlugin() {
        return plugin;
    }

    protected Game getGame(){
        return getPlugin().getGameManager().getGame();
    }

    public String getColoredName() {
        return coloredName;
    }

    public ClassType getClassType() {
        return classType;
    }
}
