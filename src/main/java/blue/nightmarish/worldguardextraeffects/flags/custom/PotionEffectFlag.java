package blue.nightmarish.worldguardextraeffects.flags.custom;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.FlagContext;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

// the potion effect flag has much the same format as the vanilla effect command
// the last args are optional though, and you can't put minecraft: before the name
// <effect name> <duration in secs> [amplifier] [show particles] [is ambient]
public class PotionEffectFlag extends Flag<PotionEffect> {
    public PotionEffectFlag(String name) {
        super(name);
    }

    // more of a validator than a parser, checks if the given strings are valid and then build the effect
    @Override
    public PotionEffect parseInput(FlagContext context) throws InvalidFlagFormat {
        String[] split = context.getUserInput().trim().split(" ");
        if (split.length < 2 || split.length > 5) {
            throw new InvalidFlagFormat("Please use the following format: <bukkit effect name> <duration in secs> [amplifier] [show particles] [is ambient]\nOptional values are assigned to 0 or false");
        }

        PotionEffectType potionEffect = PotionEffectType.getByName(split[0]);
        if (potionEffect == null) {
            throw new InvalidFlagFormat("Unable to find a potion effect type for '" + split[0] + "'! Please refer to https://hub.spigotmc.org/javadocs/spigot/org/bukkit/potion/PotionEffectType.html");
        }

        // check the duration's validity, and convert it from seconds to ticks
        try {
            split[1] = String.valueOf(Integer.parseInt(split[1]) * 20);
        } catch (NumberFormatException e) {
            throw new InvalidFlagFormat("The specified duration '" + split[1] + "' could not be coerced to an integer!");
        }

        return this.buildPotionEffect(split);
    }

    // functions that converts an effect to and from a string representation
    @Override
    public Object marshal(PotionEffect effect) {
        return effect.getType().getName() + " " + effect.getDuration() + " " + effect.getAmplifier() + " " + effect.hasParticles() + " " + effect.isAmbient();
    }

    @Override
    public PotionEffect unmarshal(Object object) {
        String[] split = object.toString().split(" ");
        return this.buildPotionEffect(split);
    }

    // parse a validated potion effect string; optional params have default values in case of error
    private PotionEffect buildPotionEffect(String[] inputs) {
        PotionEffectType effectType = PotionEffectType.getByName(inputs[0]);

        int duration = Integer.parseInt(inputs[1]);

        int amplifier = 0;
        if (inputs.length >= 3) {
            try {
                amplifier = Integer.parseInt(inputs[2]);
            } catch (Exception ignored) {}
        }

        boolean showParticles = false;
        if (inputs.length >= 4) {
            showParticles = Boolean.parseBoolean(inputs[3]);
        }

        boolean isAmbient = false;
        if (inputs.length >= 4) {
            isAmbient = Boolean.parseBoolean(inputs[3]);
        }

        return new PotionEffect(effectType, duration, amplifier, isAmbient, showParticles);
    }
}
