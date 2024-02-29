# DSL syntax guide

The **Ultimate Spells System** (USS) DSL has been made to be quick to grasp and easy to use. It consists of a sequence of statements,
applying behaviours in the in-game-world.



# Types

The DSL provides those types :

|      Name       | Syntax                           | Examples                        | Comment                                                                                                                                   |
|:---------------:|----------------------------------|---------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
|   **String**    | `"<string>"`                     | `"potato"`                      | Quotes are mandatory. Colors are taken into account (legacy and kyori formats)                                                            |
|   **Number**    | \[`-`] `<digits>` \[`.<digits>`] | `43.2`, `-2`                    | The separator is a dot.                                                                                                                   |
|   **Boolean**   | { `true`, `false` }              | `false`                         |                                                                                                                                           |
|    **Null**     | `null`                           | `null`                          |                                                                                                                                           |
|  **Duration**   | `<number> <time_unit>`           | `3s`, `13 hours`, `20ms`        | Cannot be negative. Time units are `ns`, `ms`, `s`, `m`, `h`, `d` (yes, days). The all-in-letter writing is allowed.                      |
| **EntityType**  | [`EntityType.`] `value`          | `CHICKEN`, `EntityType.ZOMBIE`  | Projectiles are not allowed. See [javadoc](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html) for more examples. |
| **EffectType**  | [`EffectType.`] `value`          | `SPEED`, `EffectType.BLIND`     | See [javadoc](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html) for full list.                            |
|  **Material**   | [`Material.`] `value`            | `IRON_BARS`, `Material.APPLE`   | See [javadoc](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/material/package-summary.html) for full list.                           |
|                 |                                  |                                 |                                                                                                                                           |
|   **Entity**    | *none*                           | *none*                          | No direct syntax.                                                                                                                         |
|  **Location**   | *none*                           | *none*                          | No direct syntax.                                                                                                                         |
|                 |                                  |                                 |
| **PropertySet** | `{{ <K1>: <V1>, <K2>: <V2> }}`   | `{{ health: 12, name: "foo" }}` | The keys are of the "custom" type : a string without spaces and quotes. The values are expression evaluated at runtime.                   | 

# Variables

All variables follow this regex: `%[a-zA-Z0-9_][a-zA-Z0-9_]*`.
- Valid variables examples : `%a`, `%I_Will_Shove_63_Potatoes`.
- Incorrect variables examples : `%42_var` `%why space?`


# Statements

A statement is a non-typed expression that represents an **action**.

All statements must be followed by a `;`.

Exhaustive list will follow. Elements between square brackets `[ ]` are optionals. Arguments are enclosed between angle-brackets `< >`.

- `stop` : Stop the current spell. No other statement will be executed.

- `send [to] <TARGET> message <CONTENT>` : Send a message to a player.
    - `TARGET` is an **Entity**,
    - `CONTENT` is a **String**, content to be sent to the target.
  
- `send [to] <TARGET> effect <EFFECT> [<POWER>] for <DURATION>` : Send a potion effect to an entity.
    - `TARGET` is an **Entity**,
    - `EFFECT` is a **EffectType**,
    - `POWER` is a **Number**,
    - `DURATION` is a **Duration**.
  
- `define <VAR> = <VALUE>` : define a variable in the current scope.
    - `VAR` is the **Variable** to define.
    - `VALUE` is an **Expression** of any type.
  
- `if(<CONDITION>) <ST_TRUE> [else <ST_FALSE>]` : a classic if/else statement.
    - `CONDITION` an **Expression** of type **Boolean**.
    - `ST_TRUE` the statement to execute if the condition evaluates to true.
    - `ST_FALSE` the statement to execute otherwise.
  
- `run after <DELAY> <ST>` : run a statement after a delay.
    - `DELAY` is a **Duration**.
    - `ST` is a **Statement** to execute after the delay.
  
- `repeat [after <DELAY>] <COUNT> times every <PERIOD>: <ST>` : run a statement repeatedly after an optional delay.
    - `DELAY` is a **Duration**.
    - `COUNT` is a **Number**. Any non-integer value will be [rounded to one](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Double.html#intValue()).
    - `PERIOD` is a **Duration**.
    - `ST` is a **Statement** to execute <COUNT> times.
- `summon <TYPE> [as <VAR>] for <DURATION> [with: <ATTRIBUTES>]` : Summon an entity.
    - `TYPE` is an **EntityType** for the summoned entity.
    - `VAR` is the variable to store the newly created entity.
    - `DURATION` is the **duration** of the summon, after which it will be [removed](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/Entity.html#remove()).
    - `ATTRIBUTES` a **property set** defining all attributes for the summon.

Moreover, we can also consider the `BlockStatement`.
It begins by a `{` and closes with a `}`. All statements between those two will be children of his.

# Expressions

An expression is a **typed** value. It can also do actions.

//TODO

