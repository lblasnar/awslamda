
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class Editorial__1 {

    @SerializedName("displayDate")
    @Expose
    private Integer displayDate;
    @SerializedName("hideDate")
    @Expose
    private Boolean hideDate;
}
