package fr.jamailun.ultimatespellsystem.runner.framework;

import fr.jamailun.ultimatespellsystem.bukkit.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.bukkit.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.bukkit.runner.SpellRuntime;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

public abstract class TestFramework {

    protected Player caster;

    @BeforeEach
    void initMockBukkit() {
        caster = Mockito.mock(Player.class);
    }

    protected boolean cast(@NotNull RuntimeStatement... statements) {
        return cast(List.of(statements));
    }

    protected boolean cast(@NotNull List<RuntimeStatement> statements) {
        try(MockedStatic<UltimateSpellSystem> uss = Mockito.mockStatic(UltimateSpellSystem.class)) {
            SpellRuntime runtime = new SpellRuntime(caster);

            for(RuntimeStatement statement : statements) {
                statement.run(runtime);
                if(runtime.isStopped())
                    break;
            }
            return runtime.getFinalExitCode() == 0;
        }
    }

}
