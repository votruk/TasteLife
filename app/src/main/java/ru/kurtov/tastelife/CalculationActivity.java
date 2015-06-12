package ru.kurtov.tastelife;

import android.support.v4.app.Fragment;

import ru.kurtov.tastelife.support.SingleFragmentActivity;

/**
 * Created by KURT on 10.03.2015.
 */
public class CalculationActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CalculationFragment();
    }
}
