package com.hunk.nobank.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hunk.nobank.NConstants;
import com.hunk.nobank.R;
import com.hunk.nobank.util.ViewHelper;

public class CardInfoActivity extends BaseActivity {

    private EditText mCardNumberInput;
    private EditText mCardCVVInput;
    private EditText mCardNumberInputSufix;
    private View mCardCVVLabel;
    private int mTranslateLength;
    private State mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info, Base.NO_DRAW_LAYOUT);

        setupUI();
    }

    private void setupUI() {
        //---find views---
        mCardCVVLabel = findViewById(R.id.card_info_page_lb_card_cvv);
        mCardNumberInput = (EditText) findViewById(R.id.card_info_page_input_card_number);
        mCardCVVInput = (EditText) findViewById(R.id.card_info_page_input_card_cvv);
        mCardNumberInputSufix = (EditText) findViewById(R.id.card_info_page_input_card_number_sufix);

        //---setListeners
        mCardNumberInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // start animation for focus on CardNumber
                    anmiateToCardNumber();
                }
            }
        });
        mCardCVVInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // start animation for focus on CVV
                    animateToCVV();
                }
            }
        });

        mState = State.CardNumber;
    }

    protected void animateToCVV() {
        if (mState != State.CardCVV) {
            mState = State.CardCVV;

            int length = 0 - getLength();
            ViewHelper.translateX(mCardCVVInput, 0, length, NConstants.ANIMATION_DURATION_MEDIUM);
            ViewHelper.translateX(mCardNumberInput, 0, length, NConstants.ANIMATION_DURATION_MEDIUM);
            ViewHelper.translateX(mCardCVVLabel, 0, length, NConstants.ANIMATION_DURATION_MEDIUM);
        }
    }

    protected void anmiateToCardNumber() {
        if (mState != State.CardNumber) {
            mState = State.CardNumber;

            int length = getLength();
            ViewHelper.translateX(mCardCVVInput, 0, length, NConstants.ANIMATION_DURATION_MEDIUM);
            ViewHelper.translateX(mCardNumberInput, 0, length, NConstants.ANIMATION_DURATION_MEDIUM);
            ViewHelper.translateX(mCardCVVLabel, 0, length, NConstants.ANIMATION_DURATION_MEDIUM);
        }
    }

    private int getLength() {
        if (mTranslateLength == 0) {
            mTranslateLength = mCardNumberInput.getWidth() - mCardNumberInputSufix.getWidth();
        }
        return mTranslateLength;
    }

    enum State {
        CardCVV, CardNumber
    }
}
