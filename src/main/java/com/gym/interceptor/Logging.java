package com.gym.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class Logging implements HandlerInterceptor {
	
	public boolean preHandle(
			  HttpServletRequest req,
			  HttpServletResponse response, 
			  Object handler) throws Exception {
		System.out.println("Oke la"+response.getStatus());
		 Enumeration<String> parameterNames = req.getParameterNames();
		 
	        while (parameterNames.hasMoreElements()) {
	 
	            String paramName = parameterNames.nextElement();
	            
	 
	            String[] paramValues = req.getParameterValues(paramName);
	            for (int i = 0; i < paramValues.length; i++) {
	                String paramValue = paramValues[i];
	                System.out.println("Log Value: "+paramValue);
	            }
	 
	        }
		 	return true;
	}
	public void postHandle(
			  HttpServletRequest request, 
			  HttpServletResponse response,
			  Object handler, 
			  ModelAndView modelAndView) throws Exception {
			    // your code
			}
	public void afterCompletion(
			  HttpServletRequest request, 
			  HttpServletResponse response,
			  Object handler, Exception ex) {
			    // your code
			}
		
}
