
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.List;

@Generated("jsonschema2pojo")
public class Taxonomy__1 {

    @SerializedName("tags")
    @Expose
    private List<String> tags;
    @SerializedName("places")
    @Expose
    private List<Object> places;
    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("hicCategory")
    @Expose
    private String hicCategory;
    @SerializedName("segments")
    @Expose
    private List<Object> segments;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Object> getPlaces() {
        return places;
    }

    public void setPlaces(List<Object> places) {
        this.places = places;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getHicCategory() {
        return hicCategory;
    }

    public void setHicCategory(String hicCategory) {
        this.hicCategory = hicCategory;
    }

    public List<Object> getSegments() {
        return segments;
    }

    public void setSegments(List<Object> segments) {
        this.segments = segments;
    }

}
