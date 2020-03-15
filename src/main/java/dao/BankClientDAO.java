package dao;

import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {
    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        String sql = "select * from bank_client";
        List<BankClient> list = new ArrayList<>();
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                BankClient client = new BankClient();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setPassword(rs.getString("password"));
                client.setMoney(rs.getLong("money"));
                list.add(client);
            }
            return list;
        }
    }

    public boolean validateClient(String name, String password) throws SQLException {
        String query = "select * from bank_client where name = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, password);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                String clientName = result.getString("name");
                String clientPassword = result.getString("password");

                if (clientName.equals(name) & clientPassword.equals(password)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws SQLException {
        if (validateClient(name, password)) {
            PreparedStatement pstm = connection.prepareStatement("select * from bank_client where name=?");
            pstm.setString(1, name);
            ResultSet resultSet = pstm.executeQuery();
            resultSet.next();
            Long money = resultSet.getLong(4);
            money += transactValue;

            pstm = connection.prepareStatement("UPDATE bank_client SET money = ? WHERE name LIKE ?");
            pstm.setLong(1, money);
            pstm.setString(2, name);
            pstm.executeUpdate();
            resultSet.close();
            pstm.close();
        }
    }

    public BankClient getClientById(long id) throws SQLException {
        BankClient bankClient = null;
        String query = "select from bank_client where id=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, id);
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                Long clientById = result.getLong("id");
                String clientName = result.getString("name");
                String clientPassword = result.getString("password");
                Long clientMoney = result.getLong("money");

                bankClient = new BankClient(clientById, clientName, clientPassword, clientMoney);
            }
        }
        return bankClient;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            String sql = "select * from bank_client where name = '" + name + "'";
            ResultSet result = stmt.executeQuery(sql);
            if (result.next()) {
                result.getString("name");
                Long money = result.getLong("money");
                if (money >= expectedSum) {
                    return true;
                }
            }
            return false;
        }
    }

    public long getClientIdByName(String name) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("select * from bank_client where name='" + name + "'");
            ResultSet result = stmt.getResultSet();
            if (result.next());
                return result.getLong(1);
        }
    }

    public BankClient getClientByName(String name) throws SQLException{
        BankClient bankClient = new BankClient();
        String query = "select * from bank_client where name=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1,name);
            ResultSet result = stmt.executeQuery();
            if(result.next()){
                long id = result.getLong ("id");
                String clientName = result.getString(2);
                String clientPassword = result.getString("password");
                Long clientMoney = result.getLong("money");

                bankClient.setId(id);
                bankClient.setName(clientName);
                bankClient.setPassword(clientPassword);
                bankClient.setMoney(clientMoney);
            }
        }
        return bankClient;
        }

        public void addClient(BankClient client)throws SQLException{
        String query = "INSERT INTO bank_client(id, name , password, money) VALUES(0,?,?,?)";
           try(PreparedStatement stmt = connection.prepareStatement(query)) {
               stmt.setLong(0,  client.getId());
               stmt.setString(1, client.getName());
               stmt.setString(2, client.getPassword());
               stmt.setLong(3,   client.getMoney());
               stmt.executeUpdate();
           }
        }
        public void createTable()throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client(id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key(id))");
        stmt.close();
    }
    public void dropTable()throws SQLException{
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}
