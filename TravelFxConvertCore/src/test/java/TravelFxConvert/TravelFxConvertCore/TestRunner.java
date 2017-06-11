package TravelFxConvert.TravelFxConvertCore;

import java.util.Enumeration;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import junit.framework.TestResult;
import junit.framework.TestSuite;

public class TestRunner {

	public static void main(String[] args) {
//		Result result = JUnitCore.runClasses(TestRemoteFxQuoteThread.class);
//
//		for (Failure failure : result.getFailures()) {
//			System.out.println(failure.toString());
//		}
//
//		System.out.println(result.wasSuccessful());
		
		
		TestSuite suite = new TestSuite(AppTest.class );
		suite.addTestSuite(TestRemoteFxQuoteThread.class);
	      TestResult result2 = new TestResult();
	      suite.run(result2);
	      System.out.println("failure count:"+result2.failureCount());
	      Enumeration e = result2.failures();
	      while(e.hasMoreElements()){
	    	 // Failure failure = (Failure)result2.failures().nextElement();
	    	  System.out.println(e.nextElement());
	    	  
	      }
	      System.out.println ("error count:"+result2.errorCount());
	      Enumeration ee = result2.errors();
	      while(ee.hasMoreElements()){
	    	 // Failure failure = (Failure)result2.failures().nextElement();
	    	  System.out.println(ee.nextElement());
	    	  
	      }
	      System.out.println("Number of test cases = " + result2.runCount());
	      System.out.println("All test cases ok:"+result2.wasSuccessful());
	      
	   
	}
}
