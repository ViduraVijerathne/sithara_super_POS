/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllollers;

import appLogger.AppLog;
import appLogger.LogType;
import database.DataBase;
import gui.Dashboard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Units;
import models.User;
import modes.Roles;

/**
 *
 * @author vidur
 */
public class AdminController {

    private Auth auth;

    public AdminController(Auth auth) {
        this.auth = auth;
    }

    public List<User> getCashiers() {
        List<User> cashiers = new ArrayList<>();
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "SELECT * FROM users WHERE users.role = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, Roles.CASHIER.name());
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                User user = new User();
                user.setEmail(results.getString("email"));
                user.setName(results.getString("name"));
                user.setPassword(results.getString("password"));
                user.setRole(Roles.valueOf(results.getString("role")));
                cashiers.add(user);
            }

        } catch (SQLException ex) {
            appLogger.WriteLog.log(new AppLog(LocalDateTime.now().toString(), DashboardController.class.getName(), ex.getMessage(),auth.getRole().name(), LogType.ERROR));
            
        }
        return cashiers;
    }

    public void deleteCashier(User selectedCashier) throws SQLException {

        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "DELETE FROM users WHERE email = ? and role = ? ";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, selectedCashier.getEmail());
        statement.setString(2, Roles.CASHIER.name());
        statement.execute();
    }

    public void addCashier(User cashier) throws SQLException {
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "INSERT INTO users (`email`, `password`, `role`, `name`) VALUES (? , ? ,?,?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, cashier.getEmail());
        statement.setString(2, cashier.getPassword());
        statement.setString(3, Roles.CASHIER.name());
        statement.setString(4, cashier.getName());
        statement.execute();
    }
    
    public List<Units> getAllUnits() {
        List<Units> unitsList = new ArrayList<>();
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "SELECT * FROM units";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                Units unit = new Units();
                unit.setId(results.getInt("idunits"));
                unit.setUnit(results.getString("unit"));
                System.out.println(unit.toString());
                unitsList.add(unit);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return unitsList;
    }

    public void addUnit(Units u)throws SQLException {
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "INSERT INTO units (`unit`) VALUES (?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, u.getUnit());
        statement.execute();
    
    }

    public void updateUnit(Units selectedUnit)throws SQLException {
        DataBase instance = DataBase.getInstance();
        System.out.println(selectedUnit.toString());
        Connection connection = instance.getConnection();
        String query = "UPDATE `units` SET `unit`=? WHERE  `idunits`=?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, selectedUnit.getUnit());
        statement.setInt(2, selectedUnit.getId());
        statement.execute();
        
    }

}
