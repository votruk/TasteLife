package ru.kurtov.tastelife;

import android.support.v4.app.Fragment;

import ru.kurtov.tastelife.support.SingleFragmentActivity;

/**
 * Created by KURT on 08.05.2015.
 */
public class CircleChooseActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new CircleChooseFragment();
	}
}
