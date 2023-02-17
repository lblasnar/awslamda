package org.example.pojo;

/**
 * @author luigi
 * 16/02/2023
 */
public final class MessageDTO {
    String id;
    String description;

    public MessageDTO(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public MessageDTO(String id){
        this.id = id;
        this.description = "Default message";
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
