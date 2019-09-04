package delivery;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class BasicTest {

	@Autowired
	private MockMvc mockMvc;

	private Cookie[] myCookies;
	
	private final String EXC = "Exception: ";

	public static String postprocess(String json) {

		json = json.replace("\"", "");
		json = json.replace(":", ": ");
		json = json.replace(",", ", ");
		return json;
	}
	
	public void cleanCookies() {
		myCookies = null;
	}
	
	public String postprocess(MockHttpServletResponse response) {

		myCookies = response.getCookies();
		
		String json = "";
		try {
			json = response.getStatus() + " " + response.getContentAsString();
		} catch(Exception e) {
			json = EXC + e.getMessage();
		}
		return postprocess(json);
	}

	public String GET(String addr) {

		MockHttpServletRequestBuilder builder = get(addr);
		if(myCookies!=null && myCookies.length>0)
			builder.cookie(myCookies);
		
		try {
			ResultActions result = mockMvc.perform(builder);
			MockHttpServletResponse response = result.andReturn().getResponse();
			return postprocess(response);
		} catch(Exception e) {
			return EXC + e.getMessage();
		}
	}

	public String POST(String addr, Map<String, Object> params,
			           String login, String password) {
		
		return REQUEST(addr, params, login, password, 1);
	}

	public String PATCH(String addr, Map<String, Object> params,
	           String login, String password) {

		return REQUEST(addr, params, login, password, 2);
	}
	
		
	public String REQUEST(String addr, Map<String, Object> params,
		           String login, String password, int mode) {
		
		/*
		MockHttpServletRequestBuilder builder = post(addr);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			builder = builder.requestAttr(entry.getKey(), entry.getValue());
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}*/
		
		StringBuilder req = new StringBuilder();
		req.append(addr);
		req.append("?");
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			req.append(entry.getKey());
			req.append("=");
			req.append(entry.getValue());
			req.append("&");
		}
		String request = req.substring(0, req.length()-1);
		MockHttpServletRequestBuilder builder = null;
		if(mode==1)
			builder = post(request);
		else if(mode==2)
			builder = patch(request);
		
		if(myCookies!=null && myCookies.length>0)
			builder.cookie(myCookies);

		if(login.length()>0 && password.length()>0) {
			String credentials = login + ":" + password;
			byte[] credEncoded = Base64.getEncoder().encode(credentials.getBytes());
			String encoded = new String(credEncoded, StandardCharsets.UTF_8);
			String token = "Basic " + encoded;
			builder = builder.header("Authorization", token);
		}
		
		try {
			ResultActions result = mockMvc.perform(builder);
			MockHttpServletResponse response = result.andReturn().getResponse();
			return postprocess(response);
		} catch(Exception e) {
			return EXC + e.getMessage();
		}
	}
}
