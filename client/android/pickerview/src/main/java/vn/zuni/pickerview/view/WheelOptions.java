package vn.zuni.pickerview.view;

import java.util.ArrayList;
import android.view.View;

import vn.zuni.pickerview.R;

import vn.zuni.pickerview.adapter.ArrayWheelAdapter;
import vn.zuni.pickerview.lib.WheelView;
import vn.zuni.pickerview.listener.OnItemSelectedListener;

public class WheelOptions<T> {
	private View view;
	private WheelView wv_option1;
	private WheelView wv_option2;

	private ArrayList<T> mOptions1Items;
	private ArrayList<ArrayList<T>> mOptions2Items;

    private boolean linkage = false;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public WheelOptions(View view) {
		super();
		this.view = view;
		setView(view);
	}

	public void setPicker(ArrayList<T> optionsItems) {
		setPicker(optionsItems, null, false);
	}


	public void setPicker(ArrayList<T> options1Items,
			ArrayList<ArrayList<T>> options2Items,
			boolean linkage) {
        this.linkage = linkage;
		this.mOptions1Items = options1Items;
		this.mOptions2Items = options2Items;
		int len = ArrayWheelAdapter.DEFAULT_LENGTH;
		if (this.mOptions2Items == null)
			len = 12;
		wv_option1 = view.findViewById(R.id.options1);
		wv_option1.setAdapter(new ArrayWheelAdapter<>(mOptions1Items, len));
		wv_option1.setCurrentItem(0);
		wv_option2 =  view.findViewById(R.id.options2);
		if (mOptions2Items != null)
			wv_option2.setAdapter(new ArrayWheelAdapter<>(mOptions2Items.get(0)));
		wv_option2.setCurrentItem(wv_option1.getCurrentItem());

		if (this.mOptions2Items == null)
			wv_option2.setVisibility(View.GONE);

		OnItemSelectedListener wheelListener_option1 = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				int opt2Select = 0;
				if (mOptions2Items != null) {
                    opt2Select = wv_option2.getCurrentItem();
                    opt2Select = opt2Select >= mOptions2Items.get(index).size() - 1 ? mOptions2Items.get(index).size() - 1 : opt2Select;
					wv_option2.setAdapter(new ArrayWheelAdapter<>(mOptions2Items
							.get(index)));
					wv_option2.setCurrentItem(opt2Select);
				}
			}
		};

		if (options2Items != null && linkage)
			wv_option1.setOnItemSelectedListener(wheelListener_option1);
	}

	public void setLabels(String label1, String label2) {
		if (label1 != null)
			wv_option1.setLabel(label1);
		if (label2 != null)
			wv_option2.setLabel(label2);
	}

	public void setCyclic(boolean cyclic) {
		wv_option1.setCyclic(cyclic);
		wv_option2.setCyclic(cyclic);
	}

	public void setCyclic(boolean cyclic1,boolean cyclic2) {
        wv_option1.setCyclic(cyclic1);
        wv_option2.setCyclic(cyclic2);
	}
    public void setOption2Cyclic(boolean cyclic) {
        wv_option2.setCyclic(cyclic);
    }

	public int[] getCurrentItems() {
		int[] currentItems = new int[2];
		currentItems[0] = wv_option1.getCurrentItem();
		currentItems[1] = wv_option2.getCurrentItem();
		return currentItems;
	}

	public void setCurrentItems(int option1, int option2) {
        if(linkage){
            itemSelected(option1, option2);
        }
        wv_option1.setCurrentItem(option1);
        wv_option2.setCurrentItem(option2);
	}

	private void itemSelected(int opt1Select, int opt2Select) {
		if (mOptions2Items != null) {
			wv_option2.setAdapter(new ArrayWheelAdapter<>(mOptions2Items
					.get(opt1Select)));
			wv_option2.setCurrentItem(opt2Select);
		}
	}


}
