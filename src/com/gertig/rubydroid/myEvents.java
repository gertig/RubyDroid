package com.gertig.rubydroid;

import java.io.Serializable;
/**
 * 
 * @author Andrew Gertig
 *
 */
public class myEvents implements Serializable{

	private static final long serialVersionUID = 223L;

	public String	name			= "";
	public double	budget			= 0;
	public int		eventID			= 0;
	
	
	public String getEventName() {
        return name;
    }
    public void setEventName(String name) {
        this.name = name;
    }
    public int getEventID() {
        return eventID;
    }
    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }
    public double getEventBudget() {
        return budget;
    }
    public void setEventBudget(Integer budget) {
        this.budget = budget;
    }
    
	
}
