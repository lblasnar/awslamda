
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.List;

@Value
public class Classification__1 {

    @SerializedName("tags")
    @Expose
    List<Tag__1> tags;
    @SerializedName("featuredTag")
    @Expose
    FeaturedTag__1 featuredTag;
    @SerializedName("places")
    @Expose
    List<Object> places;
    @SerializedName("topic")
    @Expose
    Topic__1 topic;
}
