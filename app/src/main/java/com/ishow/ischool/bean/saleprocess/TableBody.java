package com.ishow.ischool.bean.saleprocess;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrS on 2016/9/14.
 */
public class TableBody implements Parcelable {
   public List<String> tablebody;

   public TableBody(){
      tablebody = new ArrayList<>();
   }
   public void add(String elment){
      tablebody.add(elment);
   }
  public String logString(){
    return tablebody.toString();
  }
   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeStringList(this.tablebody);
   }

   protected TableBody(Parcel in) {
      this.tablebody = in.createStringArrayList();
   }

   public static final Parcelable.Creator<TableBody> CREATOR = new Parcelable.Creator<TableBody>() {
      @Override
      public TableBody createFromParcel(Parcel source) {
         return new TableBody(source);
      }

      @Override
      public TableBody[] newArray(int size) {
         return new TableBody[size];
      }
   };
}
