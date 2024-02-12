package enlightment.yash.sociopy.retrofit;

import enlightment.yash.sociopy.beans.Profile;
import enlightment.yash.sociopy.beans.Status;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MyApiService {

    @Multipart
    @POST("post")
    Call<Status> post(
            @Part("post_data\"; filename=\"myJson.json\" ") RequestBody json,
            @Part("post_image\"; filename=\"myfile.jpg\" ") RequestBody file
            );

    @GET("login/{email}/{password}")
    Call<Profile> login(
            @Path("email") String email,
            @Path("password") String password
    );

    @Multipart
    @GET("register/{signature}")
    Call<Status> register(
            @Part("post_data\"; filename=\"text.json\"") RequestBody json,
            @Path("signature") String signature
    );
}
