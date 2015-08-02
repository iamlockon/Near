package com.example.jay.fragmentbasics;

import com.parse.ui.ParseLoginDispatchActivity;

public class Intro extends ParseLoginDispatchActivity {
    @Override
    protected Class<?> getTargetClass() {
        return Welcome.class;
    }
}
