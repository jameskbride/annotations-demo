import com.jameskbride.annotations.RetrofitBase;
import com.jameskbride.annotations.GET;
import okhttp3.Call;

@RetrofitBase
public interface SimpleMethodBase {

    @GET()
    Call returnSomething();
}