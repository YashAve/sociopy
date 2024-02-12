package enlightment.yash.sociopy.beans;

import com.google.gson.annotations.SerializedName;

public class Status {

    @SerializedName("message")
    private String message;

    public Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
