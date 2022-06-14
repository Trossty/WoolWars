package woolwars.woolwars.Utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigurationFile {


    private final YamlConfiguration yamlConfiguration;
    private final File file;

    /**
     * Create a new ConfigurationFile
     * @param plugin The plugin which should own this file.
     * @param name The name (without extension) of the file
     */
    public ConfigurationFile(Plugin plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name + ".yml");
        this.yamlConfiguration = new YamlConfiguration();
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.yamlConfiguration.load(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getConfiguration() {
        return yamlConfiguration;
    }

    public void save() {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload(){
        try {
            yamlConfiguration.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


}
