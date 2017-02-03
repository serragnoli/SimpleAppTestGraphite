import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MetricsUtils {
    private static final MetricRegistry metrics = new MetricRegistry();

    private static Map<String, Counter> endpoints = new HashMap<>();

    static void startReport() {
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        consoleReporter.start(20, TimeUnit.SECONDS);

        final Graphite graphite = new Graphite(new InetSocketAddress("127.0.0.1", 2003));
        final GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(metrics)
                .prefixedWith("simpleapp")
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .build(graphite);
        graphiteReporter.start(20, TimeUnit.SECONDS);

        MetricRegistry.name(QueueManager.class, "jobs", "size");
        metrics.register("jvm.memory", new MemoryUsageGaugeSet());

    }

    static void incrementEndpoint(String endpoint) {
        Counter orDefault = endpoints.getOrDefault(endpoint, metrics.counter("endpoint." + endpoint));
        orDefault.inc();
    }

    @SuppressWarnings("unused")
    static void wait5Seconds() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
    }
}
