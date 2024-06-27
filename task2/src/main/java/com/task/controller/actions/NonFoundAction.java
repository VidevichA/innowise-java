package com.task.controller.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NonFoundAction extends Action {

    public NonFoundAction(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    @Override
    public void doAction() {
        throw new UnsupportedOperationException("Unimplemented method 'doAction1'" + req.getMethod());
    }

}
