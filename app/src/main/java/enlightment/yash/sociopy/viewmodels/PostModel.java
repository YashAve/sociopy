package enlightment.yash.sociopy.viewmodels;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import java.io.File;

public class PostModel extends ViewModel {

    private String email;
    private String caption;
    private Uri uri;
    private File file;

    public PostModel() {

    }

    public PostModel(String email, String caption) {
        this.email = email;
        this.caption = caption;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
