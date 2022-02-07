package de.timesnake.extension.warp.home;


import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.ChatColor;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.extension.warp.server.ExWarpServer;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;

import java.util.List;

public class HomeCmd implements CommandListener {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        HomeManager hm = ExWarpServer.getHomeManager();

        if (!sender.isPlayer(true)) {
            return;
        }

        if (!ExWarpServer.getHomeManager().areHomesEnabled()) {
            sender.sendPluginMessage(ChatColor.WARNING + "Homes are not enabled on this server");
            return;
        }

        User user = sender.getUser();
        if (cmd.getName().equalsIgnoreCase("home")) {
            if (!sender.hasPermission("exwarp.home", 2654)) {
                return;
            }

            if (hm.areWorldHomesEnabled()) {
                ExWorld world = user.getExWorld();

                if (!hm.containsHome(user.getUniqueId(), world.getBukkitWorld())) {
                    sender.sendPluginMessage(ChatColor.WARNING + "You have no home set in world " + ChatColor.VALUE + world.getName());
                    sender.sendMessageCommandHelp("Set home", "sethome");
                    return;
                }

                user.teleport(hm.getHome(user.getUniqueId(), world.getBukkitWorld()).getLocation());
                sender.sendPluginMessage(ChatColor.PERSONAL + "Teleported to home in world " + ChatColor.VALUE + world.getName());

            } else {
                if (!hm.containsHome(user.getUniqueId(), null)) {
                    sender.sendPluginMessage(ChatColor.WARNING + "You have no home set");
                    sender.sendMessageCommandHelp("Set home", "sethome");
                    return;
                }

                user.teleport(hm.getHome(user.getUniqueId(), null).getLocation());
                sender.sendPluginMessage(ChatColor.PERSONAL + "Teleported to home");
            }

        } else if (cmd.getName().equalsIgnoreCase("sethome")) {
            if (!sender.hasPermission("exwarp.sethome", 2655)) {
                return;
            }

            ExWorld world = user.getExWorld();
            hm.setHome(user.getUniqueId(), world.getBukkitWorld(), user.getLocation());

            if (hm.areWorldHomesEnabled()) {
                sender.sendPluginMessage(ChatColor.PERSONAL + "Home placed for world " + ChatColor.VALUE + world.getName());
            } else {
                sender.sendPluginMessage(ChatColor.PERSONAL + "Home placed");
            }

        } else {
            sender.sendMessageCommandHelp("Teleport to home", "home");
            sender.sendMessageCommandHelp("Set home", "sethome");
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (ExWarpServer.getHomeManager().areWorldHomesEnabled() && args.getLength() == 0) {
            return Server.getCommandManager().getTabCompleter().getWorldNames();
        }
        return null;
    }
}
