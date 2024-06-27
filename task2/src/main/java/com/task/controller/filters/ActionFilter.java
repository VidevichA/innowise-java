package com.task.controller.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.task.controller.actions.Action;
import com.task.controller.actions.NonFoundAction;
import com.task.controller.actions.SubmitFormAction;
import com.task.controller.actions.SubmitOrderAction;
import com.task.controller.actions.pageactions.GetHomePageAction;
import com.task.controller.actions.pageactions.GetOrderPageAction;
import com.task.controller.actions.pageactions.GetShopPageAction;

public class ActionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req;
        HttpServletResponse res;

        if (request instanceof HttpServletRequest) {
            req = (HttpServletRequest) request;
            res = (HttpServletResponse) response;
        } else {
            return;
        }
        String uri = req.getRequestURI();
        req.setAttribute("url", uri);
        Action action;
        switch (uri) {
            case "/task2/":
                if ("GET".equals(req.getMethod())) {
                    action = new GetHomePageAction(req, res);
                } else if ("POST".equals(req.getMethod())) {
                    action = new SubmitFormAction(req, res);
                } else {
                    action = new NonFoundAction(req, res);
                }
                break;
            case "/task2/shop":
                if ("GET".equals(req.getMethod())) {
                    action = new GetShopPageAction(req, res);
                } else if ("POST".equals(req.getMethod())) {
                    action = new SubmitOrderAction(req, res);
                } else {
                    action = new NonFoundAction(req, res);
                }
                break;
            case "/task2/order":
                action = "GET".equals(req.getMethod()) ? new GetOrderPageAction(req, res)
                        : new NonFoundAction(req, res);
                break;
            default:
                action = new NonFoundAction(req, res);
                break;
        }
        req.setAttribute("action", action);
        req.getRequestDispatcher("/MainServlet").forward(req, res);
    }

    @Override
    public void destroy() {
    }

}
