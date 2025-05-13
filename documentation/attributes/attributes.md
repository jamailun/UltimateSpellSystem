# Attributes data-types

Here's a list of all attributes for all default-implementation (and extension) elements.

## Entities

Entities expect some attributes to be defined.

### Shared-attributes

All summoned entities share some attributes.

|       Attribute        |     Type      |                                     Description                                      | Default |
|:----------------------:|:-------------:|:------------------------------------------------------------------------------------:|:-------:|
|         `name`         |   `String`    |                    A custom value for the nametag of the entity.                     | *None*  |
|     `name_visible`     |   `Boolean`   |                   If true, the custom-name will be always visible.                   | `false` |
|                        |               |                                                                                      |         |
|        `health`        |   `Number`    |              If set, will overwrite the `GENERIC_MAX_HEALTH` attribute.              | *None*  |
|    `attack_damage`     |   `Number`    |            If set, will overwrite the `GENERIC_ATTACK_DAMAGE` attribute.             | *None*  |
|  `projectile_damage`   |   `Number`    | Raw amount of damage to inflict on hit targets. Only works with `Pojectile` objects. | *None*  |
|        `armor`         |   `Number`    |                If set, will overwrite the `GENERIC_ARMOR` attribute.                 | *None*  |
|      `toughness`       |   `Number`    |           If set, will overwrite the `GENERIC_ARMOR_TOUGHNESS` attribute.            | *None*  |
|        `speed`         |   `Number`    |            If set, will overwrite the `GENERIC_MOVEMENT_SPEED` attribute.            | *None*  |
| `knockback_resistance` |   `Number`    |         If set, will overwrite the `GENERIC_KNOCKBACK_RESISTANCE` attribute.         | *None*  |
|      `knockback`       |   `Number`    |           If set, will overwrite the `GENERIC_ATTACK_KNOCKBACK` attribute.           | *None*  |
|                        |               |                                                                                      |         |
|         `baby`         |   `Boolean`   |   If set (and if the entity is Ageable) will set the type of mob to adult or baby.   | *None*  |
|                        |               |                                                                                      |         |
|         `head`         | [Item](#item) |                        Set the item in the `HEAD` armor slot.                        | *None*  |
|        `chest`         | [Item](#item) |                       Set the item in the `CHEST` armor slot.                        | *None*  |
|         `legs`         | [Item](#item) |                        Set the item in the `LEGS` armor slot.                        | *None*  |
|         `feet`         | [Item](#item) |                        Set the item in the `FEET` armor slot.                        | *None*  |
|         `hand`         | [Item](#item) |                        Set the item in the `HAND` armor slot.                        | *None*  |
|       `off_hand`       | [Item](#item) |                      Set the item in the `OFF_HAND` armor slot.                      | *None*  |

### Specific entities-attributes

#### The `Orb` type

The orb represents a virtual entity, applying effects in a certain radius. It can move in a linear path.

|      Attribute      |                          Type                           |                                                       Description                                                        |          Default          |
|:-------------------:|:-------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------:|:-------------------------:|
|      `radius`       |                        `Number`                         |                                           Spherical radius to apply effect to.                                           |           `0.4`           |
|    `apply_self`     |                        `Boolean`                        |                                      If true, the effects can trigger on the owner.                                      |          `false`          |
|       `sound`       |              [Sound-Holder](#sound-holder)              |                                                 Sound to make on spawn.                                                  |          *None**          |
|     `sound_hit`     |              [Sound-Holder](#sound-holder)              |                                                  Sound to make on hit.                                                   |          *None**          |
|     `particle`      |            [Particle-Holder](#sound-holder)             |                                        Particles to generate at each clock tick.                                         |          *None**          |
|     `particles`     |       `Array[`[Particle-Holder](#sound-holder)`]`       |                                    Multiple particles to generate at each clock tick.                                    |          *None**          |
|      `effect`       |      [PotionEffect-Holder](#potion-effect-holder)       |                                         Potion-Effects to apply on hit targets.                                          |          *None**          |
|      `effects`      | `Array[`[PotionEffect-Holder](#potion-effect-holder)`]` |                                     Array of Potion-Effects to apply on hit targets.                                     |          *None**          |
|       `fire`        |                        `Number`                         |                                     Amount of ticks to set the hit targets on fire.                                      |            `0`            |
|      `damages`      |                        `Number`                         |                                     Raw amount of damage to inflict on hit targets.                                      |            `0`            |
|  `max_collisions`   |                        `Number`                         |         Amount of entity-collision before self destroy. If zero or negative, will pierce any number of targets.          |            `0`            |
|  `max_blocks_hit`   |                        `Number`                         | Maximum amount of blocks to hit. Any collision greater than this will destroy the orb. Negative value disable hit count. |           `-1`            ||
|     `velocity`      |                        `Number`                         |                                      Linear velocity of the orb, in blocs/seconds.                                       |            `0`            |
|     `direction`     |                     `Vector` *(\*)*                     |                 Direction to move to. Will be normalized to be applied to the velocity each clock tick.                  | *Direction of the caster* |
|      `_clock`       |                        `Number`                         |         Frequency of the clock, in server-ticks (20/s). Beware, as changing this may impact server performances.         | *Check you configuration* |

**Note:** The direction may also be derived from a `Location` type (*ex*: `(position of %entity) - (position of %caster)`), or a list of Number (*ex*: `[[0.5, 0, 1]]`).

## "Holders"

Holder are often-met "types", represented as a map.

### Particle-holder

Defines a particle effect.
Expected attributes:

| Attribute | Type | Description | Default |
|:---------:|:----:|:-----------:|:-------:|
| `type`    | `String` as [Particle](https://jd.papermc.io/paper/1.21.1/org/bukkit/Particle.html) | The enum entry of particle to spawn. | *Mandatory* |
| `speed`   | `Number` | The speed of the particles. According to the particle type, can also change the lifetime. | `1` |
| `count`   | `Number` | The amount of particles to spawn. | `1` |
| `radius`  | `Number` | The cuboid-radius to randomly make particle appear. | `0.1` *(Can inherit from context)* |
| `shape`   | [ParticleShape](#particle-shape) | If defined, will override `radius` to set a specific configuration of particle shaper. | *None* |

**Note:** the `radius` propetty can inherit from its context. For instance, if used with a `Orb`, because this entity defines a radius, it will inherit the same by default.

### Sound-holder

Defines a sound effect.

| Attribute | Type | Description | Default |
|:---------:|:----:|:-----------:|:-------:|
| `type`    | `String` as [Sound](https://jd.papermc.io/paper/1.21.1/org/bukkit/Sound.html) | The enum entry of the sound. | *Mandatory* |
| `volume`  | `Number` | The volume of the sound. Apparently, mostly effective between `0.5` and `2`. | `1` |
| `pitch`   | `Number` | The pitch of the sound. Apparently, mostly effective between `0.5` and `2`. | `1` |

### Block-holder

Defines a block effect, i.e. a "fake" block spawning for a specific duration.

| Attribute | Type | Description | Default |
|:---------:|:----:|:-----------:|:-------:|
| `type`    | `String` as [Material](https://jd.papermc.io/paper/1.21.1/org/bukkit/Material.html) | The enum entry of the block to spawn. | *Mandatory* |
| `duration` | `Number` | The duration of the effect. | `1 second` |

### Potion-effect holder

Defines a potion effect.

| Attribute | Type | Description | Default |
|:---------:|:----:|:-----------:|:-------:|
| `type`    | `String` as [PotionEffect](/fr/jamailun/ultimatespellsystem/dsl/nodes/type/PotionEffect.java) | The enum entry or alias of the potion-effect to define. | *Mandatory* |
| `duration` | `Number` | The duration of the effect. | *Mandatory* |
| `power`   | `Number` | The power to give to the potion effect. | `1` |

## Particle Shape

A particle shape is a provided class (see [ParticleShaper](/fr/jamailun/ultimatespellsystem/api/utils/ParticleShaper.java)).

Here are the [default extension](/fr/jamailun/ultimatespellsystem/extension/providers/ParticleShapes.java) of the plugin. You can always create and register custom ones in your plugins.

### Circle

The `circle` shape creates a X-Z oriented circle.

| Attribute | Type | Description | Default |
|:---------:|:----:|:-----------:|:-------:|
| `radius`  | `Number` | Radius of the circle | `5` |
| `delta`   | `Number` | The distance, in radians, to spawn a particle along the circle. | `0.15` |

### Sphere and half-sphere

The `sphere` shape creates a hollow sphere.

THe `half-sphere` creates only the top-half of the sphere.

| Attribute | Type | Description | Default |
|:---------:|:----:|:-----------:|:-------:|
| `radius`  | `Number` | Radius of the circle | `5` |
| `delta`   | `Number` | The distance, in radians, to spawn a particle along the sphere, X-Z axis. | `0.5` |
| `phi`     | `Number` | The distance, in radians, to spawn a particle along the sphere, Y-axis. | `0.1` |

## Item

Defines an item.

|   Attribute   | Type |                             Description                             | Default |
|:-------------:|:----:|:-------------------------------------------------------------------:|:-------:|
|    `type`     | `String` as [Material](https://jd.papermc.io/paper/1.21.1/org/bukkit/Material.html) |                     The enum entry of the item.                     | *Mandatory* |
|   `amount`    | `Number` |               The amount of items in the item-stack.                | `1` |
|   `damage`    | `Number` |         For Damageable items, the damage to the durability.         | `0` |
|    `name`     | `String` |                  A custom-name for the item-stack.                  | *None* |
|    `lore`     | `Array[String]` |      A list of string for the lore. Will be grayed by default.      | *None* |
| `unbreakable` | `Boolean` |                If true, the item will be unbreable.                 | `false` |
|  `droppable`  | `Boolean` | If true, the item may be dropped. Mostly use with summon equipment. | `false` |
