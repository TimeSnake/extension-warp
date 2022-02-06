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
