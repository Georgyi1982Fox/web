package servlet;

import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
@WebServlet("/transaction")
public class MoneyTransactionServlet extends HttpServlet {
    BankClientService bankClientService = new BankClientService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(200);
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", pageVariables));
    }
    @Override
    protected void doPost(HttpServletRequest req , HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> mapResult = new HashMap<>();
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        String moneyCount = req.getParameter("count");
        Long clientCount = Long.parseLong(moneyCount);
        String nameTo = req.getParameter("nameTo");
        BankClient sender = new BankClient(senderName, senderPass , clientCount);
        String message = "";
        try {
            if(bankClientService.sendMoneyToClient(sender,nameTo,clientCount)){
                    mapResult.put("message", "The transaction was successful");
                }else{
                    mapResult.put("message", "transaction rejected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        resp.getWriter ( ).println (PageGenerator.getInstance().getPage("resultPage.html", mapResult));
                    resp.setContentType ("text/html;charset=utf-8");
                    resp.setStatus (HttpServletResponse.SC_OK);
                  }
        }

