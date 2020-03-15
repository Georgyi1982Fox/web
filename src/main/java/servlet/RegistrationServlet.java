package servlet;

import exception.DBException;
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
@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    BankClientService bankClientService = new BankClientService ( );

    @Override
    protected void doGet(HttpServletRequest req , HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> mapResult = new HashMap<>();
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(200);
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", mapResult));
    }
    @Override
    protected void doPost(HttpServletRequest req , HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> mapResult = new HashMap<>();
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String mon = req.getParameter("money");
        Long money = Long.parseLong(mon);
        BankClient bankClient = new BankClient(name , password , money);
        resp.setContentType ("text/html");
        String message = "";
        try {
            if (new BankClientService().addClient(bankClient)){
                mapResult.put("message", "Add client successful");
            }else{
                mapResult.put("message", "Client not add");
            }
        }catch (SQLException | DBException e) {
            e.printStackTrace ( );
        }
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", mapResult));
            resp.setContentType ("text/html;charset=utf-8");
            resp.setStatus (HttpServletResponse.SC_OK);
    }
}


