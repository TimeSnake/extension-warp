/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.warp.home;


import de.timesnake.basic.bukkit.util.chat.cmd.Argument;
import de.timesnake.basic.bukkit.util.chat.cmd.CommandListener;
import de.timesnake.basic.bukkit.util.chat.cmd.Completion;
import de.timesnake.basic.bukkit.util.chat.cmd.Sender;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.extension.warp.Plugin;
import de.timesnake.extension.warp.server.ExWarpServer;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.commands.PluginCommand;
import de.timesnake.library.commands.simple.Arguments;
import de.timesnake.library.extension.util.chat.Code;
import net.kyori.adventure.text.Component;

public class HomeCmd implements CommandListener {

  private final Code perm = Plugin.WARP.createPermssionCode("exwarp.home");
  private final Code setPerm = Plugin.WARP.createPermssionCode("exwarp.sethome");

  @Override
  public void onCommand(Sender sender, PluginCommand cmd, Arguments<Argument> args) {
    HomeManager hm = ExWarpServer.getHomeManager();

    if (!sender.isPlayer(true)) {
      return;
    }

    if (!ExWarpServer.getHomeManager().areHomesEnabled()) {
      sender.sendPluginMessage(
          Component.text("Homes are not enabled on this server", ExTextColor.WARNING));
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
          sender.sendPluginMessage(
              Component.text("You have no home set in world ", ExTextColor.WARNING)
                  .append(Component.text(world.getName(), ExTextColor.VALUE)));
          sender.sendTDMessageCommandHelp("Set home", "sethome");
          return;
        }

        user.teleport(hm.getHome(user.getUniqueId(), world.getBukkitWorld()).getLocation());
        sender.sendPluginMessage(
            Component.text("Teleported to home in world ", ExTextColor.PERSONAL)
                .append(Component.text(world.getName(), ExTextColor.VALUE)));

      } else {
        if (!hm.containsHome(user.getUniqueId(), null)) {
          sender.sendPluginMessage(
              Component.text("You have no home set", ExTextColor.WARNING));
          sender.sendTDMessageCommandHelp("Set home", "sethome");
          return;
        }

        user.teleport(hm.getHome(user.getUniqueId(), null).getLocation());
        sender.sendPluginMessage(
            Component.text("Teleported to home", ExTextColor.PERSONAL));
      }

    } else if (cmd.getName().equalsIgnoreCase("sethome")) {
      if (!sender.hasPermission(this.setPerm)) {
        return;
      }

      ExWorld world = user.getExWorld();
      hm.setHome(user.getUniqueId(), world.getBukkitWorld(), user.getLocation());

      if (hm.areWorldHomesEnabled()) {
        sender.sendPluginMessage(
            Component.text("Home placed for world ", ExTextColor.PERSONAL)
                .append(Component.text(world.getName(), ExTextColor.VALUE)));
      } else {
        sender.sendPluginMessage(Component.text("Home placed", ExTextColor.PERSONAL));
      }

    } else {
      sender.sendTDMessageCommandHelp("Teleport to home", "home");
      sender.sendTDMessageCommandHelp("Set home", "sethome");
    }
  }

  @Override
  public Completion getTabCompletion() {
    return new Completion(this.perm)
        .addArgument(() -> ExWarpServer.getHomeManager().areWorldHomesEnabled() ? Completion.ofWorldNames() : Completion.empty());
  }

  @Override
  public String getPermission() {
    return this.perm.getPermission();
  }
}
