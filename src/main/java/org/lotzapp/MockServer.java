package org.lotzapp;

import com.fasterxml.jackson.databind.Module;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(
        basePackages = {"org.lotzapp"},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = ".*\\.configuration\\.(HomeController|SpringDocConfiguration)"
                ),
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = ".*\\.OpenApiGeneratorApplication"
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                org.lotzapp.regiologapi.api.ClientApiController.class,
                                org.lotzapp.regiologapi.api.ProductApiController.class
                        }
                )
        },
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class MockServer {

    public static void main(String[] args) {
        SpringApplication.run(MockServer.class, args);
    }

    @Bean(name = "org.lotzap.MockServer.jsonNullableModule")
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }
}
