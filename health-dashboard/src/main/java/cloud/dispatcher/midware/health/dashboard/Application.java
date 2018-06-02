package cloud.dispatcher.midware.health.dashboard;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cloud.dispatcher.base.framework.context.ApplicationContextListener;
import cloud.dispatcher.base.framework.error.CheckedException;
import cloud.dispatcher.base.framework.error.DefaultExceptionMessage;
import cloud.dispatcher.midware.health.dashboard.config.GlobalConfigValue;

@ImportResource("aspect.xml")
@SpringCloudApplication
public class Application extends WebMvcConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Autowired public MongoMappingContext mongoMappingContext;

    @Bean
    public ApplicationContextListener getApplicationContextListener() {
        return new ApplicationContextListener();
    }

    @Bean
    public MappingMongoConverter mongoConverter() throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        mappingMongoConverter.setMapKeyDotReplacement("_");
        mappingMongoConverter.afterPropertiesSet();
        return mappingMongoConverter;
    }

    public static String getCollectionName(String applicationName) {
        if (StringUtils.isBlank(applicationName)) {
            throw new CheckedException(DefaultExceptionMessage.ILLEGAL_ARGUMENT, "applicationName", applicationName);
        }

        int startWith = applicationName.toLowerCase().charAt(0);
        if (startWith < 97 || startWith > 122) {
            throw new CheckedException(DefaultExceptionMessage.ILLEGAL_ARGUMENT, "applicationName", applicationName);
        }

        return GlobalConfigValue.MONGO_COLLECTION_NAME_PREFIX + (char) startWith;
    }

    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        SpringApplication.run(Application.class, args);
        LOGGER.info("Spring boot application startup completed, cost: {}ms",
                System.currentTimeMillis() - currentTimeMillis);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true).allowedHeaders("*").allowedOrigins("*").allowedMethods("*");
    }

    @Autowired private MongoDbFactory mongoDbFactory;
}
