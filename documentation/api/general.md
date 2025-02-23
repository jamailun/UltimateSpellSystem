# API usage

## How-to

### Add the dependency

Using maven, add the following dependency (inside the `<depencendies>` tag in your `pom.xml`) :

```xml
<dependency>
    <groupId>fr.jamailun.paper</groupId>
    <artifactId>ultimate-spell-system-api</artifactId>
    <version>1.0.1</version>
</dependency> 
```

Don't forgot to add a dependency in your `plugin.yml`, so that the dependency will be loaded **before** your plugin.

### Java entry-point

Then, inside your code, you can use the `UltimateSpellSystem` entry-point.

````java
import org.bukkit.plugin.java.JavaPlugin;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;

public class ExamplePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        boolean ussOk = UltimateSpellSystem.isValid();
        // Get the item-binder, the spells-manager, ...
    }
}
````

Please, not that using USS in the `onLoad()` phase way throw NPE. Wait for the `onEnable()`.
