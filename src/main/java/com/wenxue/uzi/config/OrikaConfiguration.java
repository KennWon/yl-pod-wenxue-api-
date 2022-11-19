
package com.wenxue.uzi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kun
 */
@Configuration
public class OrikaConfiguration {
    public OrikaConfiguration() {}

    @Bean
    public OrikaBeanMapper beanMapper() {
        return new OrikaBeanMapper();
    }
}
