/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import modes.Roles;

/**
 *
 * @author vidur
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String email;
    private String password;
    private Roles role;
    private String name;
    private int is_active;
}
