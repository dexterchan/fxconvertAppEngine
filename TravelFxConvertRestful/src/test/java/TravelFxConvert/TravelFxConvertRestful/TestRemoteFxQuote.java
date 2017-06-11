package TravelFxConvert.TravelFxConvertRestful;

import java.util.HashSet;
import java.util.Set;

import TravelFxConvert.Controller.ExternalFxRefresherFactory;
import TravelFxConvert.Controller.FxRateRefresherInterface;
import TravelFxConvert.Model.FXQuote;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class TestRemoteFxQuote 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestRemoteFxQuote( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( TestRemoteFxQuote.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	try{
//    		RestfulClient cl=RestfulClient.getInstance();
//    		String urlString="http://www.apilayer.net/api/live";
//    		Map<String,String> InputMapArg = new HashMap<String,String>();
//    		InputMapArg.put("access_key", "f690f9da3650c85aff3a94715e5b58de");
//    		InputMapArg.put("format", "1");
//    		InputMapArg.put("currencies", "AUD,CNY,JPY,MXN,HKD,NZD");
//    		RestfulClient.ClientReturn ret=null;
//    		ret=cl.connectServerByPara(urlString, InputMapArg, "GET", 1);
//    		
//    		Gson gson = new Gson();
//    		
//    		FxQuoteResponse r =gson.fromJson(ret.getResponse(), FxQuoteResponse.class);
//    		r=r;;
    		Set<String> l = new HashSet<String>();
    		l.add("AUD");l.add("JPY");l.add("HKD");
    		FxRateRefresherInterface i = ExternalFxRefresherFactory.getExtFxRresher("ApiLayerFxRefresher");
    		FXQuote q=i.getFx(l, "USD");
    		System.out.println(q.toString());
    		
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    }
}
