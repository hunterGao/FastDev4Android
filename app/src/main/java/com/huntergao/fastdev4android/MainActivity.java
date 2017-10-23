package com.huntergao.fastdev4android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.colonel.annotation.processor.ColonelAnnotation;
import com.colonel.annotation.processor.generated.GeneratedClass;

import javax.security.auth.login.LoginException;

@ColonelAnnotation
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    @ColonelAnnotation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GeneratedClass generatedClass = new GeneratedClass();
        Log.e(TAG, "onCreate: " + generatedClass.getMessage());
    }

}

