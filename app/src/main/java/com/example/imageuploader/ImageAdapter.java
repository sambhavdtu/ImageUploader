package com.example.imageuploader;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    List<Upload> mUploads;
    Context mContext;
    OnItemClickListener mListener;



    public ImageAdapter(List<Upload> uploads, Context context) {
        this.mUploads = uploads;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_view, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageView imageView = holder.getImageView();
        TextView textView = holder.getTextView();


        textView.setText(mUploads.get(position).getName());
        Log.e("ADAPTER", mUploads.get(position).getUri());
        Picasso.get()
                .load(mUploads.get(position).getUri())
                .placeholder(R.mipmap.ic_launcher_round)
                .fit()
                .centerCrop()
                .into(imageView);

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemDelete(int position);
        void onDoSomething(int position);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener,
            MenuItem.OnMenuItemClickListener{
        private TextView mTextView;
        private ImageView mImageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.image_name);
            mImageView = itemView.findViewById(R.id.uploaded_image);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        public TextView getTextView() {
            return mTextView;
        }

        public ImageView getImageView() {
            return mImageView;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do Whatever");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onDoSomething(position);
                            return true;
                        case 2:
                            mListener.onItemDelete(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }


}



