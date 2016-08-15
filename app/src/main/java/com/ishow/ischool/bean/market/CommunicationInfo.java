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
    public int type;//	Int	1				0
    public String content;//	String	1			沟通内容	0
    public String result;//	String	1			沟通结果	0
    public int refuse;//	Int	1			抗拒点	0
    public int belief;//	Int	1			学习信念	0
    public String tuition_source;//	String	0			学费来源	0
    public long communication_date;//	Int	1			沟通日期	0
    public long callback_date;//	Int	0			回访日期	0
    public int user_id;//	Int	1			经办人ID	0
    public int is_del;//	Int	1				0
    public int create_time;//	Int	1				0
    public long update_time;//	Int	1				0
    public int campus_id;//	Int	1			校区 ID	0

    protected CommunicationInfo(Parcel in) {
        id = in.readInt();
        student_id = in.readInt();
        status = in.readInt();
        type = in.readInt();
        content = in.readString();
        result = in.readString();
        refuse = in.readInt();
        belief = in.readInt();
        tuition_source = in.readString();
        communication_date = in.readLong();
        callback_date = in.readLong();
        user_id = in.readInt();
        is_del = in.readInt();
        create_time = in.readInt();
        update_time = in.readLong();
        campus_id = in.readInt();
    }

    public static final Creator<CommunicationInfo> CREATOR = new Creator<CommunicationInfo>() {
        @Override
        public CommunicationInfo createFromParcel(Parcel in) {
            return new CommunicationInfo(in);
        }

        @Override
        public CommunicationInfo[] newArray(int size) {
            return new CommunicationInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(student_id);
        dest.writeInt(status);
        dest.writeInt(type);
        dest.writeString(content);
        dest.writeString(result);
        dest.writeInt(refuse);
        dest.writeInt(belief);
        dest.writeString(tuition_source);
        dest.writeLong(communication_date);
        dest.writeLong(callback_date);
        dest.writeInt(user_id);
        dest.writeInt(is_del);
        dest.writeInt(create_time);
        dest.writeLong(update_time);
        dest.writeInt(campus_id);
    }
}
