package com.gym.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.gym.interceptor.CheckAllSite;
import com.gym.interceptor.CheckSession;
import com.gym.interceptor.Menu;
import com.gym.interceptor.Menu1;
import com.gym.interceptor.Logging;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	// Cấu hình để sử dụng các file nguồn tĩnh (html, image, ..)
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
		registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
		registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
		registry.addResourceHandler("/include/**").addResourceLocations("/js/").setCachePeriod(31556926);
	}

	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	//
	public void addInterceptors(InterceptorRegistry registry) {

		// LogInterceptor áp dụng cho mọi URL.

		registry.addInterceptor(new CheckAllSite()).addPathPatterns("/**");
		registry.addInterceptor(new Logging()).addPathPatterns("/**");
		//hiển thị list trang home
		registry.addInterceptor(new Menu()).addPathPatterns("/*");
		//check sau khi trang admin tên url đều đặt sau /manager/
		registry.addInterceptor(new CheckSession()).addPathPatterns("/manager/*");
		//hiển thị list trang admin
		registry.addInterceptor(new Menu1()).addPathPatterns("/manager/*");
	}

}