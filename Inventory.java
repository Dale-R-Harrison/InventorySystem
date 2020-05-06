package inventorySystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private ObservableList<Part> allParts;
    private ObservableList<Product> allProducts;

    public Inventory() {
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
    }

    public void addPart(Part part) {
        allParts.add(part);
    }

    public void addProduct(Product product) {
        allProducts.add(product);
    }

    public Part lookupPart(int id) {
        for(Part part : allParts) {
            if (part.getId() == id) {
                return part;
            }
        }

        return null;
    }
    public Part lookupPart(String name) {
        for(Part part : allParts) {
            if (part.getName() == name)
                return part;
        }
        return null;
    }

    public Product lookupProduct(int id) {
        for (Product product : allProducts) {
            if (product.getId() == id)
                return product;
        }

        return null;
    }
    public Product lookupProduct(String name) {
        for (Product product: allProducts) {
            if (product.getName() == name)
                return product;
        }

        return null;
    }

    public void updatePart(int index, Part newPart) {
        if (index >= 0 && allParts.size() - 1 >= index) {
            allParts.remove(index);
            allParts.add(index, newPart);
        }
    }

    public void updateProduct(int index, Product newProduct) {
        if (index >= 0 && allProducts.size() - 1 >= index) {
            allProducts.remove(index);
            allProducts.add(index, newProduct);
        }
    }

    public void removePart (Part selectedPart) {
        allParts.remove(selectedPart);
    }
    public void removeProduct (Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }

    public ObservableList<Part> getAllParts() {
        return allParts;
    }
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public void setAllParts(ObservableList<Part> allParts) {
        this.allParts = allParts;
    }

    public void setAllProducts(ObservableList<Product> allProducts) {
        this.allProducts = allProducts;
    }
}
