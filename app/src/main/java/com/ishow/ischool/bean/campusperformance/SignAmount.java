package com.ishow.ischool.bean.campusperformance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wqf on 16/9/19.
 */
public class SignAmount implements Parcelable {
    public int scene;
    public int sign;
    public int fullPay;
    public float signRate;
    public float fullSignRate;
    public float fullRate;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.scene);
        dest.writeInt(this.sign);
        dest.writeInt(this.fullPay);
        dest.writeFloat(this.signRate);
        dest.writeFloat(this.fullSignRate);
        dest.writeFloat(this.fullRate);
    }

    public SignAmount() {
    }

    protected SignAmount(Parcel in) {
        this.scene = in.readInt();
        this.sign = in.readInt();
        this.fullPay = in.readInt();
        this.signRate = in.readFloat();
        this.fullSignRate = in.readFloat();
        this.fullRate = in.readFloat();
    }

    public static final Creator<SignAmount> CREATOR = new Creator<SignAmount>() {
        @Override
        public SignAmount createFromParcel(Parcel source) {
            return new SignAmount(source);
        }

        @Override
        public SignAmount[] newArray(int size) {
            return new SignAmount[size];
        }
    };
}
