package com.example.javeduhw.ui.login;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.javeduhw.MainActivity;

import com.example.javeduhw.MainActivity;
import com.example.javeduhw.databinding.FragmentRegisterBinding;

import com.example.javeduhw.R;

public class RegisterFragment extends Fragment{

    private LoginViewModel registerViewModel;
    private FragmentRegisterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    MySend mmySend;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerViewModel = new ViewModelProvider(this, new RegisterViewModelFactory())
                .get(LoginViewModel.class);
        mmySend = (MySend) getActivity();
        final EditText usernameEditText = binding.username;
        final EditText displaynameEditText = binding.displayname;
        final EditText passwordEditText = binding.password;
        final EditText passwdCheckEditText = binding.passwdCheck;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        usernameEditText.setFocusable(true);
        usernameEditText.setFocusableInTouchMode(true);
        usernameEditText.requestFocus();
        usernameEditText.requestFocusFromTouch();

        registerViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    if (loginFormState.getUsernameError() == R.string.invalid_register_username) {
                        usernameEditText.setError(getString(loginFormState.getUsernameError()));
                    }
                    if (loginFormState.getUsernameError() == R.string.invalid_register_displayname) {
                        displaynameEditText.setError(getString(loginFormState.getUsernameError()));
                    }
                }else if (loginFormState.getPasswordError() != null) {
                    if (loginFormState.getPasswordError() == (R.string.invalid_register_password)) {
                        passwordEditText.setError(getString(loginFormState.getPasswordError()));
                    }
                    if (loginFormState.getPasswordError() == (R.string.invalid_register_check_passwd)) {
                        passwdCheckEditText.setError(getString(loginFormState.getPasswordError()));
                    }
                }
            }
        });
        registerViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
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
                    mmySend.userCreated(usernameEditText.getText().toString());
                }
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
                registerViewModel.registerDataChanged(usernameEditText.getText().toString(),
                        displaynameEditText.getText().toString(),
                        passwordEditText.getText().toString(), passwdCheckEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        displaynameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.registerDataChanged(usernameEditText.getText().toString(),
                            displaynameEditText.getText().toString(),
                            passwordEditText.getText().toString(), passwdCheckEditText.getText().toString());
                }
                return false;
            }
        });
        passwdCheckEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.registerDataChanged(usernameEditText.getText().toString(),
                            displaynameEditText.getText().toString(),
                            passwordEditText.getText().toString(), passwdCheckEditText.getText().toString());
                }
                return false;
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registerViewModel.register(usernameEditText.getText().toString(),
                        displaynameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.register_success);
        // TODO : initiate successful logged in experience
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(getContext().getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface MySend{
        void userCreated(String s);
    }

}
