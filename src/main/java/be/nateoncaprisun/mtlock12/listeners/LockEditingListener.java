package be.nateoncaprisun.mtlock12.listeners;

import be.nateoncaprisun.mtlock12.MTLock;
import be.nateoncaprisun.mtlock12.lock.LockUtils;
import be.nateoncaprisun.mtlock12.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class LockEditingListener implements Listener {

    private MTLock main;

    public LockEditingListener(MTLock main){
        this.main = main;

        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void lockCreate(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block target = event.getClickedBlock();

        if (!MTLock.getInstance().getLockCreating().contains(player)) return;
        if (target == null) return;
        if (target.getType() == Material.AIR) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        MTLock.getInstance().getLockCreating().remove(player);

        Block block = MTLock.getInstance().doorCheck(target);

        event.setCancelled(true);

        if (!MTLock.getInstance().canLock(player, block)){
            player.sendMessage(Utils.color("&cJe kan hier geen lock plaatsen."));
            return;
        }

        if (!MTLock.getInstance().isLockable(block)){
            player.sendMessage(Utils.color("&cDat is geen lockable block."));
            return;
        }

        LockUtils.createLock(block.getLocation(), player);
    }

    @EventHandler
    public void lockDeleting(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block target = event.getClickedBlock();

        if (!MTLock.getInstance().getLockDeleting().contains(player)) return;
        if (target == null) return;
        if (target.getType() == Material.AIR) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        MTLock.getInstance().getLockDeleting().remove(player);

        Block block = MTLock.getInstance().doorCheck(target);

        event.setCancelled(true);

        if (!MTLock.getInstance().canLock(player, block)){
            player.sendMessage(Utils.color("&cJe kan hier geen lock plaatsen."));
            return;
        }

        if (!MTLock.getInstance().isLockable(block)){
            player.sendMessage(Utils.color("&cDat is geen lockable block."));
            return;
        }

        LockUtils.deleteLock(block.getLocation(), player);
    }

}
