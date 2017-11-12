import com.jameskbride.annotations.Base;
import com.jameskbride.annotations.GET;
import com.jameskbride.adapter.Call;

@Base
public interface SimpleMethodBase {

    @GET()
    Call<TestClass> returnSomething();
}