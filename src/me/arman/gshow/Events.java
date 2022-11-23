package me.arman.gshow;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Events implements Listener {

	private Main plugin;

	public Events(Main instance) {
		plugin = instance;
	}

	@EventHandler
	public void onPlayerCahat(AsyncPlayerChatEvent e) {
		Player sender = e.getPlayer();
		for (Player p : e.getRecipients()) {
			if (p.getName() != sender.getName()) {
				if (plugin.dc.getBoolean(p.getUniqueId() + ".enabled")) {
					if (!plugin.dc.getStringList(p.getUniqueId() + ".whitelist").isEmpty()) {
						List<String> playerWhitelist = plugin.dc.getStringList(p.getUniqueId() + ".whitelist");
						for (int i = 0; i < playerWhitelist.size(); i++) {
							if (!sender.hasPermission("group." + playerWhitelist.get(i)))
								e.getRecipients().remove(p);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
		String message = e.getMessage();
		String[] messageArgs = message.split(" ");
		if (messageArgs[0].equalsIgnoreCase("/tell") || messageArgs[0].equalsIgnoreCase("/msg")
				|| messageArgs[0].equalsIgnoreCase("/w")) {
			if (messageArgs.length > 1) {
				Player sender = e.getPlayer();
				if (Bukkit.getServer().getPlayer(messageArgs[1]) != null) {
					Player target = Bukkit.getServer().getPlayer(messageArgs[1]);
					if (sender.getName() != target.getName()) {
						if (plugin.dc.getBoolean(target.getUniqueId() + ".enabled")) {
							if (!plugin.dc.getStringList(target.getUniqueId() + ".whitelist").isEmpty()) {
								List<String> playerWhitelist = plugin.dc
										.getStringList(target.getUniqueId() + ".whitelist");
								for (int i = 0; i < playerWhitelist.size(); i++) {
									if (!sender.hasPermission("group." + playerWhitelist.get(i))) {
										e.setCancelled(true);
										sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
												"&cYou cannot message this player."));
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
