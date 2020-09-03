package com.example.imageuploader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseReference;
    private List<Upload> mUploads;
    private ImageAdapter mImageAdapter;
    private ProgressBar mProgressBar;
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ImagesActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mUploads = new ArrayList<>();
        mImageAdapter = new ImageAdapter(mUploads, ImagesActivity.this);
        mImageAdapter.setOnItemClickListener(ImagesActivity.this);
        mRecyclerView.setAdapter(mImageAdapter);
        mProgressBar = findViewById(R.id.progress_circular);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        mValueEventListener = mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploads.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = childSnapshot.getValue(Upload.class);
                    upload.setKey(childSnapshot.getKey());
                    Log.e("TAG", upload.getName() + upload.getUri());
                    mUploads.add(upload);
                }
                mImageAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ImagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });


    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Item clicked at position" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemDelete(int position) {
        final Upload upload = mUploads.get(position);
        StorageReference storageReference = mStorage.getReferenceFromUrl(upload.getUri());
        storageReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mDatabaseReference.child(upload.getKey()).removeValue();
                        Toast.makeText(ImagesActivity.this, "Deletion Successful", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ImagesActivity.this, "Deletion failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onDoSomething(int position) {
        Toast.makeText(this, "Do something with item" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseReference.removeEventListener(mValueEventListener);
    }
}
