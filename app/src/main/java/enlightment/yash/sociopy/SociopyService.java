package enlightment.yash.sociopy;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import enlightment.yash.sociopy.retrofit.MyApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@InstallIn(SingletonComponent.class)
@Module
class SociopyServiceBasics {

    private final String BASE_URL = "http://10.0.2.2:7777/";

    @Singleton
    @Provides
    public SociopyService provideString() {
        return new SociopyService(BASE_URL);
    }
}

@Singleton
public class SociopyService {

    private final String BASE_URL;
    private MyApiService service;

    @Inject
    public SociopyService(String BASE_URL) {
        this.BASE_URL = BASE_URL;
        this.service = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApiService.class);
    }

    public MyApiService getService() {
        return service;
    }
}
