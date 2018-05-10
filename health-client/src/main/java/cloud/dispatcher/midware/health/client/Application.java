package cloud.dispatcher.midware.health.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        new SpringApplicationBuilder().sources(Application.class).web(false).run(args);
        LOGGER.info("Spring boot application startup completed, cost: {}ms",
                System.currentTimeMillis() - currentTimeMillis);
    }
}
