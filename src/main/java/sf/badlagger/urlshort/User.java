package sf.badlagger.urlshort;

import java.util.HashMap;
import java.util.Map;

public class User {
    
    private int userHash;
    private Map<Integer, ShortUrl> urlList = new HashMap<>();
    
    public User(int userHash) {
	this.userHash = userHash;
    }
    
    public boolean isLongUrlPresent(int hash) {
	return urlList.containsKey(Integer.valueOf(hash));
    }
    
    public boolean addNewUrl(int hash, ShortUrl shortUrl) {
	if (!isLongUrlPresent(hash)) {
	    urlList.put(hash, shortUrl);
	    return true;
	}
	return false;
    }
    
    public boolean removeUrl(int hash) {
	if (isLongUrlPresent(hash)) {
	    urlList.remove(hash);
	    return true;
	}
	
	return false;
    }
    
    public ShortUrl getShortUrl(int hash) {
	if (isLongUrlPresent(hash)) {
	    return urlList.get(hash);
	}
	return null;
    }
    
    private String urlListToJsonString() {
	if (urlList.isEmpty())
	    return null;
	
	String jsonStr = "[";
	boolean first = false;
	
	for (var url : urlList.entrySet()) {
	    if (!first) {
		jsonStr += String.format("{%d : %s}", url.getKey(), url.getValue().getStringJson());
		first = true;
	    } else {
		jsonStr += String.format(",{%d : %s}", url.getKey(), url.getValue().getStringJson());
	    }
	}
	jsonStr += "]";
	return jsonStr;
    }
    
    public String getJsonStr() {
	if (urlList.isEmpty())
	    return null;
	
	return String.format("%d : %s", userHash, urlListToJsonString());
    }
}
