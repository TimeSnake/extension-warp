/*
 * workspace.extension-warp.main
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

package de.timesnake.extension.warp.home;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.exception.WorldNotExistException;
import de.timesnake.basic.bukkit.util.file.ExFile;
import de.timesnake.extension.warp.Plugin;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class HomesFile extends ExFile {

    public static final String HOMES = "homes";
    public static final String HOMES_ENABLED = "homes_enabled";
    public static final String WORLD_HOMES_ENABLED = "world_homes_enabled";

    public HomesFile() {
        super("exwarp", "homes");
        super.addDefault(HOMES_ENABLED, false);
        super.addDefault(WORLD_HOMES_ENABLED, false);
    }

    public void addHome(Home home) {
        super.setLocation(HOMES + "." + home.getUuid().toString() + "." + home.getLocation().getWorld().getName(),
                home.getLocation(), true);
    }

    public void removeHome(Home home) {
        super.remove(HOMES + "." + home.getUuid().toString() + "." + home.getLocation().getWorld().getName());
    }

    public Home getHome(UUID uuid, String worldName) {
        String path = HOMES + "." + uuid.toString() + "." + worldName;
        if (super.contains(path)) {
            Location location;
            try {
                location = super.getLocation(path);
            } catch (WorldNotExistException e) {
                Server.printWarning(Plugin.HOME,
                        "Can not get location (world not exist) from uuid: " + uuid.toString() + " world: " + worldName);
                return null;
            }
            if (location != null) {
                return new Home(uuid, location);
            }
        }
        return null;
    }

    public Collection<Home> getHomes() {
        Set<String> uuids = super.getPathStringList(HOMES);
        Collection<Home> homes = new ArrayList<>();
        if (uuids != null) {
            for (String uuidString : uuids) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(uuidString);
                } catch (IllegalArgumentException e) {
                    Server.printWarning(Plugin.HOME, "Can not get uuid from string: " + uuidString);
                    continue;
                }

                Set<String> worldNames = super.getPathStringList(HOMES + "." + uuidString);
                if (worldNames != null) {
                    for (String worldName : worldNames) {
                        Home home = this.getHome(uuid, worldName);
                        if (home != null) {
                            homes.add(home);
                        }
                    }
                }
            }
        }
        return homes;
    }

    public boolean areHomesEnabled() {
        return super.getBoolean(HOMES_ENABLED);
    }

    public boolean areWorldHomesEnabled() {
        return super.getBoolean(WORLD_HOMES_ENABLED);
    }

    public void resetHomes() {
        super.remove(HOMES);
    }
}
