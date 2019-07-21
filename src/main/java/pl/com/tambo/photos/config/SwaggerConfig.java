package pl.com.tambo.photos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("pl.com.tambo"))
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("Photo gallery")
                        .description("API documentation")
                        .contact(new Contact("Mateusz Tamborek", "https://github.com/mateusz-tamborek", ""))
                        .build());
    }

}
