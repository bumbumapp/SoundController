package eu.darken.bluemusic.bluetooth.core;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import eu.darken.bluemusic.AppComponent;
import eu.darken.bluemusic.R;
import eu.darken.bluemusic.main.core.audio.AudioStream;

@AppComponent.Scope
public class FakeSpeakerDevice implements SourceDevice, Parcelable {
    public static final String ADDR = "self:speaker:main";
    private final String alias;

    @Inject
    public FakeSpeakerDevice(Context context) {
        alias = context.getString(R.string.label_device_speaker);
    }

    @Nullable
    @Override
    public BluetoothClass getBluetoothClass() {
        return null;
    }

    @Nullable
    @Override
    public String getName() {
        return android.os.Build.MODEL;
    }

    @Override
    public String getAddress() {
        return ADDR;
    }

    @Override
    public boolean setAlias(String newAlias) {
        return false;
    }

    @Nullable
    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public String getLabel() {
        String label = getAlias();
        if (label == null) label = getName();
        if (label == null) label = getAddress();
        return label;
    }

    @Override
    public AudioStream.Id getStreamId(AudioStream.Type type) {
        switch (type) {
            case MUSIC:
                return AudioStream.Id.STREAM_MUSIC;
            case CALL:
                return AudioStream.Id.STREAM_VOICE_CALL;
            case RINGTONE:
                return AudioStream.Id.STREAM_RINGTONE;
            case NOTIFICATION:
                return AudioStream.Id.STREAM_NOTIFICATION;
            case ALARM:
                return AudioStream.Id.STREAM_ALARM;
            default:
                throw new IllegalArgumentException("Unsupported AudioStreamType: " + type);
        }
    }

    protected FakeSpeakerDevice(Parcel in) {
        alias = (String) in.readValue(getClass().getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(alias);
    }

    public static final Creator<FakeSpeakerDevice> CREATOR = new Creator<FakeSpeakerDevice>() {
        @Override
        public FakeSpeakerDevice createFromParcel(Parcel in) {
            return new FakeSpeakerDevice(in);
        }

        @Override
        public FakeSpeakerDevice[] newArray(int size) {
            return new FakeSpeakerDevice[size];
        }
    };

    @Override
    public String toString() {
        return "FakeSpeakerDevice()";
    }
}
