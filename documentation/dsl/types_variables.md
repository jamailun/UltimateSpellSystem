# Types and variables

## Types

The DSL provides those types :

|      Name       | Syntax                                         | Examples                        | Comment                                                                                                                                   |
|:---------------:|------------------------------------------------|---------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|
|   **String**    | `"<string>"`                                   | `"potato"`                      | Quotes are mandatory. Colors are taken into account (legacy and kyori formats)                                                            |
|   **Number**    | \[`-`] `<digits>` \[`.<digits>`]               | `43.2`, `-2`                    | The separator is a dot.                                                                                                                   |
|   **Boolean**   | { `true`, `false` }                            | `false`                         |                                                                                                                                           |
|    **Null**     | `null`                                         | `null`                          |                                                                                                                                           |
|  **Duration**   | `<number> <time_unit>`                         | `3s`, `13 hours`, `20ms`        | Cannot be negative. Time units are `ns`, `ms`, `s`, `m`, `h`, `d` (yes, days). The all-in-letter writing is allowed.                      |
| **EntityType**  | [`EntityType.`] `value`                        | `CHICKEN`, `EntityType.ZOMBIE`  | Projectiles are not allowed. See [javadoc](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html) for more examples. |
| **EffectType**  | [`EffectType.`] `value`                        | `SPEED`, `EffectType.BLIND`     | See [javadoc](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html) for full list.                            |
|  **Material**   | [`Material.`] `value`                          | `IRON_BARS`, `Material.APPLE`   | See [javadoc](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/material/package-summary.html) for full list.                           |
|  **Location**   | `@(<world>, <x>, <y>, <z> [, <yaw>, <pitch>])` | `@("world", 0, 100, 0)`         | Yaw and pitch are optionals.                                                                                                              |
|                 |                                                |                                 |                                                                                                                                           |
|   **Entity**    | *none*                                         | *none*                          | _No direct syntax._                                                                                                                       |
|                 |                                                |                                 |
|    **Array**    | `[[ <V1> ,..., <V2> ]]`                        | `[[ "a", "b"]]` `[[1,2,3]]`     | Types must be homogeneous !                                                                                                               |                      
| **PropertySet** | `{{ <K1>: <V1>, ..., <K2>: <V2> }}`            | `{{ health: 12, name: "foo" }}` | The keys are of the "custom" type : a string without spaces and quotes. The values are expression evaluated at runtime.                   | 

## Variables

All variables are **weakly typed**. Meaning you don't have to explicit the type in the declaration, and a variable
can be redefined with another type later in the execution.

A variable is always written with a `%` in the code.

All variables follow this regex: `%[a-zA-Z0-9_][a-zA-Z0-9_]*`.
- Valid variables examples : `%a`, `%I_Will_Shove_63_Potatoes`.
- Incorrect variables examples : `%42_var` `%why space?`

To define a variable, use the `define` statement. Check the [statements](statements.md) list for more details.
