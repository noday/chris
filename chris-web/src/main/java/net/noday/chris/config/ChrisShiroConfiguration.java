package net.noday.chris.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.ModelAttribute;

import net.noday.core.security.ShiroDbRealm;
import net.noday.core.service.SecurityService;

@Configuration
@AutoConfigureAfter()
public class ChrisShiroConfiguration {
	
	@Bean
	public Realm shiroDbRealm(SecurityService service) {
		ShiroDbRealm shiroDbRealm = new ShiroDbRealm();
		shiroDbRealm.setService(service);
		return shiroDbRealm;
	}
	
//	@Bean
//	public org.apache.shiro.mgt.SecurityManager securityManager(ShiroDbRealm realm, EhCacheManager shiroEhcacheManager) {
//		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//		securityManager.setRealm(realm);
//		securityManager.setCacheManager(shiroEhcacheManager);
//		return securityManager;
//	}
	
	@Bean
	public EhCacheManager shiroEhcacheManager() {
		EhCacheManager shiroEhcacheManager = new EhCacheManager();
		shiroEhcacheManager.setCacheManagerConfigFile("classpath:ehcache/ehcache-shiro.xml");
		return shiroEhcacheManager;
	}
	
//	/**
//	 * 保证实现了Shiro内部lifecycle函数的bean执行
//	 */
//	@Bean
//	public org.apache.shiro.spring.LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//		return new org.apache.shiro.spring.LifecycleBeanPostProcessor();
//	}
	
//	@Bean
//	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
//		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//		return authorizationAttributeSourceAdvisor;
//	}    
	
	//Filter工厂，设置对应的过滤条件和跳转条件
//    @Bean
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(org.apache.shiro.mgt.SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        shiroFilterFactoryBean.setLoginUrl("/login");
//        shiroFilterFactoryBean.setSuccessUrl("/");
//        Map<String, Filter> filters = new HashMap<>();
//        filters.put("authc", new net.noday.core.security.CaptchaFormAuthenticationFilter());
//        shiroFilterFactoryBean.setFilters(filters);
////        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
//        Map<String,String> map = new HashMap<String, String>();
//        map.put("/login","authc");
//        map.put("/logout","logout");
//        map.put("/css/**","anon");
//        map.put("/img/**","anon");
//        map.put("/js/**","anon");
//        map.put("/admin/**","roles[admin]");
//        map.put("/**","anon");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
//        return shiroFilterFactoryBean;
//    }
	
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
    
//    @Bean
//    @DependsOn(value = {"lifecycleBeanPostProcessor"})
//    public org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator DefaultAdvisorAutoProxyCreator() {
//    	return new org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator();
//    }
}
