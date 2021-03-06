package woolwars.woolwars.game;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import woolwars.woolwars.enums.Items;
import woolwars.woolwars.utils.Colorize;

public class ItemArmorStand extends BukkitRunnable {

    private ArmorStand armorStand;
    private Items type;
    private Location location;

    public ItemArmorStand(Location location, Items type){
        this.location = location;
        this.type = type;

        this.armorStand = location.getWorld().spawn(new Location(location.getWorld(), location.getBlockX(),location.getBlockY(),location.getBlockZ()), ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setMarker(true);
        armorStand.setGravity(false);
        armorStand.setHelmet(new ItemStack(type.getHeadMaterial()));
        armorStand.addScoreboardTag("type");
        armorStand.addScoreboardTag(type.name());
        armorStand.setCustomName(Colorize.format(type.getName()));
        armorStand.setCustomNameVisible(true);
    }

    @Override
    public void run() {
        if(this.armorStand.isDead()) cancel();
        double rotationY = Math.toDegrees(this.armorStand.getHeadPose().getY());
        this.armorStand.setHeadPose(new EulerAngle(Math.toRadians(0),Math.toRadians(rotationY+1),Math.toRadians(0)));
    }

    public Location getLocation() {
        return location;
    }

    public Items getType() {
        return type;
    }

    public String getName(){
        return type.getName();
    }

}
