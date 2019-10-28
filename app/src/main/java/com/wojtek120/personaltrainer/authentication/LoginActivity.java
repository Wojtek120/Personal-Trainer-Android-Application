package com.wojtek120.personaltrainer.authentication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.utils.AuthenticationFacade;
import com.wojtek120.personaltrainer.utils.ToastMessage;

public class LoginActivity extends AppCompatActivity {

    private Context context;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;

        progressBar = findViewById(R.id.progressBarInLoggingPage);

        setupLoggingButton();
        toggleProgressBar();

    }

    /**
     * Add on click listener to login button.
     * Here email and password are read from EditTexts,
     * then there is checked if user put the text inside these EditTexts,
     * if email and password aren't null authentication method is invoked,
     * in other case user is informed to write it
     */
    private void setupLoggingButton() {
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            EditText emailEditText = findViewById(R.id.emailLoggingPage);
            String email = emailEditText.getText().toString();

            EditText passwordEditText = findViewById(R.id.passwordLoggingPage);
            String password = passwordEditText.getText().toString();

            toggleProgressBar();

            if(!checkIfIsEmpty(email, password)) {
                AuthenticationFacade.authenticate(email, password, (Activity) context, progressBar);
            }

        });
    }


    /**
     * Checks if string with email or password is empty
     * and shows error message if is
     * @param email - string with email
     * @param password - string with password
     * @return - true if any of string is empty, otherwise false
     */
    private boolean checkIfIsEmpty(String email, String password) {

        if(email.isEmpty()) {
            String emailEmptyMessage = getString(R.string.fill_email);
            ToastMessage.showMessage(context, emailEmptyMessage);
            return true;
        }

        if(password.isEmpty()) {
            String passwordEmptyMessage = getString(R.string.fill_password);
            ToastMessage.showMessage(context, passwordEmptyMessage);
            return true;
        }

        return false;
    }

    /**
     * Toggle progress bar
     * Show if hidden,
     * hide is shown
     */
    private void toggleProgressBar() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
