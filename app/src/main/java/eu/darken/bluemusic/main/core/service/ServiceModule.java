package eu.darken.bluemusic.main.core.service;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import eu.darken.bluemusic.main.core.service.modules.AutoplayModule;
import eu.darken.bluemusic.main.core.service.modules.CallVolumeModule;
import eu.darken.bluemusic.main.core.service.modules.MusicVolumeModule;

@Module
public class ServiceModule {
    @Provides
    @Named("VolumeObserver")
    Handler volumeHandler() {
        HandlerThread handlerThread = new HandlerThread("VolumeObserver");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        return new Handler(looper);
    }

    @Provides
    List<ActionModule> actionModules(MusicVolumeModule m1, CallVolumeModule m2, AutoplayModule autoplay) {
        return Arrays.asList(m1, m2, autoplay);
    }

}