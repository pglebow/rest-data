/**
 * 
 */
package com.glebow.demo.util;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.json.JSONArray;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glebow.demo.domain.User;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * @author pglebow
 *
 */
public class Util {

	/**
	 * Json Document parser
	 * @param r
	 * @throws IOException
	 */
//	public static List<Document> parseJson(URL url) throws IOException {
//		List<Document> retVal = null;
//		if ( url != null ) {
//			String jsonText = Resources.toString(url, Charsets.UTF_8);
//			JSONArray array = new JSONArray(jsonText);
//
//			retVal = new ArrayList<Document>(array.length());
//			for ( int i = 0; i < array.length(); i++ ) {
//				JSONObject o = array.getJSONObject(i);
//				Document d = Document.parse(o.toString());
//				retVal.add(d);
//			}			
//		}
//		return Optional.ofNullable(retVal).orElse(Collections.emptyList());
//	}
	
	/**
	 * Json Document parser
	 * @param r
	 * @throws IOException
	 */
	public static Set<User> parseJsonIntoUsers(URL url) throws IOException {
		Set<User> retVal = null;
		if ( url != null ) {
			String jsonText = Resources.toString(url, Charsets.UTF_8);
			JSONArray array = new JSONArray(jsonText);
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			
			retVal = new HashSet<User>(array.length());
			for ( int i = 0; i < array.length(); i++ ) {
				String s = array.getJSONObject(i).toString();
				User user = mapper.readValue(s, User.class);
				retVal.add(user);
			}			
		}
		return Optional.ofNullable(retVal).orElse(Collections.emptySet());
	}
}
