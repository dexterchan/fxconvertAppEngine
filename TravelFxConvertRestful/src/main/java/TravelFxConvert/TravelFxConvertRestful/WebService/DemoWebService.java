package TravelFxConvert.TravelFxConvertRestful.WebService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class DemoWebService {
	private static final Logger log = LogManager.getLogger(DemoWebService.class);
	Map<String, Event> eventMAP=null;
	public DemoWebService(){
		eventMAP = new HashMap< String, Event>();
		Event e = new Event("abcd","asdfsaf","sadfsdf", new Date());
		eventMAP.put(e.getId(),e);
	}
	
	@RequestMapping(value = "/api/events", method = RequestMethod.GET)
    public ResponseEntity < List<Event> > getEvents() {
		List<Event> eventList = new ArrayList();
		eventMAP.forEach( (k,v) -> eventList.add(v) );
        return new ResponseEntity< List<Event> >(eventList, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/events", method = RequestMethod.POST)
    public ResponseEntity <  Event > postEvent(@RequestBody Event newEvent) {
		Event event=newEvent;
		event.setId(UUID.randomUUID().toString());
		this.eventMAP.put(newEvent.getId(), newEvent);
		event=newEvent;
        return new ResponseEntity<Event> (event,HttpStatus.OK);
    }
	
	@RequestMapping(value = "/api/events/{id}", method = RequestMethod.DELETE)
    public ResponseEntity <  Event > deleteEvent(@PathVariable("id") String id) {
		Event event=null;
		event = this.eventMAP.get(id);
		if(this.eventMAP.remove(id) ==null){
			return new ResponseEntity<Event> (event,HttpStatus.NOT_FOUND);
		}
		
		
        return new ResponseEntity<Event> (event,HttpStatus.OK);
    }
}

