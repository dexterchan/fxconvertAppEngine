package TravelFxConvert.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import TravelFxConvert.Model.FXQuote;
import TravelFxConvert.WebClient.RestfulClient;

public class ApiLayerFxRateRefresher implements FxRateRefresherInterface{
	
	class FxQuoteResponse {
		boolean success;
		String terms;
		String privacy;
		long timestamp;
		String source;
		Map<String, Double> quotes;
		ErrorDetail error;
		class ErrorDetail{
			String code;
			String type;
			String info;
		}
	}
	
	RestfulClient cl=null;
	String serviceURL;
	String access_key;
	String format;
	//String defaultBaseCcy;
	
	public ApiLayerFxRateRefresher () throws Exception{
		cl =  RestfulClient.getInstance();
	}

//	public String getDefaultBaseCcy() {
//		return defaultBaseCcy;
//	}
//	public void setDefaultBaseCcy(String defaultBaseCcy) {
//		this.defaultBaseCcy = defaultBaseCcy;
//	}
	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}

	public String getAccess_key() {
		return access_key;
	}

	public void setAccess_key(String access_key) {
		this.access_key = access_key;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	@Override
	public FXQuote getFx(Set<String> ccyLst,String baseCcy) throws Exception{
		FXQuote r=new FXQuote();
		Map<String,String> InputMapArg = new HashMap<String,String>();
		InputMapArg.put("access_key", this.access_key);
		InputMapArg.put("format", this.format);
		StringBuffer ccyString=new StringBuffer();
		ccyLst.forEach(ccy -> ccyString.append(ccy+","));
		InputMapArg.put("currencies", ccyString.substring(0, ccyString.length()-1));
		//if(baseCcy!=null && baseCcy.length()>0){
			InputMapArg.put("source", baseCcy);
		//}else{
		//	baseCcy=defaultBaseCcy;
		//	InputMapArg.put("source", this.defaultBaseCcy);
		//}
		RestfulClient.ClientReturn ret=null;
		ret=cl.connectServerByPara(this.serviceURL, InputMapArg, "GET", 1);
		
		Gson gson = new Gson();
		FxQuoteResponse res =gson.fromJson(ret.getResponse(), FxQuoteResponse.class);
		if(res.success){
			r.baseCcy=baseCcy;
			r.quote=res.quotes;
			r.timestamp = new Date();
			//r.timestamp.setTime(res.timestamp);
		}else{
			throw new Exception(res.error.code+":"+res.error.type+":"+res.error.info);
		}
		
		return r;
	}
}
