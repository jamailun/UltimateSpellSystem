# `PLAY` attributes

The `play` statement can do many things.

As a reminder, the syntax is `PLAY <EFFECT> AT <LOC> WITH: <ATTRIBUTES>` with `EFFECT` being one of {`block`, `particle`, `sound`}.
Thus, the expected attributes can vary, depending on the chosen effect.

## Attributes for a `BLOCK`

- `type` : String. Bukkit Material to use. See Spigot [Material](https://jd.papermc.io/paper/1.12/org/bukkit/Material.html).
- `duration` : Duration. The duration of the block effect. _Default value is `1 second`._

## Attributes for a `PARTICLE`

- `type` : String. Bukkit particle type to use. See Spigot [ParticleType](https://jd.papermc.io/paper/1.12/org/bukkit/Particle.html).
- `count` : Number. The amount of particles to create. _Default value is `1`._
- `speed` : Number. The speed of the particles. _Default value is `0.1`._

## Attributes for a `SOUND`

- `type` : String. Bukkit Sound to use. See Spigot [Sound](https://jd.papermc.io/paper/1.12/org/bukkit/Sound.html).
- `volume` : Number. The volume to use. _Default value is `1`._
- `pitch` : Number. The pitch to use. _Default value is `1`._

## Attributes for an `ANIMATION`

- `id` : String. Identifier for the animation. Can be registered.
- All other properties, depending on the animation. Here's a list of the bundle animations.

### Animation `explode.items`

Multiple items will "explode" and fall on the ground. The type of items is randomly picked in a list.

- `duration`* : duration on the ground.
- `types`* : array of string, each for a [Material](https://jd.papermc.io/paper/1.12/org/bukkit/Material.html).
- `count` : total amount of items on the ground. Default = `5`.

### Animation `particle.spiral`

A vertical spiral of particles.

- `duration`* : duration of the effect.
- `particle`* : particle type to use ([ParticleType](https://jd.papermc.io/paper/1.12/org/bukkit/Particle.html)).
- `radius` : radius of the spiral. Default = `1`.
- `speed_y` : vertical speed, in blocks per second. Default = `1`.
- `speed_theta` : angle-speed. Default = `72`.

### Animation `particle.ccircle`

Basic circle of particles.

- `duration`* : duration of the effect.
- `particle`* : particle type to use ([ParticleType](https://jd.papermc.io/paper/1.12/org/bukkit/Particle.html)).
- `radius` : radius of the spiral. Default = `1`.
