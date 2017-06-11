package TravelFxConvert.Controller;

import java.util.Set;

import TravelFxConvert.Model.FXQuote;

public interface FxRateRefresherInterface {
	public FXQuote getFx(Set<String> ccyLst,String baseCcy) throws Exception;
}
