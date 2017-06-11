package TravelFxConvert.TravelFxConvertRestful.JS;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {

    @RequestMapping(value="/",method = RequestMethod.GET)
    public String homepage(){
        return "indexFx.html";
    }
    @RequestMapping(value="/vue",method = RequestMethod.GET)
    public String homepageVue(){
        return "indexVue.html";
    }
    
    @RequestMapping(value="/vue2",method = RequestMethod.GET)
    public String homepageVue2(){
        return "indexVue2.html";
    }
    
    @RequestMapping(value="/table",method = RequestMethod.GET)
    public String hrhomepageTable(){
        return "hrindexTable.html";
    }
}
