package com.ysc3237.snapcat.ui.Catsfeed;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ysc3237.snapcat.R;

/**
 * Activity containing details of catphoto (image and description) once clicked on catsfeed fragment.
 * @since 1.0
 */
public class DetailActivity extends AppCompatActivity {

    ImageView mCat;
    TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCat = findViewById(R.id.ivImage);
        mDescription = findViewById(R.id.tvDescription);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mCat.setImageResource(mBundle.getInt("Image"));
            mDescription.setText(mBundle.getString("Description"));
        }
    }
}