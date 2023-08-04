package bikemanagementsystemgui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class BikeManagementSystemGUI extends JFrame {
    private static final String FILE_PATH = "bikes.txt";
    private final JTextField bikeNameTextField;
    private final JTextField bikeColorTextField;
    private final JTextField bikePriceTextField;
    private final JTextArea bikesTextArea;
    private ArrayList<String> bikeList;

    public BikeManagementSystemGUI() {
        
        setTitle("H K Motors");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        
        JLabel nameLabel = new JLabel("Bike Name:");
        bikeNameTextField = new JTextField(20);

        JLabel colorLabel = new JLabel("Bike Color:");
        bikeColorTextField = new JTextField(20);

        JLabel priceLabel = new JLabel("Bike Price:");
        bikePriceTextField = new JTextField(20);

        JButton addButton = new JButton("Add Bike");
        addButton.addActionListener(e -> addBike());

        JButton viewButton = new JButton("View Bikes");
        viewButton.addActionListener(e -> viewBikes());

        JButton deleteButton = new JButton("Delete Bike");
        deleteButton.addActionListener(e -> deleteBike());

        JButton updateButton = new JButton("Update Bike");
        updateButton.addActionListener(e -> updateBike());

        
        bikesTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(bikesTextArea);
        bikesTextArea.setEditable(false);

        
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        add(nameLabel, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        add(colorLabel, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        add(priceLabel, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        add(bikeNameTextField, gridBagConstraints);

        gridBagConstraints.gridy = 1;
        add(bikeColorTextField, gridBagConstraints);

        gridBagConstraints.gridy = 2;
        add(bikePriceTextField, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        add(addButton, gridBagConstraints);

        gridBagConstraints.gridy = 4;
        add(viewButton, gridBagConstraints);

        gridBagConstraints.gridy = 5;
        add(deleteButton, gridBagConstraints);

        gridBagConstraints.gridy = 6;
        add(updateButton, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(scrollPane, gridBagConstraints);
    }

    private void addBike() {
        String bikeName = bikeNameTextField.getText();
        String bikeColor = bikeColorTextField.getText();
        String bikePrice = bikePriceTextField.getText();

        if (!bikeName.isEmpty() && !bikeColor.isEmpty() && !bikePrice.isEmpty()) {
            try (FileWriter writer = new FileWriter(FILE_PATH, true);
                    BufferedWriter bufferedWriter = new BufferedWriter(writer);
                    PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

                printWriter.println(bikeName + "," + bikeColor + "," + bikePrice);
                bikesTextArea.append("Bike added successfully!\n");

                
                bikeNameTextField.setText("");
                bikeColorTextField.setText("");
                bikePriceTextField.setText("");

            } catch (IOException ex) {
                bikesTextArea.append("An error occurred while adding the bike: " + ex.getMessage() + "\n");
            }
        } else {
            bikesTextArea.append("Please fill in all fields.\n");
        }
    }

    private void displayBikes() {
        bikesTextArea.setText("");

        
        Collections.sort(bikeList);

        for (String bikeData : bikeList) {
            String[] bikeInfo = bikeData.split(",");
            String bikeName = bikeInfo[0];
            String bikeColor = bikeInfo[1];
            String bikePrice = bikeInfo[2];

            bikesTextArea.append("Name: " + bikeName + ", Color: " + bikeColor + ", Price: " + bikePrice + "\n");
        }
    }

    private void viewBikes() {
        bikeList = new ArrayList<>();

        try (FileReader reader = new FileReader(FILE_PATH);
                BufferedReader bufferedReader = new BufferedReader(reader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bikeList.add(line);
            }

            displayBikes();

        } catch (IOException e) {
            bikesTextArea.append("An error occurred while viewing the bikes: " + e.getMessage() + "\n");
        }
    }

    private void deleteBike() {
        String bikeNameToDelete = bikeNameTextField.getText();

        if (!bikeNameToDelete.isEmpty()) {
            try {
                File inputFile = new File(FILE_PATH);
                File tempFile = new File("temp.txt");

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String line;
                boolean bikeDeleted = false;

                while ((line = reader.readLine()) != null) {
                    String[] bikeData = line.split(",");
                    String bikeName = bikeData[0];

                    if (bikeName.equals(bikeNameToDelete)) {
                        bikeDeleted = true;
                        continue; 
                    }

                    writer.write(line + System.lineSeparator());
                }

                reader.close();
                writer.close();

                
                inputFile.delete();
                tempFile.renameTo(inputFile);

                if (bikeDeleted) {
                    bikesTextArea.append("Bike deleted successfully!\n");
                } else {
                    bikesTextArea.append("Bike not found.\n");
                }

                
                bikeNameTextField.setText("");

            } catch (IOException ex) {
                bikesTextArea.append("An error occurred while deleting the bike: " + ex.getMessage() + "\n");
            }
        } else {
            bikesTextArea.append("Please enter the name of the bike to delete.\n");
        }
    }

    private void updateBike() {
        String bikeNameToUpdate = bikeNameTextField.getText();
        String bikeColor = bikeColorTextField.getText();
        String bikePrice = bikePriceTextField.getText();

        if (!bikeNameToUpdate.isEmpty() && !bikeColor.isEmpty() && !bikePrice.isEmpty()) {
            try {
                File inputFile = new File(FILE_PATH);
                File tempFile = new File("temp.txt");

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String line;
                boolean bikeUpdated = false;

                while ((line = reader.readLine()) != null) {
                    String[] bikeData = line.split(",");
                    String bikeName = bikeData[0];

                    if (bikeName.equals(bikeNameToUpdate)) {
                      
                        line = bikeNameToUpdate + "," + bikeColor + "," + bikePrice;
                        bikeUpdated = true;
                    }

                    writer.write(line + System.lineSeparator());
                }

                reader.close();
                writer.close();

                
                inputFile.delete();
                tempFile.renameTo(inputFile);

                if (bikeUpdated) {
                    bikesTextArea.append("Bike updated successfully!\n");
                } else {
                    bikesTextArea.append("Bike not found.\n");
                }

                
                bikeNameTextField.setText("");
                bikeColorTextField.setText("");
                bikePriceTextField.setText("");

            } catch (IOException ex) {
                bikesTextArea.append("An error occurred while updating the bike: " + ex.getMessage() + "\n");
            }
        } else {
            bikesTextArea.append("Please fill in all fields.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BikeManagementSystemGUI gui = new BikeManagementSystemGUI();
            gui.setVisible(true);
   });
}
}
