package sf.badlagger.urlshort;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonSimpleDump extends FileDump {
	
	JsonNode rootNode = null;
	Map<Integer, StringWithDate> valMap = new HashMap<>();
	
	protected JsonSimpleDump(String defaultName) {
		super(defaultName);
	}
	
	private boolean loadDataToJson() {
		try {
			rootNode = new ObjectMapper().readTree(dataString);
			var keys = rootNode.fieldNames();
			Calendar date = Calendar.getInstance();
			while(keys.hasNext()) {
				String key = keys.next();
				date.setTimeInMillis(rootNode.get(key).asLong());
				StringWithDate checkValue = new StringWithDate(key, date);
				valMap.put(checkValue.hashCode(), checkValue);
			}
			return true;
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected boolean checkData() {
		
		if (dataString.length() > 0) { 
			return loadDataToJson();
		}
		
		return createDefaultData();
	}

	@Override
	protected boolean createDefaultData() {
		dataString = "{}";
		
		if (loadDataToJson()) {
			return save();
		}
		return false;
	}
	
	public boolean checkVal(String id) {
		
		if (rootNode == null)
			return false;
		
		if (rootNode.get(id) == null) {
			return false;
		}
		return true;
	}
	
	public StringWithDate getVal(int hash) {
		return valMap.get(hash);
	}
	
	public StringWithDate getVal(String id) {
		
		if (rootNode == null)
			return null;
		
		JsonNode userNode = rootNode.get(id);
		
		if (userNode == null)
			return null;
		
		long dateMs = userNode.asLong();
		
		if (dateMs == 0) {
			return null;
		}
		
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(dateMs);
		
		return new StringWithDate(id, date);
	}
	
	public int getValsNumber() {
		return (rootNode != null) ? rootNode.size() : 0;
	}
	
	public boolean isHashPresent(StringWithDate value) {
		return valMap.containsKey(value.hashCode());
	}
	
	public boolean addNewVal(String id) {
		
		if (rootNode == null)
			return false;
		
		if (!checkVal(id)) {
			StringWithDate value = new StringWithDate(id);
			
			if (isHashPresent(value))
				return false;
			
			((ObjectNode)rootNode).put(value.getVal(), value.getDate().getTime().getTime());
			try {
				dataString =  (new ObjectMapper()).writeValueAsString(rootNode);
				valMap.put(value.hashCode(), value);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return false;
			}
			System.out.format("DataString: %s\n", dataString);
			return save();
		}
		
		return false;
	}
	
	public boolean removeVal(String id) {
		if (checkVal(id)) {
			valMap.remove(getVal(id).hashCode());
			((ObjectNode)rootNode).remove(id);
			try {
				dataString =  (new ObjectMapper()).writeValueAsString(rootNode);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return false;
			}
			return save();
		}
		return false;
	}
}
