package com.deadpineapple.front.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.deadpineapple.front.Forms.LoginForm;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor  {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // Avoid a redirect loop for some urls
        if( !request.getRequestURI().equals("/index") &&
                !request.getRequestURI().equals("/user/login") &&
                !request.getRequestURI().startsWith("/resources") &&
                !request.getRequestURI().equals("/user/add") &&
                !request.getRequestURI().startsWith("/upload") &&
                !request.getRequestURI().equals("/user/login.failed"))
        {
            LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
            if(userData == null)
            {
                response.sendRedirect("/index");
                return false;
            }
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
    //override postHandle() and afterCompletion()
}

