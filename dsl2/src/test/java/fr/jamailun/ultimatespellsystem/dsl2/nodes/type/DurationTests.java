package fr.jamailun.ultimatespellsystem.dsl2.nodes.type;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

/**
 * Tests for {@link Duration} class.
 */
class DurationTests {

  @Test
  void testDurOpeDur() {
    Duration a = new Duration(12, TimeUnit.HOURS);
    Duration b = new Duration(60, TimeUnit.MINUTES);
    Assertions.assertEquals(12, a.div(b));

    Duration c = new Duration(1, TimeUnit.HOURS);
    Duration d = new Duration(1, TimeUnit.SECONDS);
    Assertions.assertEquals(new Duration(3601, TimeUnit.SECONDS), c.add(d));
    Assertions.assertEquals(new Duration(3599, TimeUnit.SECONDS), c.sub(d));

    Assertions.assertEquals(new Duration(2, TimeUnit.HOURS), c.add(c));
  }

  @Test
  void testDurDivLambda() {
    Duration a = new Duration(12, TimeUnit.HOURS);
    Assertions.assertEquals(new Duration(120, TimeUnit.MINUTES), a.div(6));
  }

  @Test
  void testDurMul() {
    Duration a = new Duration(12, TimeUnit.MINUTES);
    Assertions.assertEquals(new Duration(1, TimeUnit.HOURS), a.mul(5));
  }

  @Test
  void conversionsTest() {
    Duration oneSecond = new Duration(1, TimeUnit.SECONDS);
    Assertions.assertEquals(1000L, oneSecond.toMs());
    Assertions.assertEquals(20L, oneSecond.toTicks());
    Assertions.assertEquals(java.time.Duration.ofSeconds(1), oneSecond.asJavaDuration());
  }

}
