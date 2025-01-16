package sf.badlagger.urlshort;

import com.beust.jcommander.JCommander;

import sf.badlagger.urlshort.ConsoleUI.Action;

public class App {

    static final int USER_ID_LENGTH = 15;
    
    static Args arguments = null;
    static CfgDump cfg = null;
    static JsonSimpleDump users = null;
    static StringWithDate userId = null;
    static JsonSimpleDump longUrls = null;
    static JsonSimpleDump shortUrls = null;
    static ConsoleUI ui;
    
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
	    userId = users.getVal(arguments.id);
	    if (userId == null) {
		if (users.addNewVal(arguments.id)) {
		    System.out.println("New user was successfully added");
		}

		userId = users.getVal(arguments.id);
		if (userId == null) {
		    System.out.println("New user lost!!!");
		    return false;
		}
	    }
	} else {
	    System.out.println("Generate new User Id");
	    System.out.println("The power of the User Id set is: " + Generator.getUuidsNumber(USER_ID_LENGTH));
	    do {
		String newId = Generator.getUuid(USER_ID_LENGTH);
		userId = users.getVal(newId);
		if (userId != null) {
		    userId = null;
		    continue;
		} else {
		    if (!users.addNewVal(newId)) {
			System.out.format("User with id %s already exists! Re-generate\n", newId);
			continue;
		    }
		    System.out.println("New user was successfully generated");
		    userId = users.getVal(newId);
		}
	    } while (userId == null);
	}

	System.out.format("User ID: %s was added at %s hash: %X\n", userId.getVal(),
		userId.getPrettyDate("dd/MM/yyyy HH:mm:ss"), userId.hashCode());
	
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
    
    private static boolean loadShortUrl(String urlDefaultFile) {
	shortUrls = new JsonSimpleDump(urlDefaultFile);
	
	if ((arguments.shortPath == null) || !shortUrls.setFilePath(arguments.shortPath)) {
	    if (!shortUrls.setDefault()) {
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
	
	if (!loadShortUrl("short.list"))
	    return;
	
	System.out.format("Short Url number: %d\n", shortUrls.getValsNumber());
	
	User user = new User(userId.hashCode());
	Action userAction;
	ui = new ConsoleUI(userId.getVal());
	
	do {
	    ui.drawMenu();
	    userAction = ui.getInput();
	    
	    switch(userAction) {
	    case SHOW_URL_LIST:
		break;
	    case ADD_NEW_URL:
		String newUrl = ui.getURL();
		if (newUrl != null) {
		    longUrls.addNewVal(newUrl);
		    StringWithDate url = longUrls.getVal(newUrl);
		    if (!user.isLongUrlPresent(url.hashCode())) {
			String newShortUrl = "";
			do {
			    newShortUrl = Generator.getUuid(10);
			} while (!shortUrls.addNewVal(newShortUrl));
			ShortUrl shortUrl = new ShortUrl(shortUrls.getVal(newShortUrl).hashCode(), 3);
			user.addNewUrl(url.hashCode(), shortUrl);
			System.out.println(user.getJsonStr());
			//
		    } else {
			System.out.println("Такой URL уже присутствует и его короткая ссылка: ");
		    }
		}
		break;
	    case URL_OPEN:
		break;
	    case APP_EXIT:
		System.out.println("Выход!");
		break;
	    case ERR_VAL:
		System.out.println("Ошибка ввода! Попробуйте ещё раз.");
		break;
	    }
	} while (userAction != Action.APP_EXIT);
    }
}
