package enlightment.yash.sociopy.viewmodels;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserModel extends ViewModel {

    private int counter = 0;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public int getCounter() {
        return counter;
    }

    public void incrementCounter() {
        counter++;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void fetchData() {
        executorService.execute(() -> {

        });
    }
}
