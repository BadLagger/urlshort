package sf.badlagger.urlshort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CfgDump extends FileDump{
	private final String DEFAULT_URL_PREFIX = "http://localhost";
	private final int DEFAULT_URL_LIVETIME = 3; // days
	
	private String shortUrlPrefix = null;
	private int urlLiveTime = 0;
	
	protected CfgDump(String defaultName) {
		super(defaultName);
	}

	@Override
	protected boolean checkData() {
		if (dataString.length() > 0) {
			try {
				JsonNode rootNode = new ObjectMapper().readTree(dataString);
				shortUrlPrefix = rootNode.get("ShortUrlPrefix").asText();
				urlLiveTime = rootNode.get("UrlLiveTime").asInt();
				return true;
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		
		return createDefaultData();
	}

	@Override
	protected boolean createDefaultData() {
		dataString = String.format("{\"ShortUrlPrefix\":\"%s\",\"UrlLiveTime\": %d}", DEFAULT_URL_PREFIX, DEFAULT_URL_LIVETIME);
		
		try {
			JsonNode rootNode = new ObjectMapper().readTree(dataString);
			shortUrlPrefix = rootNode.get("ShortUrlPrefix").asText();
			urlLiveTime = rootNode.get("UrlLiveTime").asInt();
			return save();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public String getPrefix() {
		return shortUrlPrefix;
	}
	
	public int getLivetime() {
		return urlLiveTime;
	}
}
