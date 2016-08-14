package com.ishow.ischool.bean.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abel on 16/8/12.
 */
public class Avatar implements Parcelable {

    /**
     * id : 29370
     * type : 1
     * file_name : http://7xwcxj.com1.z0.glb.clouddn.com//1469341646348avator.jpg
     * user_id : 10017
     */

    public int id;
    public int type;
    public String file_name;
    public int user_id;

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

    public Avatar() {
    }

    protected Avatar(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.file_name = in.readString();
        this.user_id = in.readInt();
    }

    public static final Parcelable.Creator<Avatar> CREATOR = new Parcelable.Creator<Avatar>() {
        @Override
        public Avatar createFromParcel(Parcel source) {
            return new Avatar(source);
        }

        @Override
        public Avatar[] newArray(int size) {
            return new Avatar[size];
        }
    };
}
