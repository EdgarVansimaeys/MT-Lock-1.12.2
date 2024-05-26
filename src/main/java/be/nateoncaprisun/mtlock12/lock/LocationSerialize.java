package be.nateoncaprisun.mtlock12.lock;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.print.DocFlavor;

@UtilityClass
public class LocationSerialize {

    public Location toLocation(String string){
        String[] location = string.split(",");
        return new Location(Bukkit.getWorld(location[0]), Double.valueOf(location[1]), Double.valueOf(location[2]), Double.valueOf(location[3]));
    }

    public String toString(Location location){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(location.getWorld().getName() + ",");
        stringBuilder.append(location.getBlockX() + ",");
        stringBuilder.append(location.getBlockY() + ",");
        stringBuilder.append(location.getBlockZ() + ",");
        return String.valueOf(stringBuilder);
    }

}
