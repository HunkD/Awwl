package com.hunk.nobank.manager.flow;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HunkDeng
 * @since 2016/2/8
 */
public class ScreenNode {
    public static final Integer NORMAL_COMPLETE = 0;
    private Class<? extends Activity> mCurrentScreenClass;
    private Map<Integer, ScreenNode> nextScreenClassMap = new HashMap<>();

    public ScreenNode() {}

    /**
     * Return next screen node
     * @param condition
     * @return
     */
    public ScreenNode when(@NonNull Integer condition) {
        ScreenNode screenNode;
        if (nextScreenClassMap.containsKey(condition)) {
            screenNode = nextScreenClassMap.get(condition);
        } else {
            screenNode = new ScreenNode();
            nextScreenClassMap.put(condition, screenNode);
        }
        return screenNode;
    }

    /**
     * Set current screen Activity class
     * @param currentScreenClass
     */
    public void to(Class<? extends Activity> currentScreenClass) {
        if (mCurrentScreenClass != null) {
            throw new RuntimeException("Did you forgot calling when() method? " +
                    "You need to define screen flow like from().when().to()");
        } else {
            this.mCurrentScreenClass = currentScreenClass;
        }
    }

    /**
     * Get next screen Activity by condition
     * @param condition
     * @return
     */
    public ScreenNode getNext(@NonNull Integer condition) {
        return nextScreenClassMap.get(condition);
    }

    /**
     * Get current screen Activity class
     * @return
     */
    @NonNull
    public Class<? extends Activity> getCurrent() {
        return mCurrentScreenClass;
    }
}
