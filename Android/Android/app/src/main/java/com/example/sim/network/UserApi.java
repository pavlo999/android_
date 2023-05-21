package com.example.sim.network;

import com.example.sim.dto.user.ProfileDTO;
import com.example.sim.dto.user.UpdateProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {
    @GET("/api/user/{id}")
    public Call<Void> getById(@Path("id") int id);
    @PUT("api/user/updateProfile")
    public Call<UpdateProfileResponse> updateProfile(@Body ProfileDTO model);

}
