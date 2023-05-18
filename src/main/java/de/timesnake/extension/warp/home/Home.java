/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.warp.home;

import java.util.UUID;
import org.bukkit.Location;

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
