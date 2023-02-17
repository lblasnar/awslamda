
package org.example.pojo.disney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class Editorial {

    @SerializedName("displayDate")
    @Expose
    private Integer displayDate;
    @SerializedName("hideDate")
    @Expose
    private Boolean hideDate;

    public Integer getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(Integer displayDate) {
        this.displayDate = displayDate;
    }

    public Boolean getHideDate() {
        return hideDate;
    }

    public void setHideDate(Boolean hideDate) {
        this.hideDate = hideDate;
    }

}
