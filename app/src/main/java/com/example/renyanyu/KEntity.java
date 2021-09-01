package com.example.renyanyu;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.io.Serializable;
import java.util.Map;

@Entity
public class KEntity implements Serializable {
    @NonNull
    @PrimaryKey
    private String kEntityUri;

    private String label;
    private String course;
    private String property;
    private String content;
    public KEntity(String uri,String label, String property,
                   String content){
        super();
        this.kEntityUri = uri;
        this.label = label;
        this.property = property;
        this.content = content;
    }

    public KEntity(){
        super();
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @NonNull
    public String getkEntityUri() {
        return kEntityUri;
    }

    public void setkEntityUri(@NonNull String kEntityUri) {
        this.kEntityUri = kEntityUri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
