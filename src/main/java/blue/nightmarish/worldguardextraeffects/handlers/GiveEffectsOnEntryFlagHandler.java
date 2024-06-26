package blue.nightmarish.worldguardextraeffects.handlers;

import blue.nightmarish.worldguardextraeffects.flags.Flags;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.BukkitPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GiveEffectsOnEntryFlagHandler extends Handler {
    private Collection<Set<PotionEffect>> lastGivenEffects;

    GiveEffectsOnEntryFlagHandler(Session session) {
        super(session);
        this.lastGivenEffects = new HashSet<>();
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet regions, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        // incoming effects are checked against previous effects and then stored there for next time
        Collection<Set<PotionEffect>> incomingEffects = regions.queryAllValues(player, Flags.GIVE_EFFECTS_ON_ENTRY);

        if (!this.getSession().getManager().hasBypass(player, (World) to.getExtent())) {
            Player bukkitPlayer = ((BukkitPlayer) player).getPlayer();
            // loop over and apply incoming effects
            for (Set<PotionEffect> effects : incomingEffects) {
                // shouldn't apply the effects if they've already been given to you
                if (!this.lastGivenEffects.contains(effects)) bukkitPlayer.addPotionEffects(effects);
            }
        }

        this.lastGivenEffects = incomingEffects;
        return true;
    }


    // factory boilerplate
    public static class Factory extends Handler.Factory<GiveEffectsOnEntryFlagHandler> {
        @Override
        public GiveEffectsOnEntryFlagHandler create(Session session) {
            return new GiveEffectsOnEntryFlagHandler(session);
        }
    }

    public static final Factory FACTORY() {
        return new Factory();
    }
}
