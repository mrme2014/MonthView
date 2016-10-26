package com.ishow.ischool.business.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commonlib.widget.pullrecyclerview.PullRecyclerViewAdapter;
import com.commonlib.widget.pullrecyclerview.PullViewHolder;
import com.ishow.ischool.R;
import com.ishow.ischool.bean.university.Address;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abel on 16/10/25.
 */

public class AddrAdapter extends PullRecyclerViewAdapter<Address> {


    private Context context;
    private LayoutInflater inflater;

    public AddrAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public PullViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindDataViewHolder(PullViewHolder holder, int position) {
        if (holder instanceof AddressViewHolder) {
            AddressViewHolder addrHolder = (AddressViewHolder) holder;
            Address address = getData(position);
            addrHolder.textTv.setText(address.name);
        }
    }

    class AddressViewHolder extends PullViewHolder {

        @BindView(R.id.item_addr_text)
        TextView textTv;

        public AddressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
