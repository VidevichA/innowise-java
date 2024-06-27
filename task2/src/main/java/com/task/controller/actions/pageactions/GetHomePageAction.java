package com.task.controller.actions.pageactions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.task.controller.actions.Action;

public class GetHomePageAction extends Action {

    public GetHomePageAction(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    @Override
    public void doAction() {
        try {
            req.getRequestDispatcher("/index.jsp").forward(req, res);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

}
