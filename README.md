# Ultimate Spells System

Spigot plugin, for dynamic spells, using a custom [DSL](https://en.wikipedia.org/wiki/Domain-specific_language). Check the syntax [here](DSL.md)

# Author

Made by jamailun.

# Basics

All files in `/plugins/UltimateSpellSystem/spells/.` will be considered as spell.

A spell follow a very basic syntax. For example :

```bash
# I'm a comment
send to %caster message "You just casted a spell !";

define %x = 12;
if(%x > 6) {
    summon IRON_GOLEM for 4 seconds with: {{
        name: "&eMichel",
        health: 4
    }} 
}
```

See the [full specifications]() for more details.

# API

//TODO

You can code your own plugin to interact with USS.

You can provides custom attributes for summoned entities.

You can listen to specific events.
