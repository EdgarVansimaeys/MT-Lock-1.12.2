package be.nateoncaprisun.mtlock12.commands;

import be.nateoncaprisun.mtlock12.MTLock;
import be.nateoncaprisun.mtlock12.utils.Utils;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CommandAlias("lock")
public class LockCommand extends BaseCommand {

    @HelpCommand
    public void help(Player player){
        if (!player.hasPermission("lock.admin") && !player.isOp()) {
            player.sendMessage(Utils.color("&8&m---------------------------------"));
            player.sendMessage("");
            player.sendMessage(Utils.color("&8&l» &6MT-Lock-1.12"));
            player.sendMessage(Utils.color("&8&l» &fVersie: &e1.0.0"));
            player.sendMessage(Utils.color("&8&l» &fBy &f&lNateOnCaprisun"));
            player.sendMessage("");
            player.sendMessage(Utils.color("&7/lock help &8➠ &fKrijg dit bericht"));
            player.sendMessage(Utils.color("&7/lock create &8➠ &fMaak een lock"));
            player.sendMessage(Utils.color("&7/lock delete &8➠ &fVerwijder een lock"));
            player.sendMessage(Utils.color("&7/lock addmember &o<speler> &8➠ &fVoeg een speler toe aan een lock"));
            player.sendMessage(Utils.color("&7/lock removemember &o<speler> &8➠ &fVerwijder een speler van een lock"));
            player.sendMessage(Utils.color("&7/lock info &8➠ &fBekijk informatie over een lock"));
            player.sendMessage("");
            player.sendMessage(Utils.color("&8&m---------------------------------"));
        } else {
            player.sendMessage(Utils.color("&8&m---------------------------------"));
            player.sendMessage("");
            player.sendMessage(Utils.color("&8&l» &6MT-Lock-1.12"));
            player.sendMessage(Utils.color("&8&l» &fVersie: &e1.0.0"));
            player.sendMessage(Utils.color("&8&l» &fBy &f&lNateOnCaprisun"));
            player.sendMessage("");
            player.sendMessage(Utils.color("&7/lock help &8➠ &fKrijg dit bericht"));
            player.sendMessage(Utils.color("&7/lock create &8➠ &fMaak een lock"));
            player.sendMessage(Utils.color("&7/lock delete &8➠ &fVerwijder een lock"));
            player.sendMessage(Utils.color("&7/lock setowner &o<speler> &8➠ &fZet een owner van een lock"));
            player.sendMessage(Utils.color("&7/lock addmember &o<speler> &8➠ &fVoeg een speler toe aan een lock"));
            player.sendMessage(Utils.color("&7/lock removemember &o<speler> &8➠ &fVerwijder een speler van een lock"));
            player.sendMessage(Utils.color("&7/lock info &8➠ &fBekijk informatie over een lock"));
            player.sendMessage("");
            player.sendMessage(Utils.color("&8&m---------------------------------"));
        }
    }

    @Subcommand("create")
    public void create(Player player){
        if (MTLock.getInstance().getLockCreating().contains(player)){
            player.sendMessage(Utils.color("&cJe bent al een lock aan het maken."));
            return;
        }
        MTLock.getInstance().getLockCreating().add(player);
        player.sendMessage(Utils.color("&aKlik op het block dat je wilt locken."));
    }

    @Subcommand("delete")
    public void delete(Player player){
        if (MTLock.getInstance().getLockDeleting().contains(player)){
            player.sendMessage(Utils.color("&cJe bent al een lock aan het maken."));
            return;
        }
        MTLock.getInstance().getLockDeleting().add(player);
        player.sendMessage(Utils.color("&aKlik op het block dat je wilt unlocken."));
    }

    @Subcommand("addmember")
    public void addmember(Player player, OfflinePlayer offlinePlayer){
        Block target = player.getTargetBlock(null, 5);

        if (target == null) {
            player.sendMessage(Utils.color("&cDit is geen lockable block!"));
            return;
        }

        Block block = MTLock.getInstance().doorCheck(target);

        if (MTLock.getInstance().getLockOwner().get(block.getLocation()) == null) {
            player.sendMessage(Utils.color("&cDit block is niet gelocked!"));
            return;
        }

        if (!MTLock.getInstance().getLockOwner().get(block.getLocation()).equals(player.getUniqueId()) && !player.hasPermission("lock.admin")) {
            player.sendMessage(Utils.color("&cJe bent niet de eigenaar van deze lock!"));
            return;
        }

        if (MTLock.getInstance().getLockMembers().get(block.getLocation()) != null && MTLock.getInstance().getLockMembers().get(block.getLocation()).contains(offlinePlayer.getUniqueId().toString())){
            player.sendMessage(Utils.color("&cDeze speler is al een member van deze lock!"));
            return;
        }

        List<String> lockMembers = MTLock.getInstance().getLockMembers().get(block.getLocation());
        if (lockMembers == null) lockMembers = new ArrayList<>();
        lockMembers.add(offlinePlayer.getUniqueId().toString());

        MTLock.getInstance().getLockMembers().put(block.getLocation(), lockMembers);
        player.sendMessage(Utils.color("&aJe hebt &2" + offlinePlayer.getName() + "&a toegevoegd aan je lock."));
    }

    @Subcommand("removemember")
    public void removemember(Player player, OfflinePlayer offlinePlayer){
        Block target = player.getTargetBlock(null, 5);

        if (target == null) {
            player.sendMessage(Utils.color("&cDit is geen lockable block!"));
            return;
        }

        Block block = MTLock.getInstance().doorCheck(target);

        if (MTLock.getInstance().getLockOwner().get(block.getLocation()) == null) {
            player.sendMessage(Utils.color("&cDit block is niet gelocked!"));
            return;
        }

        if (!MTLock.getInstance().getLockOwner().get(block.getLocation()).equals(player.getUniqueId()) && !player.hasPermission("lock.admin")) {
            player.sendMessage(Utils.color("&cJe bent niet de eigenaar van deze lock!"));
            return;
        }

        if (MTLock.getInstance().getLockMembers().get(block.getLocation()) != null || !MTLock.getInstance().getLockMembers().get(block.getLocation()).contains(offlinePlayer.getUniqueId().toString())){
            player.sendMessage(Utils.color("&cDeze speler is geen member van deze lock!"));
            return;
        }

        List<String> lockMembers = MTLock.getInstance().getLockMembers().get(block.getLocation());
        lockMembers.remove(offlinePlayer.getUniqueId().toString());

        MTLock.getInstance().getLockMembers().put(block.getLocation(), lockMembers);
        player.sendMessage(Utils.color("&aJe hebt &2" + offlinePlayer.getName() + "&a verwijderd van je lock."));
    }

    @Subcommand("setowner")
    @CommandPermission("lock.admin")
    public void setOwner(Player player, OfflinePlayer offlinePlayer) {
        Block target = player.getTargetBlock(null, 5);

        if (target == null) {
            player.sendMessage(Utils.color("&cDit is geen lockable block!"));
            return;
        }

        Block block = MTLock.getInstance().doorCheck(target);

        if (MTLock.getInstance().getLockOwner().get(block.getLocation()) == null) {
            player.sendMessage(Utils.color("&cDit block is niet gelocked!"));
            return;
        }

        if (!player.hasPermission("lock.admin")) {
            player.sendMessage(Utils.color("&cJe bent niet de eigenaar van deze lock!"));
            return;
        }

        MTLock.getInstance().getLockOwner().put(block.getLocation(), offlinePlayer.getUniqueId());
        player.sendMessage(Utils.color("&aJe hebt &2" + offlinePlayer.getName() + "&a aangewezen als nieuwe owner van deze lock."));
    }

    @Subcommand("info")
    public void info(Player player) {
        Block target = player.getTargetBlock(null, 5);

        if (target == null) {
            player.sendMessage(Utils.color("&cDit is geen lockable block!"));
            return;
        }

        Block block = MTLock.getInstance().doorCheck(target);

        if (MTLock.getInstance().getLockOwner().get(block.getLocation()) == null) {
            player.sendMessage(Utils.color("&cDit block is niet gelocked!"));
            return;
        }

        player.sendMessage(Utils.color("&aLock Eigenaar: &2" + Bukkit.getOfflinePlayer(MTLock.getInstance().getLockOwner().get(block.getLocation())).getName()));
        if (MTLock.getInstance().getLockMembers().get(block.getLocation()) == null || MTLock.getInstance().getLockMembers().get(block.getLocation()).isEmpty()) return;
        for (String stringUUID : MTLock.getInstance().getLockMembers().get(block.getLocation())) {
            player.sendMessage(Utils.color(" &a- &2" + Bukkit.getOfflinePlayer(UUID.fromString(stringUUID)).getName()));
        }
    }

}
