package delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.*;

@SpringBootApplication
public class Application {

    public static void createDefaultProducts() {
        ProductDao pd = new ProductDao();
        pd.save(new Product("pizza", 450));
        pd.save(new Product("coffee", 50));
    }

    public static void showProducts() {
        ProductDao pd = new ProductDao();
        List<Product> pp = pd.findAll();
        for(Product p: pp) {
            System.out.println(p.getName() + ": " + p.getPrice());
        }
    }

    public static void main(String[] args) {
        System.out.println("Application started");
        //createDefaultProducts();
        //showProducts();
        SpringApplication.run(Application.class, args);
    }
}
