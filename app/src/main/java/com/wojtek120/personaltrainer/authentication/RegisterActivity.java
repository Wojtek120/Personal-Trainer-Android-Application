package com.wojtek120.personaltrainer.authentication;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.wojtek120.personaltrainer.R;
import com.wojtek120.personaltrainer.utils.database.UserService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {

    Context context;
    @Bean
    UserService userService;

    @ViewById(R.id.progressBarInRegisterPage)
    ProgressBar progressBar;
    @ViewById(R.id.emailRegisterPage)
    EditText emailEditText;
    @ViewById(R.id.usernameRegisterPage)
    EditText usernameEditText;
    @ViewById(R.id.passwordRepeatedRegisterPage)
    EditText repeatedPasswordEditText;
    @ViewById(R.id.passwordRegisterPage)
    EditText passwordEditText;


    @AfterViews
    void setupRegisterActivity() {
        context = this;
        toggleProgressBar();
    }


    /**
     * Add on click listener to register button.
     * Here all required data are read from EditTexts,
     * than register method is called
     */
    @Click(R.id.loginButton)
    void setupRegisterButton() {

        String email = emailEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repeatedPassword = repeatedPasswordEditText.getText().toString();

        toggleProgressBar();

        userService.registerNewUser(email, username, password, repeatedPassword, (Activity) context, progressBar);
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
