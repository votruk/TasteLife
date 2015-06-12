package ru.kurtov.tastelife;

import android.support.v4.app.Fragment;

import ru.kurtov.tastelife.support.SingleFragmentActivity;

/**
 * Created by KURT on 20.02.2015.
 */
public class IngredientListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new IngredientListFragment();
    }
}
