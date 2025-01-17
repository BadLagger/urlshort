package sf.badlagger.urlshort;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DbDump extends FileDump {

    private JsonNode rootNode = null;
    private Set<User> data = new HashSet<>();

    protected DbDump(String defaultName) {
	super(defaultName);
    }

    private boolean loadDataToJson() {
	try {
	    rootNode = new ObjectMapper().readTree(dataString);
	    var keys = rootNode.fieldNames();
	    while (keys.hasNext()) {
		String userHashStr = keys.next();
		int userHash = Integer.parseInt(userHashStr);
		User user = new User(userHash);
		var value = rootNode.get(userHashStr);
		if (value.isArray()) {
		    ArrayNode arrayNode = (ArrayNode) value;
		    for (int i = 0; i < arrayNode.size(); i++) {
			var currentNode = arrayNode.get(i);
			String longUrlHashStr = currentNode.fieldNames().next();
			int longUrlHash = Integer.parseInt(longUrlHashStr);
			var finalNode = currentNode.get(longUrlHashStr);
			String shortUrlHashStr = finalNode.fieldNames().next();
			int shortUrlHash = Integer.parseInt(shortUrlHashStr);
			int count = finalNode.get(shortUrlHashStr).asInt();
			ShortUrl shortUrl = new ShortUrl(shortUrlHash, count);
			user.addNewUrl(longUrlHash, shortUrl);
			data.add(user);
		    }
		} else {
		    return false;
		}
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

    public boolean addNewVal(User user) {

	if (rootNode == null)
	    return false;

	if (data.contains(user)) {
	    return false;
	}

	data.add(user);
	try {
	    String json = user.getJsonStr();
	    JsonNode newNode = new ObjectMapper().readTree(json).get(user.getHashStr());
	    ((ObjectNode) rootNode).set(user.getHashStr(), newNode);
	    dataString = (new ObjectMapper()).writeValueAsString(rootNode);
	    //System.out.println(dataString);
	    return save();
	} catch (JsonMappingException e) {
	    e.printStackTrace();
	} catch (JsonProcessingException e) {
	    e.printStackTrace();
	}

	return false;
    }

    public boolean updateVal(User user) {
	if (rootNode == null)
	    return false;

	if (!data.contains(user)) {
	    return false;
	}
	
	if (user.size() == 0)
	    return removeVal(user);

	data.remove(user);
	data.add(user);

	try {
	    String json = user.getJsonStr();
	    JsonNode newNode = new ObjectMapper().readTree(json).get(user.getHashStr());
	    ((ObjectNode) rootNode).replace(user.getHashStr(), newNode);
	    dataString = (new ObjectMapper()).writeValueAsString(rootNode);
	    //System.out.println(dataString);
	    return save();
	} catch (JsonMappingException e) {
	    e.printStackTrace();
	} catch (JsonProcessingException e) {
	    e.printStackTrace();
	}

	return false;
    }

    public boolean removeVal(User user) {
	if (rootNode == null)
	    return false;

	if (!data.contains(user)) {
	    return false;
	}
	
	data.remove(user);
	
	try {
	    //String json = user.getJsonStr();
	    //JsonNode newNode = new ObjectMapper().readTree(json).get(user.getHashStr());
	    ((ObjectNode) rootNode).remove(user.getHashStr());
	    dataString = (new ObjectMapper()).writeValueAsString(rootNode);
	    return save();
	} catch (JsonMappingException e) {
	    e.printStackTrace();
	} catch (JsonProcessingException e) {
	    e.printStackTrace();
	}

	return false;
    }

    public User get(int userHash) {

	for (User u : data) {
	    if (u.hashCode() == userHash)
		return u;
	}

	return null;
    }

}
