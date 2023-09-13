
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class Section {

    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("path")
    @Expose
    String path;
    @SerializedName("root")
    @Expose
    String root;
}
