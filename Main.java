package inventorySystem;

import inventorySystem.view.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static inventorySystem.view.MainScreenController.inv;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/mainScreen.fxml"));
        Parent root = loader.load();

        MainScreenController controller = loader.getController();

        InHouse screw = new InHouse(121, "Screw", .25, 500, 200, 800, 1);
        InHouse washer = new InHouse(129, "Washer", .50, 350, 200, 800, 1);
        Outsourced board = new Outsourced(201, "Board", 11.99, 50, 10, 100, "James Lumber");
        inv.addPart(screw);
        inv.addPart(washer);
        inv.addPart(board);

        Product table = new Product(2, "Table", 299.99, 2, 1,10);
        table.addAssociatedPart(screw);
        table.addAssociatedPart(washer);
        table.addAssociatedPart(board);
        inv.addProduct(table);

        primaryStage.setTitle("Inventory System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
