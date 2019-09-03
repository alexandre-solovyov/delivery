package delivery;

import org.springframework.stereotype.Component;

@Component
class Greeting {
	
    private final String result = "Delivery service is available";

    public String getResult() { return result; }
}
