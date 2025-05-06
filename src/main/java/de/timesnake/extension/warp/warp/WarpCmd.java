/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.warp.warp;

import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.Completion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.extension.warp.server.ExWarpServer;
import de.timesnake.library.chat.Code;
import de.timesnake.library.chat.Plugin;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;

import java.util.List;

public class WarpCmd implements CommandListener {

  private final Code createPerm = Plugin.SERVER.createPermssionCode("exwarp.warp.create");
  private final Code usePerm = Plugin.SERVER.createPermssionCode("exwarp.warp.use");
  private final Code removePerm = Plugin.SERVER.createPermssionCode("exwarp.warp.add");
  private final Code addPerm = Plugin.SERVER.createPermssionCode("exwarp.warp.remove");
  private final Code warpNotExists = Plugin.SERVER.createHelpCode("Warp not exists");


  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    WarpManager wm = ExWarpServer.getWarpManager();

    if (!wm.areWarpsEnabled()) {
      sender.sendPluginTDMessage("§wWarps are disabled");
      return;
    }

    args.isLengthHigherEqualsElseExit(1, true);
    sender.isPlayerElseExit(true);
    User user = sender.getUser();

    String warpName = args.get(0).toLowerCase();
    if (!wm.containsWarp(warpName)) {
      if (args.isLengthHigherEquals(2, false) && args.get(1).equalsIgnoreCase("create")
          && sender.hasPermission(this.createPerm)) {
        List<String> aliases = args.toStringList(2, args.getLength() - 1);
        wm.addWarp(new Warp(warpName, user.getLocation(), aliases));
        if (aliases == null || aliases.isEmpty()) {
          sender.sendPluginTDMessage("§sCreated warp §v" + warpName);
        } else {
          sender.sendPluginTDMessage("§sCreated warp §v" + warpName + "§s with aliases: §v" +
                                     String.join("§s, §v", aliases));
        }
      } else {
        sender.sendMessageNotExist(warpName, this.warpNotExists, "Warp");
        sender.sendTDMessageCommandHelp("Create warp", "warp <name> create [{aliases}]");
      }
    } else {
      Warp warp = wm.getWarp(warpName);

      if (args.isLengthEquals(1, false)) {
        sender.hasPermissionElseExit(this.usePerm);
        user.teleport(warp.getLocation());
        sender.sendPluginTDMessage("§sTeleported to warp §v" + warpName);
      } else if (args.isLengthEquals(3, false)) {
        if (args.get(2).toLowerCase().equals("alias") || args.isLengthEquals(4, true)) {
          String alias = args.getString(3).toLowerCase();
          if (args.get(1).toLowerCase().equals("add")) {
            sender.hasPermissionElseExit(this.addPerm);

            if (wm.containsWarp(alias)) {
              sender.sendPluginTDMessage("§wWarp §v" + alias + "§v already exists");
              return;
            }
            warp.addAlias(alias);
            sender.sendPluginTDMessage("§sAdded alias §v" + alias + "§s to warp §v" + warpName);

          } else if (args.get(1).toLowerCase().equals("remove")) {
            sender.hasPermissionElseExit(this.removePerm);
            if (!warp.getAliases().contains(alias)) {
              sender.sendPluginTDMessage("§wWarp alias §v" + warpName + "§w not exists");
              return;
            }
            warp.removeAlias(alias);
            sender.sendPluginTDMessage("§sRemoved alias §v" + alias + "§s from warp §v" + warpName);

          }
        } else {
          sender.sendTDMessageCommandHelp("Add alias", "warp <name> add alias <alias>");
          sender.sendTDMessageCommandHelp("Remove alias", "warp <name> remove alias <alias>");
        }
      } else {
        sender.sendTDMessageCommandHelp("Use the warp", "warp <name/alias>");
        sender.sendTDMessageCommandHelp("Create warp", "warp <name> create [{aliases}]");
        sender.sendTDMessageCommandHelp("Add alias", "warp <name> add alias <alias>");
        sender.sendTDMessageCommandHelp("Remove alias", "warp <name> remove alias <alias>");
      }
    }
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion(this.usePerm)
        .addArgument(new Completion(ExWarpServer.getWarpManager().getWarps().stream().map(Warp::getName).toList()))
        .addArgument(new Completion("<name>").allowAny()
            .addArgument(new Completion(this.createPerm, "create")));
  }

  @Override
  public String getPermission() {
    return this.usePerm.getPermission();
  }
}
