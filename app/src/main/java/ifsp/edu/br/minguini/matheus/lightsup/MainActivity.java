package ifsp.edu.br.minguini.matheus.lightsup;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ImageView currentLedOn;
    Chronometer chronometer;
    EditText secondsEditText;

    Boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentLedOn = findViewById(R.id.current_led_on_image_view);
        chronometer = findViewById(R.id.stopWatchChronometer);
        chronometer.setText("00:00:00");

        secondsEditText = findViewById(R.id.secondsEditText);
    }

    public void turnLedOn(View view){

        int imagePath = pickTheRightImage(view.getId());

        if(isTimeFilled()){
            currentLedOn.setImageResource(imagePath);
            setSecondsRemainingIntoScreen();

            //Call Arduino here

        }else{
            Toast.makeText(this, R.string.time_required_message, Toast.LENGTH_LONG).show();
        }
    }

    private int pickTheRightImage(Integer buttonId) {
        if(buttonId == R.id.blue_led_button){
            return R.drawable.blue_led;
        }else if(buttonId == R.id.yellow_led_button){
            return R.drawable.yellow_led;
        }else{
            return R.drawable.green_led;
        }
    }

    void setSecondsRemainingIntoScreen(){
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            String timeTyped = getTimeTyped();

            @Override
            public void onChronometerTick(Chronometer chronometer) {

                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int hour   = (int)(time / 3600000);
                int minute = (int)(time - hour * 3600000) / 60000;
                int second = (int)(time - hour * 3600000 - minute * 60000) / 1000 ;
                String timeString = (hour < 10 ? "0" + hour: hour) + ":"+(minute < 10 ? "0" + minute: minute)+":"+ (second  < 10 ? "0" + second : second );
                chronometer.setText(timeString);

                if( chronometer.getText().toString().equalsIgnoreCase(timeTyped)){
                    chronometer.stop();
                    chronometer.setText("00:00:00");
                    currentLedOn.setImageResource(R.drawable.grey_led);
                }
            }
        });

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public String getTimeTyped() {
        String timeTyped = secondsEditText.getText().toString();
        long timeTypedLong = Long.valueOf(timeTyped);

        String hms = String.format("%02d:%02d:%02d", TimeUnit.SECONDS.toHours(timeTypedLong),
                TimeUnit.SECONDS.toMinutes(timeTypedLong) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(timeTypedLong)),
                TimeUnit.SECONDS.toSeconds(timeTypedLong) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timeTypedLong)));

        return hms;
    }

    private boolean isTimeFilled() {
        return !secondsEditText.getText().toString().isEmpty();
    }
}
