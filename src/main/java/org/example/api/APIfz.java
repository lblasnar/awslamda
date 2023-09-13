package org.example.api;

import org.example.pojo.disney.ClassificationDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

/**
 * @author luigi
 * 16/02/2023
 */
public interface APIfz {
    @POST
    Call<ClassificationDTO> sendId(@Url String url);

    @GET
    Call<ClassificationDTO> getId(@Url String url);

    @PUT
    Call<ClassificationDTO> putId(@Url String url);
}
