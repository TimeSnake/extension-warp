/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.warp.main;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.extension.warp.home.HomeCmd;
import de.timesnake.extension.warp.server.ExWarpServer;
import de.timesnake.extension.warp.warp.WarpCmd;
import de.timesnake.library.chat.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ExWarp extends JavaPlugin {

  @Override
  public void onEnable() {

    Server.getCommandManager().addCommand(this, "warp", new WarpCmd(), Plugin.SERVER);
    Server.getCommandManager()
        .addCommand(this, "home", List.of("sethome"), new HomeCmd(), Plugin.SERVER);

    ExWarpServer.onEnable();
  }

  @Override
  public void onDisable() {
    ExWarpServer.onDisable();
  }
}
