# What is an expression ?

An **expression** is a **typed** operation that computes arguments and returns something.

As with [types](https://github.com/jamailun/UltimateSpellSystem/wiki/DSL-:-statements), the content is evaluated at runtime.

# Expressions list

There are a few statements, but they are powerful enough.

## Pseudo-types

Check the [types](https://github.com/jamailun/UltimateSpellSystem/wiki/DSL-:-types) list to include a typed expression.

### Examples

- Array : `[[<a>, <b>, ...]]` : a homogenous collection of elements.
- Properties : `{{ <key_1>: <value_1>, <key_2>: <value_2>, ... }}` : a map of elements.
- Location : `@(<world_name>, <x>, <y>, <z> [, <yaw>, <pitch>])` : a location in a world.

## Functions

You can call a function using the syntax: `<function_name>([args...])`. If way return something.

You'll learn more about function in its dedicated page.

## Operational expressions

### ➡️ `all` entities around

Get a list of entities of a specific scope around a Location.

**Syntax:** `all <SCOPE> within <DISTANCE> around <SOURCE> [including]`
- `SCOPE` a [scope](/documentation/scopes.md) to filter entities with. Can be an **EntityType** or a **Custom** identifier.
  If it's the latter, see the [ScopeProvider](/src/main/java/fr/jamailun/ultimatespellsystem/bukkit/providers/ScopeProvider.java).
- `DISTANCE` is a **Number**.
- `SOURCE` is either an **Entity** or a **Location**.
- If the keyword `including` at the end, the source will also be included in the list (**if** the source is an Entity).

**Returns:** a **Collection** of **Entities**.

**Example:**
```bash
define %monsters = all monsters within %distance around %caster;
define %everyone = all entities within 30 around %caster including
```

### `position of` an entity

Get the Location of an Entity.

**Syntax:** `position of <SOURCE>`
- `SOURCE` the **Entity** to get the **Location** of. Accepts collections.

**Return** a **Location**. If the `SOURCE` is a collection, returns a **collection** of Locations.

**Example:**
```bash
teleport %caster to (position of %some_entity);
```

### ➡️ `sizeof` a collection

Get the size if a collection.

- `sizeof <COLLECTION>`
- `COLLECTION` : a **Collection**. If a single element is provided, will return 1.

**Return** a Number, with the size of the collection.

**Example:**
```bash
# With a 'define' equals to all monsters around
define %count = sizeof(%monsters);
send to %caster message "There are&c %count&f monsters around !";
```
