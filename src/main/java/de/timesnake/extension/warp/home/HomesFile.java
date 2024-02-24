/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.warp.home;

import de.timesnake.basic.bukkit.util.exception.WorldNotExistException;
import de.timesnake.basic.bukkit.util.file.ExFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class HomesFile extends ExFile {

  public static final String HOMES = "homes";
  public static final String HOMES_ENABLED = "homes_enabled";
  public static final String WORLD_HOMES_ENABLED = "world_homes_enabled";

  private final Logger logger = LogManager.getLogger("home.file");

  public HomesFile() {
    super("exwarp", "homes");
    super.addDefault(HOMES_ENABLED, false);
    super.addDefault(WORLD_HOMES_ENABLED, false);
  }

  public void addHome(Home home) {
    super.setLocation(
        HOMES + "." + home.getUuid().toString() + "." + home.getLocation().getWorld()
            .getName(),
        home.getLocation(), true);
  }

  public void removeHome(Home home) {
    super.remove(HOMES + "." + home.getUuid().toString() + "." + home.getLocation().getWorld()
        .getName());
  }

  public Home getHome(UUID uuid, String worldName) {
    String path = HOMES + "." + uuid.toString() + "." + worldName;
    if (super.contains(path)) {
      Location location;
      try {
        location = super.getLocation(path);
      } catch (WorldNotExistException e) {
        this.logger.warn("Can not get location (world not exist) from uuid: '{}' world: {}", uuid, worldName);
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
          this.logger.warn("Can not get uuid from string: {}", uuidString);
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
