package TravelFxConvert.Model;

import java.util.Date;
import java.util.Map;

public class FXQuote {
	public Date timestamp;
	public String baseCcy;
	public Map<String,Double> quote;
	
	@Override
	public String toString(){
		StringBuffer s = new StringBuffer();
		s.append("timestamp:");
		s.append(timestamp.toString());
		s.append("\nBASECCY:");
		s.append(baseCcy);
		s.append("\nQuote:\n");
		quote.forEach((k,v) -> s.append(k+":"+v.toString()+"\n"));
		
		
		return s.toString();
	}
	
}
