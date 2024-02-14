package enlightment.yash.sociopy.viewmodels;

import androidx.lifecycle.ViewModel;

import com.google.gson.annotations.SerializedName;

import java.util.regex.Pattern;

import enlightment.yash.sociopy.Helper;

public class CredentialModel extends ViewModel {

    @SerializedName("email")
    protected String email;

    @SerializedName("first_name")
    protected String firstName;

    @SerializedName("last_name")
    protected String lastName;
    private String password;
    private static boolean isValid;
    private String message;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public CredentialModel() {

    }

    public CredentialModel(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public final int verify(int selection, String... credentials) throws Exception {
        int error = 0;
        return switch (selection) {
            case 1 -> {
                String email = credentials[0], password = credentials[1];
                if (email.isEmpty()) {
                    message = "please enter your email address";
                    error = 1;
                } else if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
                    message = "please enter a valid email address";
                    error = 2;
                } else if (password.isEmpty()) {
                    message = "please enter password";
                    error = 3;
                } else {
                    this.email = email;
                    this.password = password;
                    message = "sign in successful";
                    isValid = true;
                }
                yield error;
            }
            default -> {
                String fullName = credentials[0], email = credentials[1], password = credentials[2], confirmPassword = credentials[3];
                if (fullName.isEmpty()) {
                    message = "please enter your name";
                    error = 1;
                } else if (email.isEmpty()) {
                    message = "please enter your email address";
                    error = 2;
                } else if (password.isEmpty()) {
                    message = "please enter password";
                    error = 3;
                } else if (fullName.split(" ").length != 2) {
                    message = "please enter a valid full name";
                    error = 4;
                } else if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
                    message = "please enter a valid email address";
                    error = 5;
                } else if (!password.equals(confirmPassword)) {
                    message = "password does not match";
                    error = 6;
                } else {
                    Helper.KEY = "mySecretKey@123myRegisterationKey@123";
                    this.email = email;
                    this.firstName = fullName.split(" ")[0];
                    this.lastName = fullName.split(" ")[1];
                    this.password = password;
                    message = "sign up successful";
                    isValid = true;
                }
                yield error;
            }
        };
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }

    public boolean isValid() {
        return isValid;
    }
}
