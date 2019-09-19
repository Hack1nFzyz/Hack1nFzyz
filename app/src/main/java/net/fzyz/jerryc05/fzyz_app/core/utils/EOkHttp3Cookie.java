package net.fzyz.jerryc05.fzyz_app.core.utils;

import androidx.annotation.NonNull;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import okhttp3.Cookie;

public final class EOkHttp3Cookie implements Externalizable {

  private static final long   serialVersionUID = 1;
  private              Cookie mCookie;

  @SuppressWarnings("WeakerAccess")
  public EOkHttp3Cookie() {
  }

  public static EOkHttp3Cookie of(@NonNull final Cookie cookie) {
    final EOkHttp3Cookie newCookie = new EOkHttp3Cookie();
    newCookie.mCookie = cookie;
    return newCookie;
  }


  @NonNull
  public Cookie toOkHttp3Cookie() {
    return mCookie;
  }

  @NonNull
  @Override
  public String toString() {
    return mCookie.toString();
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException {
    final Cookie.Builder builder = new Cookie.Builder()
            .name(in.readUTF())
            .value(in.readUTF())
            .expiresAt(in.readLong())
            .domain(in.readUTF())
            .path(in.readUTF());

    if (in.readBoolean())
      builder.secure();
    if (in.readBoolean())
      builder.httpOnly();

    mCookie = builder.build();
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeUTF(mCookie.name());
    out.writeUTF(mCookie.value());
    out.writeLong(mCookie.expiresAt());
    out.writeUTF(mCookie.domain());
    out.writeUTF(mCookie.path());
    out.writeBoolean(mCookie.secure());
    out.writeBoolean(mCookie.httpOnly());
  }
//
//  public static final Parcelable.Creator<EOkHttp3Cookie> CREATOR = new Parcelable.Creator<EOkHttp3Cookie>() {
//    @Override
//    public EOkHttp3Cookie createFromParcel(@NonNull final Parcel in) {
//      //noinspection ConstantConditions
//      final Cookie.Builder builder = new Cookie.Builder()
//              .name(in.readUTF())
//              .value(in.readUTF())
//              .expiresAt(in.readLong())
//              .domain(in.readUTF())
//              .path(in.readUTF());
//
//      if (in.readByte() != 0)
//        builder.secure();
//      if (in.readByte() != 0)
//        builder.httpOnly();
//
//      return new EOkHttp3Cookie(builder.build());
//    }
//
//    @Override
//    public EOkHttp3Cookie[] newArray(int size) {
//      return new EOkHttp3Cookie[size];
//    }
//  };
//
//  @Override
//  public int describeContents() {
//    return 0;
//  }
//
//  @Override
//  public void writeToParcel(@NonNull final Parcel dest, int flags) {
//    dest.writeUTF(mCookie.name());
//    dest.writeUTF(mCookie.value());
//    dest.writeLong(mCookie.expiresAt());
//    dest.writeUTF(mCookie.domain());
//    dest.writeUTF(mCookie.path());
//    dest.writeByte((byte) (mCookie.secure() ? 1 : 0));
//    dest.writeByte((byte) (mCookie.httpOnly() ? 1 : 0));
//  }
}