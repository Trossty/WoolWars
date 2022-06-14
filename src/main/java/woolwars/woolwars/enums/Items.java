package woolwars.woolwars.enums;

import org.bukkit.Material;

public enum Items {

    speed(Material.SPLASH_POTION,"Speed Boost"),
    heal(Material.SPLASH_POTION, "Instant Heal"),
    jump(Material.SPLASH_POTION, "Jump Boost"),
    strength(Material.SPLASH_POTION, "Strenght Boost"),
    bow(Material.BOW,"Bow"),
    pickaxe(Material.STONE_PICKAXE, "Stone Pickaxe"),
    sword(Material.STONE_SWORD,"Stone Sword");

    private final Material headMaterial;
    private final String name;

    public static Items[] values = Items.values();

    Items(Material headMaterial, String name){
        this.headMaterial = headMaterial;
        this.name = name;
    }

    public Material getHeadMaterial() {
        return headMaterial;
    }

    public String getName() {
        return name;
    }
}
