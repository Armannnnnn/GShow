package me.arman.gshow;

import java.io.IOException;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	private Main plugin;

	public Commands(Main instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("gshow")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(
						ChatColor.translateAlternateColorCodes('&', "&cThis command can only be ran by players!"));
				return true;
			}
			Player p = (Player) sender;
			if (!p.hasPermission("gshow.use")) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&cYou do not have permission to run this command."));
				return true;
			}
			if (args.length == 0) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCommands:"));
				p.sendMessage(
						ChatColor.translateAlternateColorCodes('&', "&c/gshow enable: toggle message whitelisting"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/gshow list: show whitelisted groups"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&c/gshow add <group>: whitelist a particular group"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&c/gshow remove <group>: remove a particular group from your whitelist"));
				p.sendMessage(
						ChatColor.translateAlternateColorCodes('&', "&c/gshow reload: reload the configuration file"));
				return true;
			}
			String uuid = p.getUniqueId().toString();
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("enable")) {
					boolean enabled = plugin.dc.getBoolean(uuid + ".enabled");
					if (enabled)
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDisabled message whitelisting."));
					else
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aEnabled message whitelisting."));
					plugin.dc.set(uuid + ".enabled", !enabled);
					try {
						plugin.dc.save(plugin.d);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return true;
				} else if (args[0].equalsIgnoreCase("list")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCurrently whitelisted groups:"));
					if (plugin.dc.getStringList(uuid + ".whitelist").isEmpty()) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&aYou do not have any groups whitelisted."));
						return true;
					}
					List<String> whitelistedGroups = plugin.dc.getStringList(uuid + ".whitelist");
					for (int i = 0; i < whitelistedGroups.size(); i++) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + whitelistedGroups.get(i)));
					}
					return true;
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (!p.hasPermission("gshow.admin")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&cYou do not have permission to run this command."));
						return true;
					}
					try {
						plugin.getConfig().save(plugin.config);
					} catch (IOException e) {
						e.printStackTrace();
					}
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReloaded the configuration file."));
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCommands:"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&c/gshow enable: toggle message whitelisting"));
					p.sendMessage(
							ChatColor.translateAlternateColorCodes('&', "&c/gshow list: show whitelisted groups"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&c/gshow add <group>: whitelist a particular group"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&c/gshow remove <group>: remove a particular group from your whitelist"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&c/gshow reload: reload the configuration file"));
					return true;
				}
			} else {
				if (!args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("enable")
						&& !args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("remove")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCommands:"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&c/gshow enable: toggle message whitelisting"));
					p.sendMessage(
							ChatColor.translateAlternateColorCodes('&', "&c/gshow list: show whitelisted groups"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&c/gshow add <group>: whitelist a particular group"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&c/gshow remove <group>: remove a particular group from your whitelist"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&c/gshow reload: reload the configuration file"));
					return true;
				}
				List<String> possibleGroups = plugin.getConfig().getStringList("whitelistable");
				String group = args[1];
				if (args[0].equalsIgnoreCase("add")) {
					if (!possibleGroups.contains(group)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&cThe group " + group + " cannot be whitelisted. Groups you can whitelist are:"));
						for (int i = 0; i < possibleGroups.size(); i++) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + possibleGroups.get(i)));
						}
					} else {
						List<String> whitelistedGroups = plugin.dc.getStringList(uuid + ".whitelist");
						if (whitelistedGroups.contains(args[1])) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									"&cThe group " + args[1] + " is already in your whitelist."));
							return true;
						}
						whitelistedGroups.add(args[1]);
						plugin.dc.set(uuid + ".whitelist", whitelistedGroups);
						try {
							plugin.dc.save(plugin.d);
						} catch (IOException e) {
							e.printStackTrace();
						}
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&aAdded group " + args[1] + " to your whitelist."));
					}
				} else if (args[0].equalsIgnoreCase("remove")) {
					if (!possibleGroups.contains(group)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&cThe group " + group + " cannot be un-whitelisted. Groups you can remove are:"));
						for (int i = 0; i < possibleGroups.size(); i++) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + possibleGroups.get(i)));
						}
					} else {
						List<String> whitelistedGroups = plugin.dc.getStringList(uuid + ".whitelist");
						if (!whitelistedGroups.contains(args[1])) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&',
									"&cThe group " + args[1] + " is not in your whitelist."));
							return true;
						}
						whitelistedGroups.remove(args[1]);
						plugin.dc.set(uuid + ".whitelist", whitelistedGroups);
						try {
							plugin.dc.save(plugin.d);
						} catch (IOException e) {
							e.printStackTrace();
						}
						p.sendMessage(ChatColor.translateAlternateColorCodes('&',
								"&aRemoved group " + args[1] + " from your whitelist."));
					}
				}
			}

		}

		return false;
	}

}
