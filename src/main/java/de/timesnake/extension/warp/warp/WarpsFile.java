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

package de.timesnake.extension.warp.warp;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.exceptions.WorldNotExistException;
import de.timesnake.basic.bukkit.util.file.ExFile;
import de.timesnake.extension.warp.Plugin;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class WarpsFile extends ExFile {

    public static final String WARPS_ENABLED = "warps_enabled";

    public static final String WARPS = "warps";
    public static final String ALIASES = "aliases";

    public WarpsFile() {
        super("exwarp", "warps");
        super.addDefault(WARPS_ENABLED, false);
    }

    public void addWarp(String name, Location location, String... aliases) {
        super.setLocation(WARPS + "." + name, location, true).save();
        super.set(WARPS + "." + name + "." + ALIASES, aliases).save();
    }

    public void removeWarp(String name) {
        super.remove(WARPS + "." + name);
    }

    public Warp getWarp(String name) {
        Location loc;
        try {
            loc = super.getLocation(WARPS + "." + name);
        } catch (WorldNotExistException e) {
            return null;
        }
        String aliasesPath = WARPS + "." + name + "." + ALIASES;
        if (super.contains(aliasesPath)) {
            return new Warp(name, loc, super.getStringList(aliasesPath));
        }

        return new Warp(name, loc);
    }

    public Collection<Warp> getWarps() {
        Collection<Warp> warps = new ArrayList<>();
        Set<String> warpNames = super.getPathStringList(WARPS);
        if (warpNames != null) {
            for (String name : warpNames) {
                if (name != null) {
                    Warp warp = this.getWarp(name);
                    if (warp != null && warp.getLocation() != null) {
                        warps.add(warp);
                        Server.printText(Plugin.WARP, "Loaded warp " + warp.getName());
                    }
                }
            }
        }
        return warps;
    }

    public void resetWarps() {
        super.remove(WARPS);
    }

    public boolean areWarpsEnabled() {
        return super.getBoolean(WARPS_ENABLED);
    }
}
