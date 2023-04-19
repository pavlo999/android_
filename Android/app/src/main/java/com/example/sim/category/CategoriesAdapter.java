package com.example.sim.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sim.R;
import com.example.sim.application.HomeApplication;
import com.example.sim.constants.Urls;
import com.example.sim.dto.category.CategoryItemDTO;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoryCartViweHolder> {

    private List<CategoryItemDTO> categories;

    public CategoriesAdapter(List<CategoryItemDTO> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryCartViweHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.category_viwe,parent,false);
        return new CategoryCartViweHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryCartViweHolder holder, int position) {

        if(categories!=null && position<categories.size())
        {
            CategoryItemDTO item  = categories.get(position);
            holder.getCategoryName().setText(item.getName());
            String url = Urls.BASE+item.getImage();
            Glide.with(HomeApplication.getAppContext())
                    .load(url)
                    .apply(new RequestOptions().override(600))
                    .into(holder.getCategoryImage());
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
