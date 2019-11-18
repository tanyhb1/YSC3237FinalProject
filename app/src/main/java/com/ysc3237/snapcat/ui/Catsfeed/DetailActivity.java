package com.ysc3237.snapcat.ui.Catsfeed;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ysc3237.snapcat.R;
import com.ysc3237.snapcat.MainActivity;

import java.util.List;

/**
 * Activity containing details of catphoto (image and description) once clicked on catsfeed fragment.
 * @since 1.0
 */
public class DetailActivity extends AppCompatActivity {

    ImageView mCat;
    TextView mDescription;
    TextView mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mCat = findViewById(R.id.ivImage);
        mDescription = findViewById(R.id.tvDescription);
        mName = findViewById(R.id.tvName);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mDescription.setText(mBundle.getString("Description"));
            mName.setText(mBundle.getString("Title"));

            int cat_key = mBundle.getInt("Image");
            if (cat_key < 0 ) {
                Bitmap catimage = (Bitmap) getIntent().getParcelableExtra("Bitmap");
                mCat.setImageBitmap(catimage);
            } else
                mCat.setImageResource(cat_key);
        }
    }
}