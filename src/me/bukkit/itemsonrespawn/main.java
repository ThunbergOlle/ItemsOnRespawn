package me.bukkit.itemsonrespawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class main extends JavaPlugin implements Listener{
	@Override
	public void onEnable() {
		getLogger().info(ChatColor.GREEN + "Items on respawn enabled");
		loadConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String labvel, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("setupitemsonrespawn") && sender instanceof Player) {
			
			Player p = (Player) sender;
			String pvpWorld = p.getWorld().getName();

			getConfig().set("World."+pvpWorld, 1);
			saveConfig();
			
			p.sendMessage("Set "+pvpWorld+" to a pvp world.");
			return false;
		}
		return false;
	}
	public void loadConfig() {
		getConfig().options().copyDefaults(true);	
		saveConfig();
	}
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();
		
		String worldname = p.getWorld().getName();
		
		int checkWorld = getConfig().getInt("World."+worldname);
		if(checkWorld == 1) {
			p.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD, 1));
			p.getInventory().addItem(new ItemStack(Material.BOW, 1));
			p.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 5));
			p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
			p.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, 16));

			p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
			p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
			p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
			p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
			
			
			p.getInventory().addItem(new ItemStack(Material.ARROW, 32));
			p.getInventory().addItem(new ItemStack(Material.LAPIS_LAZULI, 16));

			
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			    public void run() {
						p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 200, 2));
			    }
			}, 20);
			

			
		}

	}
	@EventHandler
	public void onFallDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof  Player && event.getCause() == DamageCause.FALL) {
			
            Player p = (Player)event.getEntity();	
			String worldname = p.getWorld().getName();
			int checkWorld = getConfig().getInt("World."+worldname);
			if(checkWorld == 1 && p.hasPotionEffect(PotionEffectType.WATER_BREATHING)) {
				event.setCancelled(true);
				return;
			}
		}
		return;

	}
}
