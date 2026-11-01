package eu.darken.bluemusic.ads;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

/** Google Play build: no ads. Same API as the Huawei (hms) build's AdsManager. */
public final class AdsManager {
    private static AdsManager instance;

    public static synchronized AdsManager get(Context c) {
        if (instance == null) instance = new AdsManager();
        return instance;
    }

    private AdsManager() {
    }

    public void init(Activity activity) {
    }

    public void loadBanner(Activity activity, FrameLayout container) {
        container.setVisibility(View.GONE);
    }

    public int bannerHeightPx(Activity activity) {
        return 0;
    }

    public void onBreakPoint(Activity activity, Runnable next) {
        next.run();
    }
}
