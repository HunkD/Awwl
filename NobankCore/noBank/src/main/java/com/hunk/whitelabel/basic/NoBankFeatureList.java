package com.hunk.whitelabel.basic;

/**
 * @author HunkDeng
 * @since 2016/2/10
 */
public class NoBankFeatureList {

    public static class Welcome extends FeatureBasic {
        public static final String ACTION = "action.welcome.open_main";
    }

    public static class Registration extends FeatureBasic {

        public static class CardInfo extends FeatureBasic{
            public static final String ACTION = "action.registration.open_main";
        }

        public static class SignUp extends FeatureBasic {
            public static final String ACTION = "action.registration.signUp";
        }
    }

    public static class Dashboard extends FeatureBasic {
        public static final String ACTION = "action.dashboard.open_main";
    }

    public static class Deposit extends FeatureBasic {

        public static class Mrdc extends FeatureBasic {

            public static class DashboardVisible extends FeatureBasic {

                public static final boolean ENABLE = false;
            }

            public static class ReloadVisible extends FeatureBasic {

                public static final boolean ENABLE = true;
            }
        }
    }

    public static class Payment extends FeatureBasic {
        public static final String ACTION = "action.payment.open_main";
    }
}
