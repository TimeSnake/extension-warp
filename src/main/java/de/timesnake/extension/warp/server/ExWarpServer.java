/*
 * extension-warp.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
 */

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
