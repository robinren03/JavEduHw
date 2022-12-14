package com.example.renyanyu.ui.login;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renyanyu.AppDB;
import com.example.renyanyu.EntityDetails;
import com.example.renyanyu.KEntityRepository;
import com.example.renyanyu.R;
import com.example.renyanyu.databinding.ActivityLoginBinding;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class LoginActivity extends AppCompatActivity implements RegisterFragment.MySend{

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private ImageView img;
    private int imagePath = R.drawable.face;
    private Fragment fragment;
    private FragmentManager fm;
    private FragmentTransaction beginTransaction;
    private EditText usernameEditText;
    private EditText passwordEditText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout);
        img = new ImageView(LoginActivity.this);
        img.setImageResource(imagePath);
        img.setMaxHeight(2);
        layout.addView(img);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText = binding.username;
        passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        final TextView regLink = binding.regLink;


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                //Complete and destroy login activity once successful
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(v.getContext(), usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(getApplicationContext(), usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        regLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEditText.setText("");
                passwordEditText.setText("");
                fragment = new RegisterFragment();
                fm = getSupportFragmentManager();
                beginTransaction = fm.beginTransaction();
                beginTransaction.replace(R.id.container,fragment);
                beginTransaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit,
                            R.anim.fragment_fade_enter, R.anim.fragment_close_exit);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
            }
        });

    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("displayName", model.getDisplayName());
        editor.putString("token", model.getToken());
        editor.putString("username", usernameEditText.getText().toString());
        editor.commit();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void userCreated(String s) {
        getSupportFragmentManager().popBackStack();
        usernameEditText.setText(s);
        passwordEditText.setFocusable(true);
        passwordEditText.setFocusableInTouchMode(true);
        passwordEditText.requestFocus();
        passwordEditText.requestFocusFromTouch();
        //System.out.println(s);
    }
}