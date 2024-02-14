package enlightment.yash.sociopy.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import enlightment.yash.sociopy.Helper;
import enlightment.yash.sociopy.R;
import enlightment.yash.sociopy.SociopyService;
import enlightment.yash.sociopy.beans.Status;
import enlightment.yash.sociopy.viewmodels.CredentialModel;
import enlightment.yash.sociopy.viewmodels.PostModel;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class PostActivity extends AppCompatActivity {

    private View bottomSheetView;
    private ImageView postPicture, bottomCameraImage;
    private ImageButton cancelImage;
    private TextView pick, click;
    private TextInputEditText caption;
    private ViewGroup viewGroup;
    private BottomSheetDialog dialog;
    private boolean isPick = false;
    private PostModel postModel;

    @Inject
    public SociopyService service;

    private final ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isPick -> {
    });

    private final ActivityResultLauncher<Intent> pickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            assert result.getData() != null;
            File file = new File(Environment.getExternalStorageDirectory(), "my_photo.jpg");
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            postModel.setUri(uri);
        }
    });

    private final ActivityResultLauncher<Intent> clickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            Bitmap photo = data != null && data.getExtras() != null ? (Bitmap) data.getExtras().get("data") : null;
            Uri uri = photo != null ? getImageUri(getApplicationContext(), photo) : null;
            postModel.setUri(uri);
            File file = uri != null ? new File(getRealPathFromUri(uri)) : null;
            postModel.setFile(file);
            cancelImage.setVisibility(postModel.getFile() != null ? View.VISIBLE : View.GONE);
            Toast.makeText(this, file != null ? file.getAbsolutePath() : "no path", Toast.LENGTH_LONG).show();
        }
    });

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromUri(Uri uri) {
        String filePath = "";
        Cursor cursor = getContentResolver() != null ? getContentResolver().query(uri, null, null, null, null) : null;
        if (cursor != null) {
            try {
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                filePath = cursor.getString(index);
            } catch (Exception e) {
                Toast.makeText(this, "some error occurred while processing path from uri", Toast.LENGTH_SHORT).show();
            } finally {
                cursor.close();
            }
            cursor.close();
        }
        return filePath;
    }

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        initializeViews();

        if (savedInstanceState == null) {
            CredentialModel userData = getIntent().getParcelableExtra("profile") != null ? getIntent().getParcelableExtra("profile") : null;
            postModel = new ViewModelProvider(this).get(PostModel.class);
            postModel.setEmail(userData != null ? userData.getEmail() : null);
        }

        postPicture.setOnClickListener(v -> {
            dialog.show();
        });

        if (postModel.getFile() != null) {
            cancelImage.setVisibility(View.VISIBLE);
        }

        cancelImage.setOnClickListener(v -> {
            postModel.setUri(null);
            postModel.setFile(null);
            cancelImage.setVisibility(View.GONE);
        });

        click.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            /*if (intent.resolveActivity(getPackageManager()) != null) {
                clickLauncher.launch(intent);
            } else {
                Toast.makeText(this, "no activity was found", Toast.LENGTH_SHORT).show();
            }*/
            clickLauncher.launch(intent);
            dialog.dismiss();
        });

        pick.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "nothing to show right now", Toast.LENGTH_SHORT).show();
                }
            } else {
                isPick = true;
            }

            if (isPick) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    pickLauncher.launch(intent);
                } else {
                    Toast.makeText(this, "no activity was found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeViews() {
        viewGroup = findViewById(R.id.postLayout);
        postPicture = findViewById(R.id.postPicture);
        caption = findViewById(R.id.postTextTextInputEditText);
        cancelImage = findViewById(R.id.cancelImage);

        bottomSheetView = getLayoutInflater().inflate(R.layout.post_bottom_sheet, viewGroup, false);
        click = bottomSheetView.findViewById(R.id.bottomClick);
        pick = bottomSheetView.findViewById(R.id.bottomPick);
        bottomCameraImage = bottomSheetView.findViewById(R.id.bottomCameraImage);
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            bottomCameraImage.setVisibility(View.VISIBLE);
            click.setVisibility(View.VISIBLE);
        }
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(bottomSheetView);
    }

    public void cancelPost(View view) {
        finish();
    }

    public void post(View view) throws JSONException {
        String comment = Objects.requireNonNull(caption.getText()).toString();
        postModel.setCaption(!comment.isEmpty() ? comment : "no_comment");
        HashMap<String, Object> map = new HashMap<>();
        map.put("email", postModel.getEmail());
        map.put("caption_signature", postModel.getCaption());
        JSONObject jsonObject = Helper.getJSONObject(map);
        final File data = Helper.getJsonFile(getApplicationContext().getFilesDir() + "text.json", jsonObject);
        RequestBody dataBody = RequestBody.create(MediaType.parse("json"), data);
        RequestBody imageBody = postModel.getFile() != null ? RequestBody.create(MediaType.parse(getContentResolver().getType(postModel.getUri())), postModel.getFile()) : null;
        if (imageBody != null) {
            Call<Status> call = service.getService().post(dataBody, imageBody);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    Toast.makeText(PostActivity.this, response.code() == 200 ? "success" : "failure", Toast.LENGTH_SHORT).show();
                    if (data.exists()) {
                        data.delete();
                    }
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    Toast.makeText(PostActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}