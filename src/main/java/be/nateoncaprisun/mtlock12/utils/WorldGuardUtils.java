package be.nateoncaprisun.mtlock12.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class WorldGuardUtils {

    public ProtectedRegion getRegion(Location location) {
        Set<ProtectedRegion> regions = WorldGuardPlugin.inst().getRegionManager(location.getWorld()).getApplicableRegions(location).getRegions().stream().filter(region -> (region.getPriority() >= 0)).collect(Collectors.toSet());
        if (regions.size() != 1) {
            return null;
        }
        ProtectedRegion region = regions.iterator().next();
        return region;
    }

}
