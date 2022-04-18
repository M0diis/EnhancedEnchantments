package me.m0dii.enhancedenchant;

import me.m0dii.enhancedenchant.listeners.*;
import me.m0dii.enhancedenchant.listeners.custom.OnLavaWalk;
import me.m0dii.enhancedenchant.listeners.custom.OnTelepathy;
import me.m0dii.enhancedenchant.listeners.custom.OnTill;
import me.m0dii.enhancedenchant.utils.Data.ConfigManager;
import me.m0dii.enhancedenchant.commands.Enchant;
import me.m0dii.enhancedenchant.commands.Unenchant;
import me.m0dii.enhancedenchant.enchants.RegisterEnchants;
import me.m0dii.enhancedenchant.listeners.custom.CustomCombine;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EnhancedEnchant extends JavaPlugin
{
    public static EnhancedEnchant instance;
    
    private PluginManager pm;
    private ConfigManager configManager;
    
    public ConfigManager getConfigManager()
    {
        return this.configManager;
    }
    
    public FileConfiguration getCfg()
    {
        return this.configManager.getConfig();
    }
    
    public void onEnable()
    {
        instance = this;
        
        RegisterEnchants.register();
        
        this.configManager = new ConfigManager(this);
        
        this.pm = this.getServer().getPluginManager();
        
        this.getLogger().info("EnhancedEnchantments has been enabled.");
        
        registerEvents();
        registerCommands();
    }
    
    public void onDisable()
    {
        this.getLogger().info("EnhancedEnchantments has been disabled.");
    }
    
    private void registerEvents()
    {
        this.pm.registerEvents(new PlayerInteract(this), this);
        this.pm.registerEvents(new OnTelepathy(this), this);
        this.pm.registerEvents(new BlockBreak(this), this);
        this.pm.registerEvents(new BlockInterract(this), this);
        this.pm.registerEvents(new EnchantmentCombine(), this);
        this.pm.registerEvents(new CustomCombine(this), this);
        this.pm.registerEvents(new TillInteract(this), this);
        this.pm.registerEvents(new OnTill(), this);
        this.pm.registerEvents(new AnvilCombine(this), this);
        this.pm.registerEvents(new OnLavaWalk(this), this);
        this.pm.registerEvents(new ItemDrop(this), this);
    }
    
    private void registerCommands()
    {
        PluginCommand ee = getCommand("enhancedenchant");
        
        if(ee != null)
            ee.setExecutor(new Enchant(this));
    
        PluginCommand ue = getCommand("unenchant");
    
        if(ue != null)
            ue.setExecutor(new Unenchant(this));
    }
    
    public String format(String msg)
    {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}