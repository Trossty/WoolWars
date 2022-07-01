package woolwars.woolwars.managers;

import woolwars.woolwars.WoolWarsPlugin;
import woolwars.woolwars.game.classes.*;

import java.util.ArrayList;
import java.util.List;

public class ClassManager {


    private final List<AbstractClass> classes = new ArrayList<>();
    private final WoolWarsPlugin plugin;

    public ClassManager(WoolWarsPlugin plugin){
        this.plugin = plugin;
        reload();
    }

    public void reload(){
        this.classes.clear();
        this.classes.add(new ArcherClass(plugin));
        this.classes.add(new AssaultClass(plugin));
        this.classes.add(new EngineerClass(plugin));
        this.classes.add(new GolemClass(plugin));
        this.classes.add(new SwordsmanClass(plugin));
        this.classes.add(new TankClass(plugin));
    }

    public List<AbstractClass> getClasses() {
        return classes;
    }

}
