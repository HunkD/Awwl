package com.hunk.reference.extension;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.hunk.reference.R;

public class AmountInputView extends LinearLayout implements View.OnFocusChangeListener {
	
	public AmountInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.item_amount_input, this);
		v.setFocusable(true);
		v.setFocusableInTouchMode(true);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		return new BaseInputConnection(this, false) {

			@Override
			public boolean commitText(CharSequence text, int newCursorPosition) {
				// TODO Auto-generated method stub
				return super.commitText(text, newCursorPosition);
			}
			
		};
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			showKeyboard();
		} else {
			hideKeyboard();
		}
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.getWindowToken(),0); 
	}

	public void showKeyboard() {
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
	}
	
	
}
