package com.gym.interceptor;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CheckSession implements HandlerInterceptor {
	public boolean preHandle(
			  HttpServletRequest request,
			  HttpServletResponse response, 
			  Object handler) throws Exception {
			//Nếu đăng nhập thành công hoặc xảy ra các ngoại lệ trong xử lý admin thì luôn trở về trang chủ file trangchu.jsp 
				try {
					if(request.getSession().getAttribute("username").equals(null)) {
						response.sendRedirect("/GymManager/home");
					}else return true;
				}
				catch(Exception e) {
					response.sendRedirect("/GymManager/home");
					
				}
				return false;
				
			}
	public void postHandle(
			  HttpServletRequest request, 
			  HttpServletResponse response,
			  Object handler, 
			  ModelAndView modelAndView) throws Exception {
			    		  
			}
	public void afterCompletion(
			  HttpServletRequest request, 
			  HttpServletResponse response,
			  Object handler, Exception ex) {
			    // your code
			}
}
