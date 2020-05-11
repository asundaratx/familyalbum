package com.example.demo.datamysql.dao;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class Event {
    private String eventName;
    private Date eventDate;
    private Map<String, String> eventDetails;
}
