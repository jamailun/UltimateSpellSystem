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
import fr.jamailun.ultimatespellsystem.bukkit.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.bukkit.providers.JavaFunctionProvider;
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
    public Double compute(@NotNull List < RuntimeExpression > arguments, @NotNull SpellRuntime runtime){
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

## Register a custom scope

Scopes are used to search entities (`all <SCOPE> around <ENTITY> ...`) and to define the aggroable entities of a summon.
To provide a custom scope, simply use a "named" entity predicate.

### Example

Here, we create a scope named "players_with_a" and "a_players" that can target any Bukkit Player whose
name starts with the letter "a".

```java
import fr.jamailun.ultimatespellsystem.bukkit.providers.ScopeProvider;

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
import fr.jamailun.ultimatespellsystem.bukkit.providers.SummonPropertiesProvider;

public class CustomSummonPropertiesExtension extends SummonPropertiesProvider {
    public CustomSummonPropertiesExtension() {
        // this util method allow us to guarantee arguments will be the boolean we want.
        SummonProperty fireProperty = super.createForEntity((entity,bool,runtime) -> {
            entity.setVisualFire(bool);
        }, LivingEntity.class, Boolean.class);
        // then, we register it
        SummonPropertiesProvider.instance().register(fireProperty);
    }
}
```
