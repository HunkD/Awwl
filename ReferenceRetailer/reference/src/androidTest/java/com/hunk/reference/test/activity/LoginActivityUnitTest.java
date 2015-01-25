package com.hunk.reference.test.activity;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.TextView;

import com.hunk.nobank.feature.login.activity.LoginPageActivity;
import com.hunk.reference.R;
import com.hunk.reference.ReferenceApplication;

public class LoginActivityUnitTest extends ActivityUnitTestCase<LoginPageActivity> {

	private Intent mLaunchIntent;

	public LoginActivityUnitTest() {
		super(LoginPageActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		
		mLaunchIntent = new Intent(getInstrumentation().getTargetContext(), 
				LoginPageActivity.class);
	}

	public final static String PRECONFITION_PREFIX = "Precondition: ";
	@MediumTest
	public void testPrecondition() {
		setApplication(new ReferenceApplication());
		startActivity(mLaunchIntent, null, null);
		assertNotNull(PRECONFITION_PREFIX + "start activity is null.", getActivity());
		TextView mInputLoginName = (TextView) getActivity().findViewById(R.id.login_page_input_login_name);
		assertNotNull(PRECONFITION_PREFIX + "login name input field is null.", mInputLoginName);
	}
}
