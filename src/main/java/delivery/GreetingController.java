package delivery;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

class Greeting {
    private final String result = "Delivery service is available";

    public String getResult() { return result; }
}

@RestController
public class GreetingController {

    @RequestMapping("/")
    public Greeting greeting() { return new Greeting(); }
}
