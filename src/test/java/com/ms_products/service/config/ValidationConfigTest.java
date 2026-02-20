package com.ms_products.service.config;

import com.ms_products.config.ValidationConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(ValidationConfig.class);

    @Test
    @DisplayName("Debe registrar y configurar correctamente los Beans de validación")
    void shouldRegisterValidationBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MessageSource.class);
            MessageSource messageSource = context.getBean(MessageSource.class);
            assertThat(messageSource).isInstanceOf(ReloadableResourceBundleMessageSource.class);

            assertThat(context).hasSingleBean(LocalValidatorFactoryBean.class);
            LocalValidatorFactoryBean validator = context.getBean(LocalValidatorFactoryBean.class);
            assertThat(validator).isNotNull();

            assertThat(context).hasBean("getValidator");
        });
    }
}