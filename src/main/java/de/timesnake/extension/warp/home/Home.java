package de.timesnake.extension.warp.home;

import org.bukkit.Location;

import java.util.UUID;

public class Home {

    private final UUID uuid;
    private final Location location;

    public Home(UUID uuid, Location location) {
        this.uuid = uuid;
        this.location = location;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Location getLocation() {
        return location;
    }

}
