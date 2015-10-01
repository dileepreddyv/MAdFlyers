package contest.android.com.mymadflyers;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends ActionBarActivity implements
        ConnectionCallbacks, OnConnectionFailedListener {
    protected static final String TAG = "main-activity";
    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected boolean mAddressRequested;
    protected String mAddressOutput;
    private AddressResultReceiver mResultReceiver;
    protected TextView mLocationAddressTextView;
    String pinString = "";
    //ProgressBar mProgressBar;
    //Button mFetchAddressButton;
    Button mGetAdsButton;
    private boolean mAddressFound;
    public final static String PIN_CODE = "com.google.android.gms.location.sample.locationaddress.PIN_CODE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mResultReceiver = new AddressResultReceiver(new Handler());
        mLocationAddressTextView = (TextView) findViewById(R.id.location_address_view);
        //mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        //mFetchAddressButton = (Button) findViewById(R.id.fetch_address_button);
        mGetAdsButton=(Button)findViewById(R.id.get_Ads_button);

        // Set defaults, then update using values stored in the Bundle.
        mAddressRequested = false;
        mAddressFound = false;
        mAddressOutput = "";
        updateValuesFromBundle(savedInstanceState);
        Log.d(TAG, "CALLING From ONCreate(..) with mAddressFound:" + mAddressFound);
        updateUIWidgets();
        buildGoogleApiClient();
        if(mGoogleApiClient!=null) {
            Log.d(TAG, "Built the GoogleAPIClient Successfully.. Now Connecting to it");
            mGoogleApiClient.connect();
        }else{
            Log.e(TAG, "Oops!..Something went wrong while building GoogleAPIClinet");
        }
    }

    /**
     * Updates fields based on data stored in the bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                displayAddressOutput();
            }
        }
    }

    /**
     * Builds a GoogleApiClient. Uses {@code #addApi} to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Runs when user clicks the Fetch Address button. Starts the service to fetch the address if
     * GoogleApiClient is connected.
     */
   /* public void fetchAddressButtonHandler(View view) {
        // We only start the service to fetch the address if GoogleApiClient is connected.
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService();
        }
        mAddressRequested = true;
        updateUIWidgets();
    }*/

    public void getAdsAroundMe(View view){
        Log.d(TAG, "<<<<<<<<<<<<<<<GetAdsAroundME Button Clicked>>>>>>>>>>>>>");
        mAddressRequested = false;
        Intent intent = new Intent(this, AdViewActivity.class);
        intent.putExtra(PIN_CODE,pinString);//Pass the PIN_CODE extracted from current Address;
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        //mGoogleApiClient.connect();
        Log.d(TAG, "Giving it a try.. Checking if Client Connected in onStart(..)");
        if (mGoogleApiClient.isConnected() && mLastLocation != null) {
            startIntentService();
            mAddressRequested = true;
        }else{
            mAddressRequested = false;
            Log.d(TAG, "Nope.. GoogleApiClient not yet Connected");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            if (!Geocoder.isPresent()) {
                Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
            }else if (!mAddressRequested) { //IntentSvc not started on onStart
               startIntentService();
                mAddressRequested = true;
            }
        }
    }
/*The method that gets the Address*/
    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        showToast("GoogleAPI Connection Failed -" + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    protected void displayAddressOutput() {
        mLocationAddressTextView.setText(mAddressOutput);
    }

    /**
     * Toggles the visibility of the progress bar. Enables or disables the Where Am I? button.
     */
    private void updateUIWidgets() {
        Log.d(TAG, "Updating UI WIDGETS addressRequested:" + mAddressRequested + " And AddressFound:" + mAddressFound);
        if (mAddressRequested) {
            //mProgressBar.setVisibility(ProgressBar.VISIBLE);
            //mFetchAddressButton.setEnabled(false);
            mGetAdsButton.setVisibility(Button.GONE);
            mGetAdsButton.setEnabled(false);
        } else {
            //mProgressBar.setVisibility(ProgressBar.GONE);
            //mFetchAddressButton.setEnabled(true);
            if(mAddressFound){
                mGetAdsButton.setEnabled(true);
                mGetAdsButton.setVisibility(Button.VISIBLE);
            }else{
                mGetAdsButton.setEnabled(false);
                mGetAdsButton.setVisibility(Button.GONE);
            }
        }
    }

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            Log.d(TAG, "ADDRESS :" + mAddressOutput);
            int pinStartPos = mAddressOutput.length()-6;
            pinString = mAddressOutput.substring(pinStartPos);
            Log.d(TAG, "PINCODE:" +pinString);
            displayAddressOutput();
            if (resultCode == Constants.SUCCESS_RESULT) {
                //showToast(getString(R.string.address_found));
                mAddressFound = true;
            }
            mAddressRequested = false;
            updateUIWidgets();
        }
    }
}
