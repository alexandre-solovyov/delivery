package delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.out.println("Application started");
        //createDefaultProducts();
        //showProducts();
        //excelProducts();
        SpringApplication.run(Application.class, args);
    }
}
