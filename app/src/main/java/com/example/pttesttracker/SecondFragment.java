package com.example.pttesttracker;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecondFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Integer i = getArguments().getInt("someInt");
        Log.d("FUCKBUNDLES", String.valueOf(i));
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                loadData(view);
            }
        }
        );
        Boolean failure = false;

        String pushUps = getArguments().getString("pushUpNum");

        String sitUps = getArguments().getString("sitUpNum");

        String runTime = getArguments().getString("runTime");

        String waistMeasurement = getArguments().getString("waistMeasurement");

        Double runScore = calculateRunScore(runTime);

        if (runScore < 0){
            /**
             *  TODO: Put an error in if the time is malformed
             * */
        }

        Double pushUpScore = calculatePushupScore(pushUps);

        Double sitUpScore = calculateSitupScore(sitUps);

        Double waistScore = calculateWaistScore(waistMeasurement);

        TextView score = view.findViewById(R.id.textview_second);
        /*
        Create the stuff to calculate the score now

         */
        Double totalScore = runScore + pushUpScore + sitUpScore + waistScore; //Remove 20 after you calculate the waist portion

        if (totalScore < 75.0 || pushUpScore == 0 || sitUpScore == 0 || runScore == 0 || waistScore == 0){
            failure = true;
        }
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                Date date = new Date();
//                ScoreEntry user = new ScoreEntry(date.getTime(), 9.9);
//                AppDatabase.getDatabase(requireContext()).ScoreEntryDao().insertAll(user);
//            }
//        });
        score.setText(String.valueOf(totalScore));
        /*
        TODO: Make a block that changes a word from pass to fail ect. Also Include colors

         */
        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Date date = new Date();
                        ScoreEntry user = new ScoreEntry(date.getTime(), totalScore);
                        AppDatabase.getDatabase(requireContext()).ScoreEntryDao().insertAll(user);
                        /**
                         * TODO: Add to the page such that the table reloads
                         */
                    }
                });
            }
        });
    }

    /*
    This function loads the PT test data into a table
     */
    public void loadData(View view){
        Log.d("Inside Load Data", "Here");
        TableLayout stk = (TableLayout) view.findViewById(R.id.table_scores);
        TableRow tbrow0 = new TableRow(requireContext());
        TextView tv0 = new TextView(requireContext());
        tv0.setText(" Entry Num ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(requireContext());
        tv1.setText(" Score ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(requireContext());
        tv2.setText(" Time ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(requireContext());
        tv3.setText(" Stock Remaining ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);
        List<ScoreEntry> users = AppDatabase.getDatabase(requireContext()).ScoreEntryDao().getAll();

            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(int i = users.size()-1; i >= 0; i = i-1) {
                        ScoreEntry user = users.get(i);
                        Log.d("User", user.getDebugString());
                        TableRow tbrow = new TableRow(requireContext());
                        TextView t1v = new TextView(requireContext());
                        t1v.setText("" + String.valueOf(user.uid));
                        t1v.setTextColor(Color.WHITE);
                        t1v.setGravity(Gravity.CENTER);
                        tbrow.addView(t1v);
                        TextView t2v = new TextView(requireContext());
                        t2v.setText(String.valueOf(user.score));
                        t2v.setTextColor(Color.WHITE);
                        t2v.setGravity(Gravity.CENTER);
                        tbrow.addView(t2v);
                        TextView t3v = new TextView(requireContext());
                        t3v.setText(String.valueOf(user.timestamp));
                        t3v.setTextColor(Color.WHITE);
                        t3v.setGravity(Gravity.CENTER);
                        tbrow.addView(t3v);
                        Button t4v = new Button(requireContext());
                        t4v.setText("Filler");
                        t4v.setTextColor(Color.WHITE);
                        t4v.setGravity(Gravity.CENTER);
                        t4v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AsyncTask.execute(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                          AppDatabase.getDatabase(requireContext()).ScoreEntryDao().delete(user);
                                                          /**
                                                           * TODO: Delete from the page such that the table reloads
                                                           *
                                                           */
                                                      }
                                                  }
                                );
                            }
                        });

                        tbrow.addView(t4v);
                        stk.addView(tbrow);
                    }
                }
            } );




    }

    public Double calculateWaistScore(String waist){
        Double waistMeasurement = Double.parseDouble(waist);
        Double maxPoints = 35.0;
        Double minPoints = 39.5;
        if (waistMeasurement <= maxPoints){
            return 20.0;
        }

        if (waistMeasurement >= minPoints){
            return 0.0;
        }

        Double[][] score30Male = {
                {35.5, 17.6}, {36.0, 17.0},
                {36.5, 16.4}, {37.0, 15.8},
                {37.5, 15.1}, {38.0, 14.4},
                {38.5, 13.5}, {39.0, 12.6}
        };

        for(int i = 0; i < score30Male.length; i++){
            if(waistMeasurement <= score30Male[i][0]){
                return score30Male[i][1];
            }
        }
        return 0.0;
    }

    public Double calculateRunScore(String time){
        Pattern p = Pattern.compile("\\d{1,2}:\\d{2}");
        Matcher m = p.matcher(time);
        /**
         *  Return 0 if the time is malformed
         *
         */
        if (!m.matches() ) {
            return -1.0;
        }

        String minTime = "9:12";
        String firstFailTime = "10:37";
        String[][] scoreMale30= {
                {"13:36", "13:15", "42.3"}, {"13:14", "12:54", "44.9"},{"12:53","12:34", "47.2"},
                {"12:33", "12:15", "49.2"}, {"12:14", "11:57", "50.9"}, {"11:56", "11:39", "52.4"},
                {"11:38", "11:23", "53.7"}, {"11:22", "11:07", "54.8"}, {"11:06", "10:52", "55.7"},
                {"10:51", "10:38", "56.6"}, {"10:37", "10:24", "57.3"}, {"10:23", "10:11", "57.9"},
                {"10:10", "9:59", "58.5"}, {"9:58", "9:46", "58.9"}, {"9:45","9:35", "59.3"},
                {"9:34", "9:13", "59.7"}
        };

        Integer timeInSeconds = stringTimeToSeconds(time);
        Integer highEnd = scoreMale30.length-1;
        Integer lowEnd = 0;
        Integer index = ((highEnd+lowEnd)/2);
        if(timeInSeconds < 0){
            return 0.0;
        }
        if (timeInSeconds < stringTimeToSeconds(minTime)){
            return 60.0;
        }

        if(timeInSeconds > stringTimeToSeconds(firstFailTime)){
            return 0.0;
        }
        /***
         * TODO: Fix this comparison thing
         */
        for(int i = 0; i < scoreMale30.length; i ++){

            Log.d("Index", String.valueOf(index));
            String[] k = scoreMale30[index];
            Integer timeHigh = stringTimeToSeconds(k[0]);
            Integer timeLow = stringTimeToSeconds(k[1]);
            if (timeInSeconds < timeLow){
                lowEnd = index;
                index = ((highEnd+lowEnd)/2)+1;
            } else if (timeInSeconds > timeHigh){
                highEnd = index;
                index = ((highEnd+lowEnd)/2);
            } else{
                return Double.valueOf(k[2]);
            }
        }

        return 0.0;
    }

    public int stringTimeToSeconds(String time){
        Pattern p = Pattern.compile("\\d{1,2}:\\d{2}");
        Matcher m = p.matcher(time);
        String minuteString = "";
        String secondString = "";
        if (m.matches()) {
            String[] toSplit = m.group().split(":");
            minuteString = toSplit[0];
            secondString = toSplit[1];
        } else {
            return -1;
        }

        Integer minuteInt = Integer.valueOf(minuteString);
        Integer secondInt = Integer.valueOf(secondString);

        return (minuteInt*60) + secondInt;
    }

    public Double calculatePushupScore(String numPushups){
        /*
         * TODO: Make this dependent on Age.
         */

        Integer firstFailing = 32;
        Integer fullPoints = 67;
        Double[][] scoreMale30 = {{0.0,0.0}, {33.0, 5.0},{34.0, 5.3}, {35.0,5.5}, {36.0,5.6},
                {37.0,6.0}, {38.0, 6.3}, {39.0, 6.5}, {40.0, 6.8}, {41.0, 7.0}, {42.0, 7.2},
                {43.0, 7.3}, {44.0,7.5}, {45.0,7.7}, {46.0,7.8}, {47.0, 8.0}, {48.0, 8.1}, {49.0, 8.3},
                {50.0,8.4}, {51.0, 8.5}, {52.0, 5.6}, {53.0,8.7}, {54.0, 8.8}, {55.0, 8.8},
                {56.0, 8.9}, {57.0, 9.0}, {58.0, 9.1}, {59.0, 9.2}, {60.0, 9.3}, {61.0, 9.4}, {62.0,9.5},
                {63.0,9.5}, {64.0,9.5}, {65.0,9.5}, {66.0,9.5}
        };

        Integer intNumPushups = Integer.parseInt(numPushups);

        /**
         * Return 0 if you get less than the minimum
         */
        if (intNumPushups <= firstFailing){
                return 0.0;
        }
        /*
        * Return 10 if you get more than the maximum
        * */
        if (intNumPushups >= fullPoints) {
            return 10.0;
        }

        Double toRet = 0.0;

        Integer index = intNumPushups - firstFailing;

        toRet = scoreMale30[index][1];

        return toRet;
    }

    public Double calculateSitupScore(String numSitups){
        Integer firstFailing = 41;
        Integer fullPoints = 58;
        Double[][] scoreMale30 = {{0.0,0.0}, {42.0,6.0}, {43.0,6.3}, {44.0,6.4}, {45.0, 7.0}, {46.0,7.5}, {47.0, 8.0},
                {48.0,8.3}, {49.0,8.5}, {50.0,8.7}, {51.0, 8.8}, {52.0, 9.0}, {53.0, 9.2}, {54.0, 9.4},
                {55.0,9.5}, {56.0,9.5}, {57.0,9.5}, {58.0,9.5}
        };
        Integer intNumSitUps = Integer.parseInt(numSitups);
        /**
         * Return 0 if you get less than the minimum
         */
        if (intNumSitUps <= firstFailing){
            return 0.0;
        }
        /*
         * Return 10 if you get more than the maximum
         * */
        if (intNumSitUps >= fullPoints) {
            return 10.0;
        }

        Double toRet = 0.0;

        Integer index = intNumSitUps - firstFailing;

        toRet = scoreMale30[index][1];

        return toRet;
    }
}