package contest.android.com.mymadflyers;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AdViewActivity extends ActionBarActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "AdViewActivity";
    protected static final String TAG = "AdViewActivity";
    ProgressDialog prgDialog;
    String pinString = "";
    public final static String PIN_CODE = "com.google.android.gms.location.sample.locationaddress.PIN_CODE";

    boolean wsCallDone = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "AM HERE in AdViewActivity............");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity_card_view);
        pinString = getIntent().getStringExtra(PIN_CODE);
        Log.d(TAG, "Got the PIN To Query :" +pinString);
        wsCallDone= false;
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        invokeWS(new RequestParams());

        //if(adList.size()==0){
          //  getAdsSet();//Get HardCoded values just for the demo
        //}
        //mAdapter = new RecyclerViewAdapter(adList);
        //mRecyclerView.setAdapter(mAdapter);

        // Code to Add an item with default animation
        //((RecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((RecyclerViewAdapter) mAdapter).deleteItem(index);
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*((RecyclerViewAdapter) mAdapter).setOnItemClickListener(new RecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        })*/;
    }

    private ArrayList<AdObject> getAdsSet() {
        //invokeWS(new RequestParams());
        ArrayList results = new ArrayList<AdObject>();
        for (int i = 0; i < 10; i++) {
            AdObject obj = new AdObject("PiZZa Hut:Buy 1 Large, Get A Medium One FREE!", "Opp:RMZ Millenia 2, Ph:+918754467891");
            results.add(i, obj);
        }
        return results;
    }

    public void invokeWS(RequestParams params){
        // Show Progress Dialog

        Log.d(TAG, "Inside invokeWS");
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        final int DEFAULT_TIMEOUT = 20 * 1000;
        client.setTimeout(DEFAULT_TIMEOUT);
        //pinString = null;
        if(pinString!=null){
            params.add("pincode", pinString);
            Log.d(TAG, "Querying WS for the Actual PIN: " + pinString);
        }else {
            Log.d(TAG, "Querying WS for Default PIN: " + 600097);
            params.add("pincode", "600097");
        }

        client.get("http://madflyersbusiness.cfapps.io/rest/fetchAD", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            ArrayList<AdObject> wsAdList = new ArrayList<>();
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONArray array = new JSONArray(response);

                    // When the JSON response has status boolean value assigned with true
                    Log.d(TAG, "REsponse from WS : " + array);
                    for(int i=0;i<array.length();i++){
                        JSONObject obj = 	array.getJSONObject(i);
                        AdObject ad = new AdObject();
                        ad.setBusinessName(obj.getString("business"));
                        ad.setAddr1(obj.getString("addressL1"));
                        ad.setAddr2(obj.getString("addressL2"));
                        ad.setAddr3(obj.getString("addressL3"));
                        ad.setCity(obj.getString("city"));
                        ad.setState(obj.getString("state"));
                        ad.setCountry(obj.getString("country"));
                        ad.setPincode(obj.getString("pincode"));
                        ad.setDesc(obj.getString("desc"));
                        ad.setUsername(obj.getString("username"));
                        //ad.setPhone(obj.getString("phone"));
                        ad.setmText1(ad.getBusinessName() + ":" + ad.getDesc());
                        if(obj.getString("phone")==null){
                            ad.setPhone("+918754467891");
                        }else{
                            ad.setPhone(obj.getString("phone"));
                        }
                        ad.setmText2(ad.getAddr1()+", Ph:"+ad.getPhone());
                        //ad.setmText2(ad.getAddr1()+", Ph:+918754467891");
                        wsAdList.add(ad);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
                ArrayList<AdObject> renderList;
                if(wsAdList.size()==0 || wsAdList.size()<5){
                    renderList = getAdsSet();//Get HardCoded values just for the demo
                    renderList.addAll(wsAdList);
                }else{
                    renderList = wsAdList;
                }

                mAdapter = new RecyclerViewAdapter(renderList);
                mRecyclerView.setAdapter(mAdapter);
                wsCallDone = true;
                ((RecyclerViewAdapter) mAdapter).setOnItemClickListener(new RecyclerViewAdapter
                        .MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Log.i(LOG_TAG, " Clicked on Item " + position);
                    }
                });
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
                wsCallDone = true;
            }
        }
        );
    }





}