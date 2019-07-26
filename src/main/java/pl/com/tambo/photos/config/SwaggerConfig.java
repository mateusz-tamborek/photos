package pl.com.tambo.photos.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import pl.com.tambo.photos.core.model.User;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String SCHEME = "JWT";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("pl.com.tambo"))
                .build()
                .useDefaultResponseMessages(false)
                .ignoredParameterTypes(Pageable.class)
                .ignoredParameterTypes(UserDetails.class, User.class)
                .apiInfo(new ApiInfoBuilder()
                        .title("Photo gallery")
                        .description("API documentation")
                        .contact(new Contact("Mateusz Tamborek", "https://github.com/mateusz-tamborek", ""))
                        .build())
                .securitySchemes(of(apiKey()))
                .securityContexts(of(securityContext()));
    }

    private ApiKey apiKey() {
        return new ApiKey(SCHEME, "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");
        return Lists.newArrayList(new SecurityReference(SCHEME, new AuthorizationScope[]{scope}));
    }

}
