package inventorySystem.view;

import inventorySystem.MinMaxException;
import inventorySystem.Part;
import inventorySystem.PartsTooHighException;
import inventorySystem.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class AddProductController implements Initializable {
    @FXML
    private TextField productId;
    @FXML
    private TextField productName;
    @FXML
    private TextField productInv;
    @FXML
    private TextField productPrice;
    @FXML
    private TextField productMin;
    @FXML
    private TextField productMax;

    @FXML
    private TableView<Part> allPartsTable;
    @FXML
    private TableColumn<Part, Integer> allPartsIdColumn;
    @FXML
    private TableColumn<Part, String> allPartsNameColumn;
    @FXML
    private TableColumn<Part, Integer> allPartsStockColumn;
    @FXML
    private TableColumn<Part, Double> allPartsPriceColumn;

    @FXML
    private TableView<Part> addPartsTable;
    @FXML
    private TableColumn<Part, Integer> addPartsIdColumn;
    @FXML
    private TableColumn<Part, String> addPartsNameColumn;
    @FXML
    private TableColumn<Part, Integer> addPartsStockColumn;
    @FXML
    private TableColumn<Part, Double> addPartsPriceColumn;

    @FXML
    private TextField partSearch;

    public ObservableList<Part> allPartsList = FXCollections.observableArrayList();
    public ObservableList<Part> addPartsList = FXCollections.observableArrayList();

    public int generatedProdId;

    public void saveBttnPushed(ActionEvent event) throws IOException, MinMaxException {
        try {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("mainScreen.fxml"));
                Parent mainParent = loader.load();
                Scene mainScene = new Scene(mainParent);

                MainScreenController controller = loader.getController();

                int id = generatedProdId;
                String name = productName.getText();
                double price = parseDouble(productPrice.getText());
                int stock = parseInt(productInv.getText());
                int min = parseInt(productMin.getText());
                int max = parseInt(productMax.getText());

                if (min > max)
                    throw new MinMaxException(" Min must be less than max.");

                Product productToAdd = new Product(id, name, price, stock, min, max);

                for (Part part : addPartsList) {
                    productToAdd.addAssociatedPart(part);
                }

                double partsPrice = 0;
                for(Part part : productToAdd.getAllAssociatedParts()) {
                    partsPrice += part.getPrice();
                }
                if (partsPrice > productToAdd.getPrice())
                        throw new PartsTooHighException("Total cost of associated parts cannot exceed the price of product.");

                controller.inv.addProduct(productToAdd);

                Stage newScreen = (Stage) ((Node) event.getSource()).getScene().getWindow();
                newScreen.setScene(mainScene);
                newScreen.show();
            } catch (PartsTooHighException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Input Error");
                alert.setContentText("Total cost of associated parts cannot exceed the price of product.");
                alert.showAndWait();
            }
        } catch(MinMaxException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setContentText("Min amount cannot be greater than the max amount.");
            alert.showAndWait();
        }
    }

    public void cancelBttnPushed(ActionEvent event) throws IOException {
        Parent mainParent = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        Scene mainScene = new Scene(mainParent);

        //Gets info for stage
        Stage newScreen = (Stage) ((Node)event.getSource()).getScene().getWindow();
        newScreen.setScene(mainScene);
        newScreen.show();
    }

    public void addBttnPushed() {
        Part selectedPart = allPartsTable.getSelectionModel().getSelectedItem();
        Boolean partIncluded = false;

        for(Part part : addPartsList) {
            if (part.getId() == selectedPart.getId())
                    partIncluded = true;
        }

        if((selectedPart != null) && (!(partIncluded))) {
            addPartsList.add(selectedPart);
        }
    }

    public void delBttnPushed() {
        Part selectedPart = addPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart != null)
            addPartsList.remove(selectedPart);
    }

    public void partSearchBttnPressed() {
        ObservableList<Part> resultsList = FXCollections.observableArrayList();
        String searchText = partSearch.getText();

        for(Part part : allPartsList) {
            String textToSearch = part.getName() + String.valueOf(part.getId());

            if (textToSearch.contains(searchText)) {
                resultsList.add(part);
            }
        }

        allPartsTable.setItems(resultsList);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productId.setDisable(true);

        allPartsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        addPartsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addPartsStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        allPartsTable.setItems(allPartsList);
        addPartsTable.setItems(addPartsList);
    }
}
