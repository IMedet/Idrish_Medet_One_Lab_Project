package kz.medet.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@ComponentScan("kz.medet")
@EnableAspectJAutoProxy
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class AopConfig {
}
