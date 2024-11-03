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
