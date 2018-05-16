package cloud.dispatcher.midware.health.dashboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    private static final String BASE_PACKAGE = "cloud.dispatcher.midware.health.dashboard.web";

    @Bean
    public Docket docket() {
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder().title("Spring Cloud Health").description("可视化调试工具入口")
                .contact("developer@dispatcher.cloud");

        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any()).build().apiInfo(apiInfoBuilder.build());
    }
}
