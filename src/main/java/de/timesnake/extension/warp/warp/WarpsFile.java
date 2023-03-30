/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.warp.warp;

import de.timesnake.basic.bukkit.util.exception.WorldNotExistException;
import de.timesnake.basic.bukkit.util.file.ExFile;
import de.timesnake.library.basic.util.Loggers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import org.bukkit.Location;

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
                        Loggers.WARPS.info("Loaded warp " + warp.getName());
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
