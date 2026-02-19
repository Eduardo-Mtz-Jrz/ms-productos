package com.ms_products.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configuration class for customized bean validation.
 * <p>
 * This class integrates {@link MessageSource} with the validation framework,
 * allowing for internationalized and externalized validation messages.
 * </p>
 */
@Configuration
public class ValidationConfig {

    /**
     * Configures the message source for validation error descriptions.
     * * @return a configured {@link MessageSource} instance pointing to 'messages.properties'.
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        // SonarQube Tip: Consider setting a cache duration for production environments
        messageSource.setCacheSeconds(3600);
        return messageSource;
    }

    /**
     * Integrates the custom {@link MessageSource} into the Bean Validation framework.
     * * @return a {@link LocalValidatorFactoryBean} that uses the custom message source.
     */
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}