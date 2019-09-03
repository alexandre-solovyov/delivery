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

	public static String postprocess(String json) {

		json = json.replace("\"", "");
		json = json.replace(":", ": ");
		json = json.replace(",", ", ");
		return json;
	}
	
	public String postprocess(MockHttpServletResponse response) throws Exception {

		myCookies = response.getCookies();
		
		String json = response.getStatus() + " " + response.getContentAsString();
		return postprocess(json);
	}

	public String GET(String addr) throws Exception {

		MockHttpServletRequestBuilder builder = get(addr);
		if(myCookies!=null && myCookies.length>0)
			builder.cookie(myCookies);
		ResultActions result = mockMvc.perform(builder);
		MockHttpServletResponse response = result.andReturn().getResponse();
		return postprocess(response);
	}

	public String POST(String addr, Map<String, Object> params,
			           String login, String password) throws Exception {

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
		MockHttpServletRequestBuilder builder = post(request);
		if(myCookies!=null && myCookies.length>0)
			builder.cookie(myCookies);

		if(login.length()>0 && password.length()>0) {
			String credentials = login + ":" + password;
			byte[] credEncoded = Base64.getEncoder().encode(credentials.getBytes());
			String encoded = new String(credEncoded, StandardCharsets.UTF_8);
			String token = "Basic " + encoded;
			builder = builder.header("Authorization", token);
		}
		
		ResultActions result = mockMvc.perform(builder);
		MockHttpServletResponse response = result.andReturn().getResponse();
		return postprocess(response);
	}
}
