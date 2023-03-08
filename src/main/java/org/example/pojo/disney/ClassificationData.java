
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.List;

@Value
public class ClassificationData {

    @SerializedName("tags")
    @Expose
    List<TagClassificationData> tags;
    @SerializedName("featuredTag")
    @Expose
    FeaturedTagClassificationData featuredTag;
    @SerializedName("places")
    @Expose
    List<Object> places;
    @SerializedName("topic")
    @Expose
    TopicClassificationData topic;
}
