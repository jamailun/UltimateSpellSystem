package fr.jamailun.ultimatespellsystem.runner.framework;

import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystem;
import fr.jamailun.ultimatespellsystem.api.UltimateSpellSystemPlugin;
import fr.jamailun.ultimatespellsystem.api.providers.JavaFunctionProvider;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.UssMain;
import fr.jamailun.ultimatespellsystem.api.utils.Scheduler;
import fr.jamailun.ultimatespellsystem.plugin.runner.SpellRuntimeImpl;
import fr.jamailun.ultimatespellsystem.runner.framework.functions.AssertNotCalledFunction;
import fr.jamailun.ultimatespellsystem.runner.framework.functions.AssertTrueFunction;
import fr.jamailun.ultimatespellsystem.runner.framework.functions.PrintFunction;
import io.papermc.paper.ServerBuildInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class TestFramework {

    protected Player caster;
    protected World world;

    @BeforeAll
    static void initAll() {
        if(Bukkit.getServer() == null) { // We replace the Bukkit Server instance : so it's null the first time.
            Server server = Mockito.mock(Server.class);
            ConsoleCommandSender sender = Mockito.mock(ConsoleCommandSender.class);
            Logger logger = Mockito.mock(Logger.class);
            Mockito.doNothing().when(sender).sendMessage(Mockito.anyString());
            Mockito.doNothing().when(logger).info(Mockito.anyString());
            Mockito.when(server.getConsoleSender()).thenReturn(sender);
            Mockito.when(server.getName()).thenReturn("test");
            Mockito.when(server.getBukkitVersion()).thenReturn("test");
            Mockito.when(server.getLogger()).thenReturn(logger);
            Mockito.when(server.getPluginCommand(Mockito.anyString())).thenReturn(null);


            try (MockedStatic<ServerBuildInfo> sbi = Mockito.mockStatic(ServerBuildInfo.class)) {
                ServerBuildInfo info = Mockito.mock(ServerBuildInfo.class);
                Mockito.when(info.asString(Mockito.any())).thenReturn("v");
                sbi.when(ServerBuildInfo::buildInfo).thenReturn(info);
                Bukkit.setServer(server);
            }
        }

        UltimateSpellSystemPlugin fakePlugin = Mockito.mock(UssMain.class);
        Mockito.when(fakePlugin.getScheduler()).thenReturn(Mockito.mock(Scheduler.class));
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
        UUID uuid = UUID.randomUUID();
        Mockito.when(caster.getUniqueId()).thenReturn(uuid);
        Mockito.when(caster.getName()).thenReturn("MockedPlayer");
    }

    protected boolean cast(@NotNull RuntimeStatement... statements) {
        return cast(List.of(statements));
    }

    protected boolean cast(@NotNull List<RuntimeStatement> statements) {
        SpellRuntime runtime = new SpellRuntimeImpl(caster, null);

        for(RuntimeStatement statement : statements) {
            statement.run(runtime);
            if(runtime.isStopped())
                break;
        }
        return runtime.getFinalExitCode() == 0;
    }

}
