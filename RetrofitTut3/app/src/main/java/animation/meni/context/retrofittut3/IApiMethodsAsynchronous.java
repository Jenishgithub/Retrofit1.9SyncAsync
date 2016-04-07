package animation.meni.context.retrofittut3;


import rx.Observable;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by CrossoOverNepal on 4/7/2016.
 */
public interface IApiMethodsAsynchronous {

    @GET("/users/{login}")
    Observable<Github> getCurators(
            @Path("login") String login
    );
}
