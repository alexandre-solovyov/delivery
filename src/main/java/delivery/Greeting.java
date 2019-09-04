package delivery;

import org.springframework.stereotype.Component;

@Component
class Greeting {

	public String getResult() {
		return Messages.HELLO;
	}
}
