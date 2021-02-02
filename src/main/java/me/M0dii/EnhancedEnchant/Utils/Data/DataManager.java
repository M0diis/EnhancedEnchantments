package me.M0dii.EnhancedEnchant.Utils.Data;

import me.M0dii.EnhancedEnchant.EnhancedEnchant;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class DataManager
{
    private static DataManager instance = null;
    
    public static DataManager getInstance()
    {
        if(instance == null)
            instance = new DataManager();
        
        return instance;
    }
    
    private final ConfigManager config = new ConfigManager(EnhancedEnchant.getPlugin(EnhancedEnchant.class));
    
    public FileConfiguration getConfig()
    {
        return this.config.getConfig();
    }
    
    public void saveConfig()
    {
        this.config.saveConfig();
    }
    
    public void reloadConfig()
    {
        this.config.reloadConfig();
    }
    
    public String format(String msg)
    {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}