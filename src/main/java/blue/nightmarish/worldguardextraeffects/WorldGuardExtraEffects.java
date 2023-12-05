package blue.nightmarish.worldguardextraeffects;

import blue.nightmarish.worldguardextraeffects.flags.Flags;
import blue.nightmarish.worldguardextraeffects.handlers.GiveEffectsOnEntryFlagHandler;
import blue.nightmarish.worldguardextraeffects.handlers.GiveEffectsOnExitFlagHandler;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.session.SessionManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class WorldGuardExtraEffects extends JavaPlugin {
    @Getter private static WorldGuardExtraEffects plugin;
    @Getter public static Logger LOGGER;

    @Getter private WorldEditPlugin worldEditPlugin;

    @Getter private WorldGuardPlugin worldGuardPlugin;
    @Getter private WorldGuard worldGuard;

    @Getter private RegionContainer regionContainer;
    @Getter private SessionManager sessionManager;

    public WorldGuardExtraEffects() {
        WorldGuardExtraEffects.plugin = this;
    }

    @Override
    public void onLoad() {
        LOGGER = this.getLogger();

        // grab the worldedit and worldguard plugins and put them into the class's fields
        this.worldEditPlugin = (WorldEditPlugin) this.getServer().getPluginManager().getPlugin("WorldEdit");
        this.worldGuardPlugin = (WorldGuardPlugin) this.getServer().getPluginManager().getPlugin("WorldGuard");
        this.worldGuard = WorldGuard.getInstance();
        try {
            FlagRegistry flagRegistry = this.worldGuard.getFlagRegistry();
            flagRegistry.register(Flags.GIVE_EFFECTS_ON_ENTRY);
            flagRegistry.register(Flags.GIVE_EFFECTS_ON_EXIT);
        }
        catch (Exception e) {
            this.getServer().getPluginManager().disablePlugin(this);

            throw new RuntimeException(e instanceof IllegalStateException ?
                    "WorldGuard prevented flag registration. Did you reload the plugin? This is not supported!" :
                    "Flag registration failed!", e);
        }

        // they do some funky things with protocollib here
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        // they do shit with the session manager here
        this.regionContainer = this.worldGuard.getPlatform().getRegionContainer();
        this.sessionManager = this.worldGuard.getPlatform().getSessionManager();
        // register the event handlers
        this.sessionManager.registerHandler(GiveEffectsOnEntryFlagHandler.FACTORY(), null);
        this.sessionManager.registerHandler(GiveEffectsOnExitFlagHandler.FACTORY(), null);
//        this.getServer().getPluginManager().registerEvents(new PlayerListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // the other plugin doesn't use this
    }
}
