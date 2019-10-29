package vn.zuni.pickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import vn.zuni.pickerview.R;

import vn.zuni.pickerview.view.BasePickerView;
import vn.zuni.pickerview.view.MyWheelOptions;

import java.util.List;

public class MyOptionsPickerView<T> extends BasePickerView implements View.OnClickListener {
    MyWheelOptions wheelOptions;
    private View btnSubmit, btnCancel;
    private TextView tvTitle;
    private OnOptionsSelectListener optionsSelectListener;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";

    public MyOptionsPickerView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.pickerview_options, contentContainer);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        final View optionspicker = findViewById(R.id.optionspicker);
        wheelOptions = new MyWheelOptions(optionspicker);
    }

    public void setPicker(List<T> optionsItems) {
        wheelOptions.setPicker(optionsItems, null, false);
    }

    public void setPicker(List<T> options1Items,
                          List<T> options2Items,
                          boolean linkage) {
        wheelOptions.setPicker(options1Items, options2Items,
                linkage);
    }

    public void setSelectOptions(int option1) {
        wheelOptions.setCurrentItems(option1, 0);
    }

    public void setSelectOptions(int option1, int option2) {
        wheelOptions.setCurrentItems(option1, option2);
    }

    public void setLabels(String label1) {
        wheelOptions.setLabels(label1, null);
    }
    public void setLabels(String label1, String label2) {
        wheelOptions.setLabels(label1, label2);
    }

    public void setCyclic(boolean cyclic) {
        wheelOptions.setCyclic(cyclic);
    }

    public void setCyclic(boolean cyclic1, boolean cyclic2) {
        wheelOptions.setCyclic(cyclic1, cyclic2);
    }


    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
        } else {
            if (optionsSelectListener != null) {
                int[] optionsCurrentItems = wheelOptions.getCurrentItems();
                optionsSelectListener.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1]);
            }
            dismiss();
        }
    }

    public interface OnOptionsSelectListener {
        void onOptionsSelect(int options1, int option2);
    }

    public void setOnoptionsSelectListener(
            OnOptionsSelectListener optionsSelectListener) {
        this.optionsSelectListener = optionsSelectListener;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }
}

