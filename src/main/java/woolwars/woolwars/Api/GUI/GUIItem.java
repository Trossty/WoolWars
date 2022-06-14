package woolwars.woolwars.Api.GUI;

import org.bukkit.inventory.ItemStack;

public class GUIItem {
    private final GUI.ButtonCompletion onClick;
    private final ItemStack itemStack;

    public GUIItem(GUI.ButtonCompletion onClick, ItemStack itemStack){
        this.itemStack = itemStack;
        this.onClick = onClick;
    }

    public GUI.ButtonCompletion getOnClick() {
        return onClick;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
