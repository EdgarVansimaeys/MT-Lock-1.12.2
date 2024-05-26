package be.nateoncaprisun.mtlock12.listeners;

import be.nateoncaprisun.mtlock12.MTLock;
import be.nateoncaprisun.mtlock12.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

public class LockInteractionListener implements Listener {

    private MTLock main;

    public LockInteractionListener(MTLock main){
        this.main = main;

        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onlockInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block target = event.getClickedBlock();

        if (target == null) return;
        if (target.getType() == Material.AIR) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Block block = MTLock.getInstance().doorCheck(target);
        if (!MTLock.getInstance().isLockable(block)) return;
        if (MTLock.getInstance().getLockOwner().get(block.getLocation()) == null) return;
        if (!MTLock.getInstance().getLockOwner().get(block.getLocation()).equals(player.getUniqueId()) && MTLock.getInstance().getLockMembers().get(block.getLocation()) == null) {
            event.setCancelled(true);
            player.sendMessage(Utils.color("&cDit block is gelocked."));
            return;
        }

        if (!MTLock.getInstance().getLockOwner().get(block.getLocation()).equals(player.getUniqueId())
                && !player.hasPermission("lock.admin")
                && !MTLock.getInstance().getLockMembers().get(block.getLocation()).contains(player.getUniqueId().toString())){
            event.setCancelled(true);
            player.sendMessage(Utils.color("&cDit block is gelocked."));
            return;
        }

        if (block.getType().equals(Material.IRON_TRAPDOOR)){
            BlockState state = block.getState();
            Openable openable = (Openable) state.getData();
            openable.setOpen(!openable.isOpen());
            if (openable.isOpen()) {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 3, 3);
            } else {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 3, 3);
            }
            //state.setData((MaterialData) openable);
            state.update();

        }

        if (block.getType().equals(Material.IRON_DOOR_BLOCK)) {
            BlockState blockState = block.getState();
            Door iDoor = (Door) blockState.getData();
            iDoor.setOpen(!iDoor.isOpen());
            blockState.update();
            if (iDoor.isOpen()) {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 3, 3);
            } else {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 3, 3);
            }
        }

        player.sendMessage(Utils.color("&aJe opent een gelocked block van &2" + Bukkit.getOfflinePlayer(MTLock.getInstance().getLockOwner().get(block.getLocation())).getName()));
    }

}
