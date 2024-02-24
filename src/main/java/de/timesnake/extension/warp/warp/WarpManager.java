/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.warp.warp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;

public class WarpManager {

  private final Logger logger = LogManager.getLogger("warp.manager");

  private final WarpsFile file = new WarpsFile();
  private final Collection<Warp> warps;

  private final boolean areWarpsEnabled;

  public WarpManager() {
    this.areWarpsEnabled = this.file.areWarpsEnabled();
    this.logger.info("Warps enabled");

    this.warps = this.file.getWarps();
    this.logger.info("Loaded warps from file");
  }

  public void saveWarpsToFile() {
    this.file.resetWarps();
    for (Warp warp : this.warps) {
      if (!warp.getAliases().isEmpty()) {
        Collection<String> aliases = warp.getAliases();
        String[] stringArray = new String[aliases.size()];
        this.file.addWarp(warp.getName(), warp.getLocation(), aliases.toArray(stringArray));
      } else {
        this.file.addWarp(warp.getName(), warp.getLocation());
      }
    }
    this.logger.info("Saved warps to file");
  }

  public Collection<Warp> getWarps() {
    return warps;
  }

  public boolean containsWarp(String warpName) {
    return this.getWarp(warpName) != null;
  }

  public Warp getWarp(String warpName) {
    for (Warp warp : this.warps) {
      if (warp.getName().equals(warpName)) {
        return warp;
      } else if (warp.getAliases().contains(warpName)) {
        return warp;
      }
    }
    return null;
  }

  public void addWarp(Warp warp) {
    this.warps.add(warp);
  }

  public void removeWarp(Warp warp) {
    this.warps.remove(warp);
  }

  public void removeWarp(String warpName) {
    Warp warp = this.getWarp(warpName);
    if (warp != null) {
      this.removeWarp(warp);
    }
  }

  public boolean areWarpsEnabled() {
    return areWarpsEnabled;
  }
}
