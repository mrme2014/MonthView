package com.ishow.ischool.widget.pickerview;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.ishow.ischool.R;

import butterknife.OnClick;

/**
 * Created by MrS on 2016/8/22.
 */
public class PickDialogFragment extends DialogFragment {
  //  @BindView(R.id.cancel)
    TextView cancel;
  //  @BindView(R.id.title)
    TextView title;
    //@BindView(R.id.ok)
    TextView ok;
   // @BindView(R.id.picker)
    TimePicker picker;

    PickerWheelViewLinearlayout linearlayout;

    private Dialog dialog;

    private static final int PICK_TYPE_DATE = 0;

    private static final int PICK_TYPE_OTHERS = 1;

    private static  int PICK_TITLE = -1;

    private int PICK_TYPE = PICK_TYPE_DATE;

    private View contentView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            PICK_TYPE = bundle.getInt("PICK_TYPE");
            PICK_TITLE = bundle.getInt("PICK_TITLE");
        }
        if (PICK_TYPE == PICK_TYPE_DATE) {
            contentView = LayoutInflater.from(getContext()).inflate(R.layout.time_picker_linearlayout, null);
            picker = (TimePicker) contentView.findViewById(R.id.picker);
        } else if (PICK_TYPE == PICK_TYPE_OTHERS) {
            contentView = LayoutInflater.from(getContext()).inflate(R.layout.time_picker_controller, null);
            linearlayout = (PickerWheelViewLinearlayout) contentView.findViewById(R.id.PickerWheelViewLinearlayout);
        }

        cancel = (TextView) contentView.findViewById(R.id.cancel);
        title = (TextView) contentView.findViewById(R.id.title);
        ok = (TextView) contentView.findViewById(R.id.ok);
        if (PICK_TITLE!=-1)title.setText(getContext().getString(PICK_TITLE));

        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);

        return contentView;
    }

    @OnClick({R.id.cancel, R.id.title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                break;
            case R.id.title:
                break;
        }
    }
}
