package org.godzillawiththemicrowave.playerheads;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerHeads extends JavaPlugin implements Listener {

    private boolean dropPlayerHeads = true; // Flag to toggle dropping player heads

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("PlayerHeads plugin enabled.");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("toggleplayerheads")) {
            // Check if the sender is not a player or if they are an OP
            if (!(sender instanceof Player) || sender.isOp() || sender instanceof ConsoleCommandSender) {
                // Toggle the flag
                dropPlayerHeads = !dropPlayerHeads;

                // Inform the sender about the status
                if (dropPlayerHeads) {
                    sender.sendMessage("Player heads dropping is now enabled.");
                } else {
                    sender.sendMessage("Player heads dropping is now disabled.");
                }

                // Debug info to server console
                getLogger().info("Player heads dropping is now " + (dropPlayerHeads ? "enabled" : "disabled") + " by " + sender.getName());

                return true;
            } else {
                sender.sendMessage("You don't have permission to use this command.");
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        if (!dropPlayerHeads) return; // Check if dropping player heads is enabled
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer != null) {
            ItemStack playerHead = getPlayerHead(player.getName());

            if (playerHead != null) {
                event.getDrops().add(playerHead);
            }
        }
    }

    private @Nullable ItemStack getPlayerHead(@NotNull String playerName) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwningPlayer(getServer().getOfflinePlayer(playerName));
        skull.setItemMeta(meta);

        if (meta.hasOwner()) {
            return skull;
        } else {
            return null;
        }
    }
}
