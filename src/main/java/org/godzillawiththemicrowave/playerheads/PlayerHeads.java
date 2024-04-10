package org.godzillawiththemicrowave.playerheads;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class PlayerHeads extends JavaPlugin implements Listener {

    private boolean dropPlayerHeads = true; // Flag to toggle dropping player heads

    @Override
    public void onEnable() {
        // Registering the plugin's event listener when the plugin is enabled
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("toggleplayerheads")) {
            // Check if the sender is not a player or if they are an OP
            if (!(sender instanceof Player) || sender.isOp()) {
                // Toggle the flag
                dropPlayerHeads = !dropPlayerHeads;

                // Inform the sender about the status
                if (dropPlayerHeads) {
                    sender.sendMessage("Player heads dropping is now enabled.");
                } else {
                    sender.sendMessage("Player heads dropping is now disabled.");
                }
                return true;
            } else {
                sender.sendMessage("You don't have permission to use this command.");
            }
        }
        return false;
    }



    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Retrieving the player who died
        Player player = event.getEntity();
        // Retrieving the player who killed the player
        Player killer = player.getKiller();

        // Checking if the player was killed by another player
        if (killer != null) {
            // Getting the player head ItemStack
            ItemStack playerHead = getPlayerHead(player.getName());

            // Checking if the player head exists
            if (playerHead != null) {
                // Adding the player head to the drops
                event.getDrops().add(playerHead);
            }
        }
    }

    private ItemStack getPlayerHead(String playerName) {
        // Creating a new ItemStack for the player head
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        // Getting the SkullMeta of the player head ItemStack
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        // Setting the owning player of the player head
        meta.setOwningPlayer(getServer().getOfflinePlayer(playerName));
        // Applying the SkullMeta to the ItemStack
        skull.setItemMeta(meta);

        // Checking if the player head was successfully created
        if (meta.hasOwner()) {
            // Returning the player head ItemStack
            return skull;
        } else {
            // Returning null if the player head couldn't be created
            return null;
        }
    }
}


