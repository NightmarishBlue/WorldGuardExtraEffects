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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static blue.nightmarish.worldguardextraeffects.WorldGuardExtraEffects.LOGGER;

public class GiveEffectsOnEntryFlagHandler extends Handler {
    private Collection<Set<PotionEffect>> lastGivenEffects;

    GiveEffectsOnEntryFlagHandler(Session session) {
        super(session);
        this.lastGivenEffects = new ArrayList<>();
    }

//    @Override
//    public void initialize(LocalPlayer player, Location currentPos, ApplicableRegionSet regions) {
//        // when initialised, add every effect from the region's defined effects for this flag to this object's field
//        // using a set, possibly not worth it
//        for (Set<PotionEffect> effectSet : regions.queryAllValues(player, Flags.GIVE_EFFECTS_ON_ENTRY)) {
//            this.givenEffects.addAll(effectSet);
//        }
//    }

    @Override
    public boolean onCrossBoundary(LocalPlayer player, Location from, Location to, ApplicableRegionSet regions, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {

        Collection<Set<PotionEffect>> regionEffects = regions.queryAllValues(player, Flags.GIVE_EFFECTS_ON_ENTRY);

        if (!this.getSession().getManager().hasBypass(player, (World) to.getExtent())) {
            Player bukkitPlayer = ((BukkitPlayer) player).getPlayer();
            for (Set<PotionEffect> effects : regionEffects) {
                // shouldn't apply the effect if
                if (!this.lastGivenEffects.contains(effects)) {
                    for (PotionEffect effect : effects) {
                        if (effect != null) {
                            LOGGER.info(effect.getType().getName());
                            bukkitPlayer.addPotionEffect(effect);
                        }
                    }
                } else {
                    LOGGER.info("last given effects contains this effect or it is empty");
                }
            }
        }

        this.lastGivenEffects = new ArrayList<>(regionEffects);

        if (!this.lastGivenEffects.isEmpty()) {
            for (ProtectedRegion region : regions) {
                Set<PotionEffect> effects = region.getFlag(Flags.GIVE_EFFECTS_ON_ENTRY);
                if (effects != null) this.lastGivenEffects.add(effects);
            }
        }

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
