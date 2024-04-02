/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utlis;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import models.InvoiceTotal;
import models.Product;

public class InvoiceTempletePages implements Printable {

    private final String shopName = "Sithara Super Center";
    private final String shopAddress = "Rathnapathwila, Kahatagasdigiliya";
    private final String contactInfo = "077 923 2200 / 077 881 7850";

    private final Set<Product> products;
    private final String invoiceID;
    private final InvoiceTotal invoiceTotal;

    private Font titleFont;
    private Font sinhalaBold;
    private Font sinhalaMed;
    private Font sinhalaReg;
    private Font sinhalaGeeet;

    double cash = 0;
    double balance = 0;

    public double invoiceWidth = 0;
    public int invoiceHight = 0;
    public List<InvoicePageStatus> invoicePageStatus;

    public InvoiceTempletePages(Set<Product> products, String invoiceID, InvoiceTotal invoiceTotal, double cash, double balance) {
        this.products = products;
        this.invoiceID = invoiceID;
        this.invoiceTotal = invoiceTotal;

        this.balance = balance;
        this.cash = cash;

//        src/main/java/org/example/fonts/Satisfy-Regular.ttf
        // Load custom font
        try {
            File fontFile = new File("src/fonts/Satisfy-Regular.ttf"); // Path to your font file
            titleFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.BOLD, 18);
        } catch (FontFormatException | IOException e) {
            // Handle font loading error
            e.printStackTrace();
            throw new RuntimeException("Error loading custom font");
        }

        try {
            File fontFile = new File("src/fonts/AbhayaLibre-Bold.ttf"); // Path to your font file
            sinhalaBold = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.BOLD, 9);
        } catch (FontFormatException | IOException e) {
            // Handle font loading error
            e.printStackTrace();
            throw new RuntimeException("Error loading custom font");
        }

        try {
            File fontFile = new File("src/fonts/AbhayaLibre-Medium.ttf"); // Path to your font file
            sinhalaMed = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.BOLD, 8);
        } catch (FontFormatException | IOException e) {
            // Handle font loading error
            e.printStackTrace();
            throw new RuntimeException("Error loading custom font");
        }

        try {
            File fontFile = new File("src/fonts/AbhayaLibre-Regular.ttf"); // Path to your font file
            sinhalaReg = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.PLAIN, 10);
        } catch (FontFormatException | IOException e) {
            // Handle font loading error
            e.printStackTrace();
            throw new RuntimeException("Error loading custom font");
        }

        try {
            File fontFile = new File("src/fonts/AbhayaLibre-Bold.ttf"); // Path to your font file
            sinhalaGeeet = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.BOLD, 12);
        } catch (FontFormatException | IOException e) {
            // Handle font loading error
            e.printStackTrace();
            throw new RuntimeException("Error loading custom font");
        }
    }

    public String getCurrentTimeFormatted() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/dd hh:mm a");
        return now.format(formatter);
    }

    public String[] productNameFormat(String name) {
        int splitInt = 7;
        // Split the string by spaces
        String[] words = name.split(" ");
        String returnWord = "";
        if (words[0].length() >= splitInt) {
            returnWord += words[0].substring(0, splitInt) + " \n " + words[0].substring(splitInt);
            for (int i = 1; i < words.length; i++) {
                returnWord += words[i] + " ";
            }
        } else {
            for (int i = 0; i < words.length; i++) {
                if (i == 0) {
                    returnWord += words[i] + " \n ";
                } else {
                    returnWord += words[i] + " ";
                }

            }
        }

        String[] split = returnWord.split("\n");

        return split;

    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        System.out.println(invoicePageStatus);

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        int x = 10; // Starting X position
        int y = 20; // Starting Y position

        // Calculate total height required for printing content
        int totalHeight = calculateTotalHeight(pageFormat);
        invoiceHight = totalHeight;
        invoiceWidth = pageFormat.getImageableWidth();

        System.out.println("page format+" + pageFormat.getHeight());
        // Adjust page height if necessary
        if (totalHeight > pageFormat.getImageableHeight()) {
            Paper paper = pageFormat.getPaper();
            paper.setSize(pageFormat.getWidth(), totalHeight);
            pageFormat.setPaper(paper);
//            pageFormat
        }
        System.out.println("page format+" + pageFormat.getHeight());
        Font addressFont = new Font("Arial", Font.PLAIN, 12);
        FontMetrics addressMetrics = graphics.getFontMetrics(addressFont);

        int[] colWidths = {48, 53, 43, 53}; // Adjust column widths as needed 290
        if (invoicePageStatus.contains(InvoicePageStatus.FIRST)) {
            //        // Print shop name
//        Font titleFont = new Font("Arial", Font.BOLD, 14);
            FontMetrics titleMetrics = graphics.getFontMetrics(titleFont);
            graphics.setFont(titleFont);
            int titleWidth = titleMetrics.stringWidth(shopName);
            graphics.drawString(shopName, (int) (pageFormat.getImageableWidth() - titleWidth) / 2, y);
            y += titleMetrics.getHeight();

            //
//        // Print shop address and contact info
            graphics.setFont(addressFont);
            graphics.drawString(shopAddress, (int) (pageFormat.getImageableWidth() - addressMetrics.stringWidth(shopAddress)) / 2, y);
            y += addressMetrics.getHeight();
            graphics.drawString(contactInfo, (int) (pageFormat.getImageableWidth() - addressMetrics.stringWidth(contactInfo)) / 2, y);
            y += addressMetrics.getHeight() + 20; // Add some space after the address

            //        // Print invoice header
            Font headerFont = new Font("Arial", Font.BOLD, 18);
            graphics.setFont(headerFont);
            int InvoiceWidth = titleMetrics.stringWidth("INVOICE");
            graphics.drawString("INVOICE", (int) (pageFormat.getImageableWidth() - InvoiceWidth) / 2, y);
            y += titleMetrics.getHeight(); // Add some space after the header
//
//        // Print invoice details
            Font descfont = new Font("Arial", Font.PLAIN, 8);
            graphics.setFont(descfont);
            graphics.drawString("INVOICE NO. " + invoiceID, x, y);
            y += addressMetrics.getHeight();
            graphics.drawString("Date:" + getCurrentTimeFormatted(), x, y);
            y += addressMetrics.getHeight() + 20; // Add some space after the details

            // Print table header
            graphics.drawLine(x, y + 5, (int) pageFormat.getImageableWidth(), y + 5);
            String[] headers = {"ප්‍රමාණය", "ඒකක මිල(රු)", "වට්ටම(රු)", "වටිනාකම(රු)"};

            graphics.setFont(sinhalaBold);
            for (int i = 0; i < headers.length; i++) {
                graphics.drawString(headers[i], x, y);
                x += colWidths[i];
            }
            y += addressMetrics.getHeight();

            System.out.println("END OF HEAD :" + y);

        }
            graphics.setFont(sinhalaReg);

        if (invoicePageStatus.contains(InvoicePageStatus.MIDDLE)) {
            // Print table content
            for (Product product : products) {
                x = 10; // Reset X position for each row
//
                y += 5; // Add a little space before the row text
                String productName = product.getSinhalaName();
                if (!productName.isBlank()) {
                    graphics.drawString(productName, x, y);
                } else {
                    graphics.drawString(product.getName(), x, y);
                }
                y += 10;
//            x += colWidths[0];
                graphics.drawString("x " + product.getQty() + " " + product.getUnit().getUnit(), x, y);
                x += colWidths[0];
                graphics.drawString(String.format("%.2f", product.getRetail_price()), x, y);
                x += colWidths[1];
                graphics.drawString(String.format("%.2f", product.getDiscount()), x, y);
                x += colWidths[2];
                double subtotal = product.getQty() * (product.getRetail_price() - product.getDiscount());
                graphics.drawString(String.format("%.2f", subtotal), x, y);
                y += addressMetrics.getHeight();
                x = 10;

            }

            System.out.println("END OF BODY :" + y);
        }
        if (invoicePageStatus.contains(InvoicePageStatus.LAST)) {

            // Print totals
            y += 5; // Add some space between table and totals
            graphics.drawLine(x, y - 5, (int) pageFormat.getImageableWidth(), y - 5);
            graphics.setFont(sinhalaGeeet);
            FontMetrics totalMetrix = graphics.getFontMetrics(sinhalaGeeet);
            int totalWidth = totalMetrix.stringWidth("ගෙවිය යුතු මුලු මුදල : රු." + String.format("%.2f", invoiceTotal.getGrandTotal()));
            int subTotalWidth = totalMetrix.stringWidth("මුළු වටිනාකම (වට්ටම් රහිත) : රු." + String.format("%.2f", invoiceTotal.getInviceTotal()));
            int discountWidth = totalMetrix.stringWidth("මුළු වට්ටම   : රු." + String.format("%.2f", invoiceTotal.getAllDiscount()));
            int cashWidth = totalMetrix.stringWidth("දුන් මුදල : රු." + String.format("%.2f", cash));
            int balanceWidth = totalMetrix.stringWidth("ඉතිරි දුන් මුදල : රු." + String.format("%.2f", balance));
            x = (int) pageFormat.getImageableWidth() - subTotalWidth;
            y += 15; // Add a little space before the total text
            graphics.drawString("මුළු වටිනාකම (වට්ටම් රහිත) : රු." + String.format("%.2f", invoiceTotal.getInviceTotal()), x, y);
            y += addressMetrics.getHeight();

            x = (int) pageFormat.getImageableWidth() - discountWidth;
            graphics.drawString("මුළු වට්ටම   : රු." + String.format("%.2f", invoiceTotal.getAllDiscount()), x, y);
            y += addressMetrics.getHeight();

            x = (int) pageFormat.getImageableWidth() - totalWidth;
            graphics.drawLine(x, y, (int) pageFormat.getImageableWidth(), y);
            y += addressMetrics.getHeight();
            graphics.drawString("ගෙවිය යුතු මුළු මුදල : රු." + String.format("%.2f", invoiceTotal.getGrandTotal()), x, y);
            y += addressMetrics.getHeight();
            graphics.drawLine(x, y, (int) pageFormat.getImageableWidth(), y);
            if (cash > 0) {
                y += addressMetrics.getHeight();
                x = (int) pageFormat.getImageableWidth() - cashWidth;
                graphics.drawString("දුන් මුදල : රු." + String.format("%.2f", cash), x, y);
                y += addressMetrics.getHeight();
                x = (int) pageFormat.getImageableWidth() - balanceWidth;
                graphics.drawString("ඉතිරි දුන් මුදල : රු." + String.format("%.2f", balance), x, y);
                y += addressMetrics.getHeight();
                graphics.drawLine(x, y, (int) pageFormat.getImageableWidth(), y);
                y += 5;
                graphics.drawLine(x, y, (int) pageFormat.getImageableWidth(), y);
            } else {
                y += 5;
                graphics.drawLine(x, y, (int) pageFormat.getImageableWidth(), y);
            }

            y += 20;
            FontMetrics greetingMetrics = graphics.getFontMetrics(sinhalaGeeet);
            graphics.setFont(sinhalaGeeet);
            int greetingWidth = greetingMetrics.stringWidth("ස්තූතියි..!  නැවත එන්න.");
            graphics.drawString("ස්තූතියි..!  නැවත එන්න.", (int) (pageFormat.getImageableWidth() - greetingWidth) / 2, y);
//        graphics.drawLine(x, y, (int) pageFormat.getImageableWidth(), y);
            System.out.println("END OF FOOTER :" + y);
        }
        y = invoiceHight;
        graphics.drawLine(0, y, 500, y);
        return PAGE_EXISTS;
    }

    private int calculateTotalHeight(PageFormat pageFormat) {
        // Calculate the total height required for printing the content
        int totalHeight = 0;
        // Add the height required for each section of the invoice
        int headSize = 184;
        int bodySize = 29 * products.size();
        int footer = cash == 0 ? 101 : 143;
        int margin = 1000;
        totalHeight = headSize + bodySize + footer + margin;
        System.out.println("FULL SIZE : " + totalHeight);
        // Adjust totalHeight accordingly
        return totalHeight;
    }

}
