package de.timesnake.extension.warp.warp;

import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.ChatColor;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.warp.server.ExWarpServer;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WarpCmd implements CommandListener {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        WarpManager wm = ExWarpServer.getWarpManager();

        if (!wm.areWarpsEnabled()) {
            sender.sendPluginMessage(ChatColor.WARNING + "Warps are disabled");
            return;
        }

        if (!args.isLengthHigherEquals(1, true)) {
            return;
        }
        if (!sender.isPlayer(true)) {
            return;
        }
        User user = sender.getUser();

        String warpName = args.get(0).toLowerCase();
        if (!wm.containsWarp(warpName)) {
            if (args.isLengthHigherEquals(2, false) && args.get(1).equals("create") && sender.hasPermission("exwarp" +
                    ".warp.create", 2650)) {
                List<String> aliases = args.toStringList(2, args.getLength() - 1);
                wm.addWarp(new Warp(warpName, user.getLocation(), aliases));
                if (aliases == null || aliases.size() == 0) {
                    sender.sendPluginMessage(ChatColor.PERSONAL + "Created warp " + ChatColor.VALUE + warpName);
                } else {
                    sender.sendPluginMessage(ChatColor.PERSONAL + "Created warp " + ChatColor.VALUE + warpName + ChatColor.PERSONAL + " with aliases: " + Arrays.toString(aliases.toArray()));
                }
            } else {
                sender.sendMessageNotExist(warpName, 2750, "Warp");
                sender.sendMessageCommandHelp("Create warp", "warp <name> create [{aliases}]");
            }
        } else {
            Warp warp = wm.getWarp(warpName);

            if (args.isLengthEquals(1, false)) {
                if (!sender.hasPermission("exwarp.warp.use", 2651)) {
                    return;
                }
                user.teleport(warp.getLocation());
                sender.sendPluginMessage(ChatColor.PERSONAL + "Teleported to warp " + ChatColor.VALUE + warpName);
            } else if (args.isLengthEquals(3, false)) {
                if (args.get(2).toLowerCase().equals("alias") || args.isLengthEquals(4, true)) {
                    String alias = args.getString(3).toLowerCase();
                    if (args.get(1).toLowerCase().equals("add")) {
                        if (!sender.hasPermission("exwarp.warp.add", 2652)) {
                            return;
                        }
                        if (wm.containsWarp(alias)) {
                            sender.sendPluginMessage(ChatColor.WARNING + "Warp " + ChatColor.VALUE + alias + ChatColor.WARNING + " already exists");
                            return;
                        }
                        warp.addAlias(alias);
                        sender.sendPluginMessage(ChatColor.PERSONAL + "Added alias " + ChatColor.VALUE + alias + ChatColor.PERSONAL + " to warp " + ChatColor.VALUE + warpName);

                    } else if (args.get(1).toLowerCase().equals("remove")) {
                        if (!sender.hasPermission("exwarp.warp.remove", 2653)) {
                            return;
                        }
                        if (!warp.getAliases().contains(alias)) {
                            sender.sendPluginMessage(ChatColor.WARNING + "Warp alias " + ChatColor.VALUE + warpName + ChatColor.WARNING + " not exists");
                            return;
                        }
                        warp.removeAlias(alias);
                        sender.sendPluginMessage(ChatColor.PERSONAL + "Removed alias " + ChatColor.VALUE + alias + ChatColor.PERSONAL + "from warp " + ChatColor.VALUE + warpName);

                    }
                } else {
                    sender.sendMessageCommandHelp("Add alias", "warp <name> add alias <alias>");
                    sender.sendMessageCommandHelp("Remove alias", "warp <name> remove alias <alias>");
                }
            } else {
                sender.sendMessageCommandHelp("Use the warp", "warp <name/alias>");
                sender.sendMessageCommandHelp("Create warp", "warp <name> create [{aliases}]");
                sender.sendMessageCommandHelp("Add alias", "warp <name> add alias <alias>");
                sender.sendMessageCommandHelp("Remove alias", "warp <name> remove alias <alias>");

            }
        }
    }

    @Override
    public List<String> getTabCompletion(ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        if (args.getLength() == 1) {
            List<String> warps = new ArrayList<>();
            warps.add("create");
            for (Warp warp : ExWarpServer.getWarpManager().getWarps()) {
                warps.add(warp.getName());
            }
            return warps;
        }
        return null;
    }
}
