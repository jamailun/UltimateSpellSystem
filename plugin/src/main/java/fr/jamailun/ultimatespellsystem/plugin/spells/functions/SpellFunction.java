package fr.jamailun.ultimatespellsystem.plugin.spells.functions;

import fr.jamailun.ultimatespellsystem.UssLogger;
import fr.jamailun.ultimatespellsystem.api.providers.JavaFunctionProvider;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeExpression;
import fr.jamailun.ultimatespellsystem.api.runner.RuntimeStatement;
import fr.jamailun.ultimatespellsystem.api.runner.SpellRuntime;
import fr.jamailun.ultimatespellsystem.api.runner.functions.RunnableJavaFunction;
import fr.jamailun.ultimatespellsystem.api.utils.MultivaluedMap;
import fr.jamailun.ultimatespellsystem.dsl.UltimateSpellSystemDSL;
import fr.jamailun.ultimatespellsystem.dsl.nodes.StatementNode;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionArgument;
import fr.jamailun.ultimatespellsystem.dsl.nodes.expressions.functions.FunctionType;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.Type;
import fr.jamailun.ultimatespellsystem.dsl.nodes.type.TypePrimitive;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.MetadataNode;
import fr.jamailun.ultimatespellsystem.plugin.spells.SpellDefinition;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpellFunction {

    @Getter private final String name;
    private final List<RuntimeStatement> steps;
    private final List<FunctionArgument> args;
    private final @Nullable Output outputVar;

    public SpellFunction(String name, MultivaluedMap<String, MetadataNode> metadata, List<RuntimeStatement> steps) {
        this.name = name;
        this.steps = steps;

        args = readArgs(metadata);
        outputVar = readOutputVar(metadata);

        registerFunction();
    }

    private void registerFunction() {
        JavaFunctionProvider.instance().registerFunction(
                // Definition
                new RunnableJavaFunction(
                    name,
                    outputVar == null ? TypePrimitive.NULL.asType() : outputVar.type(),
                    args
                ) {
                    // Execution
                    @Override
                    public Object compute(@NotNull List<RuntimeExpression> arguments, @NotNull SpellRuntime runtime) {
                        // Register variables in sub-scope
                        SpellRuntime childRuntime = runtime.makeChild();
                        for(int i = 0; i < args.size(); i++) {
                            Object arg = arguments.get(i).evaluate(childRuntime);
                            childRuntime.variables().set(args.get(i).debugName(), arg);
                        }
                        // Execute
                        return executeSteps(childRuntime);
                    }
                }
        );
    }

    public Object executeSteps(@NotNull SpellRuntime runtime) {
        for(RuntimeStatement statement : steps) {
            UssLogger.logDebug("FCT["+name+"] Running " + statement.toString());
            statement.run(runtime);
            if(runtime.isStopped())
                break;
        }
        if(outputVar != null) {
            return runtime.variables().get(outputVar.name());
        }
        return runtime.getFinalExitCode();
    }

    public static @Nullable SpellFunction loadFile(@NotNull File file) {
        MultivaluedMap<String, MetadataNode> metadata = new MultivaluedMap<>();
        List<RuntimeStatement> steps = new ArrayList<>();

        try {
            List<StatementNode> dsl = UltimateSpellSystemDSL.parse(file);
            List<RuntimeStatement> rawStatements = SpellDefinition.load(dsl);

            // Metadata are already sorted (thanks to AST validation)
            for(RuntimeStatement statement : rawStatements) {
                if(statement instanceof MetadataNode meta) {
                    metadata.put(meta.getName(), meta);
                } else {
                    steps.add(statement);
                }
            }

            return new SpellFunction(getName(file, metadata), metadata, steps);
        } catch(Exception e) {
            UssLogger.logError("In function "+file+" : " + e.getMessage());
            for(StackTraceElement se : e.getStackTrace()) {
                UssLogger.logDebug("  §c" + se.toString());
            }
            return null;
        }
    }

    private static @NotNull String getName(@NotNull File file, @NotNull MultivaluedMap<String, MetadataNode> metadata) {
        if(metadata.containsKey("name")) {
            return extractName("name", metadata);
        }
        if(metadata.containsKey("named")) {
            return extractName("named", metadata);
        }
        if(metadata.containsKey("function")) {
            return extractName("function", metadata);
        }
        return file.getName()
                .replace(" ", "-")
                .toLowerCase()
                .replaceFirst("[.][^.]+$", "");
    }

    private static @NotNull String extractName(@NotNull String key, @NotNull MultivaluedMap<String, MetadataNode> metadata) {
        MetadataNode node = Objects.requireNonNull(metadata.getFirst(key));
        if(node.isEmpty())
            throw new InvalidMetadata(node, "a function @"+key+" needs to specify 1 value: the name.");
        return node.getFirst(String.class);
    }

    private static @NotNull List<FunctionArgument> readArgs(@NotNull MultivaluedMap<String, MetadataNode> metadata) {
        List<FunctionArgument> list = new ArrayList<>();
        for(MetadataNode node : metadata.getOrEmpty("param")) {
            if(node.getParams().size() < 2)
                throw new InvalidMetadata(node, "a function @param needs to have at least 2 values: 'var-name' and 'type'.");

            String varName = node.get(0, String.class);
            String varType = node.get(1, String.class);
            TypePrimitive type = TypePrimitive.parsePrimitive(varType);
            if(type == null)
                throw new InvalidMetadata(node, "unknown @param primitive: '" + varType + "'.");

            list.add(new FunctionArgument(
                    FunctionType.accept(type),
                    varName,
                    false
            ));
        }
        return list;
    }

    private record Output(@NotNull String name, @NotNull Type type) {}

    private static @Nullable Output readOutputVar(@NotNull MultivaluedMap<String, MetadataNode> metadata) {
        MetadataNode node = metadata.getFirstNonNull("output", "out");
        if(node == null) return null;

        if(node.getParams().size() < 2)
            throw new InvalidMetadata(node, "a function @Output needs to have at least 2 values: 'output variable name' and 'type'.");

        String outVar = node.get(0, String.class);
        String outType = node.get(1, String.class);
        boolean collection = false;
        TypePrimitive type = TypePrimitive.parsePrimitive(outType);
        if(type == null)
            throw new InvalidMetadata(node, "unknown @output primitive : '" + outType + "'.");
        if(node.size() > 2 && node.getRaw(2) instanceof Boolean isColl) {
            collection = isColl;
        }
        return new Output(outVar, type.asType(collection));
    }

}
