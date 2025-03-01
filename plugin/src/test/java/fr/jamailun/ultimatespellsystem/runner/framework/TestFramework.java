package fr.jamailun.ultimatespellsystem.runner.framework;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystemPlugin;
import fr.jamailun.ultimatespellsystem.api.providers.JavaFunctionProvider;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.UssMain;
import fr.jamailun.ultimatespellsystem.plugin.runner.SpellRuntimeImpl;
import fr.jamailun.ultimatespellsystem.runner.framework.functions.AssertNotCalledFunction;
import fr.jamailun.ultimatespellsystem.runner.framework.functions.AssertTrueFunction;
import fr.jamailun.ultimatespellsystem.runner.framework.functions.PrintFunction;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.List;

public abstract class TestFramework {

    protected Player caster;
    protected World world;

    @BeforeAll
    static void initAll() {
        UltimateSpellSystemPlugin fakePlugin = Mockito.mock(UssMain.class);
        Mockito.doNothing().when(fakePlugin).logDebug(Mockito.anyString());
        Mockito.doNothing().when(fakePlugin).logInfo(Mockito.anyString());
        Mockito.doNothing().when(fakePlugin).logWarning(Mockito.anyString());
        Mockito.doNothing().when(fakePlugin).logError(Mockito.anyString());
        try {
            UltimateSpellSystem.setPlugin(fakePlugin);

            JavaFunctionProvider.instance().registerFunction(new AssertTrueFunction());
            JavaFunctionProvider.instance().registerFunction(new AssertNotCalledFunction());
            JavaFunctionProvider.instance().registerFunction(new PrintFunction());
        } catch(IllegalStateException ignored) {}
    }

    @BeforeEach
    void initMockBukkit() {
        world = Mockito.mock(World.class);

        caster = Mockito.mock(Player.class);
        Mockito.when(caster.getLocation()).thenReturn(new Location(world, 12, 12, 12));
        Mockito.when(caster.getWorld()).thenReturn(world);
        Mockito.when(caster.getEyeHeight()).thenReturn(1d);
        Mockito.when(caster.getEyeLocation()).thenReturn(new Location(world, 12, 13, 12));
        Mockito.when(caster.getWalkSpeed()).thenReturn(1f);
    }

    protected boolean cast(@NotNull RuntimeStatement... statements) {
        return cast(List.of(statements));
    }

    protected boolean cast(@NotNull List<RuntimeStatement> statements) {
        SpellRuntime runtime = new SpellRuntimeImpl(caster);

        for(RuntimeStatement statement : statements) {
            statement.run(runtime);
            if(runtime.isStopped())
                break;
        }
        return runtime.getFinalExitCode() == 0;
    }

}
