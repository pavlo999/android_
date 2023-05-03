package com.example.sim.category;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sim.R;

public class CategoryCartViweHolder extends RecyclerView.ViewHolder {
    private ImageView categoryImage;
    private TextView categoryName;
    private Button editBtn;
    private Button deleteBtn;

    public ImageView getCategoryImage() {
        return categoryImage;
    }
    public TextView getCategoryName() {
        return categoryName;
    }
    public Button getEditBtn() {return editBtn;}
    public Button getDeleteBtn() {return deleteBtn;}


    public CategoryCartViweHolder(@NonNull View itemView) {
        super(itemView);
        categoryName = itemView.findViewById(R.id.categoryName);
        categoryImage = itemView.findViewById(R.id.categoryImage);
        editBtn= itemView.findViewById(R.id.editBtn);
        deleteBtn= itemView.findViewById(R.id.deleteBtn);
    }
}
