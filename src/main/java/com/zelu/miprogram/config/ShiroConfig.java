package com.zelu.miprogram.config;

import java.util.LinkedHashMap;
import java.util.Map;
import com.zelu.miprogram.shiro.cache.RedisCachManager;
import com.zelu.miprogram.shiro.cache.SessionManagers;
import com.zelu.miprogram.shiro.matcher.CredentialsMatcher;
import com.zelu.miprogram.shiro.realm.MyRealm;
import com.zelu.miprogram.shiro.token.WebSessionDao;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;

@Configuration
public class ShiroConfig {

	//用户密码加密次数
	@Value("${shiro.password.random}")
	private int randoms;

	// shiro拦截器
	@Bean
	public ShiroFilterFactoryBean shirofilter(@Qualifier("securityManager") SecurityManager manager) {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(manager);
		// 需要的登陆接口
		bean.setLoginUrl("/user/needlogin/");
		// 权限配置信息
		Map<String, String> filtermap = new LinkedHashMap<String, String>();
		// 放行的接口地址
		filtermap.put("/manager_user/login", "anon");//后台用户登陆
		filtermap.put("/user/login", "anon");//微信用户登陆
		filtermap.put("/user/loginout", "anon");//微信用户退出
		filtermap.put("/wechat/**", "anon");//微信小程序

		//自定义拦截器
		//Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
		//bean.setFilters(filters);

		// 剩余的请求shiro都拦截
		filtermap.put("/**/**", "authc");
		bean.setFilterChainDefinitionMap(filtermap);
		return bean;
	}

	//shiro manager
	@Bean("securityManager")
	public DefaultWebSecurityManager getManager(@Qualifier("realms") Realm realms,@Qualifier("sessionManager")SessionManager sessionManager){
		DefaultWebSecurityManager defaultWebSecurityManager =new  DefaultWebSecurityManager();
		defaultWebSecurityManager.setRealm(realms);
		defaultWebSecurityManager.setSessionManager(sessionManager);
		return defaultWebSecurityManager;
	}

	//session会话管理器
	@Bean("sessionManager")
	public SessionManager sessionManager(@Qualifier("sessionDao") SessionDAO dao){
		SessionManagers sessionManager=new SessionManagers();
		//开启session监测有效性 默认开启
		sessionManager.setSessionValidationSchedulerEnabled(true);
		//是否删除无效的 默认开启
		sessionManager.setDeleteInvalidSessions(true);
		//间隔多久检查 session是否有效 单位:毫秒
		sessionManager.setSessionValidationInterval(1800000);
		//session多久实效 单位:毫秒
		sessionManager.setGlobalSessionTimeout(1800000);
		//sessiondao
		sessionManager.setSessionDAO(dao);
		return sessionManager;
	}

	//sessionDao
	@Bean("sessionDao")
	public SessionDAO sessionDAO(){
		WebSessionDao dao= new WebSessionDao(new RedisCachManager());
		//设置登陆用户session的缓存名字
		dao.setActiveSessionsCacheName("login-session");
		return dao;
	}

	//ream
	@Bean("realms")
	public Realm getRealm(@Qualifier("matcher") CredentialsMatcher matcher){
		MyRealm customerRealm=new MyRealm();
		customerRealm.setCredentialsMatcher(matcher);
		//1设置缓存管理=设置EhCache缓存管理器 shiro默认集成
		//customerRealm.setCacheManager(new EhCacheManager());
		//2设置缓存管理=设置Redis缓存管理器 需要实现自定义cache接口
		customerRealm.setCacheManager(new RedisCachManager());
		//开启全局缓存
		customerRealm.setCachingEnabled(true);
		//开启授权缓存
		customerRealm.setAuthorizationCachingEnabled(true);
		//开启认证缓存
		customerRealm.setAuthenticationCachingEnabled(true);
		//自定义认证的名字
		customerRealm.setAuthenticationCacheName("authenticationCacheName");
		//自定义授权的名字
		customerRealm.setAuthorizationCacheName("authorizationCacheName");
		return customerRealm;
	}
	// 密码比较器
	@Bean("matcher")
	public CredentialsMatcher matcher() {
		CredentialsMatcher Matcher = new CredentialsMatcher();
		Matcher.setHashAlgorithmName("MD5");
		Matcher.setHashIterations(randoms);
		return Matcher;
	}

	// spring与shiro的关联
	// shiro开启注解
	@Bean
	public AuthorizationAttributeSourceAdvisor AttributeSourceAdvisor(
			@Qualifier("securityManager") SecurityManager manager) {
		AuthorizationAttributeSourceAdvisor Advisor = new AuthorizationAttributeSourceAdvisor();
		Advisor.setSecurityManager(manager);
		return Advisor;
	}

	// shiro开启AOP代理
	@Bean
	public DefaultAdvisorAutoProxyCreator ProxyCreator() {
		DefaultAdvisorAutoProxyCreator Proxy = new DefaultAdvisorAutoProxyCreator();
		Proxy.setProxyTargetClass(true);
		return Proxy;
	}
}
