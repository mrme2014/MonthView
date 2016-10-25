package com.ishow.ischool.bean.market;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abel on 16/8/15.
 */
public class CommunicationInfo implements Parcelable {
    public int id;//	Int	1				0
    public int student_id;//	Int	1				0
    public int status;//	Int	1				0
    public String status_other;
    public int type;//	Int	1				0
    public String content;//	String	1			沟通内容	0
    public String result;//	String	1			沟通结果	0
    public int refuse;//	Int	1			抗拒点	0
    public String refuse_other;
    public int belief;//	Int	1			学习信念	0
    public String tuition_source;//	String	0			学费来源	0
    public long communication_date;//	Int	1			沟通日期	0
    public long callback_date;//	Int	0			回访日期	0
    public int user_id;//	Int	1			经办人ID	0
    public int is_del;//	Int	1				0
    public int create_time;//	Int	1				0
    public long update_time;//	Int	1				0
    public int campus_id;//	Int	1			校区 ID	0
    public String refuse_name;
    public String belief_name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.student_id);
        dest.writeInt(this.status);
        dest.writeString(this.status_other);
        dest.writeInt(this.type);
        dest.writeString(this.content);
        dest.writeString(this.result);
        dest.writeInt(this.refuse);
        dest.writeString(this.refuse_other);
        dest.writeInt(this.belief);
        dest.writeString(this.tuition_source);
        dest.writeLong(this.communication_date);
        dest.writeLong(this.callback_date);
        dest.writeInt(this.user_id);
        dest.writeInt(this.is_del);
        dest.writeInt(this.create_time);
        dest.writeLong(this.update_time);
        dest.writeInt(this.campus_id);
        dest.writeString(this.refuse_name);
        dest.writeString(this.belief_name);
    }

    public CommunicationInfo() {
    }

    protected CommunicationInfo(Parcel in) {
        this.id = in.readInt();
        this.student_id = in.readInt();
        this.status = in.readInt();
        this.status_other = in.readString();
        this.type = in.readInt();
        this.content = in.readString();
        this.result = in.readString();
        this.refuse = in.readInt();
        this.refuse_other = in.readString();
        this.belief = in.readInt();
        this.tuition_source = in.readString();
        this.communication_date = in.readLong();
        this.callback_date = in.readLong();
        this.user_id = in.readInt();
        this.is_del = in.readInt();
        this.create_time = in.readInt();
        this.update_time = in.readLong();
        this.campus_id = in.readInt();
        this.refuse_name = in.readString();
        this.belief_name = in.readString();
    }

    public static final Creator<CommunicationInfo> CREATOR = new Creator<CommunicationInfo>() {
        @Override
        public CommunicationInfo createFromParcel(Parcel source) {
            return new CommunicationInfo(source);
        }

        @Override
        public CommunicationInfo[] newArray(int size) {
            return new CommunicationInfo[size];
        }
    };
}
