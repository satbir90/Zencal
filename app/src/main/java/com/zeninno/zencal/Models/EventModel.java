package com.zeninno.zencal.Models;

import java.util.Date;

/**
 * Created by Satbir on 27-Jul-17.
 */

public class EventModel {
    public String id, title, description;
    public Date startDate;

    public EventModel(String id, String title, String description, Date startDate){
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
    }
}
