package TravelFxConvert.TravelFxConvertRestful.Model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND,reason="This quotation is not found in system")
public class FxQuoteException extends Exception {
	public FxQuoteException(String msg){
		super(msg);
	}
	public FxQuoteException (String ccy1, String ccy2, String reason){
		this(ccy1+"/"+ccy2+":"+reason);
	}
}
