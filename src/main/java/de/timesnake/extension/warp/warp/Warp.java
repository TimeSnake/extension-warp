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

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;

public class Warp {

    private final String name;
    private final Location location;
    private Collection<String> aliases;

    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
        this.aliases = new ArrayList<>();
    }

    public Warp(String name, Location location, Collection<String> aliases) {
        this.name = name;
        this.location = location;
        this.aliases = aliases;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public Collection<String> getAliases() {
        return aliases;
    }

    public void addAlias(String name) {
        this.aliases.add(name);
    }

    public void removeAlias(String name) {
        this.aliases.remove(name);
    }
}
