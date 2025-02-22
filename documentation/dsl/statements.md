# Statements

A **statement** is a non-typed serie of keywords that represents an **action**. It is evaluated at runtime.

All statements must be followed by a `;`.

Exhaustive list will follow. Elements between square brackets `[ ]` are optionals. Arguments (i.e. expressions) are enclosed between angle-brackets `< >`.

## The list

### Variable-related statements

- `define <VAR> = <VALUE>` : define a variable in the current scope.
  - `VAR` is the **Variable** to define.
  - `VALUE` is an **Expression** of any type.
  - Following this statement, `%<VAR>` will be a variable accessible to all other statements.
  > Example: `define %var = 12;`

- `increment <VAR>` and `decrement <VAR>` : increment or decrement a variable.
  - `VAR` the **Variable** to respectively increment or decrement by one. Must hold a **Number** !
  > Example: `increment %i;`

### Control-flow

- `stop [<RETURN_CODE>]` : Stop the current spell. No other statement will be executed. Must be the last statement of the current block.
  - `RETURN_CODE` : a **Number*, equal to `0` by default. If any other number, the spell will be considered as **failed**.
  > Examples `stop;` or `stop 1;`

- `{ <STATEMENTS...>; }` "block statement".
  - A simple serie of statements to execute.
  - The context inherit the parent but won't change it.
  - A `;` is **not** expected after the closing brace.
  > Example `{ define %a = 1; define %b = 1 + %a; }`

- `if(<CONDITION>) <ST_TRUE> [else <ST_FALSE>]` : a classic IF[/ELSE] statement.
  - `CONDITION` an **Expression** of type **Boolean**.
  - `ST_TRUE` the statement to execute if the condition evaluates to true.
  - `ST_FALSE` the statement to execute otherwise.
  > Example: `if(%size >= 12) {...} else {...}`

- `for(<ST_INIT>; <CONDITION>; <ST_ITERATION>) <ST>` : a classic FOR statement.
  - `INIT` a **Statement** to do at the beginning of the FOR.
  - `CONDITION` an **Expression** of type **Boolean**.
  - `ST_ITERATION` the statement to after all iteration.
  - `ST` the statement execute on each iteration.
  > Example: `for(define %i = 0; i < 3; increment %i) {...}`

- `while(<CONDITION>) <ST>` OR `do <ST> WHILE(<CONDITION>)` : a classic WHILE/DO statement.
  - `CONDITION` an **Expression** of type **Boolean**.
  - `ST` the statement execute on each iteration.
  > Example: `while(%var > 0) {...}` or `do {decrement %var;} while(%var > 0);`

- `run after <DELAY>: <ST>` : run a statement after a delay.
  - `DELAY` is a **Duration**.
  - `ST` is a **Statement** to execute after the delay.
  > Example: `run after 4 seconds: {...}`

- `repeat [after <DELAY>] <COUNT> times every <PERIOD>: <ST>` : run a statement repeatedly after an optional delay.
  - `DELAY` is a **Duration**.
  - `COUNT` is a **Number**. Any non-integer value will be [rounded to one](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Double.html#intValue()).
  - `PERIOD` is a **Duration**.
  - `ST` is a **Statement** to execute <COUNT> times.
  > Example: `repeat %count times every 0.5 minutes: {...}`

- `foreach(<VAR> : <ARRAY>) <ST>` : Iteration over a collection.
  - `VAR` a **Variable** to hold the content of the iteration.
  - `ARRAY` a **Collection** of elements.
  - `ST` statement to be executed for each value.
  > Example: `foreach(%monster : %monsters) {...}`

### Bukkit-related

- `send [to] <TARGET> message <CONTENT>` : Send a message to a player.
  - `TARGET` is an **Entity**. Can be a list.
  - `CONTENT` is a **String**, content to be sent to the target. Can be a list. Can also contain variables (but no other expressions).
  > Example: `send to %caster message "I have %count like apples."`

- `send [to] <TARGET> effect <EFFECT> [<POWER>] for <DURATION>` : Send a potion effect to an entity.
  - `TARGET` is an **Entity**. Can be a list.
  - `EFFECT` is a **EffectType**,
  - `POWER` is a **Number**,
  - `DURATION` is a **Duration**.
  > Example: `send to %caster effect SLOWNESS 2 for 5 secs;`

- `send [to] <TARGET> attribute <VALUE> <ATTRIBUTE_TYPE> [<MODE>] for <DURATION>` : Send a transient attribute to an entity, for a duration.
  - `TARGET` is an **Entity**. Can be a list.
  - `VALUE` is a **Number**.
  - `ATTRIBUTE_TYPE` is a **String**, but **must** reference an [Attribute](https://jd.papermc.io/paper/1.21.4/org/bukkit/attribute/Attribute.html). You can skip the prefixes (like `GENERIC_`).
  - `MODE` optional operation for the attribute modifier. It's a **String** but must reference an [AttributeModifier.Operation](https://jd.papermc.io/paper/1.21.4/org/bukkit/attribute/AttributeModifier.Operation.html). Will be `ADD_NUMBER` if not set.
  - `DURATION` is a **Duration**.
  > Example: `send to %caster attribute 3 "ARMOR"for 15000ms;` or `send%caster attribute 0.5 "SCALE" ADD_SCALAR for 10s;`

- `summon <TYPE> [at <POS>] [as <VAR>] for <DURATION> [with: <PROPERTIES>]` : Summon an entity.
  - `TYPE` is an **EntityType** for the summoned entity.
  - `POS` is the location for the summon to spawn. Can be an **Entity** or a **Location**.
    - _Note: If nothing is provided, will spawn on the caster._
  - `VAR` is the variable to store the newly created entity.
  - `DURATION` is the **duration** of the summon, after which it will be [removed](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/Entity.html#remove()).
  - `PROPERTIES` a **property set** defining all attributes for the summon. Check the [summon attributes documentation](/documentation/attributes/summon_attributes.md) for more information.
  > Example [here](/examples/summon_friendly_zombie.uss), as it's too long for this summary.

- `teleport <ENTITY> to <TARGET>` Teleport an entity to another or to a location.
  - `ENTITY` is an **Entity**. Can be a list.
  - `TARGET` is an **Entity** or a **Location** to teleport the entity to.
  > Example `teleport %caster to ((position of %caster) + [[0, 100, 0]]);`

- `play <ACTION> AT <POS> WITH: <ATTRIBUTES>` : Play an effect at a position, with some attributes.
  - `ACTION`: Can be either `particle`, `sound` or `block`.
  - `POS`: a **Location** (or **Entity**) to play the effect at. Can be a list.
  - `ATTRIBUTES` : A **property set** to specify more data. Check the [play documentation](/documentation/attributes/play_attributes.md) for more informations.
  > Example 1: `play particle at %caster with: {{ type: FLAME }};`
  > Example 2: `play sound at %caster with: {{ type: BLOCK_BEACON_ACTIVATE, volume: 0.6 }};`
  > Example 3: `play block at %caster with: {{ type: GRASS_BLOCK }};`

- give [[<AMOUNT>] <TYPE>] TO <TARGET> [WITH: <PROPERTIES>] : Give an item to some players.
  - `AMOUNT` : optional **Nuber** (if `<TYPE>` is specified). Default value is `1`.
  - `TYPE` : optional **String**, matching a [Material](https://jd.papermc.io/paper/1.21.4/org/bukkit/Material.html).
  - `TARGET` : of type **Entity**, recipient of the item. Can be a list.
  - `PROPERTIES` optional properties set. Check the `summon` statement for more links.