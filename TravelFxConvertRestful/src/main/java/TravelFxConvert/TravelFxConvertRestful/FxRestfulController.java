package TravelFxConvert.TravelFxConvertRestful;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import TravelFxConvert.Daemon.FxRateUpdaterThread;
import TravelFxConvert.Model.FXRateContainer;
import TravelFxConvert.Model.FxDAG;
import TravelFxConvert.TravelFxConvertRestful.Model.FxQuoteException;
import TravelFxConvert.TravelFxConvertRestful.Model.FxQuotePair;
import TravelFxConvert.TravelFxConvertRestful.hello.Greeting;

@RestController
public class FxRestfulController {
	private static final Logger log = LogManager.getLogger(FxRateUpdaterThread.class);
	//private static final Logger log= Logger.getLogger(FxRestfulController.class);
	@RequestMapping("/greeting2")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(100,
                            "TEST");
    }
	
	@RequestMapping("/allfx")
    public Map<String,Double>  allFxRates(@RequestParam(value="ccy", defaultValue="JPY") String ccy) {
		Map<String,Double> result =new HashMap();
		log.info("AllFxRate called");
		ConcurrentHashMap<String, Double>  fxContainer=FXRateContainer.getExtFxRresher();
		
		fxContainer.forEach( (key,value) -> result.put(key, value) );
		
        return result;
    }
	
	@RequestMapping(value="/quotefxpair", method = RequestMethod.POST)
    public ResponseEntity <  Double > quoteFxPair(@RequestBody  FxQuotePair fxPair) throws FxQuoteException{
		double d = 0;
		log.info("Request Quote "+fxPair.ccy1+"/"+fxPair.ccy2);
		FxDAG fxDAG = FXRateContainer.getFxDAG();
		
		try{
		d = fxDAG.getFxRate(fxPair.ccy1, fxPair.ccy2);
		}catch (Exception e){
			StringBuffer msg = new StringBuffer();
			msg.append(fxPair.ccy1);
			msg.append("/");
			msg.append(fxPair.ccy2);
			msg.append(" not found: ");
			msg.append(e.getMessage());
			log.error(msg.toString());
			throw new FxQuoteException(msg.toString());
		}
		return new ResponseEntity<Double> (d,HttpStatus.OK);
    }
	
	@ExceptionHandler(FxQuoteException.class)
	public ResponseEntity<FxQuoteException> rulesForFxQuoteNotFound(HttpServletRequest req, Exception e) 
	{
		FxQuoteException fe=null;
		if(e instanceof FxQuoteException){
			fe=(FxQuoteException)e;
		}else{
			fe = new FxQuoteException("Quotation problem");
		}
		return new ResponseEntity<FxQuoteException>(fe, HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping("/allccyset")
    public ResponseEntity< Set<String> > getQuoteCcySet() {
		Set<String> outputSet = new TreeSet<String>();
		outputSet.addAll(App.FxUpdateThread.getCcySet());
		outputSet.add(App.FxUpdateThread.getDefaultccy());
		//App.FxUpdateThread.getCcySet().
		return new ResponseEntity< Set<String> > ( outputSet,HttpStatus.OK);
    }
	
}
