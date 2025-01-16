package sf.badlagger.urlshort;

public class ShortUrl {
    
    public int urlHash;
    public int count;
    
    ShortUrl(int urlHash, int count) {
	this.urlHash = urlHash;
	this.count = count;
    }
    
    
    String getStringJson() {
	return String.format("{ %d : %d }", urlHash, count);
    }
}
