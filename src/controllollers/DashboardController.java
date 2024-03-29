/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllollers;

import database.DataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Units;
import java.sql.ResultSet;
import java.util.Set;
import models.Product;
import java.sql.Statement;
import models.TotalSalesModel;

/**
 *
 * @author vidur
 */
public class DashboardController {

    Auth auth;

    public DashboardController(Auth auth) {
        this.auth = auth;
    }

    public Auth getAuth() {
        return this.auth;
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

    public Product findProductByID(int id) {
        Product product = null;
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "SELECT * FROM product WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                product = new Product();
                product.setId(results.getInt("id"));
                product.setBarcode(results.getString("barcode"));
                product.setRetail_price(results.getDouble("retail_price"));
                product.setDiscount(results.getDouble("discount"));
                product.setName(results.getString("name"));
                product.setSinhalaName(results.getString("si_name"));
                product.setWholesale_price(results.getDouble("wholesale_price"));
                product.setIs_active(results.getInt("is_active"));
                // You need to set the unit and sinhalaName fields too
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return product;

    }

    public Product findProductByBarcode(String barcode) {
        Product product = null;
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "SELECT * FROM product WHERE barcode = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, barcode);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                product = new Product();
                product.setId(results.getInt("id"));
                product.setBarcode(results.getString("barcode"));
                product.setRetail_price(results.getDouble("retail_price"));
                product.setDiscount(results.getDouble("discount"));
                product.setName(results.getString("name"));
                product.setWholesale_price(results.getDouble("wholesale_price"));
                product.setIs_active(results.getInt("is_active"));
                product.setSinhalaName(results.getString("si_name"));
                // You need to set the unit and sinhalaName fields too
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return product;
    }

    public void addProduct(Product product) throws Exception {
        if (!product.getBarcode().isEmpty()) {
            if (findProductByBarcode(product.getBarcode()) != null) {
                throw new Exception("Product with barcode " + product.getBarcode() + " already exists");
            }
        }

        if (findProductByName(product.getName()) != null) {
            throw new Exception("Product with name " + product.getName() + " already exists");
        } else {
            // If the product does not exist, add it to the database
            DataBase instance = DataBase.getInstance();
            Connection connection = instance.getConnection();
            String query = "INSERT INTO `product` (`barcode`, `retail_price`, `discount`, `name`, `wholesale_price`, `units_idunits`, `si_name`) VALUES (?,?,?,?,?,?,?)";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, product.getBarcode());
                statement.setDouble(2, product.getRetail_price());
                statement.setDouble(3, product.getDiscount());
                statement.setString(4, product.getName());
                statement.setDouble(5, product.getWholesale_price());
                statement.setInt(6, product.getUnit().getId());
                statement.setString(7, product.getSinhalaName());
                System.out.println(product.toString());
                statement.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
                throw new Exception("Failed to add product to the database");
            }

        }
    }

    public void addProductUpdate(Product product) throws Exception {

        if (!product.getBarcode().isBlank()) {
            if (findProductByBarcode(product.getBarcode()) != null) {
                throw new Exception("Product with barcode " + product.getBarcode() + " already exists");
            }
        }
        // If the product does not exist, add it to the database
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "INSERT INTO `product` (`barcode`, `retail_price`, `discount`, `name`, `wholesale_price`, `units_idunits`, `si_name`) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, product.getBarcode());
            statement.setDouble(2, product.getRetail_price());
            statement.setDouble(3, product.getDiscount());
            statement.setString(4, product.getName());
            statement.setDouble(5, product.getWholesale_price());
            statement.setInt(6, product.getUnit().getId());
            statement.setString(7, product.getSinhalaName());
            System.out.println(product.toString());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Failed to add product to the database");
        }

    }

    public Product findProductByName(String name) {
        Product product = null;
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "SELECT * FROM product WHERE name = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                product = new Product();
                product.setId(results.getInt("id"));
                product.setBarcode(results.getString("barcode"));
                product.setRetail_price(results.getDouble("retail_price"));
                product.setDiscount(results.getDouble("discount"));
                product.setName(results.getString("name"));
                product.setSinhalaName(results.getString("si_name"));
                product.setWholesale_price(results.getDouble("wholesale_price"));
                product.setIs_active(results.getInt("is_active"));
                // You need to set the unit field too if it exists in the database
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "SELECT * FROM product  INNER JOIN units ON product.units_idunits = units.idunits WHERE is_active = '1'";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Product product = new Product();
                product.setId(results.getInt("id"));
                product.setBarcode(results.getString("barcode"));
                product.setRetail_price(results.getDouble("retail_price"));
                product.setDiscount(results.getDouble("discount"));
                product.setName(results.getString("name"));
                product.setSinhalaName(results.getString("si_name"));
                product.setWholesale_price(results.getDouble("wholesale_price"));
                product.setIs_active(results.getInt("is_active"));

                Units unit = new Units();
                unit.setId(results.getInt("idunits"));
                unit.setUnit(results.getString("unit"));
                product.setUnit(unit);

                productList.add(product);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productList;
    }

    public List<Product> getAllProducts(boolean b) {
        List<Product> productList = new ArrayList<>();
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "SELECT * FROM product  INNER JOIN units ON product.units_idunits = units.idunits";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                Product product = new Product();
                product.setId(results.getInt("id"));
                product.setBarcode(results.getString("barcode"));
                product.setRetail_price(results.getDouble("retail_price"));
                product.setDiscount(results.getDouble("discount"));
                product.setName(results.getString("name"));
                product.setSinhalaName(results.getString("si_name"));
                product.setWholesale_price(results.getDouble("wholesale_price"));
                product.setIs_active(results.getInt("is_active"));

                Units unit = new Units();
                unit.setId(results.getInt("idunits"));
                unit.setUnit(results.getString("unit"));
                product.setUnit(unit);

                productList.add(product);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productList;
    }

    private double calculateGrandTotal(Set<Product> products) {
        double grandTotal = 0;

        for (Product product : products) {
            grandTotal += calculateGrandTotal(product);
        }

        return grandTotal;
    }

    private double calculateGrandTotal(Product product) {
        double productPrice = product.getRetail_price();
        double discount = product.getDiscount();
        double qty = product.getQty();

        return (productPrice - discount) * qty;
    }

    private int saveInvoice(double grandTotal) throws SQLException {
        int invoiceId;

        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();

//        Save invoice
        String query = "INSERT INTO `invoice` (`grand_total`, `date_time`) VALUES (?, now());";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setDouble(1, grandTotal);
        statement.executeUpdate();

        // Get the ID of the last inserted invoice
        try ( ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                invoiceId = generatedKeys.getInt(1);
                statement.close();
            } else {
                throw new SQLException("Creating invoice failed, no ID obtained.");
            }
        }

        return invoiceId;
    }

    private int addProductToInvoice(Product product) throws SQLException {
        int productInvoiceID = 1;

        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();

        String query = "INSERT INTO `invoice_product` (`price`, `discount`, `quantity`, `total`, `product_id`, `product_barcode`) VALUES (?, ?, ?, ?, ?,?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setDouble(1, product.getRetail_price());
        statement.setDouble(2, product.getDiscount());
        statement.setDouble(3, product.getQty());
        statement.setDouble(4, calculateGrandTotal(product));
        statement.setInt(5, product.getId());
        statement.setString(6, product.getBarcode());
        statement.executeUpdate();

        // Get the ID of the last inserted invoice
        try ( ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                productInvoiceID = generatedKeys.getInt(1);
                statement.close();
            } else {
                throw new SQLException("Creating invoice failed, no ID obtained.");
            }
        }
        return productInvoiceID;
    }

    private void linkInvoiceAndProduct(int invoiceID, int productInvoiceID) throws SQLException {
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();

//        Save invoice
        String query = "INSERT INTO `invoice_product_has_invoice` (`invoice_product_invoice_product_id`, `invoice_idinvoice`) VALUES (?, ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, productInvoiceID);
        statement.setInt(2, invoiceID);
        statement.executeUpdate();
        statement.close();
    }

    public int addInvoice(Set<Product> products) throws SQLException {
        int invoiceID = saveInvoice(calculateGrandTotal(products));
        List<Integer> productInvoiceIDs = new ArrayList<>();

        for (Product product : products) {
            int productInvoiceId = addProductToInvoice(product);
            System.out.println("invoice product ID" + productInvoiceId);
            productInvoiceIDs.add(productInvoiceId);
        }

        for (int productInvoiceID : productInvoiceIDs) {
            linkInvoiceAndProduct(invoiceID, productInvoiceID);
        }
        System.out.println("invoiceID" + invoiceID);
        return invoiceID;
    }

    public TotalSalesModel getProductTodayInsight(Product product) throws SQLException {
        double totalSales = 0;
        double totalRev = 0;
        double totalQty = 0;
        TotalSalesModel totalSalesModel = new TotalSalesModel();
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "SELECT * FROM invoice \n"
                + "INNER JOIN invoice_product_has_invoice\n"
                + "ON invoice.idinvoice = invoice_product_has_invoice.invoice_idinvoice\n"
                + "\n"
                + "INNER JOIN invoice_product\n"
                + "ON invoice_product.invoice_product_id = invoice_product_has_invoice.invoice_product_invoice_product_id\n"
                + "\n"
                + "\n"
                + "\n"
                + " WHERE product_id = ? AND DATE(date_time) = CURDATE()";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, product.getId());
        ResultSet results = statement.executeQuery();

        while (results.next()) {
            totalSales += results.getDouble("total");
            double salesPrice = results.getDouble("price") - results.getDouble("discount");
            double wholesalePrice = product.getWholesale_price();
            double rev = salesPrice - wholesalePrice;
            double productTotRev = rev * results.getDouble("quantity");
            totalRev += productTotRev;
            totalQty += results.getDouble("quantity");
        }
        totalSalesModel.setToday_TotalRevenue(totalRev);
        totalSalesModel.setToday_TotalSaleQty(totalQty);
        totalSalesModel.setToday_TotalSales(totalSales);

        return totalSalesModel;
    }

    public TotalSalesModel getProductWeekInsight(Product product) throws SQLException {
        double totalSales = 0;
        double totalRev = 0;
        double totalQty = 0;
        TotalSalesModel totalSalesModel = new TotalSalesModel();
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "SELECT * FROM invoice \n"
                + "INNER JOIN invoice_product_has_invoice\n"
                + "ON invoice.idinvoice = invoice_product_has_invoice.invoice_idinvoice\n"
                + "\n"
                + "INNER JOIN invoice_product\n"
                + "ON invoice_product.invoice_product_id = invoice_product_has_invoice.invoice_product_invoice_product_id\n"
                + "\n"
                + "\n"
                + "\n"
                + " WHERE product_id = ? AND date_time BETWEEN DATE_SUB(NOW(), INTERVAL 7 DAY) AND NOW();";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, product.getId());
        ResultSet results = statement.executeQuery();

        while (results.next()) {
            totalSales += results.getDouble("total");
            double salesPrice = results.getDouble("price") - results.getDouble("discount");
            double wholesalePrice = product.getWholesale_price();
            double rev = salesPrice - wholesalePrice;
            double productTotRev = rev * results.getDouble("quantity");
            totalRev += productTotRev;
            totalQty += results.getDouble("quantity");
        }
        totalSalesModel.setWeekly_TotalREvenue(totalRev);
        totalSalesModel.setWeekly_TotalSaleQty(totalQty);
        totalSalesModel.setWeekly_TotalSales(totalSales);

        return totalSalesModel;
    }

    public TotalSalesModel getProductMonthInsight(Product product) throws SQLException {
        double totalSales = 0;
        double totalRev = 0;
        double totalQty = 0;
        TotalSalesModel totalSalesModel = new TotalSalesModel();
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "SELECT * FROM invoice \n"
                + "INNER JOIN invoice_product_has_invoice\n"
                + "ON invoice.idinvoice = invoice_product_has_invoice.invoice_idinvoice\n"
                + "\n"
                + "INNER JOIN invoice_product\n"
                + "ON invoice_product.invoice_product_id = invoice_product_has_invoice.invoice_product_invoice_product_id\n"
                + "\n"
                + "\n"
                + "\n"
                + " WHERE product_id = ? AND date_time BETWEEN DATE_SUB(NOW(), INTERVAL 30 DAY) AND NOW();";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, product.getId());
        ResultSet results = statement.executeQuery();

        while (results.next()) {
            totalSales += results.getDouble("total");
            double salesPrice = results.getDouble("price") - results.getDouble("discount");
            double wholesalePrice = product.getWholesale_price();
            double rev = salesPrice - wholesalePrice;
            double productTotRev = rev * results.getDouble("quantity");
            totalRev += productTotRev;
            totalQty += results.getDouble("quantity");
        }
        totalSalesModel.setMonthly_TotalRevenue(totalRev);
        totalSalesModel.setMonthly_TotalSaleQty(totalQty);
        totalSalesModel.setMonthly_TotalSales(totalSales);

        return totalSalesModel;
    }

    public TotalSalesModel getProductTotalInsight(Product product) throws SQLException {
        double totalSales = 0;
        double totalRev = 0;
        double totalQty = 0;
        TotalSalesModel totalSalesModel = new TotalSalesModel();
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        String query = "SELECT * FROM invoice \n"
                + "INNER JOIN invoice_product_has_invoice\n"
                + "ON invoice.idinvoice = invoice_product_has_invoice.invoice_idinvoice\n"
                + "\n"
                + "INNER JOIN invoice_product\n"
                + "ON invoice_product.invoice_product_id = invoice_product_has_invoice.invoice_product_invoice_product_id\n"
                + "\n"
                + "\n"
                + "\n"
                + " WHERE product_id = ? ";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, product.getId());
        ResultSet results = statement.executeQuery();
        while (results.next()) {
            totalSales += results.getDouble("total");
            double salesPrice = results.getDouble("price") - results.getDouble("discount");
            double wholesalePrice = product.getWholesale_price();
            double rev = salesPrice - wholesalePrice;
            double productTotRev = rev * results.getDouble("quantity");
            totalRev += productTotRev;
            totalQty += results.getDouble("quantity");

            System.out.println("Sales Price : " + salesPrice);
            System.out.println("wholesale Price" + wholesalePrice);
            System.out.println("rev" + rev);
            System.out.println("product tot rev" + productTotRev);
        }
        totalSalesModel.setTotalRevenue(totalRev);
        totalSalesModel.setTotalSaleQty(totalQty);
        totalSalesModel.setTotalSales(totalSales);

        return totalSalesModel;
    }

    public TotalSalesModel getInsight() throws SQLException {
        TotalSalesModel model = new TotalSalesModel();
        TotalSalesModel todayInsight = new TotalSalesModel();
        TotalSalesModel weekInsight = new TotalSalesModel();
        TotalSalesModel monthInsight = new TotalSalesModel();
        TotalSalesModel totalInsight = new TotalSalesModel();

        List<Product> products = getAllProducts();

        for (Product product : products) {
            todayInsight = getProductTodayInsight(product);
            weekInsight = getProductWeekInsight(product);
            monthInsight = getProductMonthInsight(product);
            totalInsight = getProductTotalInsight(product);

            model.setTotalRevenue(model.getTotalRevenue() + totalInsight.getTotalRevenue());
            model.setTotalSaleQty(model.getTotalSaleQty() + totalInsight.getTotalSaleQty());
            model.setTotalSales(model.getTotalSales() + totalInsight.getTotalSales());

            model.setMonthly_TotalRevenue(model.getMonthly_TotalRevenue() + monthInsight.getMonthly_TotalRevenue());
            model.setMonthly_TotalSaleQty(model.getMonthly_TotalSaleQty() + monthInsight.getMonthly_TotalSaleQty());
            model.setMonthly_TotalSales(model.getMonthly_TotalSales() + monthInsight.getMonthly_TotalSales());

            model.setWeekly_TotalREvenue(model.getWeekly_TotalREvenue() + weekInsight.getWeekly_TotalREvenue());
            model.setWeekly_TotalSaleQty(model.getWeekly_TotalSaleQty() + weekInsight.getWeekly_TotalSaleQty());
            model.setWeekly_TotalSales(model.getWeekly_TotalSales() + weekInsight.getWeekly_TotalSales());

            model.setToday_TotalRevenue(model.getToday_TotalRevenue() + todayInsight.getToday_TotalRevenue());
            model.setToday_TotalSaleQty(model.getToday_TotalSaleQty() + todayInsight.getToday_TotalSaleQty());
            model.setToday_TotalSales(model.getToday_TotalSales() + todayInsight.getToday_TotalSales());
        }

        return model;
    }

    public TotalSalesModel getInsigt(Product product) throws SQLException {
        TotalSalesModel model = getProductTodayInsight(product);
        TotalSalesModel weekInsight = getProductWeekInsight(product);
        TotalSalesModel monthInsight = getProductMonthInsight(product);
        TotalSalesModel totalInsight = getProductTotalInsight(product);

        model.setWeekly_TotalREvenue(weekInsight.getWeekly_TotalREvenue());
        model.setWeekly_TotalSaleQty(weekInsight.getWeekly_TotalSaleQty());
        model.setWeekly_TotalSales(weekInsight.getWeekly_TotalSales());

        model.setMonthly_TotalRevenue(monthInsight.getMonthly_TotalRevenue());
        model.setMonthly_TotalSaleQty(monthInsight.getMonthly_TotalSaleQty());
        model.setMonthly_TotalSales(monthInsight.getMonthly_TotalSales());

        model.setTotalRevenue(totalInsight.getTotalRevenue());
        model.setTotalSaleQty(totalInsight.getTotalSaleQty());
        model.setTotalSales(totalInsight.getTotalSales());

//        System.out.println(model.getToday_TotalSales());
//        System.out.println(model.getToday_TotalSaleQty());
//        System.out.println(model.getToday_TotalRevenue());
        System.out.println(model.toString());
        return model;
    }

    public void updateProduct(Product oldProduct, Product updatedProduct) throws Exception {

        String query = "UPDATE `product` SET  `retail_price`=?, `discount`=?, `name`=?, `wholesale_price`=?, `units_idunits`=?, `si_name`=? WHERE  `id`=?";
//        remove old product barcode
//        String query = "UPDATE `product` SET  `is_active`=0 WHERE  `id`=?;";
        DataBase instance = DataBase.getInstance();
        Connection connection = instance.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
//        statement.setString(1, updatedProduct.getBarcode());
        statement.setDouble(1, updatedProduct.getRetail_price());
        statement.setDouble(2, updatedProduct.getDiscount());
        statement.setString(3, updatedProduct.getName());
        statement.setDouble(4, updatedProduct.getWholesale_price());
        statement.setInt(5, updatedProduct.getUnit().getId());
        statement.setString(6, updatedProduct.getSinhalaName());
        statement.setInt(7, updatedProduct.getId());
        statement.execute();
//        updatedProduct.setName(updatedProduct.getName() + "*");

    }

}
