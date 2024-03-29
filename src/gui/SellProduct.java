/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui;

import controllollers.DashboardController;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.InvoiceTotal;
import models.Product;
import models.Units;
import utlis.InvoiceTemplate;
import utlis.Validator;

/**
 *
 * @author vidur
 */
public class SellProduct extends javax.swing.JPanel {

    /**
     * Creates new form SellProduct
     */
    private List<Product> allProducts;
    private DashboardController controller;
    private Product selectedProduct;
    private Product selectedInvoiceProduct;
    private List<Units> units;
    private boolean needToRefreshProductTable = false;
    private Set<Product> invoiceProducts = new HashSet();
    private boolean PrintingJobStatates_ISRUN = false;

    public SellProduct(DashboardController controller, List<Product> products) {
        this.controller = controller;
        this.allProducts = products;
        if (this.controller == null) {
            controller = new DashboardController(Dashboard.authStatic);
        }
//        this.units = controller.getAllUnits();
        initComponents();
//        loadSellProductTable();
    }

    public void SetDependencies(DashboardController controller, List<Product> products) {
        this.controller = controller;
        this.allProducts = products;
        loadSellProductTable();
    }

    public SellProduct() {
        initComponents();
        tf_selectedBarcode.requestFocus();
    }

    public void refresh() {
        System.out.println("refresh sell product");
        allProducts = controller.getAllProducts();
        loadSellProductTable();
    }

    private void loadSellProductTable() {
//       update product jtable : variable name produt_table
        DefaultTableModel model = (DefaultTableModel) allProductTable.getModel();
        model.setRowCount(0); // Clear the existing table data

        for (Product product : allProducts) {
            Object[] row = {
                product.getId(),
                product.getBarcode(),
                product.getName(),
                product.getRetail_price(),
                product.getDiscount(),};
            model.addRow(row);
        }
    }

    private void loadSellProductTable(List<Product> p) {
        DefaultTableModel model = (DefaultTableModel) allProductTable.getModel();
        model.setRowCount(0); // Clear the existing table data
        for (Product product : p) {
            Object[] row = {
                product.getId(),
                product.getBarcode(),
                product.getName(),
                product.getRetail_price(),
                product.getDiscount(),};
            model.addRow(row);
        }
        needToRefreshProductTable = true;
    }

    private void loadInvoiceTable() {
        DefaultTableModel model = (DefaultTableModel) invoiceJTable.getModel();
        model.setRowCount(0); // Clear the existing table data

        for (Product product : invoiceProducts) {
            double total = (product.getRetail_price() - product.getDiscount()) * product.getQty();
            Object[] row = {
                product.getId(),
                product.getBarcode(),
                product.getName(),
                product.getRetail_price(),
                product.getDiscount(),
                product.getQty(),
                total

            };
            model.addRow(row);
        }

        setInvoiceTotalField();
    }

    private void removeItemFromInvoiceProductList(Product product) {
        Set<Product> newInvoiceProduct = new HashSet();

        for (Product oldProduct : invoiceProducts) {
//            if (oldProduct.getId() == product.getId()
//                    && (oldProduct.getBarcode() == null ? product.getBarcode() == null : oldProduct.getBarcode().equals(product.getBarcode()))
//                    && oldProduct.getQty() == product.getQty()
//                    && oldProduct.getRetail_price() == product.getRetail_price()
//                    && oldProduct.getDiscount() == product.getDiscount()) {
//
//            } else {
//                newInvoiceProduct.add(product);
//            }
            invoiceProducts.remove(product);

//            invoiceProducts = newInvoiceProduct;
            loadInvoiceTable();
        }
    }

    private InvoiceTotal invoiceTotalCalculate() {
        InvoiceTotal invoiceTotal = new InvoiceTotal();

        for (Product p : invoiceProducts) {
            double productPrice = p.getRetail_price();
            double productQty = p.getQty();
            double productDiscount = p.getDiscount();

            double productAllDiscount = productDiscount * productQty;
            double productAllPrice = productPrice * productQty;

            invoiceTotal.setAllDiscount(invoiceTotal.getAllDiscount() + productAllDiscount);
            invoiceTotal.setInviceTotal(invoiceTotal.getInviceTotal() + productAllPrice);

        }
        invoiceTotal.setGrandTotal(invoiceTotal.getInviceTotal() - invoiceTotal.getAllDiscount());

        return invoiceTotal;
    }

    private void setInvoiceTotalField() {
        InvoiceTotal invoiceTotal = invoiceTotalCalculate();
        tf_invoiceTotal.setText(Double.toString(invoiceTotal.getInviceTotal()));
        tf_allDiscount.setText(Double.toString(invoiceTotal.getAllDiscount()));
        tf_grandTotal.setText(Double.toString(invoiceTotal.getGrandTotal()));
        System.out.println(invoiceTotal);
    }

    private void setDatatoProductFields(Product product, int quantity) {
        tf_selectedBarcode.setText(product.getBarcode());
        tf_selectedPrice.setText(Double.toString(product.getRetail_price()));
        tf_selectedDiscount.setText(Double.toString(product.getDiscount()));
        tf_quantity.setText(String.valueOf(quantity));
        tf_selectedTotal.setText(Double.toString((product.getRetail_price() - product.getDiscount()) * quantity));
        tf_selectedName.setText(product.getName());
        tf_selectedUnit.setText(product.getUnit().getUnit());
    }

    private void setDatatoProductFields() {
        tf_selectedBarcode.setText("");
        tf_selectedPrice.setText("");
        tf_selectedDiscount.setText("");
        tf_quantity.setText("");
        tf_selectedTotal.setText("");
        tf_selectedName.setText("");
        tf_selectedUnit.setText("");
    }

    public void setProductList(List<Product> products) {
        this.allProducts = products;
        loadSellProductTable();
    }

    private void selectUsingBarcode() {
        String barcode = tf_selectedBarcode.getText();
        for (Product p : allProducts) {
            if (!p.getBarcode().isBlank()) {
                if (p.getBarcode().equals(barcode)) {
                    selectedProduct = p;
                    setDatatoProductFields(p, 1);
                    break;
                }
            }
        }
    }

    private void searchProductInName() {
        String searchword = tf_selectedName.getText().toLowerCase();
        if (!searchword.isBlank()) {
            List<Product> products = new ArrayList<>();
            for (Product p : allProducts) {
                if (p.getName().toLowerCase().startsWith(searchword)) {
                    products.add(p);
                }
            }
            loadSellProductTable(products);
        } else {
            loadSellProductTable();
        }

    }

    private void calcultateProductTotal() {
        String strQty = tf_quantity.getText();
        String strDis = tf_selectedDiscount.getText();
        String strPrice = tf_selectedPrice.getText();
        if (Validator.isValidDouble(strQty)) {
            if (Validator.isValidDouble(strDis)) {
                if (Validator.isValidDouble(strPrice)) {
                    double price = Double.parseDouble(strPrice);
                    double discount = Double.parseDouble(strDis);
                    double qty = Double.parseDouble(strQty);

                    double total = (price - discount) * qty;

                    System.out.println("run");
                    tf_selectedTotal.setText(Double.toString(total));

                } else {
                    System.out.println("pricr is not double");
                }
            } else {
                System.out.println("discount is not double");
            }
        } else {
            System.out.println("qty is not double");
        }

    }

    private void addinvoiceProduct(Product product) {

        for (Product p : invoiceProducts) {
            if (p.getId() == product.getId()) {
                if (p.getRetail_price() == product.getRetail_price()) {
                    if (p.getDiscount() == product.getDiscount()) {
                        invoiceProducts.remove(p);
                        double oldQty = p.getQty();
                        double addQty = product.getQty();
                        double newQty = oldQty + addQty;
                        p.setQty(newQty);
                        invoiceProducts.add(p);
                        return;
                    }
                }
            }
        }

        invoiceProducts.add(product);

    }

    private Product getProductFromField() {
        Product p = new Product();
        p.setRetail_price(Double.parseDouble(tf_selectedPrice.getText()));
        p.setDiscount(Double.parseDouble(tf_selectedDiscount.getText()));
        p.setQty(Double.parseDouble(tf_quantity.getText()));
        p.setName(tf_selectedName.getText());

        p.setBarcode(selectedProduct.getBarcode());
        p.setId(selectedProduct.getId());
        p.setWholesale_price(selectedProduct.getWholesale_price());
        p.setUnit(selectedProduct.getUnit());
        p.setSinhalaName(selectedProduct.getSinhalaName());
        return p;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        allProductTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tf_selectedBarcode = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tf_selectedPrice = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        tf_selectedDiscount = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        tf_quantity = new javax.swing.JFormattedTextField();
        tf_selectedTotal = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        tf_selectedName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tf_selectedUnit = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        invoiceJTable = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        tf_cash = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        tf_allDiscount = new javax.swing.JLabel();
        tf_invoiceTotal = new javax.swing.JLabel();
        tf_grandTotal = new javax.swing.JLabel();
        tf_balance = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(579, 413));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Products");

        allProductTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "System ID", "Barcode", "Name", "Price", "Discount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        allProductTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                allProductTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(allProductTable);
        if (allProductTable.getColumnModel().getColumnCount() > 0) {
            allProductTable.getColumnModel().getColumn(0).setResizable(false);
            allProductTable.getColumnModel().getColumn(1).setResizable(false);
            allProductTable.getColumnModel().getColumn(2).setResizable(false);
            allProductTable.getColumnModel().getColumn(3).setResizable(false);
            allProductTable.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel2.setText("Barcode");

        tf_selectedBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_selectedBarcodeActionPerformed(evt);
            }
        });
        tf_selectedBarcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_selectedBarcodeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tf_selectedBarcodeKeyTyped(evt);
            }
        });

        jLabel3.setText("Price");

        tf_selectedPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        tf_selectedPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_selectedPriceActionPerformed(evt);
            }
        });
        tf_selectedPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tf_selectedPriceKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_selectedPriceKeyReleased(evt);
            }
        });

        jLabel4.setText("Discount");

        tf_selectedDiscount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        tf_selectedDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_selectedDiscountActionPerformed(evt);
            }
        });
        tf_selectedDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tf_selectedDiscountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_selectedDiscountKeyReleased(evt);
            }
        });

        jLabel5.setText("Quantity");

        tf_quantity.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        tf_quantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_quantityKeyReleased(evt);
            }
        });

        tf_selectedTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jLabel6.setText("Total");

        jButton1.setText("ADD");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        tf_selectedName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_selectedNameKeyReleased(evt);
            }
        });

        jLabel7.setText("Product Name");

        tf_selectedUnit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        tf_selectedUnit.setEnabled(false);

        jLabel11.setText("Unit");

        jButton5.setText("Clear Fields");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(tf_selectedBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tf_selectedPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(82, 82, 82)
                                .addComponent(jLabel3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(tf_selectedDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(tf_quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(tf_selectedUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(tf_selectedName, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(tf_selectedTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_quantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_selectedDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_selectedBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tf_selectedPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_selectedUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_selectedName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_selectedTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        invoiceJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "System ID", "Barcode", "Name", "Price", "Discount", "Quantity", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        invoiceJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                invoiceJTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(invoiceJTable);
        if (invoiceJTable.getColumnModel().getColumnCount() > 0) {
            invoiceJTable.getColumnModel().getColumn(0).setResizable(false);
            invoiceJTable.getColumnModel().getColumn(1).setResizable(false);
            invoiceJTable.getColumnModel().getColumn(2).setResizable(false);
            invoiceJTable.getColumnModel().getColumn(3).setResizable(false);
            invoiceJTable.getColumnModel().getColumn(4).setResizable(false);
            invoiceJTable.getColumnModel().getColumn(5).setResizable(false);
            invoiceJTable.getColumnModel().getColumn(6).setResizable(false);
        }

        jButton2.setText("Remove Selected Item");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel8.setText("Invoice Total");

        jLabel9.setText("All Discounts");

        jLabel10.setText("Grant Total");

        jButton3.setText("Print Invoice");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel12.setText("Cash");

        tf_cash.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_cashKeyReleased(evt);
            }
        });

        jLabel13.setText("Balance");

        jButton4.setText("Clear Invoice");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        tf_allDiscount.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tf_allDiscount.setText("0.00");

        tf_invoiceTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tf_invoiceTotal.setText("0.00");

        tf_grandTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tf_grandTotal.setText("0.00");

        tf_balance.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tf_balance.setText("0.00");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(25, 25, 25))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(tf_invoiceTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(27, 27, 27))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(tf_allDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(27, 27, 27)
                                .addComponent(jLabel12)
                                .addGap(63, 63, 63)
                                .addComponent(jLabel13)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(tf_grandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_cash, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tf_balance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton3)
                                .addComponent(tf_cash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tf_allDiscount)
                                .addComponent(tf_invoiceTotal)
                                .addComponent(tf_grandTotal))
                            .addComponent(tf_balance))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void allProductTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_allProductTableMouseClicked
        // TODO add your handling code here:
        // TODO add your handling code here:
        int rowIndex = allProductTable.getSelectedRow();
        if (rowIndex != -1) {
            int productId = (int) allProductTable.getValueAt(rowIndex, 0);
            for (Product product : allProducts) {
                if (product.getId() == productId) {
                    selectedProduct = product;
                    setDatatoProductFields(selectedProduct, 1);
                    break;
                }
            }
        }
        if (needToRefreshProductTable) {
            loadSellProductTable();
            needToRefreshProductTable = false;
        }
    }//GEN-LAST:event_allProductTableMouseClicked

    private void tf_quantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_quantityKeyReleased
        // TODO add your handling code here:

        if (selectedProduct != null) {
            calcultateProductTotal();
        }
    }//GEN-LAST:event_tf_quantityKeyReleased

    private void tf_selectedDiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_selectedDiscountKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_tf_selectedDiscountKeyPressed

    private void tf_selectedPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_selectedPriceKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_tf_selectedPriceKeyPressed

    private void tf_selectedDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_selectedDiscountActionPerformed
        // TODO add your handling code here:
        if (selectedProduct != null) {
            calcultateProductTotal();
        }
    }//GEN-LAST:event_tf_selectedDiscountActionPerformed

    private void tf_selectedPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_selectedPriceActionPerformed
        // TODO add your handling code here:
        if (selectedProduct != null) {
            calcultateProductTotal();
        }
    }//GEN-LAST:event_tf_selectedPriceActionPerformed

    private void tf_selectedDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_selectedDiscountKeyReleased
        // TODO add your handling code here:
        if (selectedProduct != null) {
            calcultateProductTotal();
        }
    }//GEN-LAST:event_tf_selectedDiscountKeyReleased

    private void tf_selectedPriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_selectedPriceKeyReleased
        // TODO add your handling code here:
        if (selectedProduct != null) {
            calcultateProductTotal();
        }
    }//GEN-LAST:event_tf_selectedPriceKeyReleased

    private void tf_selectedBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_selectedBarcodeActionPerformed
        // TODO add your handling code here:
        selectUsingBarcode();
    }//GEN-LAST:event_tf_selectedBarcodeActionPerformed

    private void tf_selectedBarcodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_selectedBarcodeKeyReleased
        // TODO add your handling code here:
        selectUsingBarcode();
    }//GEN-LAST:event_tf_selectedBarcodeKeyReleased

    private void tf_selectedBarcodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_selectedBarcodeKeyTyped
        // TODO add your handling code here:
        selectUsingBarcode();
    }//GEN-LAST:event_tf_selectedBarcodeKeyTyped

    private void tf_selectedNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_selectedNameKeyReleased
        // TODO add your handling code here:
        searchProductInName();
    }//GEN-LAST:event_tf_selectedNameKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         refreshCashBalance();
        Product p = getProductFromField();
        if (p.getQty() > 0) {
            if (p.getDiscount() < p.getRetail_price()) {

                addinvoiceProduct(p);
                loadInvoiceTable();
            } else {
                JOptionPane.showMessageDialog(this, "Discount cannot be greter than price !", "Input Error", JOptionPane.ERROR_MESSAGE);

            }

        } else {
            JOptionPane.showMessageDialog(this, "Quantity cannot be zero!", "Input Error", JOptionPane.ERROR_MESSAGE);
        }

        setDatatoProductFields();
        tf_selectedBarcode.requestFocus();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
       
        double cash = 0;
        double balance = 0;
        if (invoiceProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invoice Items is Empty! ", "OOPS!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            System.out.println(invoiceProducts.toString());
            controller = new DashboardController(Dashboard.authStatic);
            int invoiceID = controller.addInvoice(invoiceProducts);
            String invoiceStrID = String.valueOf(invoiceID);
            if (invoiceID < 9999) {
                invoiceStrID = "0000" + invoiceID;
            }
            if (!tf_cash.getText().isBlank()) {
                if (Validator.isValidDouble(tf_cash.getText())) {
                    InvoiceTotal invoiceTotalCalculate = invoiceTotalCalculate();
                    cash = Double.parseDouble(tf_cash.getText());
                    balance = cash - invoiceTotalCalculate.getGrandTotal();
                    System.out.println(balance + " " + cash);
                } else {
                    JOptionPane.showMessageDialog(null, "Cash Option value is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            InvoiceTemplate invoice = new InvoiceTemplate(invoiceProducts, invoiceStrID, invoiceTotalCalculate(), cash, balance);
            // Print the invoice
            PrinterJob job = PrinterJob.getPrinterJob();

            // Set print attributes
            HashPrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
            attrs.add(MediaSizeName.INVOICE); // Adjust as per your paper size requirement

            job.setPrintable(invoice);
            try {

                job.print(attrs);
                invoiceProducts.clear();
                loadInvoiceTable();
                tf_cash.setText("0.00");
                tf_balance.setText("0.00");
                

            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(null, "Error printing invoice: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            PrintingJobStatates_ISRUN = false;
//                Logger.getLogger(SellProduct.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void invoiceJTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoiceJTableMouseClicked
        // TODO add your handling code here:
        int rowIndex = invoiceJTable.getSelectedRow();
        if (rowIndex != -1) {
            int productId = (int) invoiceJTable.getValueAt(rowIndex, 0);
            for (Product product : invoiceProducts) {
                if (product.getId() == productId) {
                    selectedInvoiceProduct = product;
//                    setDatatoProductFields(selectedInvoiceProduct, 1);
                    break;
                }
            }
        }
    }//GEN-LAST:event_invoiceJTableMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (selectedInvoiceProduct == null) {
            JOptionPane.showMessageDialog(this, "Please Select a Row To Delete !", "oops!", JOptionPane.ERROR_MESSAGE);
            return;
        }
                 refreshCashBalance();

        removeItemFromInvoiceProductList(selectedInvoiceProduct);
        
       

        


    }//GEN-LAST:event_jButton2ActionPerformed

    private void tf_cashKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_cashKeyReleased
        // TODO add your handling code here:
        calculateCashBalance();

    }//GEN-LAST:event_tf_cashKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        invoiceProducts.clear();
        loadInvoiceTable();
        tf_cash.setText("0.00");
                tf_balance.setText("0.00");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        selectedProduct = null;
        setDatatoProductFields();
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable allProductTable;
    private javax.swing.JTable invoiceJTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel tf_allDiscount;
    private javax.swing.JLabel tf_balance;
    private javax.swing.JFormattedTextField tf_cash;
    private javax.swing.JLabel tf_grandTotal;
    private javax.swing.JLabel tf_invoiceTotal;
    private javax.swing.JFormattedTextField tf_quantity;
    private javax.swing.JTextField tf_selectedBarcode;
    private javax.swing.JFormattedTextField tf_selectedDiscount;
    private javax.swing.JTextField tf_selectedName;
    private javax.swing.JFormattedTextField tf_selectedPrice;
    private javax.swing.JFormattedTextField tf_selectedTotal;
    private javax.swing.JFormattedTextField tf_selectedUnit;
    // End of variables declaration//GEN-END:variables

    private void calculateCashBalance() {
        InvoiceTotal invoiceTotal = invoiceTotalCalculate();
        if (invoiceTotal.getGrandTotal() == 0) {
            tf_balance.setText("0.00");
            return;
        }

        if (Validator.isValidDouble(tf_cash.getText())) {
            double cash = Double.parseDouble(tf_cash.getText());
            double balance = cash - invoiceTotal.getGrandTotal();
            tf_balance.setText(Double.toString(balance));
        }
    }
    
    private void refreshCashBalance(){
        System.out.println("Running here=================");
         tf_balance.setText("0.00");
         tf_cash.setText("");
    }
}
