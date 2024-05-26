package be.nateoncaprisun.mtlock12.lock;

import be.nateoncaprisun.mtlock12.MTLock;
import be.nateoncaprisun.mtlock12.utils.Utils;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@UtilityClass
public class LockUtils {

    public void createLock(Location location, Player owner){

        if (MTLock.getInstance().getLockOwner().get(location) != null){
            owner.sendMessage(Utils.color("&cEr staat al een lock op dit block!"));
            return;
        }

        MTLock.getInstance().getLockOwner().put(location, owner.getUniqueId());

        owner.sendMessage(Utils.color("&aLock aangemaakt."));
    }

    public void deleteLock(Location location, Player owner){

        if (MTLock.getInstance().getLockOwner().get(location) == null){
            owner.sendMessage(Utils.color("&cEr staat geen lock op dit block!"));
            return;
        }

        MTLock.getInstance().getLockOwner().remove(location);
        MTLock.getInstance().getLockMembers().remove(location);

        if (MTLock.getInstance().getLocks().getConfig().getConfigurationSection("locks."+ LocationSerialize.toString(location)) != null){
            MTLock.getInstance().getLocks().getConfig().set("locks."+LocationSerialize.toString(location), null);
            MTLock.getInstance().getLocks().saveConfig();
        }

        owner.sendMessage(Utils.color("&aLock verwijderd."));
    }

}
