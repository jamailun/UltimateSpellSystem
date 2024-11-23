package fr.jamailun.ultimatespellsystem.runner;

import fr.jamailun.ultimatespellsystem.api.utils.StringTransformation;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions.RawLiteral;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.expressions.VariableNode;
import fr.jamailun.ultimatespellsystem.plugin.runner.nodes.functions.SendMessageNode;
import fr.jamailun.ultimatespellsystem.runner.framework.TestFramework;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Check the framework is working.
 */
public class SimpleTest extends TestFramework {

    @Test
    void test() {
        SendMessageNode node = new SendMessageNode(new VariableNode("caster"), new RawLiteral<>("message"));
        Assertions.assertTrue(cast(node));

        Mockito.verify(caster).sendMessage(StringTransformation.parse("message"));
    }

}
