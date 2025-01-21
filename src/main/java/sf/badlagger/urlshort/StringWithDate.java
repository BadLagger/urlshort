package sf.badlagger.urlshort;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StringWithDate {
	private String val = null;
	private Calendar date = null;
	
	public StringWithDate(String val, Calendar date) {
		this.val = val;
		this.date = date;
	}
	
	public StringWithDate(String val) {
		this.val = val;
		this.date = Calendar.getInstance();
	}
	
	public String getVal() {
		return val;
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public boolean checkDate(int days) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar bestBefore = (Calendar) date.clone();
		Calendar now = Calendar.getInstance();
		bestBefore.add(Calendar.DATE, days);
		System.out.format("Ссылка годна до: %s\n", simpleDateFormat.format(bestBefore.getTime()));
		System.out.format("Сейчас: %s\n", simpleDateFormat.format(now.getTime()));
		int result = bestBefore.compareTo(now);
		System.out.format("Результат сравнения: %d\n", result);
		if (result > 0) {
			return true;
		}
		return false;
	}
	
	public String getPrettyDate(String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date.getTime());
	}
	
	@Override
	public int hashCode() {
		int ret = 0;
		
		for (int i = 0; i < val.length(); ++i) {
			ret += val.charAt(i);
		}
		
		return (ret + (int)date.getTimeInMillis());
	}
}
