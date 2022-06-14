package woolwars.woolwars.Enums;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public enum Items {

    speed(1,Material.SPLASH_POTION,"Speed Boost"),
    heal(2,Material.SPLASH_POTION, "Instant Heal"),
    jump(3,Material.SPLASH_POTION, "Jump Boost"),
    strength(4,Material.SPLASH_POTION, "Strenght Boost"),
    bow(5,Material.BOW,"Bow"),
    pickaxe(6,Material.STONE_PICKAXE, "Stone Pickaxe"),
    sword(7,Material.STONE_SWORD,"Stone Sword");

    private final Material headMaterial;
    private final String name;
    private final int value;

    private static HashMap<Integer,Items> map = new HashMap<>();

    Items(int value,Material headMaterial, String name){
        this.headMaterial = headMaterial;
        this.name = name;
        this.value = value;
    }

    public static void listAll(){
        for(Items items: Items.values()){
            map.put(items.value,items);
        }
    }

    public Material getHeadMaterial() {
        return headMaterial;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static HashMap<Integer,Items> getMap() {
        return map;
    }
}
