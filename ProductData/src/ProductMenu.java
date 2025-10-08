import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ProductMenu extends JFrame {
    public static void main(String[] args) {
        ProductMenu menu = new ProductMenu();
        menu.setSize(700, 600);
        menu.setLocationRelativeTo(null);
        menu.setContentPane(menu.mainPanel);
        menu.getContentPane().setBackground(Color.WHITE);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private int selectedIndex = -1;
    private ArrayList<Product> listProduct;

    private JPanel mainPanel;
    private JTextField idField;
    private JTextField skinNameField;
    private JTextField hargaField;
    private JTable productTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> gunComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel skinNameLabel;
    private JLabel hargaLabel;
    private JLabel gunLabel;
    private JSlider floatSlider;
    private JLabel floatValueLabel;

    public ProductMenu() {
        listProduct = new ArrayList<>();
        populateList();

        floatSlider.setMinimum(0);
        floatSlider.setMaximum(100);
        floatSlider.setValue(0);
        floatSlider.setPaintTicks(true);
        floatSlider.setMajorTickSpacing(20);
        floatSlider.setMinorTickSpacing(5);
        floatSlider.setPaintLabels(false);

        // Label untuk menampilkan float value
        double initialFloat = floatSlider.getValue() / 100.0;
        String initialWear = getWearFromFloat(initialFloat);
        floatValueLabel.setText(String.format("Float: %.2f (%s)", initialFloat, initialWear));

        // update label saat slider digeser
        floatSlider.addChangeListener(e -> {
            double floatValue = floatSlider.getValue() / 100.0;
            String wear = getWearFromFloat(floatValue);
            floatValueLabel.setText(String.format("Float: %.2f %s", floatValue, wear));
        });

        // isi tabel
        productTable.setModel(setTable());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // isi combo box kategori
        String[] kategoriData = {
                "— Pilih Senjata —",
                // Rifles
                "AK-47", "M4A4", "M4A1-S", "FAMAS", "Galil AR", "AUG", "SG 553",
                "AWP", "SSG 08", "SCAR-20", "G3SG1",
                // SMGs
                "UMP-45", "P90", "MP9", "MP7", "MP5-SD", "MAC-10", "PP-Bizon",
                // Heavy
                "Nova", "XM1014", "MAG-7", "Sawed-Off", "M249", "Negev",
                // Pistols
                "Glock-18", "USP-S", "P2000", "P250", "Five-SeveN", "CZ75-Auto", "Tec-9",
                "Dual Berettas", "Desert Eagle", "R8 Revolver",
                // Knife / Melee
                "Knife (Default)", "Bayonet", "Karambit", "M9 Bayonet", "Butterfly Knife",
                "Flip Knife", "Gut Knife", "Talon Knife", "Huntsman Knife", "Bowie Knife",
                "Falchion Knife", "Ursus Knife", "Nomad Knife", "Paracord Knife", "Skeleton Knife",
        };
        gunComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        deleteButton.setVisible(false);

        // Tombol add/update
        addUpdateButton.addActionListener(e -> {
            if (selectedIndex == -1) insertData();
            else updateData();
        });

        // Tombol delete
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Hapus data?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) deleteData();
        });

        // Tombol cancel
        cancelButton.addActionListener(e -> clearForm());

        // Klik tabel
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = productTable.getSelectedRow();
                String curId = productTable.getModel().getValueAt(selectedIndex, 0).toString();
                String curGun = productTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curSkin = productTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curHarga = productTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curWear = productTable.getModel().getValueAt(selectedIndex, 4).toString();

                idField.setText(curId);
                gunComboBox.setSelectedItem(curGun);
                skinNameField.setText(curSkin);
                hargaField.setText(curHarga);
                setSliderFromWear(curWear);

                addUpdateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });
    }

    private String getWearFromFloat(double f) {
        if (f >= 0.00 && f < 0.07) return "Factory New (FN)";
        else if (f >= 0.07 && f < 0.15) return "Minimal Wear (MW)";
        else if (f >= 0.15 && f < 0.38) return "Field-Tested (FT)";
        else if (f >= 0.38 && f < 0.45) return "Well-Worn (WW)";
        else return "Battle-Scarred (BS)";
    }

    private void setSliderFromWear(String wear) {
        if (wear.startsWith("Factory")) floatSlider.setValue(3);
        else if (wear.startsWith("Minimal")) floatSlider.setValue(10);
        else if (wear.startsWith("Field")) floatSlider.setValue(25);
        else if (wear.startsWith("Well")) floatSlider.setValue(40);
        else floatSlider.setValue(70);
    }

    public final DefaultTableModel setTable() {
        Object[] cols = {"ID", "Gun", "Skin Name", "Price", "Wear"};
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        for (int i = 0; i < listProduct.size(); i++) {
            Product p = listProduct.get(i);
            Object[] row = {
                    p.getId(),
                    p.getGun(),
                    p.getSkinName(),
                    p.getSkinPrice(),
                    p.getSkinWear()
            };
            tmp.addRow(row);
        }
        return tmp;
    }

    public void insertData() {
        try {
            String id = idField.getText();
            String gun = gunComboBox.getSelectedItem().toString();
            String skin = skinNameField.getText();
            double price = Double.parseDouble(hargaField.getText());
            double floatValue = floatSlider.getValue() / 100.0;
            String wear = getWearFromFloat(floatValue);

            listProduct.add(new Product(id, gun, skin, price, wear));
            productTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        try {
            String id = idField.getText();
            String gun = gunComboBox.getSelectedItem().toString();
            String skin = skinNameField.getText();
            double price = Double.parseDouble(hargaField.getText());
            double floatValue = floatSlider.getValue() / 100.0;
            String wear = getWearFromFloat(floatValue);

            Product p = listProduct.get(selectedIndex);
            p.setId(id);
            p.setGun(gun);
            p.setSkinName(skin);
            p.setSkinPrice(price);
            p.setSkinWear(wear);

            productTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        listProduct.remove(selectedIndex);
        productTable.setModel(setTable());
        clearForm();
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    public void clearForm() {
        idField.setText("");
        skinNameField.setText("");
        hargaField.setText("");
        gunComboBox.setSelectedIndex(0);
        floatSlider.setValue(0);
        floatValueLabel.setText("Float: 0.00");
        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        selectedIndex = -1;
    }

    private void populateList() {
        listProduct.add(new Product("1", "AK-47", "Redline", 245.00, "Field-Tested (FT)"));
        listProduct.add(new Product("2", "M4A1-S", "Printstream", 410.50, "Minimal Wear (MW)"));
        listProduct.add(new Product("3", "AWP", "Asiimov", 620.00, "Well-Worn (WW)"));
        listProduct.add(new Product("4", "Glock-18", "Water Elemental", 65.00, "Factory New (FN)"));
        listProduct.add(new Product("5", "USP-S", "Kill Confirmed", 320.00, "Minimal Wear (MW)"));
        listProduct.add(new Product("6", "Desert Eagle", "Blaze", 310.00, "Factory New (FN)"));
        listProduct.add(new Product("7", "P90", "Death by Kitty", 199.90, "Battle-Scarred (BS)"));
        listProduct.add(new Product("8", "Nova", "Antique", 75.25, "Minimal Wear (MW)"));
        listProduct.add(new Product("9", "MAC-10", "Neon Rider", 145.00, "Factory New (FN)"));
        listProduct.add(new Product("10", "FAMAS", "Mecha Industries", 95.00, "Field-Tested (FT)"));
        listProduct.add(new Product("11", "MP9", "Food Chain", 88.20, "Minimal Wear (MW)"));
        listProduct.add(new Product("12", "UMP-45", "Primal Saber", 95.00, "Field-Tested (FT)"));
        listProduct.add(new Product("13", "MAG-7", "Justice", 105.50, "Minimal Wear (MW)"));
        listProduct.add(new Product("14", "Sawed-Off", "The Kraken", 85.00, "Well-Worn (WW)"));
        listProduct.add(new Product("15", "Negev", "Power Loader", 95.00, "Minimal Wear (MW)"));
        listProduct.add(new Product("16", "Five-SeveN", "Monkey Business", 72.50, "Factory New (FN)"));
        listProduct.add(new Product("17", "Dual Berettas", "Cobra Strike", 50.00, "Field-Tested (FT)"));
        listProduct.add(new Product("18", "CZ75-Auto", "Victoria", 90.00, "Minimal Wear (MW)"));
        listProduct.add(new Product("19", "M4A4", "Howl", 1700.00, "Field-Tested (FT)"));
        listProduct.add(new Product("20", "Karambit", "Fade", 1850.00, "Factory New (FN)"));
    }
}
