package br.com.accesstage.trustion.seguranca;

import br.com.accesstage.trustion.seguranca.handler.AuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


import br.com.accesstage.trustion.seguranca.service.UsuarioDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca extends WebSecurityConfigurerAdapter{

	@Autowired
	private UsuarioDetailsService service;
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(service);
        auth.userDetailsService(service).passwordEncoder(service.passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
		web.ignoring().antMatchers("/health", "/health/**", "/health**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf()
		.disable()
		.cors()
		.and()
		.authorizeRequests()
//		.antMatchers("/", "/index.html", "/js/**", "/css/**", "/img/**", "/partials/**", "/favicon.ico","/esqueceuSenha", "/health")
		.antMatchers("/", "/index.html", "/js/**", "/css/**", "/img/**", "/partials/**", "/favicon.ico","/esqueceuSenha", "/health", "/health/**")
		.permitAll()
		.antMatchers(HttpMethod.POST,"/cargavideo").permitAll()
		.antMatchers(HttpMethod.POST,"/cargapac").permitAll()
		.antMatchers(HttpMethod.POST,"/cargavendas").permitAll()
		//.antMatchers(HttpMethod.OPTIONS, "**").permitAll()
		.anyRequest()
		.fullyAuthenticated()
		.and()
		.httpBasic()
		.authenticationEntryPoint(unauthorizedBasicEntryPoint())
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("HEAD",
				"GET", "POST", "PUT", "DELETE", "PATCH"));
		// setAllowCredentials(true) is important, otherwise:
		// The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
		configuration.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		// will fail with 403 Invalid CORS request
		configuration.setAllowedHeaders(Arrays.asList("Authorization",
				"Cache-Control",
				"Content-Range",
				"Origin",
				"Content-Type",
				"Content-Disposition",
				"Content-Length",
				"Access-Control-Allow-Headers",
				"X-Requested-With"));
		configuration.setExposedHeaders(Arrays.asList("Content-Disposition", "Content-Length"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public BasicAuthenticationEntryPoint unauthorizedBasicEntryPoint() {
		return new AuthenticationHandler();
	}
}
