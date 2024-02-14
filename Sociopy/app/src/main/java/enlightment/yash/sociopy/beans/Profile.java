package enlightment.yash.sociopy.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import enlightment.yash.sociopy.viewmodels.CredentialModel;

public class Profile extends CredentialModel implements Parcelable {

    public Profile(Parcel in) {
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
    }

    public Profile(String email, String firstName, String lastName) {
        super(email, firstName, lastName);
    }

    public static final Parcelable.Creator<Profile> CREATOR =
            new Parcelable.Creator<>() {
                @Override
                public Profile createFromParcel(Parcel source) {
                    return new Profile(source);
                }

                @Override
                public Profile[] newArray(int size) {
                    return new Profile[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s %s is logged with %s", firstName, lastName, email);
    }
}
