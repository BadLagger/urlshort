package sf.badlagger.urlshort;

import com.beust.jcommander.JCommander;

public class App {

    static final int USER_ID_LENGTH = 15;
    
    static Args arguments = null;
    static CfgDump cfg = null;
    static JsonSimpleDump users = null;
    static StringWithDate user = null;
    static JsonSimpleDump longUrls = null;
    
    private static boolean loadCfg(String cfgDefaultFile) {
	cfg = new CfgDump(cfgDefaultFile);
	
	if ((arguments.cfgPath == null) || !cfg.setFilePath(arguments.cfgPath)) {
	    if (!cfg.setDefault()) {
		System.out.println("Oops! errors in cfg creatings");
		return false;
	    }
	}

	System.out.format("URL Prefix from config: %s\n", cfg.getPrefix());
	System.out.format("URL LiveTime from config: %d\n", cfg.getLivetime());
	return true;
    }
    
    private static boolean loadUsers(String userDefaultFile) {
	
	users = new JsonSimpleDump(userDefaultFile);
	
	if ((arguments.usersPath == null) || !users.setFilePath(arguments.usersPath)) {
	    if (!users.setDefault()) {
		System.out.println("Oops! errors in users list creatings");
		return false;
	    }
	}
	
	return true;
    }
    
    private static boolean initUser() {
	
	if ((arguments.id != null)) {
	    user = users.getVal(arguments.id);
	    if (user == null) {
		if (users.addNewVal(arguments.id)) {
		    System.out.println("New user was successfully added");
		}

		user = users.getVal(arguments.id);
		if (user == null) {
		    System.out.println("New user lost!!!");
		    return false;
		}
	    }
	} else {
	    System.out.println("Generate new User Id");
	    System.out.println("The power of the User Id set is: " + Generator.getUuidsNumber(USER_ID_LENGTH));
	    do {
		String newId = Generator.getUuid(USER_ID_LENGTH);
		user = users.getVal(newId);
		if (user != null) {
		    user = null;
		    continue;
		} else {
		    if (!users.addNewVal(newId)) {
			System.out.format("User with id %s already exists! Re-generate\n", newId);
			continue;
		    }
		    System.out.println("New user was successfully generated");
		    user = users.getVal(newId);
		}
	    } while (user == null);
	}

	System.out.format("User ID: %s was added at %s hash: %X\n", user.getVal(),
		user.getPrettyDate("dd/MM/yyyy HH:mm:ss"), user.hashCode());
	
	return true;
    }
    
    private static boolean loadLongUrl(String urlDefaultFile) {
	
	longUrls = new JsonSimpleDump(urlDefaultFile);
	
	if ((arguments.urlPath == null) || !longUrls.setFilePath(arguments.urlPath)) {
	    if (!longUrls.setDefault()) {
		System.out.println("Oops! errors in long urls list creatings");
		return false;
	    }
	}
	
	return true;
    }

    public static void main(String[] args) {
	arguments = new Args();

	JCommander.newBuilder().addObject(arguments).build().parse(args);

	if (arguments.checkHelp())
	    return;

	
	if (!loadCfg("sets.cfg"))
	    return;

	
	if (!loadUsers("users.list"))
	    return;

	System.out.format("Users number: %d\n", users.getValsNumber());

	
	if (!initUser())
	    return;
	
	if (!loadLongUrl("url.list"))
	    return;
	
	System.out.format("Long Url number: %d\n", longUrls.getValsNumber());
	
	
    }
}
