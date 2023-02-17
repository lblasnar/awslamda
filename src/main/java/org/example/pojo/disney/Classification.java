
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

import javax.annotation.Generated;
import java.util.List;

@Value
public class Classification {

    @SerializedName("tags")
    @Expose
    List<Tag> tags;
    @SerializedName("featuredTag")
    @Expose
    FeaturedTag featuredTag;
    @SerializedName("places")
    @Expose
    List<Object> places;
    @SerializedName("topic")
    @Expose
    Topic topic;
}
