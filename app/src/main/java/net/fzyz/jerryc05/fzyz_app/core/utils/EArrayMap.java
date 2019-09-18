package net.fzyz.jerryc05.fzyz_app.core.utils;

import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.EOFException;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public final class EArrayMap<K extends Serializable, V extends Serializable>
        implements Externalizable {

  private final ArrayMap<K, V> mArrayMap;

  public EArrayMap() {
    this(1);
  }

  public EArrayMap(int capacity) {
    mArrayMap = new ArrayMap<>(capacity);
  }

  @Nullable
  public V get(@NonNull final K key) {
    return mArrayMap.get(key);
  }

  @SuppressWarnings("UnusedReturnValue")
  @Nullable
  public V put(@NonNull final K key, @NonNull final V value) {
    return mArrayMap.put(key, value);
  }

  public int size() {
    return mArrayMap.size();
  }

  @NonNull
  public Collection<V> values() {
    return mArrayMap.values();
  }

  @NonNull
  @Override
  public String toString() {
    return mArrayMap.toString();
  }

  @Override
  public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException {
    try {
      //noinspection unchecked
      mArrayMap.put((K) in.readObject(), (V) in.readObject());
    } catch (final EOFException e) {//
    }
    in.close();
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    for (Map.Entry<K, V> entry : mArrayMap.entrySet()) {
      out.writeObject(entry.getKey());
      out.writeObject(entry.getValue());
    }
    out.close();
  }
//  public final Creator<EArrayMap> CREATOR =
//          new Creator<EArrayMap>() {
//            @Nullable
//            @Override
//            public EArrayMap createFromParcel(@NonNull final Parcel in) {
//              int size = in.readInt();
//              if (size <= 0) return null;
//
//              final Class clazz = (Class) in.readSerializable();
//
//              final EArrayMap map = new EArrayMap<V>(size);
//
//              for (int i = 0; i < size; i++)
//                //noinspection ConstantConditions
//                map.put(in.readString(), in.readParcelable(clazz.getClassLoader()));
//              return map;
//            }
//
//            @Override
//            public EArrayMap[] newArray(int size) {
//              return new EArrayMap[size];
//            }
//          };
//
//  @Override
//  public int describeContents() {
//    return 0;
//  }
//
//  @Override
//  public void writeToParcel(@NonNull final Parcel dest, int flags) {
//    int size = mArrayMap.size();
//
//    dest.writeInt(size);
//    if (size <= 0) return;
//
//    dest.writeSerializable(mArrayMap.valueAt(0).getClass());
//
//    for (Map.Entry<String, V> entry : mArrayMap.entrySet()) {
//      dest.writeString(entry.getKey());
//      entry.getValue().writeToParcel(dest, flags);
//    }
//  }
}
