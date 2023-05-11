package com.example.sim.category;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sim.BaseActivity;
import com.example.sim.ChangeImageActivity;
import com.example.sim.MainActivity;
import com.example.sim.R;
import com.example.sim.dto.category.CategoryCreateDTO;
import com.example.sim.service.ApplicationNetwork;
import com.example.sim.utils.CommonUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryCreateActivity extends BaseActivity {

    public static int SELECT_IMAGE_RESULT = 300;
    private ImageView avatar_image;
    private Uri selectCropImage ;
    private TextInputLayout name_TextLayout;
    private TextInputLayout priority_TextLayout;
    private TextInputLayout desc_TextLayout;
    private CategoryCreateDTO categoryTest=null;

    // Activityies
    Intent mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_create);

        avatar_image = findViewById(R.id.my_avatar);
        Button selectAvatar_btn = findViewById(R.id.btn_SelectFoto);
        Button addCategory_btn = findViewById(R.id.btn_Category_Add);
        name_TextLayout = findViewById(R.id.name_textField);
        priority_TextLayout = findViewById(R.id.priority_textField);
        desc_TextLayout = findViewById(R.id.description_textField);


        //Додаванна Активіті на яку перейду
        mainActivity = new Intent(this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //Validation fields
        setupError();


        //Додати категорію
        addCategory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validation())
                    return;
                System.out.println("-----Відправка данних на сервер. Створення нової Категорії");
                categoryTest = new CategoryCreateDTO();

                // Create DTO
                categoryTest.setName(name_TextLayout.getEditText().getText().toString());
                categoryTest.setImageBase64(uriGetBase64(selectCropImage));
                categoryTest.setPriority(Integer.parseInt(priority_TextLayout.getEditText().getText().toString()));
                categoryTest.setDescription(desc_TextLayout.getEditText().getText().toString());
                requestServer(categoryTest);
            }
        });

        //Обрати фото
        selectAvatar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(view);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE_RESULT) {
            selectCropImage = data.getParcelableExtra("croppedUri");
            avatar_image.setImageURI(selectCropImage);
        }
    }

    void requestServer(CategoryCreateDTO model) {
        CommonUtils.showLoading();
        ApplicationNetwork
                .getInstance()
                .getJsonApi()
                .addCategory(model)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response)  {
                        if (response.isSuccessful())
                        {
                            System.out.println("----Category Added successfuly");
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
                        System.out.println("----Category added Error");
                        CommonUtils.hideLoading();
                    }
                });
    }

    public  void selectImage (View view)
    {
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivityForResult(intent,SELECT_IMAGE_RESULT);
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

    private void setupError() {
        name_TextLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if (text.length() <= 2) {
                    name_TextLayout.setError(getString(R.string.category_name_required));
                } else {
                    name_TextLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        priority_TextLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                int number = 0;
                try {
                    number = Integer.parseInt(text.toString());
                } catch (Exception ex) {
                }
                if (number <= 0) {
                    priority_TextLayout.setError(getString(R.string.category_priority_required));
                }
                else {
                    priority_TextLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        desc_TextLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                if (text.length() <= 2) {
                    desc_TextLayout.setError(getString(R.string.category_description_required));
                } else {
                    desc_TextLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private boolean validation() {
        boolean isValid=true;
        String name = name_TextLayout.getEditText().getText().toString();
        int priority =0;
        if (!priority_TextLayout.getEditText().getText().toString().isEmpty())
           priority = Integer.parseInt(priority_TextLayout.getEditText().getText().toString());
        String description = desc_TextLayout.getEditText().getText().toString();

        if(name.isEmpty() || priority<= 0 ||
                description.isEmpty() || selectCropImage == null)
            isValid = false;
        if(name.isEmpty() || name.length()<=2) {
            name_TextLayout.setError(getString(R.string.category_name_required));
            isValid=false;
        } else if (priority<= 0) {
            priority_TextLayout.setError(getString(R.string.category_priority_required));
            isValid=false;
        }else if (description.isEmpty()) {
            desc_TextLayout.setError(getString(R.string.category_description_required));
            isValid=false;
        }
        else if (selectCropImage == null) {
            Toast.makeText(this,getString(R.string.foto_required),Toast.LENGTH_LONG).show();
            isValid=false;
        }

        return isValid;
    }
}