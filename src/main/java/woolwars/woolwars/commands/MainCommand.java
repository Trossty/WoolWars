package woolwars.woolwars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import woolwars.woolwars.enums.Locations;
import woolwars.woolwars.game.states.PreRoundState;
import woolwars.woolwars.utils.Colorize;
import woolwars.woolwars.WoolWarsPlugin;

public class MainCommand implements CommandExecutor {

    private WoolWarsPlugin plugin;

    int i = 1;

    public MainCommand(WoolWarsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if(!(sender instanceof Player)) return false;

            Player player = (Player) sender;

            if(player.hasPermission("woolwars.admin")){
                if(args[0].equalsIgnoreCase("set")){

                    if(args[1].equalsIgnoreCase("location")){

                        if(args[2].equalsIgnoreCase("finish")){
                            plugin.getLocationManager().setReady(true);

                            plugin.getLocationManager().save();

                            player.sendMessage(Colorize.format("&aReady!"));

                            return false;
                        }

                        if(Locations.valueOf(args[2])==null){
                            player.sendMessage(Colorize.format("&4Can't find!"));
                        }else if(Locations.valueOf(args[2])==Locations.items){
                            plugin.getLocations().getConfiguration().set("ItemSpawn."+i,player.getLocation());
                            player.sendMessage(Colorize.format("&6Succsessfully &7"+Locations.valueOf(args[2]).name()+i+"&6 Locations Setted!"));
                            i++;
                        }else {
                            plugin.getLocationManager().setLocation(Locations.valueOf(args[2]),player.getLocation());
                            player.sendMessage(Colorize.format("&6Succsessfully &7"+Locations.valueOf(args[2]).name()+"&6 Locations Setted!"));
                        }
                    }
                } else if (args[0].equalsIgnoreCase("state")) {

                    switch (args[1]){
                        case "preround":
                            plugin.getGameManager().getGame().setGameState(new PreRoundState(plugin));
                            player.sendMessage(Colorize.format("&aAight"));
                            break;
                    }

                }
            }

        return false;
    }
}
