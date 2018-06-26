package com.sunmi.doublescreen.doublescreenapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sunmi.doublescreen.doublescreenapp.R;

/**
 * 类名称：连接打印机
 * 类描述：
 * 创建人：徐翔
 * 修改人：
 */
public class PrintDialogFragment extends AppCompatDialogFragment implements View.OnClickListener {

    boolean isShow = false;//防多次点击
    private Button btnAdd, btnCancel;
    private TextView tv_booltooth;
    private int drinkItemIndex;
    private Activity activity;

    public PrintDialogFragment() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_more, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAction();
        initData();
    }

    private void initView(View view) {
//        btnAdd = (Button) view.findViewById(R.id.btn_add);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        tv_booltooth = (TextView) view.findViewById(R.id.tv_booltooth);


    }

    private void initAction() {
//        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        tv_booltooth.setOnClickListener(this);
//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                return true;
//            }
//        });
    }

    private void initData() {
//        Bundle bundle = getArguments();
//        if (null != bundle) {
//            drinkItemIndex =  bundle.getInt("item",-1);
////            Log.d("TAG", "initData: ------------>" + (ivLogo == null));
////            ivLogo.setImageResource(R.drawable.apple_dialog);
//        }
    }

    @Override
    public void onClick(View v) {

        if (listener != null) {

            listener.onAction(v.getId());
        }

        dismiss();


    }


    @Override
    public void show(FragmentManager manager, String tag) {
        if (isShow) {
            return;
        }
        super.show(manager, tag);
        isShow = true;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        isShow = false;
    }

    private ActionListener listener = null;

    public void setListener(ActionListener listener) {
        this.listener = listener;
    }

    public interface ActionListener {
        void onAction(int index);
    }
}
