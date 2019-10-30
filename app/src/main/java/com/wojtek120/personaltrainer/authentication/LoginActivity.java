package com.wojtek120.personaltrainer.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.utils.ToastMessage;
import com.wojtek120.personaltrainer.utils.database.AuthenticationFacade;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    private Context context;

    @ViewById(R.id.progressBarInLoggingPage)
    ProgressBar progressBar;

    @ViewById(R.id.emailLoggingPage)
    EditText emailEditText;

    @ViewById(R.id.passwordLoggingPage)
    EditText passwordEditText;

    @AfterViews
    void setup() {
        context = LoginActivity.this;
        toggleProgressBar();
    }

    /**
     * Add on click listener to login button.
     * Here email and password are read from EditTexts,
     * then there is checked if user put the text inside these EditTexts,
     * if email and password aren't null authentication method is invoked,
     * in other case user is informed to write it
     */
    @Click(R.id.loginButton)
    void setupLoggingButton() {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        toggleProgressBar();

        if (validate(email, password)) {
            AuthenticationFacade.authenticateAndRedirectOnSuccess(email, password, (Activity) context, progressBar);
        } else {
            toggleProgressBar();
        }
    }


    /**
     * Checks if string with email or password is empty
     * and shows error message if is
     *
     * @param email    - string with email
     * @param password - string with password
     * @return - false if any of string is empty, otherwise true
     */
    private boolean validate(String email, String password) {

        if (email.isEmpty()) {
            String emailEmptyMessage = getString(R.string.fill_email);
            ToastMessage.showMessage(context, emailEmptyMessage);
            return false;
        }

        if (password.isEmpty()) {
            String passwordEmptyMessage = getString(R.string.fill_password);
            ToastMessage.showMessage(context, passwordEmptyMessage);
            return false;
        }

        return true;
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

    /**
     * Add on click listener to register link
     * to redirect to sign up activity after clicking on it
     */
    @Click(R.id.signUpLinkLoggingPage)
    void addOnClickListenerToRegisterLink() {
        Intent intent = new Intent(context, RegisterActivity_.class);
        startActivity(intent);
    }


}
