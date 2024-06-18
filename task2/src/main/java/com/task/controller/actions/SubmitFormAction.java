package com.task.controller.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubmitFormAction extends Action {

    public SubmitFormAction(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    @Override
    public void doAction() {
        try {
            String name = req.getParameter("name");
            if (name == null || name.isEmpty()) {
                name = "anonymous";
            }
            req.getSession().setAttribute("name", name);
            res.sendRedirect(req.getContextPath() + "/shop");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
