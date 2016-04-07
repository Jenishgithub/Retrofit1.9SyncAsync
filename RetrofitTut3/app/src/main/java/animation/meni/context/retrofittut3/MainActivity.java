package animation.meni.context.retrofittut3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "60BLHNQCAOUFPIBZ";
    private static final String API_URL = "https://api.github.com";
    TextView textView;
    Button btnSynchronous, btnAsynchronous, btnClear;
    RestAdapter restAdapter;
    List<Github> curatorlistsynchronous = new ArrayList<>();
    List<Github> curatorlistasynchronous = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        btnSynchronous = (Button) findViewById(R.id.btnSynchronous);
        btnAsynchronous = (Button) findViewById(R.id.btnAsynchronous);
        btnClear = (Button) findViewById(R.id.btnClear);

        btnSynchronous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundTask task = new BackgroundTask();
                task.execute();
            }
        });
        btnAsynchronous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IApiMethodsAsynchronous methods = restAdapter.create(IApiMethodsAsynchronous.class);

                for (String login : Data.githubList) {
                    methods.getCurators(login)//method getUser() returns Observable
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Github>() {
                                @Override
                                public final void onCompleted() {
                                    // do nothing
                                    StringBuilder builder = new StringBuilder();
                                    for (int i = 0; i < curatorlistasynchronous.size(); i++) {
                                        builder.append(curatorlistasynchronous.get(i).getBlog());
                                        builder.append("\n");
                                    }
                                    String result = builder.toString();
                                    textView.setText(result);
                                }

                                @Override
                                public final void onError(Throwable e) {
                                    Log.e("GithubDemo", e.getMessage());
                                }

                                @Override
                                public final void onNext(Github response) {
                                    curatorlistasynchronous.add(response);
                                }
                            });
                }
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                curatorlistasynchronous.clear();
                curatorlistsynchronous.clear();
            }
        });


    }


    private class BackgroundTask extends AsyncTask<Void, Void,
            List<Github>> {


        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_URL)
                    .build();
        }

        @Override
        protected List<Github> doInBackground(Void... params) {
            IApiMethods methods = restAdapter.create(IApiMethods.class);
            for (String login :
                    Data.githubList) {
                Github curators = methods.getCurators(login);
                curatorlistsynchronous.add(curators);
            }
            return curatorlistsynchronous;
        }

        @Override
        protected void onPostExecute(List<Github> curators) {
//            textView.setText(curators.title + "\n\n");
//            for (Curator.Dataset dataset : curators.dataset) {
//                textView.setText(textView.getText() + dataset.curator_title +
//                        " - " + dataset.curator_tagline + "\n");
//            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < curatorlistsynchronous.size(); i++) {
                builder.append(curatorlistsynchronous.get(i).getBlog());
                builder.append("\n");
            }
            String result = builder.toString();
            textView.setText(result);
        }
    }
}