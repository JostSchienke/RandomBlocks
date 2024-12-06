package jes_ster.randomBlocks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class RandomBlocks extends JavaPlugin implements Listener {
    public Boolean IsRandom = false;
    private final Random random = new Random();
    public Boolean IsDebug = false;


    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Random Blocks Running");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Get Logger is Deactivated");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        UUID playerId = player.getUniqueId();

        switch (command.getName().toLowerCase()) {
            case "startrandom":
                StartRandomBlocks(player);
                break;

            case "stoprandom":
                StopRandomBlocks(player);

            case "randomdebug":
                ChangeRandomDebug(player);

            default:
                return false;
        }
        return true;
    }

    private void StartRandomBlocks(Player player){
        IsRandom = true;
        player.sendMessage("Item randomization is now enabled!");
        broadcastMessage(player.getName() + " has enabled item randomization!");
    }

    private void StopRandomBlocks(Player player){
        IsRandom = false;
        player.sendMessage("Item randomization is now disabled!");
        broadcastMessage(player.getName() + " has disabled item randomization!");
    }

    private void ChangeRandomDebug(Player player){
        IsDebug = !IsDebug;
        player.sendMessage("Plugin Debug Message are set to: " + IsDebug);
    }

    private void broadcastMessage(String message) {
        Bukkit.broadcastMessage("[RandomBlocks] " + message);
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (!IsRandom) return;

        // Ensure the entity is a player
        if (event.getEntity().getType() != EntityType.PLAYER) return;

        Player player = (Player) event.getEntity();
        Item itemEntity = event.getItem();
        ItemStack originalItem = itemEntity.getItemStack();

        // Randomize the item
        ItemStack randomizedItem = getRandomItem();
        randomizedItem.setAmount(originalItem.getAmount()); // Preserve stack size

        itemEntity.setItemStack(randomizedItem);
        if(IsDebug){
            player.sendMessage("You picked up: " + randomizedItem.getType());
        }
    }

    private ItemStack getRandomItem() {
        List<Material> materials = new ArrayList<>();
        for (Material material : Material.values()) {
            if (material.isItem()) {
                materials.add(material);
            }
        }

        Material randomMaterial = materials.get(random.nextInt(materials.size()));
        return new ItemStack(randomMaterial);
    }
}
