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

public class GiveEffectsOnExitFlagHandler extends Handler {
    private Collection<Set<PotionEffect>> lastGivenEffects;

    GiveEffectsOnExitFlagHandler(Session session) {
        super(session);
        this.lastGivenEffects = new HashSet<>();
    }

    @Override
    public void initialize(LocalPlayer player, Location currentPos, ApplicableRegionSet regions) {
        // when initialised, add every effect from the region's defined effects for this flag to this object's field
        // using a set, possibly not worth it
        this.lastGivenEffects = regions.queryAllValues(player, Flags.GIVE_EFFECTS_ON_EXIT);
    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet regions, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        // last effects are applied to the player and then set to incoming
        Collection<Set<PotionEffect>> incomingEffects = regions.queryAllValues(player, Flags.GIVE_EFFECTS_ON_EXIT);

        if (!this.getSession().getManager().hasBypass(player, (World) to.getExtent())) {
            Player bukkitPlayer = ((BukkitPlayer) player).getPlayer();
            // loop over and apply previous last effects
            for (Set<PotionEffect> effects : this.lastGivenEffects) {
                // shouldn't apply the effects if they're about to be given to you
                if (!incomingEffects.contains(effects)) bukkitPlayer.addPotionEffects(effects);
            }
        }

        this.lastGivenEffects = incomingEffects;
        return true;
    }


    // factory boilerplate
    public static class Factory extends Handler.Factory<GiveEffectsOnExitFlagHandler> {
        @Override
        public GiveEffectsOnExitFlagHandler create(Session session) {
            return new GiveEffectsOnExitFlagHandler(session);
        }
    }

    public static final Factory FACTORY() {
        return new Factory();
    }
}
