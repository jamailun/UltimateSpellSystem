# Statements

A **statement** is a non-typed serie of keywords that represents an **action**. It is evaluated at runtime.

All statements must be followed by a `;`.

Exhaustive list will follow. Elements between square brackets `[ ]` are optionals. Arguments are enclosed between angle-brackets `< >`.

## Variable-related

- `define <VAR> = <VALUE>` : define a variable in the current scope.
    - `VAR` is the **Variable** to define.
    - `VALUE` is an **Expression** of any type.
    - Following this statement, `%<VAR>` will be a variable accessible to all other statements.


- `increment <VAR>` and `decrement <VAR>` : increment or decrement a variable.
    - `VAR` the **Variable** to respectively increment or decrement by one. Must hold a **Number** !

## Control-flow

- `stop` : Stop the current spell. No other statement will be executed.


- `{ <STATEMENTS...>; }` "block statement".
  - A simple serie of statements to execute.
  - The context inherit the parent but won't change it.
  - A `;` is **not** expected after the closing brace.


- `if(<CONDITION>) <ST_TRUE> [else <ST_FALSE>]` : a classic IF/ELSE statement.
  - `CONDITION` an **Expression** of type **Boolean**.
  - `ST_TRUE` the statement to execute if the condition evaluates to true.
  - `ST_FALSE` the statement to execute otherwise.


- `for(<ST_INIT>; <CONDITION>; <ST_ITERATION>) <ST>` : a classic FOR statement.
    - `INIT` a **Statement** to do at the beginning of the FOR.
    - `CONDITION` an **Expression** of type **Boolean**.
    - `ST_ITERATION` the statement to after all iteration.
    - `ST` the statement execute on each iteration.


- `while(<CONDITION>) <ST>` OR `do <ST> WHILE(<CONDITION>)` : a classic WHILE/DO statement.
    - `CONDITION` an **Expression** of type **Boolean**.
    - `ST` the statement execute on each iteration.


- `run after <DELAY>: <ST>` : run a statement after a delay.
    - `DELAY` is a **Duration**.
    - `ST` is a **Statement** to execute after the delay.


- `repeat [after <DELAY>] <COUNT> times every <PERIOD>: <ST>` : run a statement repeatedly after an optional delay.
    - `DELAY` is a **Duration**.
    - `COUNT` is a **Number**. Any non-integer value will be [rounded to one](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Double.html#intValue()).
    - `PERIOD` is a **Duration**.
    - `ST` is a **Statement** to execute <COUNT> times.


- `foreach(<VAR> : <ARRAY>) <ST>` : Iteration over a collection.
    - `VAR` a **Variable** to hold the content of the iteration.
    - `ARRAY` a **Collection** of elements.
    - `ST` statement to be executed for each value.

- Basic expression. You can just write an expression. It may do nothing, but you can still do it.

### Bukkit-related

- `send [to] <TARGET> message <CONTENT>` : Send a message to a player.
    - `TARGET` is an **Entity**. Can be a list.
    - `CONTENT` is a **String**, content to be sent to the target. Can be a list.


- `send [to] <TARGET> effect <EFFECT> [<POWER>] for <DURATION>` : Send a potion effect to an entity.
    - `TARGET` is an **Entity**. Can be a list.
    - `EFFECT` is a **EffectType**,
    - `POWER` is a **Number**,
    - `DURATION` is a **Duration**.


- `summon <TYPE> [at <POS>] [as <VAR>] for <DURATION> [with: <ATTRIBUTES>]` : Summon an entity.
    - `TYPE` is an **EntityType** for the summoned entity.
    - `POS` is the location for the summon to spawn. Can be an **Entity** or a **Location**.
        - _Note: If nothing is provided, will spawn on the caster._
    - `VAR` is the variable to store the newly created entity.
    - `DURATION` is the **duration** of the summon, after which it will be [removed](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/Entity.html#remove()).
    - `ATTRIBUTES` a **property set** defining all attributes for the summon. Check the [summon attributes documentation]() for more information.


- `teleport <ENTITY> to <TARGET>` Teleport an entity to another or to a location.
    - `ENTITY` is an **Entity**. Can be a list.
    - `TARGET` is an **Entity** or a **Location** to teleport the entity to.


- `play <ACTION> AT <POS> WITH: <ATTRIBUTES>` : Play an effect at a position, with some attributes.
  - `ACTION`: Can be either `particle`, `sound` or `block`.
  - `POS`: a **Location** (or **Entity**) to play the effect at. Can be a list.
  - `ATTRIBUTES` : A **property set** to specify more data. Check the [play documentation]() for more informations.


