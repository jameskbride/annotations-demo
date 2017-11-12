import com.jameskbride.annotations.Base;
import com.jameskbride.annotations.GET;
import okhttp3.Call;

@Base
public interface SimpleMethodBase {

    @GET()
    Call returnSomething();
}