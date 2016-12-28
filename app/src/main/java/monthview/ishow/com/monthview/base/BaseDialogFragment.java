package monthview.ishow.com.monthview.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import monthview.ishow.com.monthview.R;


/**
 * Created by MrS on 2016/12/14.
 */

public abstract class BaseDialogFragment extends DialogFragment {
    protected View rootView;
    protected Dialog dialog;
    @Override
    public Dialog getDialog() {
        return dialog;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getContext(), getDialogTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        rootView = LayoutInflater.from(getContext()).inflate(getDialogLayoutId(), null);
        dialog.setContentView(rootView);
        Window window = dialog.getWindow();
        window.setLayout(-1, -2);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = getGravity();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        onDialogCreate();
        return dialog;
    }

    protected abstract int getDialogTheme();

    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    protected abstract int getDialogLayoutId();

    protected abstract void onDialogCreate();


    public interface Theme {
        int SLIDE_FROM_TO_TOP_WINANIM = R.style.Comm_dialogfragment_windowAnimationStyle;//从上滑入  滑出式的窗口动画
        int SLIDE_FROM_TO_TOP_THEME = R.style.Comm_dialogfragment;//从上滑入  滑出式的dialog theme

        int SLIDE_FROM_TO_BOTTOM_THEME = R.style.Select_dialog;//底部弹出  弹出式为上滑，消失时为下滑ialog theme
        int SLIDE_FROM_TO_BOTTOM_WINANIM = R.style.Select_dialog_windowAnimationStyle;//底部弹出  弹出式为上滑，消失时为下滑窗口动画

        int SLIDE_FROM_TO_LEFT_THEME = R.style.slide_from_to_left_theme;
        int SLIDE_FROM_TO_LEFT_TWINANIM = R.style.slide_from_to_left_animation;
    }
}
