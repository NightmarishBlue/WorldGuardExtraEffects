A Bukkit plugin add-on for [WorldGuard](https://enginehub.org/worldguard) that adds more region flags.

Currently adds 2 flags, `give-effect-on-entry` and `give-effect-on-exit`.
Both these flags take 1-5 parameters: 
`<bukkit effect name>` `<duration in secs>` `[amplifier]` `[show particles]` `[is ambient]`
The last 3 are optional, and initialised to 0/false by default. They do what they say on the tin - apply the configured potion effects to players who exit or enter a region's boundaries, once, at the exact moment they cross it.
