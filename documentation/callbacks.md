# Callbacks

Callbacks are registration that listen to events, about a summoned entity.

## Use it

The callback is made of two parts:
1. The type of the callback. I.e. the event that the callback will trigger to. Entity death, projectile landing, ...
2. The optional argument. Not all callbacks have an argument, and even then the argument is optional.
It will give the callback more information : where the projectile landed for example.

## Existing callbacks

Here are the existing callbacks. More will be added in the future.

| Callback type | Argument (keyword + type) | Details                                                                                                                 |
|:-------------:|:-------------------------:|-------------------------------------------------------------------------------------------------------------------------|
|   `LANDED`    |     `AT` (`Location`)     | Only trigger with projectiles. May trigger on a block and on an entity. Arguments contains the location of the landing. |
|     `HIT`     |      `TO` (`Entity`)      | Only trigger with projectiles. Trigger only on entity hit (argument will be the hit entity).                            |
|     `DIE`     |          _None_           | Triggered when a summoned entity is dead.                                                                               |
|   `EXPIRE`    |          _None_           | Trigger when the duration of a summoned entity is expired, and the entity will be removed.                              |

## Create a new callback

Check the [how to register](registries/how_to_register.md#register-a-callback-event) documentation.
