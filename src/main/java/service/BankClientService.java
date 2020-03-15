package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    private static BankClientDAO getBankClientDAO(){
        return new BankClientDAO(getMysqlConnection());
    }

    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e){
            throw new DBException(e);
        }
    }

    public boolean deleteClient(String name) throws SQLException {
        List <BankClient> list = getBankClientDAO().getAllBankClient();
        list.remove(getBankClientDAO().getClientByName(name));
        return true;
    }
    public BankClient getClientByName(String name) throws SQLException {
            return getBankClientDAO().getClientByName(name);
        }

    public boolean addClient(BankClient client) throws DBException, SQLException {
        //BankClient clientName = (client.getName());
        if (!getBankClientDAO().validateClient(client.getName(),client.getPassword())) {
            try {
                getBankClientDAO().addClient(client);
            } catch (SQLException e) {
                throw new DBException(e);
            }
            return true;
        }
        return false;
    }

    public List <BankClient> getAllClient() throws DBException {
        try {
            return getBankClientDAO().getAllBankClient();
        } catch (SQLException e){
            throw new DBException(e);
        }
    }
    public boolean sendMoneyToClient(BankClient sender, String name , Long value) throws SQLException {
            BankClient clientReceiver = getBankClientDAO().getClientByName(name);
            boolean existClient = getBankClientDAO().validateClient(clientReceiver.getName(), clientReceiver.getPassword());
            if(getBankClientDAO().validateClient(sender.getName(), sender.getPassword()) & getBankClientDAO ().isClientHasSum (sender.getName(), value)) {
                if(existClient){
                    if(value > 0){
                        try {
                            getBankClientDAO().updateClientsMoney(sender.getName(), sender.getPassword() , -value);
                            getBankClientDAO().updateClientsMoney(clientReceiver.getName() , clientReceiver.getPassword(), value);
                            return true;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return false;
        }

    public void cleanUp()throws DBException{
            BankClientDAO dao = getBankClientDAO();
            try {
                dao.dropTable();
            }catch (SQLException e){
                throw new DBException(e);
            }
        }
        public void createTable()throws DBException{
            BankClientDAO dao = getBankClientDAO();
            try {
                dao.createTable();
            }catch (SQLException e){
                throw new DBException(e);
            }
        }
        private static Connection getMysqlConnection(){
            try {
                DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());
                StringBuilder url = new StringBuilder();
                url.
                        append ("jdbc:mysql://").        //db type
                        append ("localhost:").           //host name
                        append ("3306/").                //port
                        append ("db_example?").          //db name
                        append ("user=root&").          //login
                        append ("password=hjk_Esk1982!").   //password
                        append("&serverTimezone=UTC");  //timezone

                System.out.println("URL: " + url + "\n");
                Connection connection = DriverManager.getConnection(url.toString());
                return connection;
            }catch(SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e){
                e.printStackTrace();
                throw new IllegalStateException();
            }
        }
    }



