package enlightment.yash.sociopy.activities;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import enlightment.yash.sociopy.MuchSimpler;
import enlightment.yash.sociopy.R;

@AndroidEntryPoint
public class MainActivity2 extends AppCompatActivity {

    @Inject
    public MuchSimpler simpler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toast.makeText(this, simpler.getComplexName(), Toast.LENGTH_LONG).show();
        Toast.makeText(this, simpler.getComplicatedName(), Toast.LENGTH_LONG).show();

        Uri uri = ContentUris.withAppendedId(UserDictionary.Words.CONTENT_URI, 4);

        //Toast.makeText(this, simple.doSomethingSimpler(), Toast.LENGTH_LONG).show();

        queryWords();
    }

    private void queryWords() {

        String[] projections = {UserDictionary.Words._ID, UserDictionary.Words.WORD};

        Cursor cursor = getContentResolver().query(
                UserDictionary.Words.CONTENT_URI,
                projections,
                null,
                null,
                null
        );

        if (cursor != null) {
            try (cursor) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String word = cursor.getString(cursor.getColumnIndex(UserDictionary.Words.WORD));
                    Toast.makeText(this, word, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}