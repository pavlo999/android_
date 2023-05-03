package com.example.sim.category;

import static com.example.sim.category.CategoryCreateActivity.SELECT_IMAGE_RESULT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sim.BaseActivity;
import com.example.sim.ChangeImageActivity;
import com.example.sim.MainActivity;
import com.example.sim.R;
import com.example.sim.constants.Urls;
import com.example.sim.dto.category.CategoryCreateDTO;
import com.example.sim.dto.category.CategoryItemDTO;
import com.example.sim.dto.category.CategoryUpdateDTO;
import com.example.sim.service.CategoryNetwork;
import com.example.sim.utils.CommonUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryUpdateActivity extends BaseActivity {

    private Uri selectCropImage ;
    private ImageView avatar_image;
    private TextInputLayout name_TextLayout;
    private TextInputLayout priority_TextLayout;
    private TextInputLayout desc_TextLayout;
    private Button selectAvatar_btn;
    private Button update_btn;
    private Intent mainActivity;
    private CategoryUpdateDTO updateDto=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_update);

        avatar_image = findViewById(R.id.my_avatar);
        name_TextLayout = findViewById(R.id.name_textField);
        priority_TextLayout = findViewById(R.id.priority_textField);
        desc_TextLayout = findViewById(R.id.description_textField);
        selectAvatar_btn = findViewById(R.id.btn_SelectFoto);
        update_btn = findViewById(R.id.btn_update);

        //Додаванна Активіті на яку перейду
        mainActivity = new Intent(this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            int id  =Integer.parseInt(arguments.get("itemId").toString());
            updateDto = new CategoryUpdateDTO(id);
            requestServerById(id);
        }

        selectAvatar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDto.setName(name_TextLayout.getEditText().getText().toString());
                updateDto.setPriority(Integer.parseInt(priority_TextLayout.getEditText().getText().toString()));
                updateDto.setDescription(desc_TextLayout.getEditText().getText().toString());
                requestServerUpdate(updateDto);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE_RESULT) {
            selectCropImage = data.getParcelableExtra("croppedUri");
            avatar_image.setImageURI(selectCropImage);

            updateDto.setImageBase64(uriGetBase64(selectCropImage));
        }
    }

    private String uriGetBase64(Uri uri) {
        try {
            Bitmap bitmap=null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch(IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] byteArr = bytes.toByteArray();
            return Base64.encodeToString(byteArr, Base64.DEFAULT);

        } catch(Exception ex) {
            return null;
        }
    }
    public  void selectImage (View view)
    {
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivityForResult(intent,SELECT_IMAGE_RESULT);
    }

    void requestServerById(int id) {
        CommonUtils.showLoading();
        CategoryNetwork
                .getInstance()
                .getJsonApi()
                .getById(id)
                .enqueue(new Callback<CategoryItemDTO>() {
                    @Override
                    public void onResponse(Call<CategoryItemDTO> call, Response<CategoryItemDTO> response) {
                        CategoryItemDTO data = response.body();
                        name_TextLayout.getEditText().setText(data.getName());
                        priority_TextLayout.getEditText().setText(String.valueOf(data.getPriority()));
                        desc_TextLayout.getEditText().setText(data.getDescription());

                        String urlImage = Urls.BASE+data.getImage();
                 Glide.with(CategoryUpdateActivity.this)
                .load(urlImage)
                .apply(new RequestOptions().override(600))
                .into(avatar_image);
                        // int a=5;
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<CategoryItemDTO> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });
    }

    void requestServerUpdate(CategoryUpdateDTO model) {
        CommonUtils.showLoading();
        CategoryNetwork
                .getInstance()
                .getJsonApi()
                .updateCategory(model)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response)  {
                        if (response.isSuccessful())
                        {

                            System.out.println("----Category update successfuly");
                            startActivity(mainActivity);
                            finish();
                            CommonUtils.hideLoading();
                        }
                        else{
                            System.out.println("Server erroe");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("----Category update Error");
                        CommonUtils.hideLoading();
                    }
                });
    }
}