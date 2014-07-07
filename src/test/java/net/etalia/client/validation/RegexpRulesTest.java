package net.etalia.client.validation;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.etalia.client.json.EtaliaObjectMapper;
import net.etalia.jalia.TypeUtil;

import org.junit.Test;

public class RegexpRulesTest {

	@Test
	public void simpleRegexpTest() throws Exception {
		RegexpRule rule = new RegexpRule("name","^S.*$","i","INVALID","Simone");
		Map<String,String> vals = new HashMap<String, String>();
		{
			vals.put("name","Mario");
			List<ValidationMessage> res = rule.validate(vals, null);
			assertThat(res, notNullValue());
			assertThat(res, hasSize(1));
			ValidationMessage msg = res.get(0);
			assertThat(msg.message, equalTo("INVALID"));
			assertThat(msg.parameters.length, equalTo(1));
			assertThat(msg.parameters[0], equalTo("Simone"));
			assertThat(msg.property, equalTo("name"));
		}
		
		{
			vals.put("name","Simone");
			List<ValidationMessage> res = rule.validate(vals, null);
			assertThat(res, notNullValue());
			assertThat(res, hasSize(0));
		}
	}
	
	@Test
	public void multipleRegexpTest() throws Exception {
		EtaliaObjectMapper eom = new EtaliaObjectMapper();
		String json =
				"[\n" + 
				"{\n" + 
				"message: \"REQUIRED\",\n" + 
				"type: \"NotNull\",\n" + 
				"re: {\n" + 
				"notnull: [\n" + 
				"\".+\",\n" + 
				"null\n" + 
				"]\n" + 
				"},\n" + 
				"groups: [\n" + 
				"\"Default\",\n" + 
				"\"Naming\"\n" + 
				"]\n" + 
				"},\n" + 
				"{\n" + 
				"message: \"LENGTH\",\n" + 
				"type: \"Length\",\n" + 
				"re: {\n" + 
				"length: [\n" + 
				"\"^$|^[\\\\s\\\\S]{2,40}$\",\n" + 
				"\"\"\n" + 
				"]\n" + 
				"},\n" + 
				"groups: [\n" + 
				"\"Default\"\n" + 
				"]" +
				"}" +
				"]"+
				"";
		List jsonParsed = eom.readValue(json, List.class);
		MultipleRegexpRules rule = new MultipleRegexpRules("name", jsonParsed);
		Map<String,String> vals = new HashMap<String, String>();
		{
			vals.put("name","M");
			List<ValidationMessage> res = rule.validate(vals, null);
			assertThat(res, notNullValue());
			assertThat(res, hasSize(1));
			ValidationMessage msg = res.get(0);
			assertThat(msg.message, equalTo("LENGTH"));
			assertThat(msg.property, equalTo("name"));
		}
		{
			vals.put("name","");
			List<ValidationMessage> res = rule.validate(vals, null);
			assertThat(res, notNullValue());
			assertThat(res, hasSize(1));
			ValidationMessage msg = res.get(0);
			assertThat(msg.message, equalTo("REQUIRED"));
			assertThat(msg.property, equalTo("name"));
		}
		
		{
			vals.put("name","Simone");
			List<ValidationMessage> res = rule.validate(vals, null);
			assertThat(res, notNullValue());
			assertThat(res, hasSize(0));
		}
		
	}

}
