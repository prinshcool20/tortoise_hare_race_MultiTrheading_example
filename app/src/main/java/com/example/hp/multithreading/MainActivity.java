package com.example.hp.multithreading;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//

public class MainActivity extends AppCompatActivity {
    public static Thread tortoise;
    public static Thread hare;
    public static Thread dog;
    public static TextView mWinner,mRunnerup;
    public Handler handler;
   // public static Context ctx;
    Button mStart,mStop;
    ProgressDialog mProgressDialog;
    //    Context ctx=getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tortoise = new ThreadRunner("Tortoise", 0, 10);
        hare = new ThreadRunner("Hare", 90, 100);
        dog = new ThreadRunner("Dog", 50, 20);
        mWinner=findViewById(R.id.winner);
        mRunnerup=findViewById( R.id.runerup);
        handler=new Handler();
        mStart=findViewById(R.id.start);
       // mStop=findViewById(R.id.stop);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( dog.getState() == Thread.State.NEW && tortoise.getState()==Thread.State.NEW && hare.getState()==Thread.State.NEW ) {
                    Toast.makeText(MainActivity.this, "Get set.... Go!", Toast.LENGTH_LONG).show();
                    System.out.println("Get set... Go!");
                    tortoise.start();
                    hare.start();
                    dog.start();
                    //ProgressDialog
                    mProgressDialog = new ProgressDialog(MainActivity.this);
                    mProgressDialog.setTitle("Be Patience.. ");
                    mProgressDialog.setMessage("Race is going on...");
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.show();
                } else{
                   // Toast.makeText(MainActivity.this, "Close the app!" , Toast.LENGTH_LONG).show();
                    AlertDialog.Builder mAlert = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Race is Already Finished, Close and Re-open the app to Start the race Again.. ")
                            .setCancelable(false)
                            .setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });
                    AlertDialog alert = mAlert.create();
                    alert.setTitle("Sorry");
                    alert.show();

                }
            }
        });
    }
    class ThreadRunner extends Thread {
        private String name;
        private int restValue, speed;

        ThreadRunner(String Name, int RestValue, int Speed) {
            name = Name;
            restValue = RestValue;
            speed = Speed;
        }

        public void run() {
            int distance = 0;
            while (!isInterrupted() && distance < 1000) {
                try {
                    int dis = (int) (Math.random() * 100);
                    if (restValue <= dis) {
                        distance += speed;
                        System.out.println(name + " : " + distance);
                    }
                    Thread.sleep(100);
                }
                catch (InterruptedException e){

                    System.out.println(name + ": You beat me fair and square." + "\n");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            mRunnerup.setText(name + ": You beat me fair and square." + "\n");
                        }
                    });
                    break;
                }
            }
            if(distance >= 1000)
            {
                System.out.println(name + ": I finished!" + "\n");
                finished(Thread.currentThread(), name);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                         Toast.makeText(MainActivity.this, name + ": I finished!" , Toast.LENGTH_LONG).show();
                         mProgressDialog.dismiss();
                    }
                });

            }
        }
        public  synchronized void finished (Thread winner, String winnerName) {

            //Context ctx=MainActivity.this;
            if(winner.equals(hare))
            {//Toast.makeText(MainActivity.this, "The race is over! The hare is the winner" , Toast.LENGTH_LONG).show();
                // mWinner.setText("The race is over! The hare is the winner");
                System.out.println("The race is over! The hare is the winner" + "\n");
                tortoise.interrupt();
                dog.interrupt();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "The race is over! The hare is the winner" , Toast.LENGTH_LONG).show();
                        mWinner.setText("Hare is the Winner");
                    }
                });

            }
            if(winner.equals(tortoise))
            {//Toast.makeText(MainActivity.this, "The race is over! The hare is the winner" , Toast.LENGTH_LONG).show();
                // mWinner.setText("The race is over! The hare is the winner");
                System.out.println("The race is over! The tortoise is the winner" + "\n");
                hare.interrupt();
                dog.interrupt();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "The race is over! The tortoise is the winner" , Toast.LENGTH_LONG).show();
                   mWinner.setText("Tortoise is the Winner");
                    }
                });
            }
            if(winner.equals(dog))
            {//Toast.makeText(MainActivity.this, "The race is over! The hare is the winner" , Toast.LENGTH_LONG).show();
                // mWinner.setText("The race is over! The hare is the winner");
                System.out.println("The race is over! The dog is the winner" + "\n");
                tortoise.interrupt();
                hare.interrupt();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "The race is over! The dog is the winner" , Toast.LENGTH_LONG).show();
                        mWinner.setText("Dog is the Winner");
                    }
                });
            }
        }
    }
}