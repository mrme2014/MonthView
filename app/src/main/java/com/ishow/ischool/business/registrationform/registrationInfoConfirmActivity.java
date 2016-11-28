package com.ishow.ischool.business.registrationform;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.commonlib.util.DateUtil;
import com.commonlib.util.PermissionUtil;
import com.commonlib.util.StorageUtil;
import com.commonlib.widget.LabelTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.registrationform.RegistraInfo;
import com.ishow.ischool.bean.registrationform.RegistraResult;
import com.ishow.ischool.bean.student.StudentInfo;
import com.ishow.ischool.common.base.BaseActivity4Crm;
import com.ishow.ischool.util.AppUtil;
import com.ishow.ischool.widget.custom.RegistraTableRowTextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by MrS on 2016/11/24.
 */

public class registrationInfoConfirmActivity extends BaseActivity4Crm<regisPresenter, regisModel> implements regisView {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    LabelTextView name;
    @BindView(R.id.english_name)
    LabelTextView englishName;
    @BindView(R.id.phone)
    LabelTextView phone;
    @BindView(R.id.identy)
    LabelTextView identy;
    @BindView(R.id.course_type)
    LabelTextView courseType;
    @BindView(R.id.date_want)
    LabelTextView dateWant;
    @BindView(R.id.registra_left1)
    LabelTextView registraLeft1;
    @BindView(R.id.registra_left2)
    LabelTextView registraLeft2;
    @BindView(R.id.registra_left3)
    LabelTextView registraLeft3;
    @BindView(R.id.registra_row1)
    RegistraTableRowTextView registraRow1;
    @BindView(R.id.registra_row2)
    RegistraTableRowTextView registraRow2;
    @BindView(R.id.registra_row3)
    RegistraTableRowTextView registraRow3;
    @BindView(R.id.registra_row4)
    RegistraTableRowTextView registraRow4;
    @BindView(R.id.cheap_type)
    LabelTextView cheapType;
    @BindView(R.id.pay_type)
    LabelTextView payType;
    @BindView(R.id.pay_money)
    LabelTextView payMoney;
    @BindView(R.id.pay_real)
    LabelTextView payReal;
    @BindView(R.id.pay_recept_no)
    LabelTextView payReceptNo;
    @BindView(R.id.pay_date)
    LabelTextView payDate;
    @BindView(R.id.sec_pay_date)
    LabelTextView secPayDate;
    @BindView(R.id.pay_memo)
    LabelTextView payMemo;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    public static final String STUDENT_ID = "student_id";
    public static final String STUDENT_STATUS = "student_status";
    public static final String REQUEST_CODE = "request_code";

    private int student_id;
    private int student_status;
    private String action = "pay";
    private String feilds = "payListInfo,studentInfo";

    @Override
    protected void initEnv() {
        super.initEnv();
        student_id = getIntent().getIntExtra(STUDENT_ID, student_id);
        student_status = getIntent().getIntExtra(STUDENT_STATUS, student_status);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_registration_info_sure, R.string.registration_apply_sure_title, R.menu.menu_registaration_save, MODE_BACK);
    }

    @Override
    protected void setUpView() {
     /*   if (student_status == 1) {
            action = "apply";
        } else if (student_status == 2) {
            action = "pay";
        }*/
        mPresenter.getStudentApplyInfo(student_id, student_status, action, feilds);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        PermissionUtil.getInstance().checkPermission(this, new PermissionUtil.PermissionChecker() {
            @Override
            public void onGrant(String grantPermission, int index) {
                getBitmapByView();
            }

            @Override
            public void onDenied(String deniedPermission, int index) {
                showToast(getString(R.string.permisson_quanxian_storage));
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return super.onMenuItemClick(item);
    }

    @Override
    protected void setUpData() {

    }


    @Override
    public void getRegistraInfo(RegistraResult registraResult) {
        if (registraResult != null) {
            List<List<Integer>> free_time_arr = registraResult.free_time_arr;
            setUpFreeTimeTable(free_time_arr);
            StudentInfo studentInfo = registraResult.studentInfo;
            List<RegistraInfo> payListInfo = registraResult.payListInfo;
            //  double totalRealMoney = 0;
            if (payListInfo != null && payListInfo.size() > 0) {
                RegistraInfo registraInfo = payListInfo.get(payListInfo.size() - 1);
                //       totalRealMoney += registraInfo.payed;
                setUpRegistrationInfoFirst(registraInfo);
            }
          /*  if (payListInfo != null && payListInfo.size() > 0) {
                setUpRegistrationInfoFirst(payListInfo.get(payListInfo.size()), totalRealMoney);
            }*/

            setUpStudentInfo(studentInfo);
        }
    }

    private void setUpFreeTimeTable(List<List<Integer>> free_time_arr) {
        registraRow1.setTxtList(AppUtil.getWeek());
        registraRow2.setIconList((free_time_arr == null || free_time_arr.size() == 0) ? null : free_time_arr.get(0));
        registraRow3.setIconList((free_time_arr == null || free_time_arr.size() == 0) ? null : free_time_arr.get(free_time_arr.size() > 1 ? 1 : 0));
        registraRow4.setIconList((free_time_arr == null || free_time_arr.size() == 0) ? null : free_time_arr.get(free_time_arr.size() > 2 ? 2 : 1));
    }

    private void setUpRegistrationInfoFirst(RegistraInfo registraInfo) {
        String pay_info = registraInfo.pay_info;
        Type type1 = new TypeToken<List<PayType>>() {
        }.getType();
        Gson gson = new Gson();
        List<PayType> typeList = gson.fromJson(pay_info, type1);
        if (typeList != null) {
            payMoney.setText(registraInfo.arrearage + "元");
            double payMoney = 0;
            for (int i = 0; i < typeList.size(); i++) {
                payType.append(typeList.get(i).method + "  ");
                payMoney += Double.valueOf(typeList.get(i).balance);
            }
            payReal.setText(payMoney + "元");
        }

        cheapType.setText(registraInfo.preferential_course_name);

        payMemo.setText(registraInfo.memo + "");
        payDate.setText(registraInfo.pay_time != 0 ? DateUtil.parseSecond2Str(Long.valueOf(registraInfo.pay_time)) : "- -");
        if (registraInfo.arrearage_time != 0)
            secPayDate.setText(DateUtil.parseSecond2Str(Long.valueOf(registraInfo.arrearage_time)));
        payReceptNo.setText(registraInfo.receipt_no);

    }

    private void setUpStudentInfo(StudentInfo studentInfo) {
        name.setText(studentInfo.name);
        englishName.setText(studentInfo.english_name);
        phone.setText(studentInfo.mobile);
        identy.setText(studentInfo.idcard);
        courseType.setText(studentInfo.intention_class_name);
        dateWant.setText(studentInfo.intention_time != 0 ? DateUtil.parseSecond2Str(Long.valueOf(studentInfo.intention_time)) : "- -");
    }

    @Override
    public void getRegistraError(String error) {
        showToast(error);
    }

    @Override
    public void payActionSucess(String info) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.getInstance().notifyPermissionsChange(this, permissions, grantResults);
    }

    /**
     * 截取scrollview的屏幕
     *
     * @return
     */
    public void getBitmapByView() {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        savePic(bitmap);
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static void compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 10;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);

    }

    /**
     * 保存到sdcard
     *
     * @param b
     * @return
     */
    public void savePic(Bitmap b) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        File dir = StorageUtil.getTempDir();
        File outfile = new File(dir.getPath(), sdf.format(new Date()) + ".png");
        //  String fname = outfile + "/" + sdf.format(new Date()) + ".png";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outfile);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (outfile.exists()) {
            showToast(R.string.save_pic_complete);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(outfile);
            intent.setData(uri);
            sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片
        } else showToast(R.string.save_pic_faild);
    }
}
