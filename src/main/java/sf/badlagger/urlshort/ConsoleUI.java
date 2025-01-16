package sf.badlagger.urlshort;

import java.net.URI;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUI {
    
    public enum Action {
	
	SHOW_URL_LIST("Показать список URL"),
	ADD_NEW_URL("Добавить новый URL"),
	URL_OPEN("Перейти по ссылке"),
	APP_EXIT("Выйти"),
	ERR_VAL("");
	
	private String action;
	
	Action(String act) {
	    action = act;
	}
	
	public int toInt() {
	    return ordinal() + 1;
	}
	
	public String toString() {
	    return action;
	}
	
	public static int minInt() {
	    return SHOW_URL_LIST.toInt();
	}
	
	public static int maxInt() {
	    return APP_EXIT.toInt();
	}
	
	public static Action fromInt(int act) {
	    for (var curAct : values()) {
		if ((curAct.ordinal() + 1) == act)
		    return curAct;
	    }
	    return ERR_VAL;
	}
    }
    
    private final char DELIM_CHAR = '-';
    private final int DELIM_LENGTH = 20;
    private String  userId;
    private Scanner userInput;
    
    ConsoleUI(String userId) {
	this.userId = userId;
	userInput = new Scanner(System.in);
    }
    
    private void drawDelim() {
	for (int i = 0; i < DELIM_LENGTH; ++i) {
	    System.out.format("%c", DELIM_CHAR);
	}
	System.out.println();
    }
    
    void drawMenu() {
	
	drawDelim();
	System.out.format("Сессия пользователя: %s\n", userId);
	drawDelim();
	
	for (var act : Action.values()) {
	    if (act == Action.ERR_VAL)
		continue;
	    System.out.format("%d. %s\n", act.toInt(), act.toString());
	}
	
	System.out.format("Введите число от %d до %d: ", Action.minInt(), Action.maxInt());
    }
    
    Action getInput() {
	String input = userInput.next();
	if (input.length() == 1) {
	    char inputCh = input.charAt(0);
	    if (Character.isDigit(inputCh)) {
		return Action.fromInt(inputCh - '0');
	    }
	}
	return Action.ERR_VAL;
    }
    
    String getURL() {
	System.out.print("Введите URL: ");
	String input = userInput.next();
	
	try {
            new URI(input).toURL(); // Попытка создать объект URL
            return input;
        } catch (Exception e) {
            System.out.println("Введеная строка не является URL!");
            return null;
        }
    }
}
