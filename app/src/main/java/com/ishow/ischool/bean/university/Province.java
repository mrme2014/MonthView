package com.ishow.ischool.bean.university;

import java.util.List;

/**
 * Created by MrS on 2016/7/21.
 */
public class Province {


    /**
     * error_no : 0
     * error_msg : 操作成功
     * result : [{"id":2,"parent_id":1,"name":"北京","type":1},{"id":3,"parent_id":1,"name":"安徽","type":1},{"id":4,"parent_id":1,"name":"福建","type":1},{"id":5,"parent_id":1,"name":"甘肃","type":1},{"id":6,"parent_id":1,"name":"广东","type":1},{"id":7,"parent_id":1,"name":"广西","type":1},{"id":8,"parent_id":1,"name":"贵州","type":1},{"id":9,"parent_id":1,"name":"海南","type":1},{"id":10,"parent_id":1,"name":"河北","type":1},{"id":11,"parent_id":1,"name":"河南","type":1},{"id":12,"parent_id":1,"name":"黑龙江","type":1},{"id":13,"parent_id":1,"name":"湖北","type":1},{"id":14,"parent_id":1,"name":"湖南","type":1},{"id":15,"parent_id":1,"name":"吉林","type":1},{"id":16,"parent_id":1,"name":"江苏","type":1},{"id":17,"parent_id":1,"name":"江西","type":1},{"id":18,"parent_id":1,"name":"辽宁","type":1},{"id":19,"parent_id":1,"name":"内蒙古","type":1},{"id":20,"parent_id":1,"name":"宁夏","type":1},{"id":21,"parent_id":1,"name":"青海","type":1},{"id":22,"parent_id":1,"name":"山东","type":1},{"id":23,"parent_id":1,"name":"山西","type":1},{"id":24,"parent_id":1,"name":"陕西","type":1},{"id":25,"parent_id":1,"name":"上海","type":1},{"id":26,"parent_id":1,"name":"四川","type":1},{"id":27,"parent_id":1,"name":"天津","type":1},{"id":28,"parent_id":1,"name":"西藏","type":1},{"id":29,"parent_id":1,"name":"新疆","type":1},{"id":30,"parent_id":1,"name":"云南","type":1},{"id":31,"parent_id":1,"name":"浙江","type":1},{"id":32,"parent_id":1,"name":"重庆","type":1},{"id":33,"parent_id":1,"name":"香港","type":1},{"id":34,"parent_id":1,"name":"澳门","type":1},{"id":35,"parent_id":1,"name":"台湾","type":1}]
     */

    private int error_no;
    private String error_msg;
    /**
     * id : 2
     * parent_id : 1
     * name : 北京
     * type : 1
     */

    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private int id;

        @Override
        public String toString() {
            return "ResultBean{" +
                    "id=" + id +
                    ", parent_id=" + parent_id +
                    ", name='" + name + '\'' +
                    ", type=" + type +
                    '}';
        }

        private int parent_id;
        private String name;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
