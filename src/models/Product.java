/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
/**
 *
 * @author vidur
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    private int id;
    private String barcode;
    private double retail_price;
    private double wholesale_price;
    private double discount;
    private Units unit;
    private String sinhalaName;
    private String name;
    private int is_active;
    private double qty;
    
}
