# Ultimate Spells System

Bukkit API, for dynamic spells, using a custom [DSL](https://en.wikipedia.org/wiki/Domain-specific_language).

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
        name: "Michel",
        health: 4
    }} 
}
```

See the [full specifications]() for more details.

# API

You can code your own plugin to interact with USS.
