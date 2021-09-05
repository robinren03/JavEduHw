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
@TypeConverters({Converter.class})
public class KEntity implements Serializable {

    @NonNull
    @PrimaryKey
    private String kEntityUri;

    private String label;
    private String course;
    private String property;
    private String content;

    private Date createdate;

    public KEntity(String uri,String label, String property,
                   String content, Date createdate){
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
