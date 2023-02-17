
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class ImageOverride {

    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("source")
    @Expose
    String source;
    @SerializedName("dynamicSource")
    @Expose
    String dynamicSource;
}
