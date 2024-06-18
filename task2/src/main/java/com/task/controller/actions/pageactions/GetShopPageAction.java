package com.task.controller.actions.pageactions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.task.controller.actions.Action;
import com.task.store.ProductStore;

public class GetShopPageAction extends Action {

    public GetShopPageAction(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    @Override
    public void doAction() {
        try {
            req.setAttribute("data", ProductStore.getProducts());
            req.getRequestDispatcher("pages/shop.jsp").forward(req, res);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

}
