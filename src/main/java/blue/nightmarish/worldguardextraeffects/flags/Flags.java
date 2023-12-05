package blue.nightmarish.worldguardextraeffects.flags;

import blue.nightmarish.worldguardextraeffects.flags.custom.PotionEffectFlag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import org.bukkit.potion.PotionEffect;

public final class Flags {
    public static final SetFlag<PotionEffect> GIVE_EFFECTS_ON_ENTRY = new SetFlag<>("give-effect-on-entry", new PotionEffectFlag(null));
    public static final SetFlag<PotionEffect> GIVE_EFFECTS_ON_EXIT = new SetFlag<>("give-effect-on-exit", new PotionEffectFlag(null));
}
