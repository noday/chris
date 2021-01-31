package net.noday.chris.config;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.noday.chris.interceptor.SideDataInterceptor;
import net.noday.core.security.CaptchaFormAuthenticationFilter;
import net.noday.core.security.ShiroDbRealm;
import net.noday.core.security.freemarker.ShiroTags;
import net.noday.core.service.SecurityService;

@Configuration
public class ChrisShiroConfiguration implements WebMvcConfigurer {
	
	@Autowired private SideDataInterceptor sideDataInterceptor;
	@Bean
	public Realm shiroDbRealm(SecurityService service) {
		ShiroDbRealm shiroDbRealm = new ShiroDbRealm();
		shiroDbRealm.setService(service);
		return shiroDbRealm;
	}
	
	@Bean
	public CaptchaFormAuthenticationFilter authc() {
		return new CaptchaFormAuthenticationFilter();
	}
	
	@Bean
	public EhCacheManager shiroEhcacheManager() {
		EhCacheManager shiroEhcacheManager = new EhCacheManager();
		shiroEhcacheManager.setCacheManagerConfigFile("classpath:ehcache/ehcache-shiro.xml");
		return shiroEhcacheManager;
	}
	
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/login", "authc"); // need to accept POSTs from the login form
        chainDefinition.addPathDefinition("/logout", "logout");
        chainDefinition.addPathDefinition("/css/**","anon");
        chainDefinition.addPathDefinition("/img/**","anon");
        chainDefinition.addPathDefinition("/js/**","anon");
        chainDefinition.addPathDefinition("/admin/**","roles[admin]");
        chainDefinition.addPathDefinition("/**","anon");
        return chainDefinition;
    }
    
    @ModelAttribute(name = "subject")
    public Subject subject() {
        return SecurityUtils.getSubject();
    }
    
    @Bean
    public ShiroTags shiroTags(freemarker.template.Configuration configuration) {
    	ShiroTags tags = new ShiroTags();
    	configuration.setSharedVariable("shiro", tags);
    	return tags;
    }

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sideDataInterceptor);
		WebMvcConfigurer.super.addInterceptors(registry);
	}
    
}
