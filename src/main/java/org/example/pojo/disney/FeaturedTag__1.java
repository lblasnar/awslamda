
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class FeaturedTag__1 {

    @SerializedName("name")
    @Expose
     String name;
    @SerializedName("data")
    @Expose
     String data;

}
