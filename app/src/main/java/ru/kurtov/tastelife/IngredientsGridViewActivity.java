package ru.kurtov.tastelife;

import android.support.v4.app.Fragment;

import ru.kurtov.tastelife.support.SingleFragmentActivity;

/**
 * Created by KURT on 22.04.2015.
 */
public class IngredientsGridViewActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return new IngredientsGridViewFragment();
	}
}
