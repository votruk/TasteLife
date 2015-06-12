package ru.kurtov.tastelife;

import android.support.v4.app.Fragment;

import ru.kurtov.tastelife.support.SingleFragmentActivity;

/**
 * Created by KURT on 10.04.2015.
 */
public class MainMenuActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return new  MainMenuFragment();
	}

}
