package com.spring.cemstools;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
public class CemstoolsApplication implements WebMvcConfigurer{/*WebMvcConfigurer para activar la interceptacion del cambio de idiomas*/

	public static void main(String[] args) {
		SpringApplication.run(CemstoolsApplication.class, args);
	}

	//Este bloque es por la internacionalizacion
	 @Bean
	  public LocaleResolver localeResolver() {
	    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
	    localeResolver.setDefaultLocale(Locale.getDefault());
	    return localeResolver;
	  }
	
	  @Bean
	  public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
	    localeInterceptor.setIgnoreInvalidLocale(true);
	    localeInterceptor.setParamName("lang");//nombre del parametro que usaremos para seleccionar el idioma
	    return localeInterceptor;
	  }
	
	  @Override
	  public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(localeChangeInterceptor());
	  }
	  
	  //Fin de la internacionalizacion
	  
}
