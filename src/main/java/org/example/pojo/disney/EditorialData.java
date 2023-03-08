
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class EditorialData {

    @SerializedName("displayDate")
    @Expose
    Integer displayDate;
    @SerializedName("hideDate")
    @Expose
    Boolean hideDate;
}
