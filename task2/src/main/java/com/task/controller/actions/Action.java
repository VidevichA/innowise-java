package com.task.controller.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Action {
    protected HttpServletRequest req;
    protected HttpServletResponse res;

    protected Action(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
    }

    public abstract void doAction();
}
