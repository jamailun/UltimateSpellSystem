# Registries

A lot of elements can be modified in the DSL.

## Register java-function

To register a java-function, you will need to provide :
1. A definition for the DSL,
2. An executable code.
Then, what remain will be a registration, using the [JavaFunctionProvider # register](/src/main/java/fr/jamailun/ultimatespellsystem/bukkit/providers/JavaFunctionProvider.java) method.

Those 2 things are united in one class : [RunnableJavaFunction](/src/main/java/fr/jamailun/ultimatespellsystem/bukkit/runner/functions/RunnableJavaFunction.java).

### Example

Let's say we want a function that generate a random number between 2 bounds.

The signature would be `(int, int) -> int`. So the function definition implementation can be :

```java
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.bukkit.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.api.bukkit.providers.JavaFunctionProvider;
import org.jetbrains.annotations.NotNull;

public class RandBoundFunction extends RunnableJavaFunction {

    private RandBoundFunction() {
        super(
                // Id of the function
                "rand_in_bounds",
                // Returned type
                TypePrimitive.NUMBER.asType(),
                // Expects two arguments: 'min' and 'max'. Both are not-optional.
                List.of(
                        new FunctionArgument(FunctionType.acceptOnlyMono(TypePrimitive.NUMBER), "min", false),
                        new FunctionArgument(FunctionType.acceptOnlyMono(TypePrimitive.NUMBER), "max", false)
                )
        );
    }

    @Override
    public Double compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
        // because of the DSL declaration, we know arguments 1 and 2 exist and are numbers.
        double min = runtime.safeEvaluate(arguments.get(0), Double.class);
        double max = runtime.safeEvaluate(arguments.get(1), Double.class);

        // return random number
        return new Random().nextDouble(max - min) + min;
    }

    // And here, how's to register it
    public static void registerElement() {
        JavaFunctionProvider.instance().register(new RandBoundFunction(), "rand_in_bounds");
    }
}
```

_The syntax has not been improved, but it can be changed later on._

## Register a custom entity type

Implement a [UssEntityType](/src/main/java/fr/jamailun/ultimatespellsystem/bukkit/entities/UssEntityType.java).
Then, register it using [EntityTypeProvider#register](/src/main/java/fr/jamailun/ultimatespellsystem/bukkit/providers/EntityTypeProvider.java).

This will be used when summoning a creature, for example.

## Register a custom scope

[Scopes](/documentation/scopes.md) are used to search entities (`all <SCOPE> around <ENTITY> ...`) and to define the aggroable entities of a summon.
To provide a custom scope, simply use a "named" entity predicate.

### Example

Here, we create a scope named "players_with_a" and "a_players" that can target any Bukkit Player whose
name starts with the letter "a".

```java
import fr.jamailun.ultimatespellsystem.api.bukkit.providers.ScopeProvider;

public static void someSetup() {
    // We want to target all player whose name starts with the letter 'a'.
    ScopeProvider.instance().register(
            e -> e instanceof Player p && p.getName().startsWith("a"),
            // Accepted names in the DSL :
            "players_with_a", "a_players"
    );
}
```

## Register a custom summon-attribute

As listed in the [summons attributes page](/documentation/attributes/summon_attributes.md), a lot of attributes do exist. You can create your own.

Don't forget that, at any point, you can get the attribute-values (and map) of a summon.

### Example

Here, we create an attribute "is_fire" (and "fire") that put the newly created summon in fire.

```java
import fr.jamailun.ultimatespellsystem.api.bukkit.providers.SummonPropertiesProvider;

public class CustomSummonPropertiesExtension extends SummonPropertiesProvider {
    public CustomSummonPropertiesExtension() {
        // this util method allow us to guarantee arguments will be the boolean we want.
        SummonProperty fireProperty = super.createForEntity((entity, bool, runtime) -> {
            entity.setVisualFire(bool);
        }, LivingEntity.class, Boolean.class);
        // then, we register it
        SummonPropertiesProvider.instance().register(fireProperty, "is_fire", "fire");
    }
}
```

## Register a callback event

Callbacks can be used to react to some elements about summons. A callback is made of two parts:
1. A registration for an event type, with a DSL keyword,
2. A listener for the specific event. On this event, you'll need to check if a summon entity matches. Let's examine the
"projectile land" callback.

### 1. DSL registration

Tht goal is to allow for a new `callback` type.
Check the [corresponding documentation](../callbacks.md) for more details.

```java
import fr.jamailun.ultimatespellsystem.api.entities.CallbackAction;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.dsl.objects.CallbackEvent;
import fr.jamailun.ultimatespellsystem.dsl.tokenization.TokenType;
import org.bukkit.Location;

public class RegistrationDemo {
    public static void registerProjectileLand() {
        // Create a new callback action.
        CallbackAction<ProjectileHitEvent, Location> definition = new CallbackAction<>(
                // Defines the expected keyword, expected argument keyword and argument type.
                // Here, the callback type will be "landed".
                // The argument will be identified with the "AT %var" element.
                // The argument will be the location.
                CallbackEvent.of("landed", TokenType.AT, TypePrimitive.LOCATION),
                // The listened event
                ProjectileHitEvent.class,
                // The argument content to use. Generally, we extract the value from the event.
                e -> e.getEntity().getLocation()
        );
        // And call the register
        CallbackEventProvider.instance().registerCallback(definition);
    }
}
```

### 2. Event listening

Here's an example to detect when a projectile lands.

```java
import org.bukkit.event.Listener;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.entities.SummonAttributes;

public class ProjectileLandCallbacks implements Listener {
    @EventHandler
    void onEvent(@NotNull ProjectileHitEvent event) {
        // The projectile will be the return value of the ProjectileHitEvent#getentity() method.
        SummonAttributes summon = UltimateSpellSystem.getSummonsManager().find(event.getEntity().getUniqueId());
        if (summon != null) {
            // If no callback exist, it won't do anything.
            summon.applyCallback(event);
        }
    }
}
```

## Register a spell-cost

The spells can be cast using a cost. Each cost must be defined in the Java API.

You need to implement a `SpellCost` class, and register it, wrapped in a `SpellCostEntry`.
This entry will required a method to produce a cost from a list of string. Indeed, each cost
can register data into the item its bound to. For instance, let's implement a "RandomCost" that has a
specific chance of succeeding (this seems to be a very bad design, don't do it :P)

```java
import fr.jamailun.ultimatespellsystem.api.bind.SpellCost;
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;

public class RandomCost implements SpellCost {

  private double chances = 0.5;

  // This is the method used to deserialize the cost.
  // But a static method can also produce the same result !
  public RandomCost(@NotNull List<String> serialized) {
    this.cost = Double.parseDouble(serialized.getFirst());
  }

  @Override
  public boolean canPay(@NotNull SpellEntity caster) {
    return new java.util.Random().nextDouble(1) <= chances;
  }

  @Override
  public void pay(@NotNull SpellEntity caster) {
    // We don't remove anything from the caster :)
  }

  @Override
  public @NotNull List<Object> serialize() {
    return List.of(chances);
  }

  @Override
  public String toString() {
    return "Random[" + chances + "]";
  }
  
  // Register this class
  public static void registerCost() {
    UltimateSpellSystem.getSpellCostRegistry().register(
      SpellCostEntry.of(
          "random", // ID of the cost : must be unique.
          RandomCost.class, // Serialized class
          RandomCost::new, // Method to deserialize the cost : here the constructor
          SpellCostArgType.DOUBLE // Varargs of arguments (here the 'chance' field)
      )  
    );
  }
}
```

## Register a allies check

From the `2.2.0`, properties exist to forbid a summon from attacking a caster "allies" (or projectile damage, or spell damage, ...).

Thus, USS must know if two entities are related. Using the same paradigm as every other registry, a "client" plugin will register a "AlliesCheck".
It is simply a form of `BiFunction`. It accepts the spell-caster and the targeted bukkit entity, and return a result.
The output value accepts 3 values :
- `ALLIES` : the caster and the target are allied. Thus, if the spell supports it, not damage / aggro will be applied.
- `ENEMIES` : the plugin knows for sure entities are not allies.
- `IGNORE` : the plugin does not know. The next check may know more.

Both `ALLIES` and `ENEMIES` will stop any further check if returned.

### Demo

Let's say we have a plugin `FooParty` that handles parties. Party members should not have summons attack each others.
The method `FooParty.getPartyOf(UUID)` will supposedly return a `Party`, and `Party#contains(UUID)` will check if entities share the same party.

```java
import fr.jamailun.ultimatespellsystem.api.entities.SpellEntity;
import fr.jamailun.ultimatespellsystem.api.providers.AlliesProvider;
import fr.jamailun.ultimatespellsystem.api.providers.AlliesProvider.AlliesResult;
import org.bukkit.entity.Entity;

/**
 * We implement the thing to be registered
 */
public class FooPartyAlliesCheck implements AlliesProvider.AlliesCheck {
    @Override
    public @NotNull AlliesResult test(@NotNull SpellEntity spellEntity, @NotNull Entity entity) {
        Party party = FooParty.getPartyOf(spellEntity.getUniqueId());
        if(party == null) return AlliesResult.IGNORE; // Caster is not in any party. Thus, this check may not now about alliance.

        // Simply check if the party contains the target !
        return party.contains(entity.getUniqueId()) ? AlliesResult.ALLIES : AlliesResult.IGNORE;
    }
    
    // Register to the provider
    public static void registerThis() {
        AlliesProvider.instance().register(new FooPartyAlliesCheck(), "foo-party");
    }
}
```
