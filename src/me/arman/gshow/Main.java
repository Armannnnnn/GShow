package me.arman.gshow;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public File d;
	public File config;
	public FileConfiguration dc;

	@Override
	public void onEnable() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
		config = new File(this.getDataFolder(), "config.yml");
		this.createData();
		this.getCommand("gshow").setExecutor(new Commands(this));

	}

	@Override
	public void onDisable() {

	}

	public void createData() {
		d = new File(this.getDataFolder(), "data.yml");
		if (!d.exists()) {
			d.getParentFile().mkdirs();
			saveResource("data.yml", false);
		}
		dc = new YamlConfiguration();
		try {
			dc.load(d);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}
