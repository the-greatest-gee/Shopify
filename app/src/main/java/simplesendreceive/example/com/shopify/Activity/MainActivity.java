package simplesendreceive.example.com.shopify.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import simplesendreceive.example.com.shopify.Adapter.CustomCollectionAdapter;
import simplesendreceive.example.com.shopify.Model.CustomCollectionArray;
import simplesendreceive.example.com.shopify.Network.DataPullService;
import simplesendreceive.example.com.shopify.Model.CustomCollection;
import simplesendreceive.example.com.shopify.R;
import simplesendreceive.example.com.shopify.Network.RetrofitClient;

public class MainActivity extends AppCompatActivity {

    private CustomCollectionAdapter collectionAdapter;
    private ListView customCollectionsListView;
    private DataPullService apiService;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        apiService = RetrofitClient.getRetrofitClient().create(DataPullService.class);

        customCollectionsListView = findViewById(R.id.customCollectionsList);
        populateCollectionsList();
        addCollectionItemListener();
    }

    private void populateCollectionsList() {

        Thread pullCustomCollections = new Thread(){

            @Override
            public void run(){
                Call<CustomCollectionArray> customCollections = apiService.getCustomCollections();

                customCollections.enqueue(new Callback<CustomCollectionArray>(){

                    @Override
                    public void onResponse(Call<CustomCollectionArray> call, Response<CustomCollectionArray> response) {
                        CustomCollectionArray returnedCollections = response.body();

                        collectionAdapter = new CustomCollectionAdapter(context, returnedCollections.getCustomCollections());
                        customCollectionsListView.setAdapter(collectionAdapter);
                    }

                    @Override
                    public void onFailure(Call<CustomCollectionArray> call, Throwable t) {
                        Log.e(getString(R.string.exception), t.getMessage());
                    }

                });
            }
        };

        pullCustomCollections.start();

    }

    private void addCollectionItemListener(){
        //add an on-click event listener to the custom collection list view
        customCollectionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomCollection selectedCollection = (CustomCollection) parent.getItemAtPosition(position);

                Intent intent = new Intent(context, ProductListActivity.class);
                intent.putExtra(getString(R.string.selectedCollection), selectedCollection);

                startActivity(intent);
            }
        });
    }

}
