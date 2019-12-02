package com.example.analogclock;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    private static TextView countdownTimerText;
    private static EditText minutes;
    private static Button startTimer, resetTimer;
    private static CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        countdownTimerText = (TextView) findViewById(R.id.countdownText);
        minutes = (EditText) findViewById(R.id.enterMinutes);
        startTimer = (Button) findViewById(R.id.startTimer);
        resetTimer = (Button) findViewById(R.id.resetTimer);

        setListeners();

    }


    //Set Listeners over button
    private void setListeners() {
        startTimer.setOnClickListener(this);
        resetTimer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startTimer:
                //If CountDownTimer is null then start timer
                if (countDownTimer == null) {


                    String getMinutes = minutes.getText().toString();//Get minutes from edittext


                    //Check validation over edittext
                    if (!getMinutes.equals("") && getMinutes.length() > 0) {

                        int noOfMinutes = Integer.parseInt(getMinutes) * 60 * 1000;//Convert minutes into milliseconds

                        startTimer(noOfMinutes);//start countdown
                        startTimer.setBackgroundResource(R.drawable.ic_pause_white_24dp);//Change Text

                    } else {
                        Toasty.warning(TimerActivity.this, "Please enter no. of Minutes", Toast.LENGTH_SHORT, true).show();//Display toast if edittext is empty
                    }

                } else {

                    //Else stop timer and change text
                    stopCountdown();
                    startTimer.setBackgroundResource(R.drawable.ic_play_arrow_white_24dp);
                }
                break;
            case R.id.resetTimer:
                stopCountdown();//stop count down
                startTimer.setBackgroundResource(R.drawable.ic_play_arrow_white_24dp);//Change text to Start Timer
                countdownTimerText.setText(getString(R.string.timer));//Change Timer text
                minutes.setText("");
                break;
        }


    }


    //Stop Countdown method
    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    //Start Countodwn method
    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                countdownTimerText.setText(hms);//set text
            }

            public void onFinish() {

                countdownTimerText.setText("TIME'S UP!!"); //On finish change timer text
                countDownTimer = null;//set CountDownTimer to null
                startTimer.setBackgroundResource(R.drawable.ic_pause_white_24dp);//Change button text
            }
        }.start();

    }

}
