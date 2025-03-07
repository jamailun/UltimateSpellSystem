# Entity scopes

A scope is a predicate to select entities.

It is used by the `all <SCOPE> around <ENTITY>` expression, and to define what entities a summon can aggro.

## Use a scope

When a scope is expected, you can either :
- Use an entity type (`IRON_GOLEM`, `COW`, `PLAYER`, ...). The case does not really matter, but by convention let's use upper-casing.
- Use a "category", i.e. a "real" scope. For instance: `living`, `monster`, `animal`, ... We use lower-casing by convention.

This way, we can summon a zombie that will only target animals using the `aggro_scope`: attribute
```
summon ZOMBIE at %caster for 25 seconds with: {{
  name: "&cFred",
  aggro_scope: animals
}};
```

## Scopes list

Current default scopes. You can still create custom ones (see next part).

Every entry below is lso valid with an ending "s" letter (i.e. `player` also works for `players`).

| Scope names                             | Description                                                             |
|-----------------------------------------|-------------------------------------------------------------------------|
| `living`, `living_entity`, `any`, `all` | Any entity implementing the `LivingEntity` interface.                   |
| `mob`                                   | Any entity implementing the `Mob` interface.                            |
| `monster`                               | Any entity implementing the `Monster` interface.                        |
| `animal`                                | Any entity implementing the `Animal` interface.                         |
| `player`, `human`                       | Any entity implementing the `Player` interface.                         |
| `item`                                  | Any entity implementing the `Item` interface.                           |
|                                         |                                                                         |
| `summon`, `summoned`                    | Any spell-summoned entity.                                              |
| `undead`                                | Any "undead" entity. _Also includes th phantom._                        |
| `spider`, `arthropod`                   | Any "arthropod" entity.                                                 |
| `end`, `end_monster`                    | Any "ender"-related entity. Also considers ender pearls or end-crystal. |
| `pillager`, `illager`                   | Any "pillager" entity. _Includes witch and vex_.                        |

## Register a custom scope

Please, follow the corresponding tutorial [here](/documentation/registries/how_to_register.md#register-a-custom-scope).
