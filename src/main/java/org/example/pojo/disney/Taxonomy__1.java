
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.List;

@Value
public class Taxonomy__1 {

    @SerializedName("tags")
    @Expose
    List<String> tags;
    @SerializedName("places")
    @Expose
    List<Object> places;
    @SerializedName("topic")
    @Expose
    String topic;
    @SerializedName("hicCategory")
    @Expose
    String hicCategory;
    @SerializedName("segments")
    @Expose
    List<Object> segments;
}
