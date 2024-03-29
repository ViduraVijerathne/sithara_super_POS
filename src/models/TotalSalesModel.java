/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author vidur
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Data
@Getter
public class TotalSalesModel {

    private Product product;

    private double totalSales;
    private double totalRevenue;
    private double totalSaleQty;

    private double monthly_TotalSales;
    private double monthly_TotalRevenue;
    private double monthly_TotalSaleQty;

    private double weekly_TotalSales;
    private double weekly_TotalREvenue;
    private double weekly_TotalSaleQty;

    private double today_TotalSales;
    private double today_TotalRevenue;
    private double today_TotalSaleQty;

}
