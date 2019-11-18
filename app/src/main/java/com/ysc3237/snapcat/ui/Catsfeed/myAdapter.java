package com.ysc3237.snapcat.ui.Catsfeed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.ysc3237.snapcat.R;
import java.util.List;



/**
 * Creates RecyclerView.Adapter for the CatViewHolders
 * @since 1.0
 * @see CatViewHolder
 */
public class myAdapter extends RecyclerView.Adapter<CatViewHolder> {

    private Context mContext;
    private List< CatData > mCatList;

    /**
     * this constructor will be used to pass data from MainActivity.java to this adapter.
     * @param mContext
     * @param mCatList
     */
    myAdapter(Context mContext, List<CatData> mCatList) {
        this.mContext = mContext;
        this.mCatList = mCatList;
    }

    /**
     * Creates empty CatViewHolder within parent ViewGroup.
     * @param parent
     * @param viewType
     * @return CatViewHolder
     */
    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_item, parent, false);
        return new CatViewHolder(mView);
    }

    /**
     * Click function so that when we tap on any of the cat images, we will move to another page which shows you the picture and a description.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final CatViewHolder holder, int position) {
        CatData cat = mCatList.get(position);
        int cat_idx = cat.getCatImage();

        if(cat.getCatBitmap() != null)
            holder.mImage.setImageBitmap(cat.getCatBitmap());
        else
            holder.mImage.setImageResource(cat_idx);

        holder.mTitle.setText(cat.getCatName());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {

            /**
             * use Android Intent to pass the image, title and description to DetailActivity.java class.
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, DetailActivity.class);
                CatData cat = mCatList.get(holder.getAdapterPosition());

                mIntent.putExtra("Title", cat.getCatName());
                mIntent.putExtra("Description", cat.getCatDescription());


                Bitmap catB = cat.getCatBitmap() ;
                if(catB != null) {
                    mIntent.putExtra("Image", -1);
                    mIntent.putExtra("Bitmap", catB);
                }
                else
                    mIntent.putExtra("Image", cat.getCatImage());

                mContext.startActivity(mIntent);

            }
        });

    }

    /**
     * Counts number of cats.
     * @return int size of cat list.
     */
    @Override
    public int getItemCount() {
        return mCatList.size();
    }
}


/**
 * Template class for viewing single cat image with name and caption
 * @see myAdapter
 * @since 1.0
 */
class CatViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    TextView mTitle;
    CardView mCardView;

    /**
     * Declare and initialize Android ImageView and TextView.
     * @param itemView
     */

    CatViewHolder(View itemView) {
        super(itemView);

        mImage =  itemView.findViewById(R.id.ivImage);
        mTitle =  itemView.findViewById(R.id.tvTitle);
        mCardView = itemView.findViewById(R.id.cardview);

    }
}

