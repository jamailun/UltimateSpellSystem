# General usage

The main usage of this plugin, is the following :
1. Create 'bound' items with admin commands.
2. Distribute those items in your playerbase / economy. A lot of other plugins will exist to do so.

As such, this documentation will only focus on the first point.

Here's a guide for the commands.

## Create a bound item

Use the `/uss bind` command, holding an item in your hand. You need to be a player to do this command.
This will bind a spell to the current item. You can bind multiple spells on one item.

You can specify 3 things (in the following order) :
1. The spell ID. _Required._
2. The cost. Can have a default value (set in the configuration).
   - A custom cost can be registered following [this documentation](registries/how_to_register.md#register-a-spell-cost).
   - A majority of costs have arguments. They _must_ follow the ID of the cost.
3. The trigger sequence. Can have a default value (idem).
   - A sequence is made of steps, each one can be a right-click, a 'sneak', an attack... The autocompletion will help you.

As such, the 'signature' of the `bind` command is :
```
/uss bind <spell_id> [<cost_type> [cost args...]  [trigger_step...] ]
```

## Check the bound spells of an item

Simply do the `/uss bind-check` command, holding the item.

## Unbind

To cleanse bindings from an item, do `/uss unbind`, holding the item.

## Bind command

As mentionned before, the bind command takes the following arguments :
- The spell ID (mandatory),
- An optional condition,
- An optional trigger sequence.

If the last two elements are not specified, the values will be the one from the configuration.

### Bind condition

A condition will define the condition for a spell to be cast by a player. The condition can be on the player himself, or the held item, or more.

Here's the list of included conditions. Don't forget that your plugin can [register](.) custom one.

| Condition name | Arguments                    | Description                                                                                                                |
|:--------------:|------------------------------|----------------------------------------------------------------------------------------------------------------------------|
|     `none`     | _None_                       | No condition, the bound spell can always be cast.                                                                          |
|  `durability`  | `amount (int)`               | Remove durability on the item. May destroy the item if the it does not have enough durability.                             |
|  `experience`  | `amount (int), level (bool)` | The player must have a certain level or raw experience points. The second argument is `true` if the unit is "level".       |
|  `food-level`  | `amount (int)`               | Amount of food-level to remove. If the player doesn't have enough, spell will not cast.                                    |
|    `health`    | `amount (double)`            | Amount of health to remove. If the player doesn't have enough, spell will not cast.                                        |
| `item-amount`  | `amount (int)`               | Amount to remove on the item. Casting will fail is the held amount is not enough.                                          |
|  `permission`  | `value (string)`             | A permission the player is required to have to be able to cast the spell. This can be a safety with administration spells. |

When typing the command, the expected argument will automatically help you type.

### Bind trigger

After the condition, you also have to set the sequence for a player to trigger the spell. You may have multiple of them, don't forget to use `/uss bind-check` to see them.

A trigger is a sequence of trigger-step. each step will be one of the following :
- `attack` : a left-click attack on a living entity.
- `left_click` : all left-clicks except for the attack.
   - `left_click_block` : only left click on blocks.
   - `left_click_ait` : only left click on air.
- `right_click` : all right-clicks.
   - `right_click_block` : only right click on blocks.
   - `right_click_ait` : 
- `sneak` the sneak action (from standing to sneaking).

There is no limit of the amount of steps required.

### Example

> On a "magic fire wand", you want "RRR" for a fireball, and "RRL" for a fire-tornado.
>
> Each need a different durability cost.
> 
> `/uss bind fireball durability 5 right_click right_click right_click`
>
> `/uss bind fireball durability 12 right_click right_click left_click`

## Test a spell

Simply do `/uss cast <spell_id>` to cast it on yourself.

Moreover, you can see the code of a spell with the `/uss debug <spell_id>` command.

