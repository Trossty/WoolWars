package woolwars.woolwars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;
import woolwars.woolwars.Objects.gui.ClassGUI;
import woolwars.woolwars.enums.Locations;
import woolwars.woolwars.game.states.PlayingState;
import woolwars.woolwars.game.states.PreRoundState;
import woolwars.woolwars.utils.Colorize;
import woolwars.woolwars.WoolWarsPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

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
                    }else if(args[1].equalsIgnoreCase("time")){
                        if(args.length!=2) return false;
                        try{
                            int time = Integer.parseInt(args[2]);
                            plugin.getGameManager().getGame().setTime(time);
                        }catch (NumberFormatException ignored){
                            player.sendMessage(Colorize.format("&cInput Number..."));
                        }
                    }


                } else if (args[0].equalsIgnoreCase("state")) {

                    switch (args[1]){
                        case "preround":
                            plugin.getGameManager().getGame().setGameState(new PreRoundState(plugin));
                            player.sendMessage(Colorize.format("&aPreround"));
                            break;
                        case "playing":
                            plugin.getGameManager().getGame().setGameState(new PlayingState(plugin));
                            player.sendMessage(Colorize.format("&aPlaying"));
                            break;
                    }

                } else if (args[0].equalsIgnoreCase("gui")) {
                    plugin.getGuiapi().openGUI(player,new ClassGUI(plugin,player));
                }
            }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if(sender.hasPermission("woolwars.admin")){
            if(args.length==1){
                return Arrays.asList("set","state");
            }else if(args.length==2){
                if(args[0].equalsIgnoreCase("state")){
                    return Arrays.asList("preround","playing");
                }else if(args[0].equalsIgnoreCase("set")){
                    return Arrays.asList("location","time");
                }
            }else if(args.length==3){
                if(args[1].equalsIgnoreCase("location")){
                    ArrayList<String> arrayList = new ArrayList();

                    for(Locations locations:Locations.values()){
                        arrayList.add(locations.name());
                    }

                    arrayList.add("finish");

                    return arrayList;
                }
            }
        }
        return Collections.emptyList();
    }

}

