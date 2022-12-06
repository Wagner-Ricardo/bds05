package com.devsuperior.movieflix.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig  extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenStore tokenStore;
	
	private static final String[] PUBLIC = {"/oauth/token","/h2-console/**"};
	private static final String[] VISITOR_AND_MEMBER_GET= { "/departments/**", "/employees/**"};
	private static final String[] MEMBER_INSERT = {"/users/**"};
	
	@Override
	public void configure (ResourceServerSecurityConfigurer resources) throws Exception{
		resources.tokenStore(tokenStore);
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception{
		
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		// configurando as rotas dos perfis dos tipos usuários
		http.authorizeRequests()
		.antMatchers(PUBLIC).permitAll()
		.antMatchers(HttpMethod.GET, VISITOR_AND_MEMBER_GET ).hasAnyRole("MEMBER","VISITOR")
		.antMatchers(HttpMethod.POST, MEMBER_INSERT ).hasAnyRole("MEMBER")
		//.anyRequest().hasAnyRole("MEMBER")
		.anyRequest().authenticated();
	}

}
