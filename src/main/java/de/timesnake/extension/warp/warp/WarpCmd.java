/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.warp.warp;

import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.Completion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.warp.Plugin;
import de.timesnake.extension.warp.server.ExWarpServer;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.extension.util.chat.Chat;
import de.timesnake.library.extension.util.chat.Code;
import net.kyori.adventure.text.Component;

import java.util.List;

public class WarpCmd implements CommandListener {

  private final Code createPerm = Plugin.WARP.createPermssionCode("exwarp.warp.create");
  private final Code usePerm = Plugin.WARP.createPermssionCode("exwarp.warp.use");
  private final Code removePerm = Plugin.WARP.createPermssionCode("exwarp.warp.add");
  private final Code addPerm = Plugin.WARP.createPermssionCode("exwarp.warp.remove");
  private final Code warpNotExists = Plugin.WARP.createHelpCode("Warp not exists");


  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
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
      if (args.isLengthHigherEquals(2, false) && args.get(1).equalsIgnoreCase("create")
          && sender.hasPermission(this.createPerm)) {
        List<String> aliases = args.toStringList(2, args.getLength() - 1);
        wm.addWarp(new Warp(warpName, user.getLocation(), aliases));
        if (aliases == null || aliases.isEmpty()) {
          sender.sendPluginMessage(Component.text("Created warp ", ExTextColor.PERSONAL)
              .append(Component.text(warpName, ExTextColor.VALUE)));
        } else {
          sender.sendPluginMessage(Component.text("Created warp ", ExTextColor.PERSONAL)
              .append(Component.text(warpName, ExTextColor.VALUE))
              .append(Component.text(" with aliases: ", ExTextColor.PERSONAL))
              .append(Chat.listToComponent(aliases, ExTextColor.VALUE,
                  ExTextColor.PERSONAL)));
        }
      } else {
        sender.sendMessageNotExist(warpName, this.warpNotExists, "Warp");
        sender.sendMessageCommandHelp(Component.text("Create warp"),
            Component.text("warp <name> create [{aliases}]"));
      }
    } else {
      Warp warp = wm.getWarp(warpName);

      if (args.isLengthEquals(1, false)) {
        if (!sender.hasPermission(this.usePerm)) {
          return;
        }
        user.teleport(warp.getLocation());
        sender.sendPluginMessage(Component.text("Teleported to warp ", ExTextColor.PERSONAL)
            .append(Component.text(warpName, ExTextColor.VALUE)));
      } else if (args.isLengthEquals(3, false)) {
        if (args.get(2).toLowerCase().equals("alias") || args.isLengthEquals(4, true)) {
          String alias = args.getString(3).toLowerCase();
          if (args.get(1).toLowerCase().equals("add")) {
            if (!sender.hasPermission(this.addPerm)) {
              return;
            }
            if (wm.containsWarp(alias)) {
              sender.sendPluginMessage(Component.text("Warp ", ExTextColor.WARNING)
                  .append(Component.text(alias, ExTextColor.VALUE))
                  .append(Component.text(" already exists",
                      ExTextColor.WARNING)));
              return;
            }
            warp.addAlias(alias);
            sender.sendPluginMessage(
                Component.text("Added alias ", ExTextColor.PERSONAL)
                    .append(Component.text(alias, ExTextColor.VALUE))
                    .append(Component.text(" to warp ", ExTextColor.PERSONAL))
                    .append(Component.text(warpName, ExTextColor.VALUE)));

          } else if (args.get(1).toLowerCase().equals("remove")) {
            if (!sender.hasPermission(this.removePerm)) {
              return;
            }
            if (!warp.getAliases().contains(alias)) {
              sender.sendPluginMessage(
                  Component.text("Warp alias ", ExTextColor.WARNING)
                      .append(Component.text(warpName, ExTextColor.VALUE))
                      .append(Component.text(" not exists",
                          ExTextColor.WARNING)));
              return;
            }
            warp.removeAlias(alias);
            sender.sendPluginMessage(
                Component.text("Removed alias ", ExTextColor.PERSONAL)
                    .append(Component.text(alias, ExTextColor.VALUE))
                    .append(Component.text("from warp ", ExTextColor.PERSONAL))
                    .append(Component.text(warpName, ExTextColor.VALUE)));

          }
        } else {
          sender.sendMessageCommandHelp(Component.text("Add alias"),
              Component.text("warp <name> add alias <alias>"));
          sender.sendMessageCommandHelp(Component.text("Remove alias"),
              Component.text("warp <name> remove alias <alias>"));
        }
      } else {
        sender.sendMessageCommandHelp(Component.text("Use the warp"),
            Component.text("warp <name/alias>"));
        sender.sendMessageCommandHelp(Component.text("Create warp"),
            Component.text("warp <name> create [{aliases}]"));
        sender.sendMessageCommandHelp(Component.text("Add alias"),
            Component.text("warp <name> add alias <alias>"));
        sender.sendMessageCommandHelp(Component.text("Remove alias"),
            Component.text("warp <name> remove alias <alias>"));

      }
    }
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion(this.usePerm)
        .addArgument(new Completion(ExWarpServer.getWarpManager().getWarps().stream().map(Warp::getName).toList())
            .addArgument(new Completion(this.createPerm, "create")));
  }

  @Override
  public String getPermission() {
    return this.usePerm.getPermission();
  }
}
