package br.com.sabino.lab.infra.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Sabino Labs.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 *
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
}
