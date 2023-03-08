
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class LinkVideo {

    @SerializedName("url")
    @Expose
    String url;
    @SerializedName("canonical")
    @Expose
    String canonical;
    @SerializedName("api")
    @Expose
    String api;
    @SerializedName("top")
    @Expose
    String top;
}
