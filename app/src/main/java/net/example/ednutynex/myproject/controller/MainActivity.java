package net.example.ednutynex.myproject.controller;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import net.example.ednutynex.myproject.R;
import net.example.ednutynex.myproject.api.Client;
import net.example.ednutynex.myproject.api.Service;
import net.example.ednutynex.myproject.model.Item;
import net.example.ednutynex.myproject.model.ItemResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import net.example.ednutynex.myproject.ItemAdapter;

public class MainActivity extends AppCompatActivity {

    TextView Disconnected;
    ProgressDialog pd;
    private RecyclerView recyclerView;
    private Item item;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViews();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                loadJSON();
                Toast.makeText(MainActivity.this, "Github Users Refreshed, please wait", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //handles the display of dialog message as details of developers are imported
    private void initViews(){
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);
        loadJSON();
    }

    //handles the importing developer details in JSON Format
    private void loadJSON(){
        Disconnected = (TextView) findViewById(R.id.disconnected);
        try{
            Client Client = new Client();
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<ItemResponse> call = apiService.getItems();
            call.enqueue(new Callback<ItemResponse>() {
                @Override
                public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                    List<Item> items = response.body().getItems();
                    recyclerView.setAdapter(new ItemAdapter(getApplicationContext(),items));
                    recyclerView.smoothScrollToPosition(0);
                    swipeContainer.setRefreshing(false);
                    pd.hide();
                }

                //handles error message
                @Override
                public void onFailure(Call<ItemResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error in loading", Toast.LENGTH_SHORT).show();
                    Disconnected.setVisibility(View.VISIBLE);
                    pd.hide();

                }
            });

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
