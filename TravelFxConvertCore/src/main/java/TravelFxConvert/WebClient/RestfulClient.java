package TravelFxConvert.WebClient;

import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;



public class RestfulClient {
	public class ClientReturn{
		private String response;
		private int Status;
		public String getResponse() {
			return response;
		}
		public void setResponse(String response) {
			this.response = response;
		}
		public int getStatus() {
			return Status;
		}
		public void setStatus(int status) {
			Status = status;
		}
		
	}
	private static final Logger log = LogManager.getLogger(RestfulClient.class);
	//private static final Logger log= Logger.getLogger(RestfulClient.class);
	static RestfulClient myInstance=null;
	
	protected RestfulClient(){}
	
	protected RestTemplate restTemplate=null;
	
	protected void prepareRestTemplate() throws Exception{
		TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] certificate, String authType) {
                return true;
            }
        };
        
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        SSLSocketFactory csf = new SSLSocketFactory(new TrustStrategy() {
            public boolean isTrusted(X509Certificate[] certificate, String authType) {
                return true;
            }
        });
        
        csf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        
        HttpClientBuilder cb = HttpClients.custom();
        cb=cb.setSSLSocketFactory(csf);
        cb=cb.setSSLHostnameVerifier(new NoopHostnameVerifier());
        
        CloseableHttpClient httpClient = cb
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        
        restTemplate= new RestTemplate(requestFactory);
	}
	
	public static RestfulClient getInstance() throws Exception{
		if(myInstance==null){
			synchronized(RestfulClient.class){
				if(myInstance==null){
					myInstance=new RestfulClient();
					myInstance.prepareRestTemplate();
				}
			}
		}
		return myInstance;
	}
	
	protected HttpMethod convertStringtoHttpMethod(String s) throws Exception{
		HttpMethod m=HttpMethod.GET;
		String st = s.trim().toUpperCase();
		if(st.equals("GET")){
			m=HttpMethod.GET;
		}else if(st.equals("POST")){
			m=HttpMethod.POST;
		}else if(st.equals("PUT")){
			m=HttpMethod.PUT;
		}else if(st.equals("DELETE")){
			m=HttpMethod.DELETE;
		}else{
			log.error("HTTP method only support GET,POST,PUT,DELETE");
			throw new Exception("HTTP method only support GET,POST,PUT,DELETE");
		}
		return m;
	}
	
	public ClientReturn connectServerByPara(String urlString, Map<String,String> InputMapArg,String MethodStr, int maxTryUnAuthorize) throws Exception{
		boolean OK=false;
		ClientReturn cl = new ClientReturn();
		int trial=0;
		HttpMethod method=convertStringtoHttpMethod(MethodStr);
		StringBuffer paraArg=new StringBuffer("?");
		
		Iterator entries = InputMapArg.entrySet().iterator();
		int countArg=0;
		while (entries.hasNext()) {
		  Entry<String,String> thisEntry = (Entry) entries.next();
		  //String key = URLEncoder.encode( thisEntry.getKey().toString(), "UTF-8");
		  //String value = URLEncoder.encode(thisEntry.getValue().toString(), "UTF-8");
		  if(countArg++!=0){
			  paraArg.append("&");
		  }
		  paraArg.append(thisEntry.getKey().toString()+"="+thisEntry.getValue().toString());
		}
		urlString+=paraArg;
		
		ResponseEntity<String> loginResponse=null;
        do {
        	try{
        		loginResponse= restTemplate.exchange(urlString, method, null, String.class);
				cl.response=loginResponse.getBody();
				cl.Status =loginResponse.getStatusCode().value(); 
				if (loginResponse.getStatusCode() == HttpStatus.OK) {
					break;
				} else if (loginResponse.getStatusCode() == HttpStatus.CREATED) {
					break;
				} else if (loginResponse.getStatusCode() == HttpStatus.ACCEPTED) {
					break;
				}else if (loginResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					log.info("Unauthorized return, try "+ (++trial) +" times;");
				}else{
					break;
				}
        	}catch (HttpClientErrorException hex){
        		HttpStatus sCode=hex.getStatusCode();
        		cl.setStatus(sCode.value());
        		cl.setResponse(hex.getResponseBodyAsString());
        		if (sCode == HttpStatus.UNAUTHORIZED) {
					log.info("Unauthorized return, try "+ (++trial) +" times;");
				}else{
					break;
				}
        	}
        	try {
				if(!OK)
					Thread.sleep(30*1000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
        }while(trial<maxTryUnAuthorize);
		
		return cl;
	}
	public ClientReturn connectServerByJson(String urlString, Map<String,String> JsonMapArg,String MethodStr, int maxTryUnAuthorize) throws Exception{
		boolean OK=false;
		ClientReturn cl = new ClientReturn();
		int trial=0;
		HttpMethod method=convertStringtoHttpMethod(MethodStr);
		Gson gson = new Gson();
		String jsonStr=null;
		if(JsonMapArg.size()>0){
			jsonStr = gson.toJson(JsonMapArg);
		}
		//setheader
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(jsonStr, headers);
        
        
        ResponseEntity<String> loginResponse=null;
        do {
        	try{
        		loginResponse= restTemplate.exchange(urlString, method, entity, String.class);
				cl.response=loginResponse.getBody();
				cl.Status =loginResponse.getStatusCode().value(); 
				if (loginResponse.getStatusCode() == HttpStatus.OK) {
					OK=true;
					break;
				} else if (loginResponse.getStatusCode() == HttpStatus.CREATED) {
					break;
				} else if (loginResponse.getStatusCode() == HttpStatus.ACCEPTED) {
					break;
				}else if (loginResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
					log.info("Unauthorized return, try "+ (++trial) +" times;");
				}else{
					break;
				}
        	}catch (HttpClientErrorException hex){
        		HttpStatus sCode=hex.getStatusCode();
        		cl.setStatus(sCode.value());
        		cl.setResponse(hex.getResponseBodyAsString());
        		if (sCode == HttpStatus.UNAUTHORIZED) {
					log.info("Unauthorized return, try "+ (++trial) +" times;");
				}else{
					break;
				}
        	}
        	try {
				if(!OK)
					Thread.sleep(30*1000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
        }while(trial<maxTryUnAuthorize);
		
		return cl;
	}
	
}

