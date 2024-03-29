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
public class InvoiceTotal {
    private  double inviceTotal;
    private double allDiscount;
    private double grandTotal;
}
