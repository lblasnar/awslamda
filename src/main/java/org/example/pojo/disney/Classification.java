
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.util.List;

@Generated("jsonschema2pojo")
public class Classification {

    @SerializedName("tags")
    @Expose
    private List<Tag> tags;
    @SerializedName("featuredTag")
    @Expose
    private FeaturedTag featuredTag;
    @SerializedName("places")
    @Expose
    private List<Object> places;
    @SerializedName("topic")
    @Expose
    private Topic topic;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public FeaturedTag getFeaturedTag() {
        return featuredTag;
    }

    public void setFeaturedTag(FeaturedTag featuredTag) {
        this.featuredTag = featuredTag;
    }

    public List<Object> getPlaces() {
        return places;
    }

    public void setPlaces(List<Object> places) {
        this.places = places;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

}
