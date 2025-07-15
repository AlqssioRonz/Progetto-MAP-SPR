/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package map.beforedeorbiting.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import map.beforedeorbiting.GameDesc;
import map.beforedeorbiting.type.BDObject;

/**
 * Pannello grafico per visualizzare lo stato dell'inventario di gioco. Mostra
 * una JTextArea con l'elenco aggiornato degli oggetti presenti nell'inventario
 * del {@link GameDesc}.
 *
 * @author andre
 * @author lorenzopeluso
 * @author ronzu
 */
public class InventoryUI extends JPanel {

    /**
     * Area di testo usata per mostrare il contenuto dell'inventario.
     */
    private static JTextArea inventoryTextArea;

    /**
     * Costruisce un nuovo pannello InventoryUI e inizializza la view popolando
     * la lista degli oggetti presi dal gioco.
     *
     * @param game istanza di {@link GameDesc} da cui recuperare l'inventario
     */
    public InventoryUI(GameDesc game) {
        // Colore per il testo
        Color bluchiaro = Color.decode("#00e1d4");
        setLayout(new BorderLayout());

        // Configura l'area di testo
        inventoryTextArea = new JTextArea(0, 100);
        inventoryTextArea.setEditable(false);
        inventoryTextArea.setForeground(bluchiaro);
        inventoryTextArea.setBackground(Color.decode("#25283d"));
        inventoryTextArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        inventoryTextArea.setMargin(new Insets(10, 10, 10, 10));

        // Aggiunge l'area di testo con scroll
        JScrollPane scrollPane = new JScrollPane(inventoryTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // Popola inizialmente l'inventario
        StringBuilder inventoryString = new StringBuilder();
        inventoryString.append(">>INVENTARIO\n\n");
        for (BDObject item : game.getInventory().getList()) {
            inventoryString.append(item.getName()).append("    ");
        }
        inventoryTextArea.setText(inventoryString.toString());
    }

    /**
     * Aggiorna il contenuto visibile nell'area di testo con gli oggetti
     * correntemente presenti nell'inventario di {@code game}. Deve essere
     * chiamato ogni volta che l'inventario cambia.
     *
     * @param game istanza di {@link GameDesc} da cui recuperare la lista
     * aggiornata
     */
    public static void updateInventory(GameDesc game) {
        StringBuilder inventoryString = new StringBuilder();
        inventoryString.append(">>INVENTARIO\n\n");
        for (BDObject item : game.getInventory().getList()) {
            inventoryString.append(item.getName()).append("    ");
        }
        inventoryTextArea.setText(inventoryString.toString());
    }

}
