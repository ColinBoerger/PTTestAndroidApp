package com.example.pttesttracker.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pttesttracker.DataEntryPage;
import com.example.pttesttracker.R;

public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView username = view.findViewById(R.id.username);

        TextView password = view.findViewById(R.id.username);
        /**
         * TODO: Set a way to only allow login if username and passwords are filled in
         */
        Button loginOnline = view.findViewById(R.id.login);
        loginOnline.setEnabled(true);

        Button loginOffline = view.findViewById(R.id.login_without_account);
        loginOffline.setEnabled(true);
        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                /*
                Gets the username and password
                 */
                String usernameInfo = username.getText().toString();

                /**
                 * TODO: Add password salt
                 */

                String passwordInfo = password.getText().toString();
                Log.d(usernameInfo, passwordInfo);
                if (checkLoginInformationOnline(usernameInfo, passwordInfo)){
                    args.putString("Online", "True");
                    Log.d(usernameInfo, passwordInfo);
                    NavHostFragment.findNavController(LoginFragment.this)
                            .navigate(R.id.action_loginFragment_to_FirstFragment, args);
                }
                /**
                 * TODO add a error message for incorrect password
                 */
            }
        });
        view.findViewById(R.id.login_without_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("Online", "false");
                NavHostFragment.findNavController(LoginFragment.this)
                            .navigate(R.id.action_loginFragment_to_FirstFragment, args);

            }
        });
    }

    private boolean checkLoginInformationOnline(String username, String password){
        //TODO: Interface this with a request to a web server if they want to be online
        if (username.equals("test") && password.equals("test")){
            return true;
        } else{
            return false;
        }
    }




    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }
}