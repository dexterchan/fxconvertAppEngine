package TravelFxConvert.TravelFxConvertCore;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import TravelFxConvert.Daemon.FxRateUpdaterThread;
import TravelFxConvert.Model.FXDefaultWeightedEdge;
import TravelFxConvert.Model.FXQuote;
import TravelFxConvert.Model.FXRateContainer;
import TravelFxConvert.Model.FxDAG;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	private static final Logger log = LogManager.getLogger(SimpleDirectedWeightedGraph.class);
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() 
    {
    	ApplicationContext context = new ClassPathXmlApplicationContext("SpringBeanConfig.xml");
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
		
		FxRateUpdaterThread Fxtask = (FxRateUpdaterThread) context.getBean("ApiLayerFxUpdateThread");

		taskExecutor.execute(Fxtask);
		//KShortestPaths???
        //ApplicationContext context = new ClassPathXmlApplicationContext("FxRefresherConfig.xml");
        TravelFxConvert.Controller.ApiLayerFxRateRefresher refresher = (TravelFxConvert.Controller.ApiLayerFxRateRefresher) context.getBean("ApiLayerFxRefresher");
        Set<String> ccyLst=new HashSet<String>();
        ccyLst.add("EUR");
        ccyLst.add("JPY");
        ccyLst.add("AUD");
        ccyLst.add("GBP");
        ccyLst.add("NZD");
        ccyLst.add("THB");
        ccyLst.add("SGD");
        ccyLst.add("TWD");
        ccyLst.add("CAD");
        ccyLst.add("CHF");
        ccyLst.add("MYR");
        ccyLst.add("HKD");
        ccyLst.add("CNY");
        ccyLst.add("EUR");
        ccyLst.add("KRW");
        ccyLst.add("INR");
		
        try{
        	FXQuote q=refresher.getFx(ccyLst, "USD");
        	FxDAG d = FXRateContainer.getFxDAG();
        	d.updateFxRate(q);
        	
        	//System.out.println(d.getFxRate("AUD", "JPY"));
        	String ccy1="AUD";
        	String ccy2="JPY";
        	{
        		
        		double qFxRate = 1.0;
        		//synchronized (this) 
        		{
        			DefaultEdge e;

        			List<FXDefaultWeightedEdge> path = BellmanFordShortestPath.findPathBetween(d, ccy1, ccy2);
        			qFxRate = 0.0;
        			ListIterator i = path.listIterator();

        			while (i.hasNext()) {
        				FXDefaultWeightedEdge w = (FXDefaultWeightedEdge) i.next();
        				qFxRate += w.getWeight();
        			}
        			path.forEach((v) -> {
        				FXDefaultWeightedEdge w = (FXDefaultWeightedEdge) v;
        				System.out.println(w + ":" + Math.pow(Math.E, w.getWeight()) + "\n");
        			});
        			qFxRate=Math.pow(Math.E, qFxRate);
        			System.out.println(qFxRate);
        		}
        	}
        	
        }catch(IllegalArgumentException ee){
        	throw ee;
        }catch(Exception e){
        	
        	e.printStackTrace();
        }
        assertTrue( true );
    }
}
