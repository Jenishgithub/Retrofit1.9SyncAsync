package animation.meni.context.retrofittut3;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by CrossoOverNepal on 4/6/2016.
 */
//synchronous
public interface IApiMethods {

    @GET("/users/{login}")
    Github getCurators(
            @Path("login") String login
    );
}
