package com.example.sim.user;

import static com.example.sim.category.CategoryCreateActivity.SELECT_IMAGE_RESULT;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sim.BaseActivity;
import com.example.sim.ChangeImageActivity;
import com.example.sim.R;
import com.example.sim.application.HomeApplication;
import com.example.sim.constants.Urls;
import com.example.sim.dto.user.ProfileDTO;
import com.example.sim.dto.user.UpdateProfileResponse;
import com.example.sim.service.ApplicationNetwork;
import com.example.sim.utils.CommonUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    private Uri selectCropImage =null;
    private ImageView avatar_image;
    private TextInputLayout tlFirstName;
    private TextInputLayout tlSecondName;
    private TextInputLayout tlEmail;
    private Button selectAvatar_btn;
    private Button update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        avatar_image = findViewById(R.id.IVPreviewImage);
        tlFirstName = findViewById(R.id.tfFirstName);
        tlSecondName = findViewById(R.id.tfLastName);
        tlEmail = findViewById(R.id.tfEmail);
        selectAvatar_btn = findViewById(R.id.btnSelectImage);
        update_btn = findViewById(R.id.btnUpdate);

        String token = HomeApplication.getInstance().getToken();
        getDataFromToken(token);

    }

    public void onClickUpdate(View view) {
        ProfileDTO profile= new ProfileDTO();
        profile.setLastName(tlSecondName.getEditText().getText().toString());
        profile.setFirstName(tlFirstName.getEditText().getText().toString());
        profile.setEmail(tlEmail.getEditText().getText().toString());
        if(selectCropImage !=null)
        profile.setImageBase64(uriGetBase64(selectCropImage));
        CommonUtils.showLoading();
        ApplicationNetwork.getInstance()
                .getUserJsonApi()
                .updateProfile(profile)
                .enqueue(new Callback<UpdateProfileResponse>() {
                    @Override
                    public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                        if(response.isSuccessful())
                        {
                            String token = response.body().getToken();
                            HomeApplication.getInstance().saveJwtToken(token);
                            getDataFromToken(token);
                        }
                        else
                        {
//                            try {
//                                String resp = response.errorBody().string();
//                                showErrorsServer(resp);
//                            }catch(Exception ex) {
//                                System.out.println("Error try");;
//                            }
                            Toast.makeText(ProfileActivity.this,"Не вірно вказані данні" , Toast.LENGTH_LONG).show();
                        }
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });


    }
    private void getDataFromToken(String token){
        String [] parts = token.split("\\.");
        String payload = parts[1];
        String payloadDecoded = new String(Base64.decode(payload, Base64.DEFAULT));
        String [] payloadParts = payloadDecoded.split(",");
        String email = payloadParts[0].split(":")[1];
        String firstName = payloadParts[1].split(":")[1];
        String lastName = payloadParts[2].split(":")[1];
        String image = payloadParts[4].split(":")[1];
        tlEmail.getEditText().setText(email.replace(("\""),""));
        tlFirstName.getEditText().setText(firstName.replace(("\""),""));
        tlSecondName.getEditText().setText(lastName.replace(("\""),""));
        Glide.with(this).load(Urls.BASE + "/images/" + image.replace(
                "\"", "")).into(avatar_image);
    }
    private String uriGetBase64(Uri uri) {
        try {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] byteArr = bytes.toByteArray();
            return Base64.encodeToString(byteArr, Base64.DEFAULT);

        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_RESULT) {
            selectCropImage = (Uri) data.getParcelableExtra("croppedUri");
            avatar_image.setImageURI(selectCropImage);
        }
    }
    //Вибір фото і її обрізання
    public void onClickSelectImage(View view) {
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivityForResult(intent, SELECT_IMAGE_RESULT);
    }
}