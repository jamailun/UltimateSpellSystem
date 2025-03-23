# What is a statement ?

A statement is an instruction. All statements are executed one after another. Statements must end with a `;`.

You can create "blocks" by placing multiple statements between curcly braces (`{}`).

# Statements list

Keywords in squared-brackets `[]` are optional. Expressions (i.e. arguments) are between _chevrons_ (`<>`).

## Variable-related statements

### ➡️ `define`

Define a new variable, or redefine an existing one.
Only applies to the current scope.

**Syntax :** `define <VAR> = <VALUE>`
- `VAR` is the **Variable** to define. Must be of the format `%<name>`.
- `VALUE` is an **Expression** of any type.
- Following this statement, `<VAR>` will be a variable accessible to all other statements, for the current scope.

**Example:**

```bash
define %var = 12;
```

### ➡️ `increment` and `decrement`

Increment or decrement a **numeric** variable.

**Syntax:** `increment <VAR>` or `decrement <VAR>`
- `VAR` the **Variable** to respectively increment or decrement by one.

**Examples:**

```bash
define %i = 12;

increment %i; # i = 13
decrement %i; # i = 12;
```

## Control-flow statements

### ➡️ `stop`

Stop the current execution. Must be the last statement of a scope.

**Syntax:** `stop [<RETURN_CODE>]` :
- `RETURN_CODE` : an optional **Number*, equal to `0` by default. If any other number, the spell will be considered as **failed**.

**Example:**

```bash
# Stop the execution in success
stop;
# Stop it with an error code
stop 42;
```

_Note:_ In reality, this example does not compile, as it is not possible to have a statement following a `stop`.

### ➡️ `if` / `else` branching

Classic conditional branching.

**Syntax:**  `if(<CONDITION>) <ST_TRUE> [else <ST_FALSE>]`
- `CONDITION` an **Expression** of type **Boolean**.
- `ST_TRUE` the statement to execute if the condition evaluates to true.
- `ST_FALSE` the statement to execute otherwise.

_Note:_ Obvisouly, you can put another `if` in a `else` statement, thus obtaining `else if` statements.

**Example:**
```bash
if(%var <= 12 || %foo != 0) {
  ...
} else if(%bar == %toto) {
  ...
} else {
  ...
}
```

### ➡️ `for` loop

Classic ranged-iterative statement.

**Syntax:** `for(<ST_INIT>; <CONDITION>; <ST_ITERATION>) <ST>`
- `INIT` a **Statement** to do at the beginning of the FOR.
- `CONDITION` an **Expression** of type **Boolean**.
- `ST_ITERATION` the statement to after all iteration.
- `ST` the statement execute on each iteration.

**Example:**
```bash
for(define %i = 0; i < 3; increment %i) {
  ...
}
```

### ➡️ `while` loop

Classic conditional loop statement.

**Syntax:** `while(<CONDITION>) <ST>` or `do <ST> WHILE(<CONDITION>)`
- `CONDITION` an **Expression** of type **Boolean**.
- `ST` the statement execute on each iteration.

**Example:**
```bash
define %var = 2;

# While (do)
while(%var > 0) {
  decrement %var;
}

# Do, while
do {
  increment %var;
} while(%var < 2);
```

### ➡️ `foreach` loop

Iterative loop over a collection.

**Syntax:** `foreach(<VAR_ELEM> : <COLLECTION>) <ST>`
- `VAR_ELEM` a **Variable** to hold the content of the current iteration.
- `COLLECTION` a **Collection** of elements.
- `ST` statement to be executed for each value.

**Example:**
```bash
foreach(%monster : %monsters) { # You'll see later how to create / obtain collections
  ...
}
```

## "Tasks" statements

Sometimes, you want to either do something after a while, or repeat it for a duration.

### ➡️ `run after`

A way executing statements after a delay.

**Syntax:** `run after <DELAY>: <ST>`
- `DELAY` is a **Duration**.
- `ST` is a **Statement** to execute after the delay.

**Example:**
```bash
run after 4 seconds: {...}
run after 42s: {...}
```

### ➡️ `repeat N times`

Run an action periodically for a duration, with an optional initial delay.

**Syntax:**  `repeat [after <DELAY>] <COUNT> times every <PERIOD>: <ST>`.
- `DELAY` is a **Duration**.
- `COUNT` is a **Number**. Any non-integer value will be [rounded to one](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Double.html#intValue()).
- `PERIOD` is a **Duration**.
- `ST` is a **Statement** to execute <COUNT> times.

**Example:**
```bash
repeat %count times every 0.5 minutes: {...} # run every 30 seconds
repeat after 3s 12 times every 1m: {...} # run 12 times every minute, after an initial delay of 3s.
```

### ➡️ `callback` on a summon

Run an action when a summoned entity his part of a Bukkit event.
An argument specific to the event can be used.

**Syntax:**  `callback <ENTITY> <TYPE> [<KEYWORD> <ARGUMENT_VAR>]: <ST>`
- `ENTITY` is a **Summoned entity**.
- `TYPE` is a custom callback type. Check the [corresponding documentation](../callbacks.md) for the complete list.
- `KEYWORD` is a **String** or custom, matching the `type`. Check the doc.
- `ARGUMENT_VAR` is a **Variable** to store the argument value to.
- `ST` is a **Statement** to execute when the event triggers.

**Example:**
```bash
# First, summon a projectile with the SUMMON statement. Saved in the %projectile var.

# Teleport the caster to the landing location
callback %projectile LANDED at %pos: {
  teleport %caster to %projectile;
}

# Or send a message when the projectile expired instead of landing. 
callback %projectile expire: {
  send %caster message "Projectile expired :(";
}
```


### Minecraft statements

Now that we can control variables and control the flow of the spell exection, let's act on the world !

Note that, in general, one of the philosphy here is to **not** impact the world durably. All modifications are either transient, or limited to a duration.

### ➡️ `send` a message ✉️

Send a text-message to a player.

**Syntax:** `send [to] <TARGET> message <CONTENT>;`
- `TARGET` is an **Entity**. Can be a list.
- `CONTENT` is a **String**, content to be sent to the target. Can be a list.

_Note_: The content in the string will evaluate the variables expressions (without having to concatenate the string).

**Examples:**
```bash
send to %caster message "I &d&lLOVE&f apples."; # Colors are classic, with a '&' character.
send to %caster message "You are %caster"; # Will print the caster's name.
```

### ➡️ `send` a potion effect 🧴

Send an potion effect to an entity.

**Syntax:** `send [to] <TARGET> effect <EFFECT> [<POWER>] for <DURATION>;`
- `TARGET` is an **Entity**. Can be a list.
- `EFFECT` is a **EffectType**
  > Will change to accept any string.
- `POWER` is a **Number**,
- `DURATION` is a **Duration**.

**Example:**
```bash
send to %caster effect STRENGTH 3 for 12 secs;
send to %caster effect glowing for 1m;
```

### ➡️ `send` an attribute 📈

Send a transient attribute to an entity, for a duration.

**Syntax:** `send [to] <TARGET> attribute <VALUE> <ATTRIBUTE_TYPE> [<MODE>] for <DURATION>;`
- `TARGET` is an **Entity**. Can be a list.
- `VALUE` is a **Number** : the amount of attribute to add/remove.
- `ATTRIBUTE_TYPE` is a **String**, but **must** reference an [Attribute](https://jd.papermc.io/paper/1.21.4/org/bukkit/attribute/Attribute.html). You can skip the prefixes (like `GENERIC_`).
- `MODE` optional operation for the attribute modifier. It's a **String** but must reference an [AttributeModifier.Operation](https://jd.papermc.io/paper/1.21.4/org/bukkit/attribute/AttributeModifier.Operation.html). Will be `ADD_NUMBER` if not set.
- `DURATION` is a **Duration**.

**Example:**
```bash
# +3 armor for 15 secs
send to %caster attribute 3 ARMOR for 15000ms;
# - 50% model scale for 10 secs
send %caster attribute -0.5 "SCALE" "MULTIPLY_SCALAR_1" for 10s;
```

### ➡️ `summon` an entity 🥚

Summon an entity.

**Syntax:** `summon <TYPE> [at <POS>] [as <VAR>] for <DURATION> [with: <PROPERTIES>]`
- `TYPE` is an **EntityType** for the summoned entity.
- `POS` is the location for the summon to spawn. Can be an **Entity** or a **Location**.
  - _Note: If nothing is provided, will spawn on the caster._
- `VAR` is the optional **Variable** to store the newly created entity.
- `DURATION` is the **duration** of the summon, after which it will be [removed](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/entity/Entity.html#remove()).
- `PROPERTIES` a **property set** defining all attributes for the summon. Check the [summon attributes documentation](/documentation/attributes/summon_attributes.md) for more information.

**Example:**
Check the documentation or [this example](/examples/summon_friendly_zombie.uss).

### ➡️ `teleport`

Teleport an Entity to a position.

**Syntax:** `teleport <ENTITY> to <TARGET>`
- `ENTITY` is an **Entity**. Can be a list.
- `TARGET` is an **Entity** or a **Location** to teleport the entity to.

**Example:**
```bash
# Teleport the caster 100 blocks up.
teleport %caster to ((position of %caster) + [[0, 100, 0]]);
```

### ➡️ `play` particle, sound, block

Play an effect to a position.

: Play an effect at a position, with some attributes.
> Example 1: ``


**Syntax:** `play <ACTION> AT <POS> WITH: <ATTRIBUTES>`
- `ACTION`: Can be either `particle`, `sound` or `block`.
- `POS`: a **Location** (or **Entity**) to play the effect at. Can be a list.
- `ATTRIBUTES` : A **property set** to specify more data. Check the [play documentation](/documentation/attributes/play_attributes.md) for more informations.

**Example:**
```bash
play particle at %caster with: {{ type: FLAME, count: 12, speed: 0.2 }};
play sound at %caster with: {{ type: BLOCK_BEACON_ACTIVATE, volume: 0.6, pitch: 1.1 }};
play block at %caster with: {{ type: GRASS_BLOCK, duration: 0.6 minutes }};
```

### ➡️ `give` an item

Give an item to a player.

**Syntax:** `give [[<AMOUNT>] <TYPE>] TO <TARGET> [WITH: <PROPERTIES>]`
- `AMOUNT` : optional **Nuber** (if `<TYPE>` is specified). Default value is `1`.
- `TYPE` : optional **String**, matching a [Material](https://jd.papermc.io/paper/1.21.4/org/bukkit/Material.html).
- `TARGET` : of type **Entity**, recipient of the item. Can be a list.
- `PROPERTIES` optional properties set. Check the `summon` statement for more links. May override the `AMOUNT` and `TYPE` arguments.

**Example:**
```bash
give 3 APPLE to %caster;
give TO %caster with: {{type: APPLE, name: "&dSuper apple"}}
```
