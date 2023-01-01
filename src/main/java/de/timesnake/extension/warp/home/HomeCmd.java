/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.warp.home;


import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.extension.warp.server.ExWarpServer;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Code;
import de.timesnake.library.extension.util.chat.Plugin;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;

import java.util.List;

public class HomeCmd implements CommandListener {

    private Code.Permission perm;
    private Code.Permission setPerm;

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        HomeManager hm = ExWarpServer.getHomeManager();

        if (!sender.isPlayer(true)) {
            return;
        }

        if (!ExWarpServer.getHomeManager().areHomesEnabled()) {
            sender.sendPluginMessage(Component.text("Homes are not enabled on this server", ExTextColor.WARNING));
            return;
        }

        User user = sender.getUser();
        if (cmd.getName().equalsIgnoreCase("home")) {
            if (!sender.hasPermission(this.perm)) {
                return;
            }

            if (hm.areWorldHomesEnabled()) {
                ExWorld world = user.getExWorld();

                if (!hm.containsHome(user.getUniqueId(), world.getBukkitWorld())) {
                    sender.sendPluginMessage(Component.text("You have no home set in world ", ExTextColor.WARNING)
                            .append(Component.text(world.getName(), ExTextColor.VALUE)));
                    sender.sendMessageCommandHelp("Set home", "sethome");
                    return;
                }

                user.teleport(hm.getHome(user.getUniqueId(), world.getBukkitWorld()).getLocation());
                sender.sendPluginMessage(Component.text("Teleported to home in world ", ExTextColor.PERSONAL)
                        .append(Component.text(world.getName(), ExTextColor.VALUE)));

            } else {
                if (!hm.containsHome(user.getUniqueId(), null)) {
                    sender.sendPluginMessage(Component.text("You have no home set", ExTextColor.WARNING));
                    sender.sendMessageCommandHelp("Set home", "sethome");
                    return;
                }

                user.teleport(hm.getHome(user.getUniqueId(), null).getLocation());
                sender.sendPluginMessage(Component.text("Teleported to home", ExTextColor.PERSONAL));
            }

        } else if (cmd.getName().equalsIgnoreCase("sethome")) {
            if (!sender.hasPermission(this.setPerm)) {
                return;
            }

            ExWorld world = user.getExWorld();
            hm.setHome(user.getUniqueId(), world.getBukkitWorld(), user.getLocation());

            if (hm.areWorldHomesEnabled()) {
                sender.sendPluginMessage(Component.text("Home placed for world ", ExTextColor.PERSONAL)
                        .append(Component.text(world.getName(), ExTextColor.VALUE)));
            } else {
                sender.sendPluginMessage(Component.text("Home placed", ExTextColor.PERSONAL));
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

    @Override
    public void loadCodes(Plugin plugin) {
        this.perm = plugin.createPermssionCode("hom", "exwarp.home");
        this.setPerm = plugin.createPermssionCode("hom", "exwarp.sethome");
    }
}
