package ru.kurtov.tastelife;


import android.support.v4.app.Fragment;

import ru.kurtov.tastelife.support.SingleFragmentActivity;

public class ChooseIngredientActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ChooseIngredientFragment();
    }
}
