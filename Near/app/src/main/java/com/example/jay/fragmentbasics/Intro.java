package com.example.jay.fragmentbasics;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ui.ParseLoginDispatchActivity;
import com.parse.ui.ParseSignupFragment;

import java.util.List;

public class Intro extends ParseLoginDispatchActivity {

    @Override
    protected Class<?> getTargetClass() {
        return Welcome.class;
    }
}
