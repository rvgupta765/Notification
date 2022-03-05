package com.zappos.iitc.notifications.interceptor;

import com.zappos.iitc.commons.account.domain.Employee;
import com.zappos.iitc.commons.account.service.ExternalEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author RaviGupta
 * This intercepts every notificationRequest and adds Employee object to the notificationRequest.
 */
@Component
public class NotificationInterceptor implements HandlerInterceptor {

    //unimplemented methods comes here. Define the following method so that it
    //will handle the request before it is passed to the controller.

    @Autowired
    ExternalEmployeeService externalEmployeeService;

    public static final String EMPLOYEE_ATTR = "notificableEmployee";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //your custom logic here.
        request.setAttribute("user-token", request.getHeader("x-auth-token"));
        if (request.getHeader("x-auth-token") != null) {
            Employee emp = externalEmployeeService.getEmployeeDetails(request.getHeader("x-auth-token"));
            if (emp != null) {
                request.setAttribute(EMPLOYEE_ATTR, emp);
            }
        }
        return true;
    }
}