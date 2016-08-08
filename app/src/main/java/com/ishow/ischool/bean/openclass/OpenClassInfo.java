package com.ishow.ischool.bean.openclass;

import java.util.List;

/**
 * Created by MrS on 2016/7/22.
 */
public class OpenClassInfo {


    /**
     * error_no : 0
     * error_msg : 操作成功
     * result : {"lists":[{"fileinfo":{"id":29358,"type":4,"file_name":"http://7xwcxj.com1.z0.glb.clouddn.com/FsgR26nwCgnYEi81d0q7yq__O66W","user_id":10017},"openclassinfo":{"id":787,"begin_time":1469498400,"end_time":1469505600,"time_type":1,"position":"505","saler_id":10017,"create_time":1469243485,"update_time":0,"campus_id":1,"file_id":29358},"user":{"userInfo":{"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469428144,"login_num":263,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"},"position":[]}},{"fileinfo":{"id":29371,"type":4,"file_name":"http://7xwcxj.com1.z0.glb.clouddn.com/FsgR26nwCgnYEi81d0q7yq__O66W","user_id":10017},"openclassinfo":{"id":790,"begin_time":1469404800,"end_time":1469422800,"time_type":1,"position":"龙门","saler_id":10017,"create_time":1469420425,"update_time":0,"campus_id":1,"file_id":29371},"user":{"userInfo":{"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469428144,"login_num":263,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"},"position":[]}},{"fileinfo":{"id":29372,"type":4,"file_name":"http://7xwcxj.com1.z0.glb.clouddn.com/FsgR26nwCgnYEi81d0q7yq__O66W","user_id":10017},"openclassinfo":{"id":791,"begin_time":1469577600,"end_time":1469595600,"time_type":1,"position":"肚饿拖鞋","saler_id":10017,"create_time":1469420683,"update_time":0,"campus_id":1,"file_id":29372},"user":{"userInfo":{"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469428144,"login_num":263,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"},"position":[]}},{"fileinfo":{"id":29373,"type":4,"file_name":"http://7xwcxj.com1.z0.glb.clouddn.com/FsgR26nwCgnYEi81d0q7yq__O66W","user_id":10017},"openclassinfo":{"id":792,"begin_time":1469491200,"end_time":1469509200,"time_type":1,"position":"V5那我就","saler_id":10017,"create_time":1469427754,"update_time":0,"campus_id":1,"file_id":29373},"user":{"userInfo":{"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469428144,"login_num":263,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"},"position":[]}}],"total":4}
     */

    private int error_no;
    private String error_msg;
    /**
     * lists : [{"fileinfo":{"id":29358,"type":4,"file_name":"http://7xwcxj.com1.z0.glb.clouddn.com/FsgR26nwCgnYEi81d0q7yq__O66W","user_id":10017},"openclassinfo":{"id":787,"begin_time":1469498400,"end_time":1469505600,"time_type":1,"position":"505","saler_id":10017,"create_time":1469243485,"update_time":0,"campus_id":1,"file_id":29358},"user":{"userInfo":{"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469428144,"login_num":263,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"},"position":[]}},{"fileinfo":{"id":29371,"type":4,"file_name":"http://7xwcxj.com1.z0.glb.clouddn.com/FsgR26nwCgnYEi81d0q7yq__O66W","user_id":10017},"openclassinfo":{"id":790,"begin_time":1469404800,"end_time":1469422800,"time_type":1,"position":"龙门","saler_id":10017,"create_time":1469420425,"update_time":0,"campus_id":1,"file_id":29371},"user":{"userInfo":{"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469428144,"login_num":263,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"},"position":[]}},{"fileinfo":{"id":29372,"type":4,"file_name":"http://7xwcxj.com1.z0.glb.clouddn.com/FsgR26nwCgnYEi81d0q7yq__O66W","user_id":10017},"openclassinfo":{"id":791,"begin_time":1469577600,"end_time":1469595600,"time_type":1,"position":"肚饿拖鞋","saler_id":10017,"create_time":1469420683,"update_time":0,"campus_id":1,"file_id":29372},"user":{"userInfo":{"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469428144,"login_num":263,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"},"position":[]}},{"fileinfo":{"id":29373,"type":4,"file_name":"http://7xwcxj.com1.z0.glb.clouddn.com/FsgR26nwCgnYEi81d0q7yq__O66W","user_id":10017},"openclassinfo":{"id":792,"begin_time":1469491200,"end_time":1469509200,"time_type":1,"position":"V5那我就","saler_id":10017,"create_time":1469427754,"update_time":0,"campus_id":1,"file_id":29373},"user":{"userInfo":{"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469428144,"login_num":263,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"},"position":[]}}]
     * total : 4
     */

    private ResultBean result;

    public int getError_no() {
        return error_no;
    }

    public void setError_no(int error_no) {
        this.error_no = error_no;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private int total;
        /**
         * fileinfo : {"id":29358,"type":4,"file_name":"http://7xwcxj.com1.z0.glb.clouddn.com/FsgR26nwCgnYEi81d0q7yq__O66W","user_id":10017}
         * openclassinfo : {"id":787,"begin_time":1469498400,"end_time":1469505600,"time_type":1,"position":"505","saler_id":10017,"create_time":1469243485,"update_time":0,"campus_id":1,"file_id":29358}
         * user : {"userInfo":{"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469428144,"login_num":263,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"},"position":[]}
         */

        private List<ListsBean> lists;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ListsBean> getLists() {
            return lists;
        }

        public void setLists(List<ListsBean> lists) {
            this.lists = lists;
        }

        public static class ListsBean {
            /**
             * id : 29358
             * type : 4
             * file_name : http://7xwcxj.com1.z0.glb.clouddn.com/FsgR26nwCgnYEi81d0q7yq__O66W
             * user_id : 10017
             */

            private FileinfoBean fileinfo;
            /**
             * id : 787
             * begin_time : 1469498400
             * end_time : 1469505600
             * time_type : 1
             * position : 505
             * saler_id : 10017
             * create_time : 1469243485
             * update_time : 0
             * campus_id : 1
             * file_id : 29358
             */

            private OpenclassinfoBean openclassinfo;
            /**
             * userInfo : {"mobile":"13512341234","user_name":"zhyon","nick_name":"jaya","avatar":29370,"campus_id":1,"position_id":1,"last_login_time":1469428144,"login_num":263,"status":1,"user_id":10017,"birthday":1469289600,"job":"PHP","qq":"12456789710"}
             * position : []
             */

            private UserBean user;

            public FileinfoBean getFileinfo() {
                return fileinfo;
            }

            public void setFileinfo(FileinfoBean fileinfo) {
                this.fileinfo = fileinfo;
            }

            public OpenclassinfoBean getOpenclassinfo() {
                return openclassinfo;
            }

            public void setOpenclassinfo(OpenclassinfoBean openclassinfo) {
                this.openclassinfo = openclassinfo;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public static class FileinfoBean {
                private int id;
                private int type;
                private String file_name;
                private int user_id;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public String getFile_name() {
                    return file_name;
                }

                public void setFile_name(String file_name) {
                    this.file_name = file_name;
                }

                public int getUser_id() {
                    return user_id;
                }

                public void setUser_id(int user_id) {
                    this.user_id = user_id;
                }
            }

            public static class OpenclassinfoBean {
                private int id;
                private int begin_time;
                private int end_time;
                private int time_type;
                private String position;
                private int saler_id;
                private int create_time;
                private int update_time;
                private int campus_id;
                private int file_id;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getBegin_time() {
                    return begin_time;
                }

                public void setBegin_time(int begin_time) {
                    this.begin_time = begin_time;
                }

                public int getEnd_time() {
                    return end_time;
                }

                public void setEnd_time(int end_time) {
                    this.end_time = end_time;
                }

                public int getTime_type() {
                    return time_type;
                }

                public void setTime_type(int time_type) {
                    this.time_type = time_type;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
                }

                public int getSaler_id() {
                    return saler_id;
                }

                public void setSaler_id(int saler_id) {
                    this.saler_id = saler_id;
                }

                public int getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(int create_time) {
                    this.create_time = create_time;
                }

                public int getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_time(int update_time) {
                    this.update_time = update_time;
                }

                public int getCampus_id() {
                    return campus_id;
                }

                public void setCampus_id(int campus_id) {
                    this.campus_id = campus_id;
                }

                public int getFile_id() {
                    return file_id;
                }

                public void setFile_id(int file_id) {
                    this.file_id = file_id;
                }
            }

            public static class UserBean {
                /**
                 * mobile : 13512341234
                 * user_name : zhyon
                 * nick_name : jaya
                 * avatar : 29370
                 * campus_id : 1
                 * position_id : 1
                 * last_login_time : 1469428144
                 * login_num : 263
                 * status : 1
                 * user_id : 10017
                 * birthday : 1469289600
                 * job : PHP
                 * qq : 12456789710
                 */

                private UserInfoBean userInfo;
                private List<?> position;

                public UserInfoBean getUserInfo() {
                    return userInfo;
                }

                public void setUserInfo(UserInfoBean userInfo) {
                    this.userInfo = userInfo;
                }

                public List<?> getPosition() {
                    return position;
                }

                public void setPosition(List<?> position) {
                    this.position = position;
                }

                public static class UserInfoBean {
                    private String mobile;
                    private String user_name;
                    private String nick_name;
                    private int avatar;
                    private int campus_id;
                    private int position_id;
                    private int last_login_time;
                    private int login_num;
                    private int status;
                    private int user_id;
                    private int birthday;
                    private String job;
                    private String qq;

                    public String getMobile() {
                        return mobile;
                    }

                    public void setMobile(String mobile) {
                        this.mobile = mobile;
                    }

                    public String getUser_name() {
                        return user_name;
                    }

                    public void setUser_name(String user_name) {
                        this.user_name = user_name;
                    }

                    public String getNick_name() {
                        return nick_name;
                    }

                    public void setNick_name(String nick_name) {
                        this.nick_name = nick_name;
                    }

                    public int getAvatar() {
                        return avatar;
                    }

                    public void setAvatar(int avatar) {
                        this.avatar = avatar;
                    }

                    public int getCampus_id() {
                        return campus_id;
                    }

                    public void setCampus_id(int campus_id) {
                        this.campus_id = campus_id;
                    }

                    public int getPosition_id() {
                        return position_id;
                    }

                    public void setPosition_id(int position_id) {
                        this.position_id = position_id;
                    }

                    public int getLast_login_time() {
                        return last_login_time;
                    }

                    public void setLast_login_time(int last_login_time) {
                        this.last_login_time = last_login_time;
                    }

                    public int getLogin_num() {
                        return login_num;
                    }

                    public void setLogin_num(int login_num) {
                        this.login_num = login_num;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public void setStatus(int status) {
                        this.status = status;
                    }

                    public int getUser_id() {
                        return user_id;
                    }

                    public void setUser_id(int user_id) {
                        this.user_id = user_id;
                    }

                    public int getBirthday() {
                        return birthday;
                    }

                    public void setBirthday(int birthday) {
                        this.birthday = birthday;
                    }

                    public String getJob() {
                        return job;
                    }

                    public void setJob(String job) {
                        this.job = job;
                    }

                    public String getQq() {
                        return qq;
                    }

                    public void setQq(String qq) {
                        this.qq = qq;
                    }
                }
            }
        }
    }
}
