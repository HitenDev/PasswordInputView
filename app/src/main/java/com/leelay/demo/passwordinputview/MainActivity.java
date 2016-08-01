package com.leelay.demo.passwordinputview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PasswordInputView passwordInputView = (PasswordInputView) findViewById(R.id.passwordInputView);
        if (passwordInputView != null)
            passwordInputView.setOnInputCallback(new PasswordInputView.OnInputCallback() {
                @Override
                public void onInputComplete(String inputText) {
                    Toast.makeText(MainActivity.this,inputText,Toast.LENGTH_SHORT).show();
                }
            });

    }
}
