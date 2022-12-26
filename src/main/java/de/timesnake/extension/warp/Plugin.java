/*
 * Copyright (C) 2022 timesnake
 */

package de.timesnake.extension.warp;

import de.timesnake.library.basic.util.LogHelper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Plugin extends de.timesnake.basic.bukkit.util.chat.Plugin {

    public static final Plugin WARP = new Plugin("Warp", "EXW", LogHelper.getLogger("Warp", Level.INFO));
    public static final Plugin HOME = new Plugin("Home", "EXW", LogHelper.getLogger("Home", Level.INFO));

    protected Plugin(String name, String code, Logger logger) {
        super(name, code, logger);
    }
}
