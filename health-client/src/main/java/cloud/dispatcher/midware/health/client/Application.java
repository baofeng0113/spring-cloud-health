package cloud.dispatcher.midware.health.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Autowired private DiscoveryClient discoveryClient;

    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        SpringApplication.run(Application.class);
        LOGGER.info("Spring boot application startup completed, cost: {}ms",
                System.currentTimeMillis() - currentTimeMillis);
    }
}
