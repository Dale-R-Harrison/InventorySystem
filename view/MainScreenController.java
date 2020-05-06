package inventorySystem.view;

import inventorySystem.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn<Part, Integer> partIdColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partStockColumn;
    @FXML
    private TableColumn<Part, Integer> partPriceColumn;

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Part, Integer> productIdColumn;
    @FXML
    private TableColumn<Part, String> productNameColumn;
    @FXML
    private TableColumn<Part, Integer> productStockColumn;
    @FXML
    private TableColumn<Part, Integer> productPriceColumn;

    @FXML Button closeBttn;

    @FXML
    private TextField partSearch;
    @FXML
    private TextField productSearch;

    public static Inventory inv = new Inventory();
    private static ArrayList<Integer> idList = new ArrayList<Integer>();

    public void setAddPartBttnPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addPart.fxml"));
        Parent addPartParent = loader.load();
        Scene addPartScene = new Scene(addPartParent);

        AddPartController controller = loader.getController();
        controller.generatedPartId = generateId();

        //Gets info for stage
        Stage newScreen = (Stage) ((Node)event.getSource()).getScene().getWindow();
        newScreen.setScene(addPartScene);
        newScreen.show();
    }

    public void modPartBttnPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modPart.fxml"));
        Parent modPartParent = loader.load();
        Scene modProdScene = new Scene(modPartParent);

        ModPartController controller = loader.getController();
        Stage newScreen = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Part partToMod = partsTable.getSelectionModel().getSelectedItem();
        controller.indexToMod = partsTable.getSelectionModel().getSelectedIndex();
        if (partToMod != null) {
            if (partToMod instanceof InHouse) {
                controller.setTextBoxesInHouse(partToMod.getId(), partToMod.getName(), partToMod.getStock(),
                        partToMod.getPrice(), partToMod.getMax(), partToMod.getMin(), ((InHouse) partToMod).getMachineId());
            }

            if (partToMod instanceof Outsourced) {
                controller.setTextBoxesOutsourced(partToMod.getId(), partToMod.getName(), partToMod.getStock(),
                        partToMod.getPrice(), partToMod.getMax(), partToMod.getMin(), ((Outsourced) partToMod).getCompanyName());
            }

            newScreen.setScene(modProdScene);
            newScreen.show();
        }
    }

    public void delPartBttnPressed() {
        Part partToDelete;

        partToDelete = (Part) partsTable.getSelectionModel().getSelectedItem();
        inv.removePart(partToDelete);
    }

    public void addProductBttnPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addProduct.fxml"));
        Parent addProdParent = loader.load();
        Scene addProdScene = new Scene(addProdParent);

        AddProductController controller = loader.getController();
        controller.generatedProdId = generateId();

        ObservableList<Part> list = inv.getAllParts();
        for(Part part : list) {
            controller.allPartsList.add(part);
        }

        //Gets info for stage
        Stage newScreen = (Stage) ((Node)event.getSource()).getScene().getWindow();
        newScreen.setScene(addProdScene);
        newScreen.show();
    }

    public void modProductBttnPressed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modProduct.fxml"));
        Parent modProdParent = loader.load();
        Scene modProdScene = new Scene(modProdParent);

        ModProductController controller = loader.getController();
        Stage newScreen = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Product productToMod = productTable.getSelectionModel().getSelectedItem();
        controller.indexToMod = productTable.getSelectionModel().getSelectedIndex();

        controller.setTextBoxes(productToMod.getId(), productToMod.getName(), productToMod.getStock(),
                productToMod.getPrice(), productToMod.getMax(), productToMod.getMin());

        ObservableList<Part> allParts = inv.getAllParts();
        for(Part part : allParts) {
            controller.allPartsList.add(part);
        }

        ObservableList<Part> assocParts = productToMod.getAllAssociatedParts();
        for(Part part : assocParts) {
            controller.addPartsList.add(part);
        }

        newScreen.setScene(modProdScene);
        newScreen.show();
    }

    public void delProductBttnPressed() {
        Product productToDelete;

        productToDelete = (Product) productTable.getSelectionModel().getSelectedItem();
        inv.removeProduct(productToDelete);
    }

    public void exitBttnPressed(ActionEvent event) {
        Stage stage = (Stage) closeBttn.getScene().getWindow();
        stage.close();
    }

    public void partSearchBttnPressed() {
        ObservableList<Part> resultsList = FXCollections.observableArrayList();
        String searchText = partSearch.getText();

        for(Part part : inv.getAllParts()) {
            String textToSearch = part.getName() + String.valueOf(part.getId());

            if (textToSearch.contains(searchText)) {
                resultsList.add(part);
            }
        }

        partsTable.setItems(resultsList);
    }

    public void productSearchBttnPressed() {
        ObservableList<Product> resultsList = FXCollections.observableArrayList();
        String searchText = productSearch.getText();

        for(Product product : inv.getAllProducts()) {
            String textToSearch = product.getName() + String.valueOf(product.getId());

            if (textToSearch.contains(searchText)) {
                resultsList.add(product);
            }
        }

        productTable.setItems(resultsList);
    }

    private int generateId() {
        Integer newId = -1;

        while ((newId.intValue() < 0) || (idList.contains(newId))) {
            Random potentialId = new Random();
            newId = potentialId.nextInt(1000);
        }

        idList.add(newId);
        return newId.intValue();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTable.setItems(inv.getAllParts());
        productTable.setItems(inv.getAllProducts());
    }
}
