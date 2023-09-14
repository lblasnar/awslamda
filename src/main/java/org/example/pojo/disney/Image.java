
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class Image {

    @SerializedName("id")
    @Expose
    Integer id;
    @SerializedName("link")
    @Expose
    LinkImage link;
    @SerializedName("source")
    @Expose
    String source;
    @SerializedName("dynamicSource")
    @Expose
    String dynamicSource;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("owner")
    @Expose
    Owner owner;
    @SerializedName("height")
    @Expose
    Integer height;
    @SerializedName("width")
    @Expose
    Integer width;
    @SerializedName("pano360")
    @Expose
    Boolean pano360;
    @SerializedName("type")
    @Expose
    String type;
    @SerializedName("imageWarning")
    @Expose
    Boolean imageWarning;
}
