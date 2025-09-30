# Ultimate Spells System

Paper plugin, for dynamic spells, using a custom [DSL](https://en.wikipedia.org/wiki/Domain-specific_language).

## Author

Made with love by [jamailun](https://github.com/jamailun).

## Basics

All files in `/plugins/UltimateSpellSystem/spells/.` will be considered as spell.

A spell follow a very basic syntax. For example :

```bash
# I'm a comment
send to %caster message "You just casted a spell !";

define %enemies_around = sizeof(all monsters within 10 around %caster)
if(%enemies_around > 2) {
    summon IRON_GOLEM at %caster as %my_golem for 40 seconds with: {{
        name: "&eMichel",
        health: 25 + 5 * %enemies_around
    }};

    # Callback when te golem dies
    callback %my_golem die: {
      send to %caster message "&cYour golem is dead :(";
      send to %caster effect resistance 2 for 12 secs;
    }

    # Triggers when it expires (after the duration)
    # This is an animation that make fake items appear for a duration.
    callback %my_golem expire: {
        play ANIMATION at %skeleton with: {{
            id: "explode.items",
            duration: 3.5s,
            count: 5,
            types: [[ "iron_ingot", "poppy" ]]
        }};
    }
}
```

See the [full specifications](/documentation/README.md) for more details, or the [wiki](https://github.com/jamailun/UltimateSpellSystem/wiki/Spell-syntax).
I've also put a handful of [examples](/examples).

# API

You can code your own plugin to interact with USS.
- Provide custom attributes for summoned entities.
- Listen to specific events.
- Cast spell on players or entities (or whatever you'd like really).

Check the [wiki](https://github.com/jamailun/UltimateSpellSystem/wiki) or the [documentation](/documentation/registries) _(roughly the same content)_.

# Questions ?

- Create an issue on Github.
- Ask your question on [Discord](https://discord.com/invite/MA5sxbKQuW).



