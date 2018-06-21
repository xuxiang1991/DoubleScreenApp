package com.sunmi.doublescreen.doublescreenapp.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sunmi.doublescreen.doublescreenapp.R;

/**
 * 类名称：删除列表
 * 类描述：
 * 创建人：xuxiang
 * 修改人：
 */
public class ItemDeleteDialogFragment  extends AppCompatDialogFragment implements View.OnClickListener {

    boolean isShow = false;//防多次点击
    private Button btnAdd,btnCancel;
    private int drinkItemIndex;

    public ItemDeleteDialogFragment() {
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
        return inflater.inflate(R.layout.dialog_delete_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAction();
        initData();
    }

    private void initView(View view) {
        btnAdd = (Button) view.findViewById(R.id.btn_add);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);






    }

    private void initAction() {
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                return true;
//            }
//        });
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            drinkItemIndex =  bundle.getInt("item",-1);
//            Log.d("TAG", "initData: ------------>" + (ivLogo == null));
//            ivLogo.setImageResource(R.drawable.apple_dialog);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_add:

                if (listener != null) {

                    listener.onDelete(drinkItemIndex);
                }
                dismiss();
                break;
            default:
                break;
        }
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

    private DeleteListener listener = null;

    public void setListener(DeleteListener listener) {
        this.listener = listener;
    }

    public interface DeleteListener {
        void onDelete(int index);
    }
}
