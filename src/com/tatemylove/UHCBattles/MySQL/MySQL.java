package com.tatemylove.UHCBattles.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MySQL {
    public static Connection connection;
    public MySQL(String ip, String userName, String password, String db){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + "/" + db + "?user=" + userName + "&password=" + password);
            createWinsTable();
            createKillsTable();
            createDeathsTable();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void createWinsTable() throws Exception{
        PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS UHCwins(uuid varchar(36) NOT NULL, wins int)");
        ps.executeUpdate();
        ps.close();
    }
    private void createKillsTable() throws Exception{
        PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS UHCkills(uuid varchar(36) NOT NULL, kills int");
        ps.executeUpdate();
        ps.close();
    }
    private void createDeathsTable() throws Exception{
        PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS UHCdeaths(uuid varchar(36) NOT NULL, deaths int");
        ps.executeUpdate();
        ps.close();
    }
}
