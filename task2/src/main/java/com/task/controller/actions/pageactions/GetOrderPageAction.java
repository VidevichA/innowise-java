package com.task.controller.actions.pageactions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.task.controller.actions.Action;

public class GetOrderPageAction extends Action {

    public GetOrderPageAction(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    @Override
    public void doAction() {
        try {
            req.getRequestDispatcher("pages/order.jsp").forward(req, res);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

}
