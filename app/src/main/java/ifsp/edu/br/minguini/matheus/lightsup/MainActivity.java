package ifsp.edu.br.minguini.matheus.lightsup;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.Format;
import java.text.SimpleDateFormat;
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

    void turnBlueLedOn(View view){
        currentLedOn.setImageResource(R.drawable.blue_led);
        setSecondsRemainingIntoScreen();
    }

    void turnGreenLedOn(View view){
        currentLedOn.setImageResource(R.drawable.green_led);
    }

    void turnYellowLedOn(View view){
        currentLedOn.setImageResource(R.drawable.yellow_led);
    }

    void setSecondsRemainingIntoScreen(){
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            String timeTyped = getTimeTyped();

            @Override
            public void onChronometerTick(Chronometer chronometer) {

                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String t = (h < 10 ? "0"+h: h)+":"+(m < 10 ? "0"+m: m)+":"+ (s < 10 ? "0"+s: s);
                chronometer.setText(t);

                if( chronometer.getText().toString().equalsIgnoreCase(timeTyped)){
                    chronometer.stop();
                    currentLedOn.setImageResource(R.drawable.grey_led);
                }
            }
        });

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
}
