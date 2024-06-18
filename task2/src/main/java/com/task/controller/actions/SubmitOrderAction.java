package com.task.controller.actions;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubmitOrderAction extends Action {

    public SubmitOrderAction(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    @Override
    public void doAction() {
        String[] products = req.getParameterValues("products");
        double total = 0.0;
        String regex = "\\d+(\\.\\d+)?";
        Pattern pattern = Pattern.compile(regex);

        try {
            for (String product : products) {
                Matcher matcher = pattern.matcher(product);
                if (matcher.find()) {
                    double price = Double.parseDouble(matcher.group());
                    total += price;
                }
            }
            req.getSession().setAttribute("total", total);
            req.getSession().setAttribute("products", req.getParameterValues("products"));
            res.sendRedirect(req.getContextPath() + "/order");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}