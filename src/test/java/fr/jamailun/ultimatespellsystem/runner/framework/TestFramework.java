package fr.jamailun.ultimatespellsystem.runner.framework;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystemPlugin;
import fr.jamailun.ultimatespellsystem.bukkit.UssMain;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

public abstract class TestFramework {

    protected Player caster;

    @BeforeAll
    static void initAll() {
        UltimateSpellSystemPlugin fakePlugin = Mockito.mock(UssMain.class);
        Mockito.doNothing().when(fakePlugin).logDebug(Mockito.anyString());
        Mockito.doNothing().when(fakePlugin).logInfo(Mockito.anyString());
        Mockito.doNothing().when(fakePlugin).logWarning(Mockito.anyString());
        Mockito.doNothing().when(fakePlugin).logError(Mockito.anyString());
        UltimateSpellSystem.setPlugin(fakePlugin);
    }

    @BeforeEach
    void initMockBukkit() {
        caster = Mockito.mock(Player.class);
    }

    protected boolean cast(@NotNull RuntimeStatement... statements) {
        return cast(List.of(statements));
    }

    protected boolean cast(@NotNull List<RuntimeStatement> statements) {
        SpellRuntime runtime = new SpellRuntime(caster);

        for(RuntimeStatement statement : statements) {
            statement.run(runtime);
            if(runtime.isStopped())
                break;
        }
        return runtime.getFinalExitCode() == 0;
    }

}
