package net.noday.chris.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.view.AbstractView;

import net.noday.chris.props.ChrisProperties;
import net.noday.core.security.ShiroDbRealm;
import net.noday.core.service.SecurityService;
import net.noday.core.web.MappingFastjsonJsonView;

@Configuration
@ComponentScan(basePackages = {"net.noday"})
@EnableConfigurationProperties(ChrisProperties.class)
public class ChrisConfiguration {

	@Bean
	public Map<String, Object> appCache() {
		return new HashMap<String, Object>();
	}
    
    @Bean
    public AbstractView defaultView() {
    	return new MappingFastjsonJsonView();
    }
    
}
