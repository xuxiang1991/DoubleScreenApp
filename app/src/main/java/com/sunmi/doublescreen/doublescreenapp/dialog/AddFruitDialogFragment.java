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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.sunmi.doublescreen.doublescreenapp.R;
import com.sunmi.doublescreen.doublescreenapp.bean.ProductList;

import java.text.DecimalFormat;

/**
 * Created by highsixty on 2018/3/15.
 * mail  gaolulin@sunmi.com
 */

public class AddFruitDialogFragment extends AppCompatDialogFragment implements View.OnClickListener {

    private Button btnCancel;
    private Button btnAdd;
    private TextView tvDes;
    private EditText tvTotal;
    private ImageView ivLogo;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private float price;
    private String name;
    private String total = "0.00";//总价
    boolean isShow = false;//防多次点击
    static int defaultNet = -10;

    private ProductList.ProductsBean bean;

    private RadioGroup rg_hot, rg_box;

    private int hotType = 2;//0 热饮  1冷饮 2 常温
    private int boxType = 1;//0 小  1中 2 大
    private Button bt_maines, bt_plus;

    public AddFruitDialogFragment() {
        super();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        tvTotal.setText("1");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.addfruit_dialog_layout, container, false);
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
        tvDes = (TextView) view.findViewById(R.id.tv_des);
        tvTotal = (EditText) view.findViewById(R.id.tv_total);
        rg_box = (RadioGroup) view.findViewById(R.id.rg_box);
        rg_hot = (RadioGroup) view.findViewById(R.id.rg_hot);
        bt_maines = (Button) view.findViewById(R.id.bt_maines);
        bt_plus = (Button) view.findViewById(R.id.bt_plus);

//        ivLogo = (ImageView) view.findViewById(R.id.iv_logo);


        bt_maines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tvTotal.getText().toString());
                count = count - 1;
                if (count < 1) {
                    return;
                } else {
                    tvTotal.setText(Integer.toString(count));
                }

            }
        });

        bt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tvTotal.getText().toString());
                count = count + 1;
                if (count > 999) {
                    return;
                } else {
                    tvTotal.setText(Integer.toString(count));
                }
            }
        });

        rg_hot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_hot:
                        hotType = 0;
                        break;
                    case R.id.rb_ice:
                        hotType = 1;
                        break;
                    case R.id.rb_nomal:
                        hotType = 2;
                        break;
                }
            }
        });


        rg_box.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_small:
                        boxType = 0;
                        break;
                    case R.id.rb_middle:
                        boxType = 1;
                        break;
                    case R.id.rb_large:
                        boxType = 2;
                        break;
                }
            }
        });

        tvTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String str = s.toString();
                if (str.length() == 0) {
                    tvTotal.setText("1");
                } else if (str.length() > 1 && str.substring(0, 1).equals("0")) {
                    tvTotal.setText(str.substring(1, str.length()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    /**
     * 显示温度
     *
     * @param hotType
     */
    private void shoHotType(int hotType) {
        switch (hotType) {
            case 0:
                ((RadioButton) getDialog().findViewById(R.id.rb_hot)).setChecked(true);
                break;
            case 1:
                ((RadioButton) getDialog().findViewById(R.id.rb_ice)).setChecked(true);
                break;
            case 2:
                ((RadioButton) getDialog().findViewById(R.id.rb_nomal)).setChecked(true);
                break;
        }

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
            bean = (ProductList.ProductsBean) bundle.getSerializable("item");
            name = bean.getName();
            tvDes.setText(bean.getUid() + "  " + bean.getName() + " ¥" + bean.getSellPrice());
            shoHotType(bean.getHotType());
            tvTotal.setText("1");
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
                    bean.setCount(Integer.parseInt(tvTotal.getText().toString()));
                    bean.setHotType(hotType);
                    bean.setBoxType(boxType);
                    listener.onAddResult(bean);
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

    private AddListener listener = null;

    public void setListener(AddListener listener) {
        this.listener = listener;
    }

    public interface AddListener {
        void onAddResult(ProductList.ProductsBean bean);
    }
}
