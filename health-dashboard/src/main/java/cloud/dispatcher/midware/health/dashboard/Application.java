package cloud.dispatcher.midware.health.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import cloud.dispatcher.base.framework.context.ApplicationContextListener;

@ImportResource("aspect.xml")
@SpringCloudApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Autowired public MongoMappingContext mongoMappingContext;

    @Bean
    public ApplicationContextListener getApplicationContextListener() {
        return new ApplicationContextListener();
    }

    @Bean
    public MappingMongoConverter mongoConverter() throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(
                dbRefResolver, mongoMappingContext);
        mappingMongoConverter.setMapKeyDotReplacement("_");
        mappingMongoConverter.afterPropertiesSet();
        return mappingMongoConverter;
    }

    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        SpringApplication.run(Application.class, args);
        LOGGER.info("Spring boot application startup completed, cost: {}ms",
                System.currentTimeMillis() - currentTimeMillis);
    }

    @Autowired private MongoDbFactory mongoDbFactory;
}
