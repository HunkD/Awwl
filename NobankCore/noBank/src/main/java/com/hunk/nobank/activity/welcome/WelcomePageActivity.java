package com.hunk.nobank.activity.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hunk.abcd.extension.util.ViewHelper;
import com.hunk.abcd.views.slides.ZoomOutPageTransformer;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.activity.login.LoginPageActivity;
import com.hunk.nobank.activity.test.TestActivity;
import com.hunk.whitelabel.retailer.RetailerFeatureList;

public class WelcomePageActivity
        extends BaseActivity<WelcomePagePresenter>
        implements WelcomeView<WelcomePagePresenter> {

    private final static int[] imgReses = {
        R.drawable.welcome_1,
        R.drawable.welcome_2,
        R.drawable.welcome_3,
        R.drawable.welcome_4,
        R.drawable.welcome_5
    };

    private ViewPager viewPager;
    private WelcomeFragmentPagerAdapter pagerAdapter;
    private FloatingActionButton signInBtn;

    {
        setPresenter(new WelcomePagePresenter());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        setBaseStyle(Base.NO_DRAW_LAYOUT);
        setBaseStyle(Base.NO_TITLE_BAR);

        signInBtn = (FloatingActionButton) findViewById(R.id.welcome_btn_sign_in);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new WelcomeFragmentPagerAdapter(imgReses, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(false, new ZoomOutPageTransformer());

        mPresenter.onCreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        exitApplication(this);
    }

    public void onClickSignUp(View view) {
        Intent gotoRegistration = new Intent();
        gotoRegistration.setPackage(getApplicationContext().getPackageName());
        gotoRegistration.setAction(RetailerFeatureList.Registration.CardInfo.ACTION);

        startActivity(gotoRegistration);
    }

    @Override
    public void showSignUp(boolean show) {

    }

    public void onClickSignIn(View view) {
//        Intent gotoLogin = new Intent();
//        gotoLogin.setPackage(getApplicationContext().getPackageName());
//        gotoLogin.setAction(LoginPageActivity.ACTION);
//        startActivity(gotoLogin);
//        if (ViewHelper.shouldShowActivityTransition(this)) {
//            overridePendingTransition(0, 0);
//        }

        startActivity(new Intent(this, TestActivity.class));
    }

    private static class WelcomeFragmentPagerAdapter extends FragmentPagerAdapter {

        private final int[] imageReses;

        WelcomeFragmentPagerAdapter(@NonNull int[] imageReses, @NonNull FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
            this.imageReses = imageReses;
        }

        @Override
        public Fragment getItem(int position) {
            return PictureFragment.getInstance(imageReses[position]);
        }

        @Override
        public int getCount() {
            return imageReses.length;
        }
    }

    public static class PictureFragment extends Fragment {

        private static final String PARAM_IMG = "PARAM_IMG";

        public static Fragment getInstance(int imageRes) {
            PictureFragment fragment = new PictureFragment();
            Bundle args = new Bundle();
            args.putInt(PARAM_IMG, imageRes);
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.frag_welcome_item, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            if (getView() == null) {
                return;
            }

            int imgRes = getArguments().getInt(PARAM_IMG);
            ImageView imageView = (ImageView) getView().findViewById(R.id.img);
            imageView.setImageResource(imgRes);
        }
    }
}
