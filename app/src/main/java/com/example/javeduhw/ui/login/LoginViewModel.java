package com.example.javeduhw.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.javeduhw.data.LoginRepository;
import com.example.javeduhw.data.Result;
import com.example.javeduhw.data.model.LoggedInUser;
import com.example.javeduhw.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void register(String username, String displayname, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.register(username, displayname, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    public void registerDataChanged(String username, String displayname, String password, String passwdCheck) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_register_username, null));
        } else if (!isDisplayNameValid(displayname)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_register_displayname, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_register_password));
        } else if (!isPassWordChecked(password, passwdCheck)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_register_check_passwd));
        }
        else {
                loginFormState.setValue(new LoginFormState(true));
        }
    }
    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        return Patterns.PHONE.matcher(username).matches();
    }

    private boolean isDisplayNameValid(String displayname) {
        return displayname != null && displayname.trim().length() > 0;
    }
    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isPassWordChecked(String password, String passwdCheck){
        return password != null && passwdCheck != null && passwdCheck.equals(password);
    }
}