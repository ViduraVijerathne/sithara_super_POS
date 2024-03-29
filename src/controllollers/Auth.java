/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllollers;

import database.DataBase;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;

import modes.Roles;

/**
 *
 * @author vidur
 */
public class Auth {

    private User user;
    private String devEmail = "vertex@dev.com";
    private String devPassword = "6jfmd672@V";
    private boolean isAuth = false;

    public Auth(User user) {
        this.user = user;
    }

    public boolean isAuthUser() throws SQLException {
//        check developer credintials;
        if ((user.getEmail() == null ? devEmail == null : user.getEmail().equals(devEmail)) && (user.getPassword() == null ? devPassword == null : user.getPassword().equals(devPassword))) {
            user.setRole(Roles.DEVELOPER);
            isAuth = true;
            return true;
        } //        check other chredintials;
        else {
//            String query = "SELECT * FROM `users` WHERE `email` = '" + user.getEmail() + "' AND `password` = '" + user.getPassword() + "' AND `role` = '" + user.getRole() + "' AND `is_active` = 1";
            String query = "SELECT * FROM `users` WHERE `email` = ? AND `password` = ? AND `role` = ? AND `is_active` = 1";

            DataBase database = DataBase.getInstance();
            Connection connection = database.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());

            ResultSet result = statement.executeQuery();

//            ResultSet result = database.DataBase.SELECT(query);
//            System.out.println(query);
            if (result.next()) {
                user.setName(result.getString("name"));
                user.setRole(Roles.valueOf(result.getString("role")));
                System.out.println(result.getString("role"));
                isAuth = true;
                return true;

            } else {

                System.out.println("invalid cred");
                return false;

            }
        }

    }

    public boolean isDev() {
        if (!isAuth) {
            try {
                isAuthUser();

            } catch (SQLException ex) {

            }
        }
      
        return isAuth && user.getRole().equals(Roles.DEVELOPER);
        
    }
    
    public boolean isCashier() {
        if (!isAuth) {
            try {
                isAuthUser();

            } catch (SQLException ex) {

            }
        }
      
        return isAuth && user.getRole().equals(Roles.CASHIER);
        
    }
    public boolean isAdmin() {
        if (!isAuth) {
            try {
                isAuthUser();

            } catch (SQLException ex) {

            }
        }
      
        return isAuth && user.getRole().equals(Roles.ADMIN);
        
    }
    
    public Roles getRole(){
        return user.getRole();
    }
    

}
