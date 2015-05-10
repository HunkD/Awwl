package com.hunk.nobank.feature.base.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hunk.nobank.R;

public class BalanceFragment extends Fragment {

	private TextView mBalance;
	private ImageView mUserLogo;

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_balance, container, false);
		setupUI(view);
		return view;
	}

	private void setupUI(View view) {
		mBalance = (TextView) view.findViewById(R.id.txt_balance);
		mUserLogo = (ImageView) view.findViewById(R.id.user_logo);

		mBalance.setText("$987.65");
	}
}
