/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.extension.warp.main;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.extension.warp.Plugin;
import de.timesnake.extension.warp.home.HomeCmd;
import de.timesnake.extension.warp.server.ExWarpServer;
import de.timesnake.extension.warp.warp.WarpCmd;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ExWarp extends JavaPlugin {

    @Override
    public void onEnable() {

        Server.getCommandManager().addCommand(this, "warp", new WarpCmd(), Plugin.WARP);
        Server.getCommandManager().addCommand(this, "home", List.of("sethome"), new HomeCmd(), Plugin.HOME);


        ExWarpServer.onEnable();
    }

    @Override
    public void onDisable() {
        ExWarpServer.onDisable();
    }
}
