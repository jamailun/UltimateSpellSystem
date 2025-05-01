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

## Test a spell

Simply do `/uss cast <spell_id>` to cast it on yourself.

Moreover, you can see the code of a spell with the `/uss debug <spell_id>` command.

