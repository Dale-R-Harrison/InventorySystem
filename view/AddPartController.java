package inventorySystem.view;

import inventorySystem.InHouse;
import inventorySystem.MinMaxException;
import inventorySystem.Outsourced;
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

public class AddPartController implements Initializable {

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

    public int generatedPartId;

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

    public void saveButtonPushed(ActionEvent event) throws IOException {
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
                id = generatedPartId;
                name = partName.getText();
                price = parseDouble(partPrice.getText());
                stock = parseInt(partInv.getText());
                min = parseInt(partMin.getText());
                max = parseInt(partMax.getText());
                companyName = partCompanyName.getText();

                if (min > max)
                    throw new MinMaxException(" Min must be less than max.");

                controller.inv.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
            }

            if (machineField.isVisible()) {


                id = generatedPartId;
                name = partName.getText();
                price = parseDouble(partPrice.getText());
                stock = parseInt(partInv.getText());
                min = parseInt(partMin.getText());
                max = parseInt(partMax.getText());
                machineID = parseInt(partMachineId.getText());

                if (min > max)
                    throw new MinMaxException(" Min must be less than max.");

                controller.inv.addPart(new InHouse(id, name, price, stock, min, max, machineID));
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
        machineField.setVisible(true);

        partID.setDisable(true);

        partType = new ToggleGroup();
        this.inHouse.setToggleGroup(partType);
        this.outsourced.setToggleGroup(partType);

        this.inHouse.setSelected(true);
    }
}
