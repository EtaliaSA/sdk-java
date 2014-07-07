package net.etalia.client.validation;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.etalia.client.domain.User;

import org.junit.Test;

public class ValidatorTest {

	String userJson = "{\"flickr\":[{\"message\":\"_URL_MALFORMED\",\"type\":\"URL\",\"re\":{\"url\":[\"^$|^https?://[-'\\\\p{L}\\\\u2019\\\\p{Nd}+&@#/\\\\$\\\\(\\\\)%?=~_|!:,\\\\.;]*\",\"i\"]},\"groups\":[\"Default\"]}],\"title\":[{\"message\":\"_REQUIRED\",\"type\":\"NotNull\",\"re\":{\"notnull\":[\".+\",\"\"]},\"groups\":[\"Default\",\"Titled\"]},{\"message\":\"_LENGTH_TITLE\",\"type\":\"Length\",\"re\":{\"length\":[\"^$|^[\\\\s\\\\S]{2,40}$\",\"\"]},\"groups\":[\"Default\"]},{\"message\":\"_LENGTH_TITLE\",\"type\":\"Length\",\"re\":{\"length\":[\"^$|^[\\\\s\\\\S]{0,255}$\",\"\"]},\"groups\":[\"Default\",\"Titled\"]}],\"website\":[{\"message\":\"_URL_MALFORMED\",\"type\":\"URL\",\"re\":{\"url\":[\"^$|^https?://[-'\\\\p{L}\\\\u2019\\\\p{Nd}+&@#/\\\\$\\\\(\\\\)%?=~_|!:,\\\\.;]*\",\"i\"]},\"groups\":[\"Default\"]}],\"email\":[{\"message\":\"_REQUIRED\",\"type\":\"NotNull\",\"re\":{\"notnull\":[\".+\",\"\"]},\"groups\":[\"Default\"]},{\"message\":\"_EMAIL\",\"type\":\"Email\",\"re\":{\"email\":[\"^$|^[a-zA-Z0-9._+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,4}$\",\"i\"]},\"groups\":[\"Default\"]}],\"description\":[{\"message\":\"_LENGTH\",\"type\":\"Length\",\"re\":{\"length\":[\"^$|^[\\\\s\\\\S]{0,500}$\",\"\"]},\"groups\":[\"Default\"]}],\"passwordValidation\":[{\"message\":\"_REQUIRED\",\"type\":\"NotNull\",\"re\":{\"notnull\":[\".+\",\"\"]},\"groups\":[\"Default\"]},{\"message\":\"_LENGTH_PASSWORD\",\"type\":\"Length\",\"re\":{\"length\":[\"^$|^[\\\\s\\\\S]{6,20}$\",\"\"]},\"groups\":[\"Default\"]}],\"youtube\":[{\"message\":\"_URL_MALFORMED\",\"type\":\"URL\",\"re\":{\"url\":[\"^$|^https?://[-'\\\\p{L}\\\\u2019\\\\p{Nd}+&@#/\\\\$\\\\(\\\\)%?=~_|!:,\\\\.;]*\",\"i\"]},\"groups\":[\"Default\"]}],\"lastname\":[{\"message\":\"_LENGTH\",\"type\":\"Length\",\"re\":{\"length\":[\"^$|^[\\\\s\\\\S]{2,20}$\",\"\"]},\"groups\":[\"Default\"]}],\"gender\":[{\"message\":\"_REQUIRED\",\"type\":\"NotNull\",\"re\":{\"notnull\":[\".+\",\"\"]},\"groups\":[\"Default\"]}],\"firstname\":[{\"message\":\"_LENGTH\",\"type\":\"Length\",\"re\":{\"length\":[\"^$|^[\\\\s\\\\S]{2,20}$\",\"\"]},\"groups\":[\"Default\"]}],\"lang\":[{\"message\":\"_REQUIRED\",\"type\":\"NotNull\",\"re\":{\"notnull\":[\".+\",\"\"]},\"groups\":[\"Default\"]}]}"; 
	
	@Test
	public void testUser() throws Exception {
		ValidatorFactory factory = new ValidatorFactory();
		factory.parseRules(User.class, new StringReader(userJson));
		
		Validator val = factory.getValidator(User.class);
		assertThat(val, notNullValue());
		
		Map<String,String> vals = new HashMap<String, String>();
		{
			vals.clear();
			List<ValidationMessage> msgs = val.validate(vals);
			assertThat(msgs, notNullValue());
			System.out.println(msgs.size() + " : " + msgs);
			assertThat(msgs, hasSize(5));
			for (ValidationMessage msg : msgs) {
				assertThat(msg, notNullValue());
				assertThat(msg.message, equalTo("_REQUIRED"));
			}
		}
		
		{
			vals.clear();
			List<ValidationMessage> msgs = val.validate("email","");
			assertThat(msgs, notNullValue());
			System.out.println(msgs.size() + " : " + msgs);
			assertThat(msgs, hasSize(1));
			ValidationMessage msg = msgs.get(0);
			assertThat(msg, notNullValue());
			assertThat(msg.message, equalTo("_REQUIRED"));
		}

		{
			vals.clear();
			val.setGroups("Titled");
			List<ValidationMessage> msgs = val.validate(vals);
			assertThat(msgs, notNullValue());
			System.out.println(msgs.size() + " : " + msgs);
			assertThat(msgs, hasSize(1));
			for (ValidationMessage msg : msgs) {
				assertThat(msg, notNullValue());
				assertThat(msg.message, equalTo("_REQUIRED"));
			}
			val.setGroups();
			msgs = val.validate(vals);
			assertThat(msgs, notNullValue());
			System.out.println(msgs.size() + " : " + msgs);
			assertThat(msgs, hasSize(5));			
		}
	}
	
	@Test
	public void sameValueTest() throws Exception {
		ValidatorFactory factory = new ValidatorFactory();
		Validator val = factory.getValidator(User.class);
		
		val.addRule(new Rule() {
			public List<ValidationMessage> validate(Map<String, String> properties, Validator validator) {
				if (!properties.containsKey("password") || !properties.containsKey("password2")) return Collections.emptyList();
				if (properties.get("password").equals(properties.get("password2"))) return Collections.emptyList();
				ValidationMessage msg = new ValidationMessage();
				msg.message = "CHECKPWD2";
				return Arrays.asList(msg);
			}
		});
		
		
		Map<String,String> vals = new HashMap<String, String>();
		{
			vals.clear();
			List<ValidationMessage> msgs = val.validate(vals);
			assertThat(msgs, notNullValue());
			assertThat(msgs, hasSize(0));
		}
		{
			vals.clear();
			vals.put("password" , "abc1234");
			vals.put("password2", "abc1235");
			List<ValidationMessage> msgs = val.validate(vals);
			assertThat(msgs, notNullValue());
			assertThat(msgs, hasSize(1));
			ValidationMessage msg = msgs.get(0);
			assertThat(msg, notNullValue());
			assertThat(msg.message, equalTo("CHECKPWD2"));
		}
		{
			vals.clear();
			vals.put("password" , "abc1234");
			vals.put("password2", "abc1234");
			List<ValidationMessage> msgs = val.validate(vals);
			assertThat(msgs, notNullValue());
			assertThat(msgs, hasSize(0));
		}
	}

}
