import com.jameskbride.annotations.Base;
import com.jameskbride.annotations.GET;

@Base
public interface WrongReturnTypeBase {

    @GET
    TestClass getTestClass();
}