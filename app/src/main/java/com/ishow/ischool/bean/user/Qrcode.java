package com.ishow.ischool.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MrS on 2016/8/17.
 */
public class Qrcode implements Parcelable {

    /**
     * id : 29417
     * type : 3
     * file_name : http://7xwcxj.com1.z0.glb.clouddn.com/2016-08-16_16290372843.png
     * user_id : 10039
     */

    private int id;
    private int type;
    private String file_name;
    private int user_id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.file_name);
        dest.writeInt(this.user_id);
    }

    public Qrcode() {
    }

    protected Qrcode(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.file_name = in.readString();
        this.user_id = in.readInt();
    }

    public static final Parcelable.Creator<Qrcode> CREATOR = new Parcelable.Creator<Qrcode>() {
        @Override
        public Qrcode createFromParcel(Parcel source) {
            return new Qrcode(source);
        }

        @Override
        public Qrcode[] newArray(int size) {
            return new Qrcode[size];
        }
    };
}
