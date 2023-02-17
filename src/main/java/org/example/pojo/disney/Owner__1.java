
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class Owner__1 {

    @SerializedName("source")
    @Expose
    String source;
    @SerializedName("link")
    @Expose
    Link__3 link;
}
