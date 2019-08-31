package delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void createDefaultProducts() {
        ProductDao pd = new ProductDao();
        pd.save(new Product());
        //p.save(Product.create("coffee", 50));
    }

    public static void main(String[] args) {
        System.out.println("Application started");
        createDefaultProducts();
        //SpringApplication.run(Application.class, args);
    }
}
