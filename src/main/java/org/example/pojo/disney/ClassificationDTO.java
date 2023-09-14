
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class ClassificationDTO {

    @SerializedName("timestamp")
    @Expose
    Integer timestamp;
    @SerializedName("version")
    @Expose
    String version;
    @SerializedName("correlationId")
    @Expose
    String correlationId;
    @SerializedName("data")
    @Expose
    Data data;
    @SerializedName("status")
    @Expose
    Status status;

}
