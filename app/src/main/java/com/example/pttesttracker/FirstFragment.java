package com.example.pttesttracker;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //This is a test to make sure that the database is saving.
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<ScoreEntry> users = AppDatabase.getDatabase(requireContext()).ScoreEntryDao().getAll();
                for(ScoreEntry user : users) {
                    Log.d("User", user.getDebugString());
                }
            }
        });
        view.findViewById(R.id.button_settings).setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {

                                                                    }
                                                                });
        /**
         * Below is the code for the pop-up manager
         */

        LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.custom_layout,null);
        //Handle an empty sit up box
        PopupWindow mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        Button closeButton = (Button) customView.findViewById(R.id.ib_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                Log.d("PRINTINGTHIS", "Click");
                mPopupWindow.dismiss();
            }
        });
        /**
         * Below is the code that pulls the PT score info to the next fragment
         */
        EditText sitUps = (EditText)view.findViewById(R.id.editTextSitup);
        EditText pushUps = (EditText)view.findViewById(R.id.editTextPushups);
        EditText run = (EditText)view.findViewById(R.id.editTextRun);
        EditText waist = (EditText)view.findViewById(R.id.editTextWaist);
        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String sitUpStr = sitUps.getText().toString();
                if (sitUpStr.equals(""))
                {
                    mPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
                    TextView errorMessage = mPopupWindow.getContentView().findViewById(R.id.tv);
                    errorMessage.setText("Please enter a valid number for situps");
                    Log.d("PRINTINGTHIS", "Empty");
                    return;
                }
                Log.d("PRINTINGTHIS", sitUpStr);

                String pushUpStr = pushUps.getText().toString();
                if (pushUpStr.equals(""))
                {

                    mPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
                    TextView errorMessage = mPopupWindow.getContentView().findViewById(R.id.tv);
                    errorMessage.setText("Please enter a valid number for pushups");
                    //Handle an empty pushup up box
                    Log.d("PRINTINGTHIS", "Empty");
                    return;
                }
                Log.d("PRINTINGTHIS", pushUpStr);

                String runTimeStr = run.getText().toString();
                if (runTimeStr.equals(""))
                {

                    mPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
                    TextView errorMessage = mPopupWindow.getContentView().findViewById(R.id.tv);
                    errorMessage.setText("Please enter a valid number for the run");
                    //Handle an empty pushup up box
                    Log.d("PRINTINGTHIS", "Empty");
                    return;
                }

                String waistStr = waist.getText().toString();

                if (waistStr.equals("")){

                    //Handle an empty waist box
                    mPopupWindow.showAtLocation(view, Gravity.CENTER,0,0);
                    TextView errorMessage = mPopupWindow.getContentView().findViewById(R.id.tv);
                    errorMessage.setText("Please enter a valid number for the waist measurement");
                    Log.d("PRINTINGTHIS", "Empty");
                    return;
                }
                Bundle args = new Bundle();

                args.putString("sitUpNum", sitUpStr);

                args.putString("pushUpNum", pushUpStr);

                args.putString("runTime", runTimeStr);

                args.putString("waistMeasurement", waistStr);

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, args);
            }
        });
    }
}