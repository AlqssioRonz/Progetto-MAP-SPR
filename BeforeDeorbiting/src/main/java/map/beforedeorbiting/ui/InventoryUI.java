/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.type.BDObject;
/**
 *
 * @author lorenzopeluso
 */
public class InventoryUI extends JPanel {
    
    private static JTextArea inventoryTextArea;
    
    public InventoryUI(GameDesc game){
        
        Color bluchiaro = Color.decode("#00e1d4");
        setLayout(new BorderLayout()); // layout semplice

        inventoryTextArea = new JTextArea(0, 100);
        inventoryTextArea.setEditable(false);
        inventoryTextArea.setForeground(bluchiaro);
        inventoryTextArea.setBackground(Color.decode("#25283d"));
        inventoryTextArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        inventoryTextArea.setMargin(new Insets(10, 10, 10, 10));
        

        JScrollPane scrollPane = new JScrollPane(inventoryTextArea);
        add(scrollPane, BorderLayout.CENTER);
        
        
        StringBuilder inventoryString = new StringBuilder();
        inventoryString.append(">>INVENTARIO\n\n");
        for(BDObject item : game.getInventory().getList()){
            inventoryString.append(item.getName()).append("    ");
        }
        
        inventoryTextArea.setText(inventoryString.toString());
        
    }
    
    public static void updateInventory(GameDesc game) {
        StringBuilder inventoryString = new StringBuilder();
        inventoryString.append(">>INVENTARIO\n\n");
        for(BDObject item : game.getInventory().getList()){
            inventoryString.append(item.getName()).append("    ");;
        }
        
        inventoryTextArea.setText(inventoryString.toString());
        
    }
    
}
