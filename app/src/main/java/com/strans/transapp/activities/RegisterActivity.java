package com.strans.transapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.strans.transapp.R;
import com.strans.transapp.dao.concrete.UserDAO;

public class RegisterActivity extends Activity
{
    private AutoCompleteTextView mEmailView;
    private EditText passwordView1;
    private EditText passwordView2;
    private String email;
    private String password;

    /**
     * This function comes with the class Activity itself, it takes care
     * of initializing the variables needed to execute the activity.
     *
     * @param savedInstanceState    Needed by Android itself. It can also be used to retrieved
     *                              the arguments passed to this activity if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set the editText so we can work with 'em later on
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView1 = (EditText) findViewById(R.id.password1);
        passwordView2 = (EditText) findViewById(R.id.password2);

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI();
            }
        });
    }

    /**
     * Once the information has been filled, this function takes care
     * of displaying messages to the user telling him if everything
     * is Ok or if there is something wrong with the given info.
     */
    private void updateUI()
    {
        email = mEmailView.getText().toString();
        String pass1 = passwordView1.getText().toString();
        String pass2 = passwordView2.getText().toString();
        Boolean success = true;

        if (!doPasswordsMatch(pass1, pass2)) {
            passwordView1.setError(getString(R.string.error_passwords_do_not_match));
            passwordView2.setError(getString(R.string.error_passwords_do_not_match));
            success = false;
        }
        else
            password = pass1;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView1.setError(getString(R.string.error_invalid_password));
            success = false;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            success = false;
        }
        else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            success = false;
        }
        if (success)
        {
            UserDAO userDAO = new UserDAO(this, email, password);
            userDAO.execute();
        }
    }

    /**
     * This function is called from the UserDAO class. It displays a message to the user.
     *
     * @param success   This parameter is passed from the onPostExecute(final Boolean success)
     *                  method from the UserDAO. Depending of the value of it the corresponding
     *                  message dialog will be displayed to the user.
     */
    public void showDialog(Boolean success)
    {
        if (success)
        {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Registration complete");
            alertDialog.setMessage("You are now officially registered on TransApp!");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                    finish();
                }
            });
            alertDialog.show();
        }
        else
        {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("An unexpected error has occurred. Try using " +
                    "a different email address.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.cancel();
                }
            });
            alertDialog.show();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") & email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private Boolean doPasswordsMatch(String pass1, String pass2) {
        return pass1.equals(pass2);
    }
}
