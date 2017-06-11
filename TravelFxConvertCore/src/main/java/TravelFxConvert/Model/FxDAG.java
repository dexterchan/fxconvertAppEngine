package TravelFxConvert.Model;

import java.util.List;
import java.util.ListIterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class FxDAG extends SimpleDirectedWeightedGraph<String, FXDefaultWeightedEdge> {
	private static final Logger log = LogManager.getLogger(SimpleDirectedWeightedGraph.class);
	private final double threshold = 0.00000000000001;

	public FxDAG() {
		super(FXDefaultWeightedEdge.class);
	}

	public double setFxQuote(String ccy1, String ccy2, double rate) {
		FXDefaultWeightedEdge ed = null;
		if (rate < threshold) {
			rate = threshold;
		}
		double lr = Math.log(rate);
		this.addVertex(ccy1);
		this.addVertex(ccy2);
		ed = this.getEdge(ccy1, ccy2);
		if (ed == null) {
			ed = this.addEdge(ccy1, ccy2);
		}
		this.setEdgeWeight(ed, lr);

		ed = this.getEdge(ccy2, ccy1);
		if (ed == null) {
			ed = this.addEdge(ccy2, ccy1);
		}
		this.setEdgeWeight(ed, lr * -1.0);
		return rate;
	}

	public boolean updateFxRate(FXQuote fxquote) {
		boolean ok = false;

		synchronized (this) {
			// this.addVertex(fxquote.baseCcy);
			fxquote.quote.forEach((k, v) -> {
				if (k.length() > 3) {
					k = k.substring(3, k.length());
				}
				this.setFxQuote(fxquote.baseCcy, k, v);
			});
		}

		return ok;
	}

	public double getFxRate(String ccy1, String ccy2) throws Exception{
		double qFxRate = 1.0;
		//synchronized (this) 
		{
			DefaultEdge e;
			if(ccy1.equals(ccy2)){
				throw new Exception ("ccy1 and ccy2 should not be the same");
			}
			List<FXDefaultWeightedEdge> path = BellmanFordShortestPath.findPathBetween(this, ccy1, ccy2);
			qFxRate = 0.0;
			ListIterator i = path.listIterator();

			while (i.hasNext()) {
				FXDefaultWeightedEdge w = (FXDefaultWeightedEdge) i.next();
				qFxRate += w.getWeight();
			}
			path.forEach((v) -> {
				FXDefaultWeightedEdge w = (FXDefaultWeightedEdge) v;
				log.debug(w + ":" + Math.pow(Math.E, w.getWeight()) + "\n");
			});
			qFxRate=Math.pow(Math.E, qFxRate);
		}

		return qFxRate;
	}

}
