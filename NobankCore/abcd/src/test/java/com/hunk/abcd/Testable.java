package com.hunk.abcd;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * Why not using interface? this is limited by Robolectric.
 *
 * @author HunkDeng
 * @since 2016/7/2
 */
@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class, sdk = 21)
public abstract class Testable {
}
