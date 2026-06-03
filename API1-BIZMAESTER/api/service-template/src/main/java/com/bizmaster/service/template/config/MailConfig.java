package com.bizmaster.service.template.config;

import java.util.Properties;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ConditionalOnClass(JavaMailSenderImpl.class)
@EnableConfigurationProperties(MailProperties.class)
public class MailConfig {

    @Bean
    @ConditionalOnMissingBean
    public JavaMailSender javaMailSender(MailProperties properties) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(properties.getHost() != null ? properties.getHost() : "localhost");
        sender.setPort(properties.getPort() != null ? properties.getPort() : 25);
        sender.setProtocol(properties.getProtocol());
        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());

        if (properties.getDefaultEncoding() != null) {
            sender.setDefaultEncoding(properties.getDefaultEncoding().name());
        }

        Map<String, String> props = properties.getProperties();
        if (props != null && !props.isEmpty()) {
            Properties javaMailProperties = new Properties();
            javaMailProperties.putAll(props);
            sender.setJavaMailProperties(javaMailProperties);
        }

        if (sender.getProtocol() == null) {
            sender.setProtocol("smtp");
        }

        return sender;
    }
}
