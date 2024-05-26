package be.nateoncaprisun.mtlock12;

import be.nateoncaprisun.mtlock12.commands.LockCommand;
import be.nateoncaprisun.mtlock12.listeners.LockEditingListener;
import be.nateoncaprisun.mtlock12.listeners.LockInteractionListener;
import be.nateoncaprisun.mtlock12.utils.ConfigurationFile;
import be.nateoncaprisun.mtlock12.lock.LocationSerialize;
import be.nateoncaprisun.mtlock12.utils.WorldGuardUtils;
import co.aikar.commands.BukkitCommandManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;
import org.bukkit.material.Openable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class MTLock extends JavaPlugin {
    private static @Getter MTLock instance;
    private @Getter ConfigurationFile locks;
    private @Getter BukkitCommandManager commandManager;
    private @Getter Map<Location, UUID> lockOwner = new HashMap<>();
    private @Getter Map<Location, List<String>> lockMembers = new HashMap<>();
    private @Getter List<Player> lockCreating = new ArrayList<>();
    private @Getter List<Player> lockDeleting = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        locks = new ConfigurationFile(this, "lock.yml", true);
        locks.saveConfig();

        commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new LockCommand());

        registerLocks();

        new LockEditingListener(this);
        new LockInteractionListener(this);
    }

    @Override
    public void onDisable() {
        saveLocks();
    }

    public Boolean isLockable(Block block){
        if (block.getState().getData() instanceof Openable) return true;
        if (block.getState() instanceof Chest) return true;
        return false;
    }

    public Boolean canLock(Player player, Block block){
        if (player.hasPermission("lock.admin")) return true;
        ProtectedRegion protectedRegion = WorldGuardUtils.getRegion(block.getLocation());
        if (protectedRegion == null) return false;
        if (!protectedRegion.isOwner(WorldGuardPlugin.inst().wrapPlayer(player))) return true;
        return false;
    }

    private void registerLocks(){
        if (locks.getConfig().getConfigurationSection("locks") == null) return;
        for (String s : locks.getConfig().getConfigurationSection("locks").getKeys(false)){
            Location location = LocationSerialize.toLocation(s);
            if (locks.getConfig().getString("locks." + s + ".owner") == null) continue;

            lockOwner.put(location, UUID.fromString(locks.getConfig().getString("locks." + s + ".owner")));

            if (locks.getConfig().getString("locks." + s + ".owner") == null) continue;

            List<String> memberlist = new ArrayList<>();
            for (String memberUUID : locks.getConfig().getStringList("locks." + s + ".members")){
                memberlist.add(memberUUID);
            }

            lockMembers.put(location, memberlist);
        }
    }

    public void saveLocks(){
        if (lockOwner == null || lockOwner.isEmpty()) return;
        lockOwner.forEach((location, uuid) -> {
            locks.getConfig().set("locks." + LocationSerialize.toString(location) + ".owner", uuid.toString());
            locks.saveConfig();
        });

        if (lockMembers == null || lockMembers.isEmpty()) return;
        lockMembers.forEach((location, uuidList) -> {
            locks.getConfig().set("locks." + LocationSerialize.toString(location) + ".members", uuidList);
            locks.saveConfig();
        });
    }

    public Block doorCheck(Block block){
        if (!(block.getState().getData() instanceof Door)) return block;
        Door door = (Door) block.getState().getData();
        if (door.isTopHalf()) block = block.getLocation().getWorld().getBlockAt(block.getLocation().add(0,-1,0));
        return block;
    }
}
