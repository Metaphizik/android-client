package com.example.metaphizik.design.dto;

import java.util.Date;

public class RemindDTO {

    private long id;
    private String title;
    private Date remindDate;

    public RemindDTO(){}

    public RemindDTO(String title) {

        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
