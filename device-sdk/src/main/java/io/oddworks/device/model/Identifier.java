package io.oddworks.device.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Identifier implements Parcelable {
    public static final String TAG = Identifier.class.getSimpleName();
    private final String id;
    private final String type;

    public Identifier(@NonNull final String id, @NonNull final String type) {
        this.id = id;
        this.type = type;
    }

    public Identifier(Parcel in) {
        id = in.readString();
        type = in.readString();
    }

    public static final Creator<Identifier> CREATOR = new Creator<Identifier>() {
        @Override
        public Identifier createFromParcel(Parcel in) {
            return new Identifier(in);
        }

        @Override
        public Identifier[] newArray(int size) {
            return new Identifier[size];
        }
    };

    @NonNull public String getId() {
        return id;
    }

    @NonNull public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifier that = (Identifier) o;

        if (!id.equals(that.id)) return false;
        return type.equals(that.type);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
