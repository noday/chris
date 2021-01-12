package net.noday.chris.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.view.AbstractView;

import net.noday.chris.props.ChrisProperties;
import net.noday.core.security.ShiroDbRealm;
import net.noday.core.service.SecurityService;
import net.noday.core.web.MappingFastjsonJsonView;

@Configuration
@EnableConfigurationProperties(ChrisProperties.class)
public class ChrisConfiguration {

	@Bean
	public Map<String, Object> appCache() {
		return new HashMap<String, Object>();
	}
	
	@Bean
	public net.noday.core.security.ShiroDbRealm shiroDbRealm(SecurityService service) {
		ShiroDbRealm shiroDbRealm = new ShiroDbRealm();
		shiroDbRealm.setService(service);
		return shiroDbRealm;
	}
	
	@Bean
	public org.apache.shiro.mgt.SecurityManager securityManager(ShiroDbRealm realm, EhCacheManager shiroEhcacheManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(realm);
		securityManager.setCacheManager(shiroEhcacheManager);
		return securityManager;
	}
	
	@Bean
	public EhCacheManager shiroEhcacheManager() {
		EhCacheManager shiroEhcacheManager = new EhCacheManager();
		shiroEhcacheManager.setCacheManagerConfigFile("classpath:ehcache/ehcache-shiro.xml");
		return shiroEhcacheManager;
	}
	
	/**
	 * 保证实现了Shiro内部lifecycle函数的bean执行
	 */
	@Bean
	public org.apache.shiro.spring.LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new org.apache.shiro.spring.LifecycleBeanPostProcessor();
	}
	
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}    
	
	//Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/");
        Map<String, Filter> filters = new HashMap<>();
        filters.put("authc", new net.noday.core.security.CaptchaFormAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filters);
//        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        Map<String,String> map = new HashMap<String, String>();
        map.put("/login","authc");
        map.put("/logout","logout");
        map.put("/css/**","anon");
        map.put("/img/**","anon");
        map.put("/js/**","anon");
        map.put("/admin/**","roles[admin]");
        map.put("/**","anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }
    
    @Bean
    @DependsOn(value = {"lifecycleBeanPostProcessor"})
    public org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator DefaultAdvisorAutoProxyCreator() {
    	return new org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator();
    }
    
    @Bean
    public AbstractView defaultView() {
    	return new MappingFastjsonJsonView();
    }
    
}
