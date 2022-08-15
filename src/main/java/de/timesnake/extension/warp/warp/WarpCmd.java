package de.timesnake.extension.warp.warp;

import de.timesnake.basic.bukkit.util.chat.Argument;
import de.timesnake.basic.bukkit.util.chat.CommandListener;
import de.timesnake.basic.bukkit.util.chat.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.warp.server.ExWarpServer;
import de.timesnake.library.basic.util.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Chat;
import de.timesnake.library.extension.util.cmd.Arguments;
import de.timesnake.library.extension.util.cmd.ExCommand;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class WarpCmd implements CommandListener {

    @Override
    public void onCommand(Sender sender, ExCommand<Sender, Argument> cmd, Arguments<Argument> args) {
        WarpManager wm = ExWarpServer.getWarpManager();

        if (!wm.areWarpsEnabled()) {
            sender.sendPluginMessage(Component.text("Warps are disabled", ExTextColor.WARNING));
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
                    sender.sendPluginMessage(Component.text("Created warp ", ExTextColor.PERSONAL)
                            .append(Component.text(warpName, ExTextColor.VALUE)));
                } else {
                    sender.sendPluginMessage(Component.text("Created warp ", ExTextColor.PERSONAL)
                            .append(Component.text(warpName, ExTextColor.VALUE))
                            .append(Component.text(" with aliases: ", ExTextColor.PERSONAL))
                            .append(Chat.listToComponent(aliases, ExTextColor.VALUE, ExTextColor.PERSONAL)));
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
                sender.sendPluginMessage(Component.text("Teleported to warp ", ExTextColor.PERSONAL)
                        .append(Component.text(warpName, ExTextColor.VALUE)));
            } else if (args.isLengthEquals(3, false)) {
                if (args.get(2).toLowerCase().equals("alias") || args.isLengthEquals(4, true)) {
                    String alias = args.getString(3).toLowerCase();
                    if (args.get(1).toLowerCase().equals("add")) {
                        if (!sender.hasPermission("exwarp.warp.add", 2652)) {
                            return;
                        }
                        if (wm.containsWarp(alias)) {
                            sender.sendPluginMessage(Component.text("Warp ", ExTextColor.WARNING)
                                    .append(Component.text(alias, ExTextColor.VALUE))
                                    .append(Component.text(" already exists", ExTextColor.WARNING)));
                            return;
                        }
                        warp.addAlias(alias);
                        sender.sendPluginMessage(Component.text("Added alias ", ExTextColor.PERSONAL)
                                .append(Component.text(alias, ExTextColor.VALUE))
                                .append(Component.text(" to warp ", ExTextColor.PERSONAL))
                                .append(Component.text(warpName, ExTextColor.VALUE)));

                    } else if (args.get(1).toLowerCase().equals("remove")) {
                        if (!sender.hasPermission("exwarp.warp.remove", 2653)) {
                            return;
                        }
                        if (!warp.getAliases().contains(alias)) {
                            sender.sendPluginMessage(Component.text("Warp alias ", ExTextColor.WARNING)
                                    .append(Component.text(warpName, ExTextColor.VALUE))
                                    .append(Component.text(" not exists", ExTextColor.WARNING)));
                            return;
                        }
                        warp.removeAlias(alias);
                        sender.sendPluginMessage(Component.text("Removed alias ", ExTextColor.PERSONAL)
                                .append(Component.text(alias, ExTextColor.VALUE))
                                .append(Component.text("from warp ", ExTextColor.PERSONAL))
                                .append(Component.text(warpName, ExTextColor.VALUE)));

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
