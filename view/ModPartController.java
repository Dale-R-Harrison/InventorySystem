package inventorySystem.view;

import inventorySystem.InHouse;
import inventorySystem.MinMaxException;
import inventorySystem.Outsourced;
import inventorySystem.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class ModPartController implements Initializable {
    @FXML
    private TextField partID;
    @FXML
    private TextField partName;
    @FXML
    private TextField partInv;
    @FXML
    private TextField partPrice;
    @FXML
    private TextField partMin;
    @FXML
    private TextField partMax;
    @FXML
    private TextField partCompanyName;
    @FXML
    private TextField partMachineId;

    @FXML
    private HBox companyNameField;
    @FXML
    private HBox machineField;

    @FXML
    private RadioButton outsourced;
    @FXML
    private RadioButton inHouse;
    private ToggleGroup partType;

    public int indexToMod;

    public void setTextBoxesInHouse(int id, String name, int stock, double price, int max, int min, int machineId) {
        partID.setText(String.valueOf(id));
        partName.setText(name);
        partInv.setText(String.valueOf(stock));
        partPrice.setText(String.valueOf(price));
        partMax.setText(String.valueOf(max));
        partMin.setText(String.valueOf(min));
        partMachineId.setText(String.valueOf(machineId));

        machineField.setVisible(true);
        inHouse.setSelected(true);
    }

    public void setTextBoxesOutsourced(int id, String name, int stock, double price, int max, int min, String companyName) {
        partID.setText(String.valueOf(id));
        partName.setText(name);
        partInv.setText(String.valueOf(stock));
        partPrice.setText(String.valueOf(price));
        partMax.setText(String.valueOf(max));
        partMin.setText(String.valueOf(min));
        partCompanyName.setText(companyName);

        companyNameField.setVisible(true);
        outsourced.setSelected(true);
    }

    public void outsourcedBttnChanged() {
        if (this.partType.getSelectedToggle().equals(this.outsourced)) {
            companyNameField.setVisible(true);
            machineField.setVisible(false);
        }

        if (this.partType.getSelectedToggle().equals(this.inHouse)) {
            companyNameField.setVisible(false);
            machineField.setVisible(true);
        }
    }

    public void saveButtonPushed(ActionEvent event) throws IOException, MinMaxException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("mainScreen.fxml"));
            Parent mainParent = loader.load();
            Scene mainScene = new Scene(mainParent);

            MainScreenController controller = loader.getController();

            int id;
            String name;
            double price;
            int stock;
            int min;
            int max;
            String companyName;
            int machineID;

            if (companyNameField.isVisible()) {
                id = parseInt(partID.getText());
                name = partName.getText();
                price = parseDouble(partPrice.getText());
                stock = parseInt(partInv.getText());
                min = parseInt(partMin.getText());
                max = parseInt(partMax.getText());
                companyName = partCompanyName.getText();

                if (min > max)
                    throw new MinMaxException(" Min must be less than max.");

                Outsourced newPart = new Outsourced(id, name, price, stock, min, max, companyName);
                controller.inv.updatePart(indexToMod, newPart);
            }

            if (machineField.isVisible()) {


                id = parseInt(partID.getText());
                name = partName.getText();
                price = parseDouble(partPrice.getText());
                stock = parseInt(partInv.getText());
                min = parseInt(partMin.getText());
                max = parseInt(partMax.getText());
                machineID = parseInt(partMachineId.getText());

                if (min > max)
                    throw new MinMaxException(" Min must be less than max.");

                InHouse newPart = new InHouse(id, name, price, stock, min, max, machineID);
                controller.inv.updatePart(indexToMod, newPart);
            }

            //Gets info for stage
            Stage newScreen = (Stage) ((Node) event.getSource()).getScene().getWindow();
            newScreen.setScene(mainScene);
            newScreen.show();
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companyNameField.managedProperty().bind(companyNameField.visibleProperty());
        companyNameField.setVisible(false);

        machineField.managedProperty().bind(machineField.visibleProperty());
        machineField.setVisible(false);

        partID.setDisable(true);

        partType = new ToggleGroup();
        this.inHouse.setToggleGroup(partType);
        this.outsourced.setToggleGroup(partType);
    }
}
