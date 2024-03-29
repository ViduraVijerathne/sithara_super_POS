/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import appLogger.AppLog;
import appLogger.LogType;
import controllollers.Auth;
import controllollers.DashboardController;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Product;
import models.TotalSalesModel;
import models.Units;
import modes.Roles;
import utlis.Validator;

/**
 *
 * @author vidur
 */
public class Dashboard extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    private Auth auth;
    public static Auth authStatic;
    DashboardController controller;

    private List<Units> unitList;
    private List<Product> productList;
    private Product selectedProduct;

    public Dashboard(Auth auth) {
        initComponents();
        this.auth = auth;
        authStatic = auth;
        controller = new DashboardController(auth);
        loadUnitComboBox();
        loadProductTable();
        sellProduct2.SetDependencies(controller, productList);
        insightPanel2.setAuth(auth);
        adminControllPanel2.setAuth(auth);
        adminControllPanel2.setSellProdutPanel(sellProduct2);
        adminControllPanel2.setAddProductPanel(this);
        tf_barcode.requestFocus();

    }

    public void refresh() {
        loadProductTable();
        unitList.clear();
        unitList = controller.getAllUnits();
        loadUnitComboBox();
        System.out.println("Refresh dashboard");
    }

    private void loadUnitComboBox() {
        if (unitList == null) {
            System.out.println("getting units..");
            unitList = controller.getAllUnits();
        }
        product_UnitComboBox.removeAll();
        product_UnitComboBox.removeAllItems();
        for (Units unit : unitList) {
            product_UnitComboBox.addItem(unit.getUnit());
        }
    }

    private void setSelectedUnitInComboBox(Units unit) {
        System.out.println(unit.toString());
        product_UnitComboBox.setSelectedItem(unit.getUnit());
    }

    private void loadProductTable() {
        productList = controller.getAllProducts(true);
//        sellProduct1.setProductList(productList);
//       update product jtable : variable name produt_table
        DefaultTableModel model = (DefaultTableModel) produt_table.getModel();
        model.setRowCount(0); // Clear the existing table data

        for (Product product : productList) {
            Object[] row = {
                product.getId(),
                product.getBarcode(),
                product.getName(),
                product.getWholesale_price(),
                product.getRetail_price(),
                product.getDiscount(),
                product.getIs_active() == 1 ? "Active" : "Deactive"
            };
            model.addRow(row);
        }
    }

    private Units getUnitComboBoxSelectedItem() {
        String selectedUnit = (String) product_UnitComboBox.getSelectedItem();
        for (Units unit : unitList) {
            if (unit.getUnit().equals(selectedUnit)) {
                return unit;
            }
        }
        return null; // Return null if no matching Units object is found
    }

    private Product getDataFromProductFields() {
        Product product = new Product();
        String productID = tf_productID.getText();
        String barcode = tf_barcode.getText();
        String wholesalePriceStr = tf_wholesalePrice.getText();
        String retailPriceStr = tf_retailPrice.getText();
        String nameEN = tf_name_EN.getText();
        String nameSI = tf_name_SI.getText();
        String defaultDiscountStr = tf_defaultDiscount.getText();
        Units unit = getUnitComboBoxSelectedItem();

        // Validate and set product ID
        if (!productID.isBlank()) {
            if (Validator.isValidProductID(productID)) {
                product.setId(Integer.parseInt(productID));
            } else {
                JOptionPane.showMessageDialog(this, "Invalid product ID!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } else {
            product.setId(-1);
        }

        // Validate and set barcode
        if (!barcode.isBlank()) {
            if (Validator.isValidBarcode(barcode)) {
                product.setBarcode(barcode);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid barcode!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } else {
            product.setBarcode("");
        }

        // Validate and set wholesale price
        if (Validator.isValidDouble(wholesalePriceStr)) {
            double wholesalePrice = Double.parseDouble(wholesalePriceStr);
            product.setWholesale_price(wholesalePrice);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid wholesale price!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Validate and set retail price
        if (Validator.isValidDouble(retailPriceStr)) {
            double retailPrice = Double.parseDouble(retailPriceStr);
            product.setRetail_price(retailPrice);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid retail price!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        product.setSinhalaName(nameSI);
        product.setName(nameEN);

        // Validate and set default discount
        if (Validator.isValidDouble(defaultDiscountStr)) {
            double defaultDiscount = Double.parseDouble(defaultDiscountStr);
            product.setDiscount(defaultDiscount);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid default discount!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Set unit (if available)
        product.setUnit(unit);

        System.out.println(product.toString());
        return product;

    }

    private void setDatatoProductFields(Product product) {
        // Set data to the text fields
        tf_productID.setText(String.valueOf(product.getId()));
        tf_barcode.setText(product.getBarcode());
        tf_wholesalePrice.setText(String.valueOf(product.getWholesale_price()));
        tf_retailPrice.setText(String.valueOf(product.getRetail_price()));
        tf_name_EN.setText(product.getName());
        tf_name_SI.setText(product.getSinhalaName());
        tf_defaultDiscount.setText(String.valueOf(product.getDiscount()));
        // Set selected unit in the combo box
        setSelectedUnitInComboBox(product.getUnit());
    }

    private void setDatatoProductFields() {
        // Set data to the text fields
        tf_productID.setText("");
        tf_barcode.setText("");
        tf_wholesalePrice.setText("");
        tf_retailPrice.setText("");
        tf_name_EN.setText("");
        tf_name_SI.setText("");
        tf_defaultDiscount.setText("");
        // Set selected unit in the combo box
        setSelectedUnitInComboBox(unitList.get(0));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        sellProduct2 = new gui.SellProduct();
        add_product_panel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        produt_table = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tf_productID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tf_barcode = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tf_wholesalePrice = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        tf_defaultDiscount = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        tf_retailPrice = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        tf_name_EN = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tf_name_SI = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        product_UnitComboBox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        insightPanel2 = new gui.insightPanel();
        adminControllPanel2 = new gui.AdminControllPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTabbedPane1.addTab("Sell Product", sellProduct2);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Products");

        produt_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "barcode", "Product", "Wholesale", "Retail Price", "Discount", "Active Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        produt_table.setMinimumSize(new java.awt.Dimension(105, 440));
        produt_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                produt_tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(produt_table);
        if (produt_table.getColumnModel().getColumnCount() > 0) {
            produt_table.getColumnModel().getColumn(0).setResizable(false);
            produt_table.getColumnModel().getColumn(1).setResizable(false);
            produt_table.getColumnModel().getColumn(2).setResizable(false);
            produt_table.getColumnModel().getColumn(3).setResizable(false);
            produt_table.getColumnModel().getColumn(4).setResizable(false);
            produt_table.getColumnModel().getColumn(5).setResizable(false);
            produt_table.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1123, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jLabel2.setText("System ID");

        tf_productID.setEnabled(false);

        jLabel3.setText("Barcode");

        tf_barcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_barcodeKeyReleased(evt);
            }
        });

        jLabel4.setText("Wholesale Price");

        tf_wholesalePrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jLabel5.setText("Default  Discount");

        tf_defaultDiscount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        tf_defaultDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_defaultDiscountActionPerformed(evt);
            }
        });

        jLabel6.setText("Retail Price");

        tf_retailPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

        jLabel7.setText("Product Name(EN)");

        tf_name_EN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tf_name_ENKeyReleased(evt);
            }
        });

        jLabel8.setText("Product Name (SI)");

        jLabel9.setText("Units");

        product_UnitComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                product_UnitComboBoxActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(4, 122, 251));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("ADD");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton5.setText("VIEW SALES");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton2.setText("CLEAR");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btn_update.setText("Update ");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tf_productID)
                                    .addComponent(tf_barcode, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                    .addComponent(tf_wholesalePrice)))
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(tf_retailPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                            .addComponent(tf_name_EN)
                                            .addComponent(tf_name_SI)))
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel5)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(product_UnitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tf_defaultDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_update)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_productID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_retailPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_defaultDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_barcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_name_EN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(product_UnitComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_wholesalePrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_name_SI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton5)
                    .addComponent(jButton2)
                    .addComponent(btn_update))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout add_product_panelLayout = new javax.swing.GroupLayout(add_product_panel);
        add_product_panel.setLayout(add_product_panelLayout);
        add_product_panelLayout.setHorizontalGroup(
            add_product_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(add_product_panelLayout.createSequentialGroup()
                .addGroup(add_product_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(add_product_panelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(add_product_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(add_product_panelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(add_product_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        add_product_panelLayout.setVerticalGroup(
            add_product_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(add_product_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Add Product", add_product_panel);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(insightPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1146, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(insightPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Insights", jPanel2);
        jTabbedPane1.addTab("Controll Panel", adminControllPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        selectedProduct = null;
        setDatatoProductFields(new Product());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (!(auth.isAdmin() || auth.isDev())) {
            JOptionPane.showMessageDialog(this, "Only admin can access this feature!", "Access failed!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Please Select A Product to view Sales", "Opps!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {

            TotalSalesModel model = controller.getInsigt(selectedProduct);
            model.setProduct(selectedProduct);

            ProductSalesPanel productSales = new ProductSalesPanel();
            productSales.setTotalSales(model);
            JDialog jsDialog = new JDialog(this, selectedProduct.getName() + " Sales");
            jsDialog.setSize(450, 300);

            jsDialog.add(productSales);
            jsDialog.setVisible(true);
            System.out.println("run");
        } catch (SQLException ex) {
            appLogger.WriteLog.log(new AppLog(LocalDateTime.now().toString(), Dashboard.class.getName(), ex.getMessage(), auth.getRole().name(), LogType.ERROR));

            System.out.println(ex);
        }


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Product product = getDataFromProductFields();
        if (product != null) {
            try {
                controller.addProduct(product);
                loadProductTable();
                sellProduct2.refresh();
                setDatatoProductFields();
                tf_barcode.requestFocus();
                JOptionPane.showMessageDialog(this, "Successfully addedd!", "Success!", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                appLogger.WriteLog.log(new AppLog(LocalDateTime.now().toString(), Dashboard.class.getName(), e.getMessage(), auth.getRole().name(), LogType.ERROR));

                JOptionPane.showMessageDialog(this, e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void product_UnitComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_product_UnitComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_product_UnitComboBoxActionPerformed

    private void tf_defaultDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_defaultDiscountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_defaultDiscountActionPerformed

    private void produt_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_produt_tableMouseClicked
        // TODO add your handling code here:
        int rowIndex = produt_table.getSelectedRow();
        if (rowIndex != -1) {
            int productId = (int) produt_table.getValueAt(rowIndex, 0);
            for (Product product : productList) {
                if (product.getId() == productId) {
                    selectedProduct = product;
                    setDatatoProductFields(selectedProduct);

                    break;
                }
            }
        }
    }//GEN-LAST:event_produt_tableMouseClicked

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        // TODO add your handling code here:
        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Please Select a Product to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            Product updatedproduct = getDataFromProductFields();

            if (!selectedProduct.getBarcode().equals(updatedproduct.getBarcode())) {
                JOptionPane.showMessageDialog(this, "You cant change Barcode in product , we recomend to add it as new Product !", "Warning", JOptionPane.WARNING_MESSAGE);

                return;
            }

            if (updatedproduct != null) {
                controller.updateProduct(selectedProduct, updatedproduct);
                JOptionPane.showMessageDialog(this, "Successfully update Product!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                loadProductTable();
                sellProduct2.refresh();
                setDatatoProductFields();
                tf_barcode.requestFocus();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_btn_updateActionPerformed

    private void tf_barcodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_barcodeKeyReleased
        // TODO add your handling code here:
        System.out.println(tf_barcode.getText());
        selectUsingBarcode();
    }//GEN-LAST:event_tf_barcodeKeyReleased

    private void tf_name_ENKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_name_ENKeyReleased
        // TODO add your handling code here:
        String name = tf_name_EN.getText();
        for(Product p : productList){
            if(p.getName().toLowerCase().equals(name.toLowerCase())){
                selectedProduct = p;
                setDatatoProductFields(p);
                break;
            }
        }
    }//GEN-LAST:event_tf_name_ENKeyReleased

//    done
    private void selectUsingBarcode(){
        String barcode = tf_barcode.getText();
        for (Product p : productList) {
            if (!p.getBarcode().isBlank()) {
                if (p.getBarcode().equals(barcode)) {
                    selectedProduct = p;
                    setDatatoProductFields(p);
                    break;
                }
            }
        }
    }
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel add_product_panel;
    private gui.AdminControllPanel adminControllPanel2;
    private javax.swing.JButton btn_update;
    private gui.insightPanel insightPanel2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<String> product_UnitComboBox;
    private javax.swing.JTable produt_table;
    private gui.SellProduct sellProduct2;
    private javax.swing.JTextField tf_barcode;
    private javax.swing.JFormattedTextField tf_defaultDiscount;
    private javax.swing.JTextField tf_name_EN;
    private javax.swing.JTextField tf_name_SI;
    private javax.swing.JTextField tf_productID;
    private javax.swing.JFormattedTextField tf_retailPrice;
    private javax.swing.JFormattedTextField tf_wholesalePrice;
    // End of variables declaration//GEN-END:variables

}
