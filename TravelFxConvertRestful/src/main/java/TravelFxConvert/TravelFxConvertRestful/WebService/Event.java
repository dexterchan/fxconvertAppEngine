package TravelFxConvert.TravelFxConvertRestful.WebService;

import java.util.Date;

public class Event {
	public Event(){}
	public Event(String Id, String title, String detail, Date date){
		this.id=Id;
		this.title=title;
		this.detail=detail;
		this.date=date;
	}
	String id;
	String title;
	String detail;
	Date date;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
