package com.example.renyanyu;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.List;

@Entity
@TypeConverters({Converter.class, ListConverter.class})
public class KEntity implements Serializable {

    @NonNull
    @PrimaryKey
    private String kEntityUri;

    private String label;
    private String course;
    private List<Map<String, String>> property;
    private List<Map<String, String>> content;

    private Date createdate;

    public KEntity(String uri,String label, List<Map<String, String>> property,
                   List<Map<String, String>> content, Date createdate){
        super();
        this.kEntityUri = uri;
        this.label = label;
        this.property = property;
        this.content = content;
        this.createdate = createdate;
    }

    public KEntity(){
        super();
        this.kEntityUri = "";
    }

    public void setContent(List<Map<String, String>> content) {
        this.content = content;
    }

    public List<Map<String, String>> getContent() {
        return content;
    }

    public List<Map<String, String>> getProperty() {
        return property;
    }

    public void setProperty(List<Map<String, String>> property) {
        this.property = property;
    }

    @NonNull
    public String getKEntityUri() {
        return kEntityUri;
    }

    public void setKEntityUri(@NonNull String kEntityUri) {
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

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
}
