/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.extension.warp;

public class Plugin extends de.timesnake.basic.bukkit.util.chat.Plugin {

  public static final Plugin WARP = new Plugin("Warp", "EXW");
  public static final Plugin HOME = new Plugin("Home", "EXW");

  protected Plugin(String name, String code) {
    super(name, code);
  }
}
