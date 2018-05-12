package cloud.dispatcher.midware.health.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@SpringCloudApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        SpringApplication.run(Application.class, args);
        LOGGER.info("Spring boot application startup completed, cost: {}ms",
                System.currentTimeMillis() - currentTimeMillis);
    }
}
