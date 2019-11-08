package com.ysc3237.snapcat.ui.Catsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ysc3237.snapcat.R;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<CatViewHolder> {

    private Context mContext;
    private List< CatData > mCatList;

    myAdapter(Context mContext, List< CatData > mCatList) {
        this.mContext = mContext;
        this.mCatList = mCatList;
    }

    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_item, parent, false);
        return new CatViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {
        holder.mImage.setImageResource(mCatList.get(position).getCatImage());
        holder.mTitle.setText(mCatList.get(position).getCatName());

    }

    @Override
    public int getItemCount() {
        return mCatList.size();
    }
}

class CatViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    TextView mTitle;
    //CardView mCardView;


    CatViewHolder(View itemView) {
        super(itemView);

        mImage =  itemView.findViewById(R.id.ivImage);
        mTitle =  itemView.findViewById(R.id.tvTitle);
        //mCardView = itemView.findViewById(R.id.cardview);

    }
}

