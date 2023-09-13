
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class Owner {

    @SerializedName("credit")
    @Expose
    String credit;
}
