package com.example.sim;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.sim.category.CategoriesAdapter;
import com.example.sim.category.CategoryUpdateActivity;
import com.example.sim.dto.category.CategoryItemDTO;
import com.example.sim.modals.DeleteConfirmation;
import com.example.sim.service.ApplicationNetwork;
import com.example.sim.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private CategoriesAdapter adapter;
    private RecyclerView rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ImageView iv = findViewById(R.id.imageView);
//        String url = "https://pv016.allin.ml/images/1.jpg";
//        Glide.with(this)
//                .load(url)
//                .apply(new RequestOptions().override(600))
//                .into(iv);

        rc = findViewById(R.id.rcvCategories);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        rc.setAdapter(new CategoriesAdapter(new ArrayList<>(), null,null));
        requestServer();
    }

    void requestServer() {
        CommonUtils.showLoading();
        ApplicationNetwork
                .getInstance()
                .getJsonApi()
                .list()
                .enqueue(new Callback<List<CategoryItemDTO>>() {
                    @Override
                    public void onResponse(Call<List<CategoryItemDTO>> call, Response<List<CategoryItemDTO>> response) {
                        List<CategoryItemDTO> data = response.body();
                        adapter = new CategoriesAdapter(data,
                                MainActivity.this::onClickByItemEdit,
                                MainActivity.this::onClickByItemDelete);
                        rc.setAdapter(adapter);
                        //int a=5;
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<List<CategoryItemDTO>> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });
    }

    void requestServerById() {
        CommonUtils.showLoading();
        ApplicationNetwork
                .getInstance()
                .getJsonApi()
                .getById(13)
                .enqueue(new Callback<CategoryItemDTO>() {
                    @Override
                    public void onResponse(Call<CategoryItemDTO> call, Response<CategoryItemDTO> response) {
                        CategoryItemDTO data = response.body();

                        // int a=5;
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<CategoryItemDTO> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });
    }

    private void onClickByItemDelete(CategoryItemDTO item){

        //Toast.makeText(HomeApplication.getAppContext(),"Delete item Id: "+item.getId(),Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, DeleteConfirmation.class);
        intent.putExtra("itemId", item.getId());
        intent.putExtra("name", item.getName());
        startActivity(intent);
        finish();
    }
    private void onClickByItemEdit(CategoryItemDTO item){
        //Toast.makeText(HomeApplication.getAppContext(),"Edit item name: ",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, CategoryUpdateActivity.class);
        intent.putExtra("itemId", item.getId());
        startActivity(intent);
        finish();
    }
}