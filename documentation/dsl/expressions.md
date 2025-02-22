# Expressions

An **expression** is a **typed** operation that computes arguments and returns something.

As with [statements](statements.md), the content is evaluated at runtime.

## Pseudo-Expression

- Array : `[[<a>, <b>, ...]]` : a homogenous collection of elements.
- Properties : `{{ <key_1>: <value_1>, <key_2>: <value_2>, ... }}` : a map of elements.
- Location : `@(<world_name>, <x>, <y>, <z> [, <yaw>, <pitch>])` : a location in a world.

## Expressions

- `all <SCOPE> within <DISTANCE> around <SOURCE> [including]` : Get all entities (of a specific scope) around something.
    - `SCOPE` a [scope](/documentation/scopes.md) to filter entities with. Can be an **EntityType** or a **Custom** identifier.
      If it's the latter, see the [ScopeProvider](/src/main/java/fr/jamailun/ultimatespellsystem/bukkit/providers/ScopeProvider.java).
    - `DISTANCE` is a **Number**.
    - `SOURCE` is either an **Entity** or a **Location**.
    - If the keyword `including` at the end, the source will also be included in the list (**if** the source is an Entity).
    - RETURNS a **Collection** of **Entity**.
    > Example: `define %monsters = all monsters within %distance around %caster;`
    > Example: `define %everyone = all entities within 30 around %caster including;`

- `position of <SOURCE>` : return the location of an entity.
    - `SOURCE` the **Entity** to get the **Location** of. Accepts collections.
    - RETURNS a **Location**. If the SOURCE is a collection, returns a collection of Locations.


- `sizeof <COLLECTION>` : return the size of a collection.
  - `COLLECTION` : a **Collection**. If a single element is provided, will return 1.
  - RETURNS the size of the collection.
