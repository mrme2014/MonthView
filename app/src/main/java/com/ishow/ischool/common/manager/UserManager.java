package com.ishow.ischool.common.manager;

import android.content.Context;

import com.commonlib.util.LogUtil;
import com.commonlib.util.SpUtil;
import com.google.gson.Gson;
import com.ishow.ischool.bean.user.Avatar;
import com.ishow.ischool.bean.user.Campus;
import com.ishow.ischool.bean.user.Position;
import com.ishow.ischool.bean.user.PositionInfo;
import com.ishow.ischool.bean.user.User;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kevin on 15/8/7.
 */
public class UserManager {

    private static final String USER_KEY = "user_key";

    private static UserManager userManager;

    private Context context;

    private Gson gson;

    private User user;

    private Object lock = new Object();

    private UserManager() {
        gson = new Gson();
    }

    public static synchronized UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }
        return userManager;
    }

    public void init(Context context) {
        this.context = context;
    }

    /**
     * 保存User到本地，必须保证user 并且 userInfo 不是null
     *
     * @param user
     */
    public void save(User user) {
        if (context == null) {
            throw new RuntimeException();
        }
        if (user == null) {
            LogUtil.d(this, "save user or userinfo is null");
            return;
        }
        this.user = user;
        synchronized (lock) {
            String data = gson.toJson(user);
            persistDate(data);
        }
    }

    public User get() {
        if (context == null) {
            throw new RuntimeException();
        }
        synchronized (lock) {
            if (user == null) {
                String data = readData();
                user = gson.fromJson(data, User.class);
            }
            if (user == null) {
                LogUtil.d(this, "get user is null");
            }
            return user;
        }
    }



    public void updateCurrentPositionInfo(Position position, List<Integer> resources) {
        if (user==null)user = get();
        if (user != null) {
            PositionInfo positionInfo = user.positionInfo;
            positionInfo.id = position.id;
            positionInfo.title = position.title;
            positionInfo.campusId = position.campus_id;
            List<Campus> campus = user.campus;
            if (campus != null) {
                for (int i = 0; i < campus.size(); i++) {
                    if (campus.get(i).id == position.campus_id) {
                        positionInfo.campus = campus.get(i).name;
                        break;
                    }
                }
            }

            if (resources!=null)user.myResources = resources;
            save(user);
        }
    }

    public void initCampusPositions(User mUser) {
        if (mUser != null) {
            this.user = mUser;
            List<Campus> campus = mUser.campus;
            List<Position> position = mUser.position;

            if (campus == null)
                return;

            /**Campus.class
             *
             * id : 1
             * name : 杭州校区
             *
             public int id;
             public String name;
             public ArrayList<String> positions;
             */
            for (int i = 0; i < campus.size(); i++) {

                ArrayList<String> campusPosition = new ArrayList<>();

                for (int j = 0; j < position.size(); j++) {
                    if (position.get(j).campus_id == campus.get(i).id) {
                        campusPosition.add(position.get(j).title);
                    }
                    campus.get(i).positions = campusPosition;
                }
            }
            /**
             * public int id;
             public String title;
             public String campus;
             public int campusId;
             每次进来 都需要对PositionInfo 值 赋值
             */
            PositionInfo positionInfo = user.positionInfo;

            for (int i = 0; i < position.size(); i++) {
                if (position.get(i).id == positionInfo.id) {
                    updateCurrentPositionInfo(position.get(i),null);
                    break;
                }
            }
        }

    }


    public void clear() {
        synchronized (lock) {
            user = null;
            persistDate("");
        }
    }

    private void persistDate(String data) {
        LogUtil.e("persistDate" + data);
        SpUtil.getInstance(context).setValue(USER_KEY, data);
        user = get();
    }

    private String readData() {
        return SpUtil.getInstance(context).getStringValue(USER_KEY);
    }
    public void upadteInfo(Avatar avatar ,int birthday,String qq){
        if (user==null)
            user = get();
        if (avatar!=null)user.avatar =avatar;
        if (birthday!=0)user.userInfo.qq =qq;
        if (qq!=null&&qq!="")user.userInfo.birthday=birthday;
        save(user);
    }

    public List<Integer>  getResurces(){
        if (user==null) user =get();
        List<Integer> myResources = user.myResources;
        if (myResources!=null) return myResources;
        return  null;
    }
}
