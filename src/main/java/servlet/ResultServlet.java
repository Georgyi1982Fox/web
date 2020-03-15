package servlet;

import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResultServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map <String, Object> pageVariables = new HashMap <> ();
        pageVariables.put("message", "");
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(200);
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html",pageVariables));
    }
}