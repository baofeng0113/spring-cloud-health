package cloud.dispatcher.midware.health.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class Application implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Value("${spring.cloud.health.interval.ms}") private int intervalMS;

    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        new SpringApplicationBuilder(Application.class).web(false).build().run(args);
        LOGGER.info("Spring boot application startup completed, cost: {}ms",
                System.currentTimeMillis() - currentTimeMillis);
    }

    @Override
    public void run(String... strings) throws Exception {
        while (true) {
            componentMetricsInfoMonitor.reportMetricsInfo();
            Thread.sleep(intervalMS);
        }
    }

    @Autowired private ComponentMetricsInfoMonitor componentMetricsInfoMonitor;
}
