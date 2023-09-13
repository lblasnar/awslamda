
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class OwnerVideo {

    @SerializedName("source")
    @Expose
    String source;
    @SerializedName("link")
    @Expose
    LinkOwnerVideo link;
}
