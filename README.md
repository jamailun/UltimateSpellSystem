# Ultimate Spells System

Spigot plugin, for dynamic spells, using a custom [DSL](https://en.wikipedia.org/wiki/Domain-specific_language).

## Author

Made by [jamailun](https://github.com/jamailun).

## Basics

All files in `/plugins/UltimateSpellSystem/spells/.` will be considered as spell.

A spell follow a very basic syntax. For example :

```bash
# I'm a comment
send to %caster message "You just casted a spell !";

define %enemies_around = sizeof(all monsters within 10 around %caster)
if(%enemies_around > 2) {
    summon IRON_GOLEM for 4 seconds with: {{
        name: "&eMichel",
        health: 25 + 5 * %enemies_around
    }} 
}
```

See the [full specifications](/documentation/README.md) for more details, or the [wiki](https://github.com/jamailun/UltimateSpellSystem/wiki/Spell-syntax).
I've also put a handful of [examples](/examples).

# API

You can code your own plugin to interact with USS.

You can provides custom attributes for summoned entities.
You can listen to specific events.

Check the [documentation](/documentation/registries).
