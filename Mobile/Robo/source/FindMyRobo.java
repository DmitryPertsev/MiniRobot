package RoboBluetoothClient.androidApp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

public class FindMyRobo extends AppCompatActivity {

    ImageView mLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find_my_robo);
        ProgressBar searchProgress = findViewById(R.id.progressBar2);
        searchProgress.setIndeterminate(false);
        ToggleButton startSearch = findViewById(R.id.start_search);
        mLogo  = findViewById(R.id.logo);
        setLogoVisibility(false);
        startSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                searchProgress.setIndeterminate(b);
                setStartSearchParameter(b);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setStartSearchParameter(boolean isOn){
        CommunicationsTask connection = ClientApplication.getApplication().getCurrentBluetoothConnection();
        connection.write("/find/");
        setLogoVisibility(isOn);
    }

    private void setLogoVisibility(boolean isVisible){
        mLogo.setVisibility(isVisible? ImageView.VISIBLE : ImageView.INVISIBLE);
    }
}
