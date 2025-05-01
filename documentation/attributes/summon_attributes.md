# `Summons` attributes

Newly created summons can have attributes

## Bukkit attributes

### Numbers attributes

- `health` : Number. The max-health value to set.
- `attack` : Number. The attack damage value to set.
- `armor` : Number. The armor value to set.
- `armor_toughness` : Number. The armor toughness value to set.
- `speed` : Number. The number value to set.
- `knockback_resistance` : Number. The knockback-resistance to set.
- `knockback` : Number. The number knockback-attack to set.

### Miscellaneous attributes

- `baby` : Boolean. If true (and if entity type is [Ageable](https://jd.papermc.io/paper/1.21.3/org/bukkit/block/data/Ageable.html)) the entity will be a Baby.

### Name attributes

- `name` : String. The name-tag to display on top of the entity.
- `name_visible` : Boolean. If true, the name-tag will always be displayed.

## Aggro management

- `aggro_scope` : String | Scope. Type of entities that can be aggro-ed. If not specified, no custom aggro will be applied_
- `aggro_range` : Number. Range of aggro lookup. _Default value: `7`._
- `can_aggro_caster` : Boolean. If true, caster can be attacked. _Default value: `false`._
- `can_aggro_summons` : Boolean. If true, caster's other summons can be attacked. _Default value: `false`._
- `can_aggro_allies` : Boolean. If true, caster's allies can be attacked. _Default value: `false`._ Check the allies documentation for more information.

## Projectile attributes

- `velocity` : Double. Velocity to shoot the projectile with. If not specified, The projectile will just drop from the caster with a value of `0`.
- `projectile_damage` : Double. Amount of damage the projectile should apply. If not specified, Minecraft will handle it with the projectile velocity.
- `can_damage_caster` : Boolean. if true, the projectile may damage the caster. _Default value: `true`._
- `can_damage_allies` : Boolean. if true, the projectile may damage the caster allies. _Default value: `true`._

## Read custom attributes

You can obtain informations about a summon entity, and then reading its attribute.

```java
import fr.jamailun.ultimatespellsystem.bukkit.UssMain;
import fr.jamailun.ultimatespellsystem.bukkit.entities.SummonAttributesImpl;

import java.util.UUID;

public void checkSummonHasAttribute(UUID uuid) {
    SummonAttributesImpl summon = UssMain.getSummonsManager().find(uuid).orElse(null);
    if (summon == null) return;

    Double value = summon.tryGetAttribute("my_attribute", Double.class);
    System.out.println("My attribute = " + value + ". Complete map = " + summon.getAttributes());
}
```

## Create custom attribute

Check the [corresponding documentation](/documentation/registries/how_to_register.md#register-a-custom-summon-attribute).
