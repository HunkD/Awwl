package com.hunk.nobank.activity.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hunk.nobank.NConstants;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.util.ViewHelper;
import com.hunk.whitelabel.retailer.RetailerFeatureList;

public class CardInfoActivity
        extends BaseActivity<CardInfoPresenter>
        implements CardInfoView<CardInfoPresenter> {

    private EditText mCardNumberInput;
    private EditText mCardCVVInput;
    private EditText mCardNumberInputSuffix;
    private View mCardCVVLabel;
    private int mTranslateLength;
    private State mState;
    private View mSignUpBtn;

    {
        setPresenter(new CardInfoPresenterImpl());
    }
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
        mCardNumberInputSuffix = (EditText) findViewById(R.id.card_info_page_input_card_number_sufix);
        mSignUpBtn = findViewById(R.id.btn_sign_up);

        //---setListeners
        mCardNumberInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // start animation for focus on CardNumber
                    animateToCardNumber();
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

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch validate card info
                // If true, go to next sign up screen.
                Intent gotoSignUpPage = new Intent();
                gotoSignUpPage.setPackage(getApplicationContext().getPackageName());
                gotoSignUpPage.setAction(RetailerFeatureList.Registration.SignUp.ACTION);
                startActivity(gotoSignUpPage);
            }
        });
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

    protected void animateToCardNumber() {
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
            mTranslateLength = mCardNumberInput.getWidth() - mCardNumberInputSuffix.getWidth();
        }
        return mTranslateLength;
    }

    enum State {
        CardCVV, CardNumber
    }
}
