package de.timesnake.extension.warp.home;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.extension.warp.Plugin;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public class HomeManager {

    private final Map<List<Object>, Home> homes = new HashMap<>();

    private final boolean areHomesEnabled;
    private final boolean areWorldHomesEnabled;


    private final HomesFile file = new HomesFile();

    public HomeManager() {
        this.areHomesEnabled = this.file.areHomesEnabled();
        if (this.areHomesEnabled) {
            Server.printText(Plugin.HOME, "Homes are enabled");
        }

        this.areWorldHomesEnabled = this.file.areWorldHomesEnabled();
        if (this.areWorldHomesEnabled) {
            Server.printText(Plugin.HOME, "Homes per world are enabled");
        }

        for (Home home : this.file.getHomes()) {
            UUID uuid = home.getUuid();
            World world;
            world = home.getLocation().getWorld();

            List<Object> key = this.getKeyList(uuid, world);
            if (key == null) {
                key = List.of(uuid, world);
            }

            this.homes.put(key, home);
            Server.printText(Plugin.HOME,
                    "Loaded home " + home.getUuid() + " world: " + home.getLocation().getWorld().getName());
        }
        Server.printText(Plugin.HOME, "Loaded homes from file");
    }

    public List<Object> getKeyList(UUID uuid, World world) {
        for (List<Object> list : this.homes.keySet()) {
            if (list.get(0).equals(uuid) && list.get(1).equals(world)) {
                return list;
            }
        }
        return null;
    }

    public void saveHomesToFile() {
        this.file.resetHomes();
        for (Home home : this.homes.values()) {
            this.file.addHome(home);
        }
        Server.printText(Plugin.HOME, "Saved homes to file");
    }

    public Home getHome(UUID uuid, World world) {
        if (world == null) {
            for (ExWorld w : Server.getWorlds()) {
                if (this.containsHome(uuid, w.getBukkitWorld())) {
                    world = w.getBukkitWorld();
                }
            }
        }
        List<Object> key = this.getKeyList(uuid, world);
        return key == null ? null : this.homes.get(key);
    }

    public Collection<Home> getHomes() {
        return this.homes.values();
    }

    public void setHome(UUID uuid, World world, Location location) {
        Home home = new Home(uuid, location);
        List<Object> key = this.getKeyList(uuid, world);
        this.homes.put(Objects.requireNonNullElseGet(key, () -> List.of(uuid, world)), home);
    }

    public void removeHome(UUID uuid, World world) {
        if (world == null) {
            for (ExWorld w : Server.getWorlds()) {
                if (this.containsHome(uuid, w.getBukkitWorld())) {
                    world = w.getBukkitWorld();
                }
            }
        }
        List<Object> key = this.getKeyList(uuid, world);
        if (key != null) {
            this.homes.remove(key);
        }
    }

    public boolean containsHome(UUID uuid, World world) {
        if (world == null) {
            for (ExWorld w : Server.getWorlds()) {
                if (this.containsHome(uuid, w.getBukkitWorld())) {
                    return true;
                }
            }
            return false;
        }
        return this.getKeyList(uuid, world) != null;
    }

    public boolean areHomesEnabled() {
        return areHomesEnabled;
    }

    public boolean areWorldHomesEnabled() {
        return areWorldHomesEnabled;
    }
}
