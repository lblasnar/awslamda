
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.List;

@Generated("jsonschema2pojo")
public class Classification__1 {

    @SerializedName("tags")
    @Expose
    private List<Tag__1> tags;
    @SerializedName("featuredTag")
    @Expose
    private FeaturedTag__1 featuredTag;
    @SerializedName("places")
    @Expose
    private List<Object> places;
    @SerializedName("topic")
    @Expose
    private Topic__1 topic;

    public List<Tag__1> getTags() {
        return tags;
    }

    public void setTags(List<Tag__1> tags) {
        this.tags = tags;
    }

    public FeaturedTag__1 getFeaturedTag() {
        return featuredTag;
    }

    public void setFeaturedTag(FeaturedTag__1 featuredTag) {
        this.featuredTag = featuredTag;
    }

    public List<Object> getPlaces() {
        return places;
    }

    public void setPlaces(List<Object> places) {
        this.places = places;
    }

    public Topic__1 getTopic() {
        return topic;
    }

    public void setTopic(Topic__1 topic) {
        this.topic = topic;
    }

}
