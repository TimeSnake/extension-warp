package de.timesnake.extension.warp.server;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.extension.warp.home.HomeManager;
import de.timesnake.extension.warp.warp.WarpManager;

public class ExWarpServer extends Server {

    private static final WarpManager warpManager = new WarpManager();
    private static final HomeManager homeManager = new HomeManager();

    public static void onEnable() {
    }

    public static void onDisable() {
        warpManager.saveWarpsToFile();
        homeManager.saveHomesToFile();
    }

    public static WarpManager getWarpManager() {
        return warpManager;
    }

    public static HomeManager getHomeManager() {
        return homeManager;
    }
}
