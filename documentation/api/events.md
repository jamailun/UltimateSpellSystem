# Events

To listen to an event, simply proceed with a class `Listener` & `@EventHandler`.

## Events list

Here's a list of events you can listen to :

- `BoundSpellCastEvent` : called when a `Spell` is cast from a bound `ItemStack`. **Maybe Cancellable.**
- `EntityCastEvent` : called when a `Spell` is cast by an `Entity`. **Cancellable.**
- `EntitySummonedEvent` : called when a `Spell` summons a `SpellEntity`.
- `ItemBoundEvent` : called when a `Spell` is bound to an `ItemStack`.
- `ItemBoundEvent` : called when a `Spell` is unbound from an `ItemStack`.
