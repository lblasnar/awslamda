
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class TopicClassificationData {

    @SerializedName("name")
    @Expose
    String name;
}
