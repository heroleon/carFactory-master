package com.leon.carfixfactory.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.leon.carfixfactory.R;
import com.leon.carfixfactory.ui.activity.AddWorkerActivity;
import com.leon.carfixfactory.ui.adapter.base.ContactsListAdapter;
import com.thinkcool.circletextimageview.CircleTextImageView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContactsListWithHeadersAdapter extends ContactsListAdapter<RecyclerView.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>, Filterable {
    private List<Map<String, String>> values = null;
    private MyFilter mFilter;

    public ContactsListWithHeadersAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contacts_item, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        View itemView = holder.itemView;
        TextView mName = itemView.findViewById(R.id.mName);
        CircleTextImageView mUserPhoto = itemView.findViewById(R.id.mUserPhoto);
        LinearLayout mBottomLayout = itemView.findViewById(R.id.mBottomLayout);
        //这里实现主界面点击联系人
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long id = getItem(position).getId();
                Intent intent = new Intent(mContext, AddWorkerActivity.class);
                intent.putExtra("id", id);
                mContext.startActivity(intent);

            }
        });
        if (position < items.size() - 1) {
            if (getItem(position).getSortKey().equals(getItem(position + 1).getSortKey())) {
                mBottomLayout.setVisibility(View.GONE);
            } else {
                mBottomLayout.setVisibility(View.VISIBLE);
            }
        } else {
            mBottomLayout.setVisibility(View.GONE);
        }
        String name = getItem(position).getName();
        try {
            mName.setText(name);
            if (name.substring(name.length() - 1).equals("(") ||
                    name.substring(name.length() - 1).equals(")") ||
                    name.substring(name.length() - 1).equals("[") ||
                    name.substring(name.length() - 1).equals("]") ||
                    name.substring(name.length() - 1).equals("（") ||
                    name.substring(name.length() - 1).equals("）") ||
                    name.substring(name.length() - 1).equals("【") ||
                    name.substring(name.length() - 1).equals("】")) {
                mUserPhoto.setText(name.substring(name.length() - 2, name.length() - 1));
            } else {
                mUserPhoto.setText(name.substring(name.length() - 1));
            }
        } catch (Exception e) {
            mName.setText(R.string.unknown_name);
            mName.setTextColor(mContext.getResources().getColor(R.color.gray));
            mUserPhoto.setText(R.string.unknown_contact_icon);
        }
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getSortKey().charAt(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contacts_head, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        View itemView = holder.itemView;

        TextView mHead = itemView.findViewById(R.id.mHead);
        mHead.setText(String.valueOf(getItem(position).getSortKey()));

//            holder.itemView.setBackgroundColor(getRandomColor());
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    private class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (prefix == null || prefix.length() == 0) {
                List<Map<String, String>> list1 = new ArrayList<>(values);
                results.values = list1;
                results.count = list1.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();
                List<Map<String, String>> values1 = values;
                int count = values1.size();
                List<Map<String, String>> newValues = new ArrayList<>(count);
                for (Map<String, String> value : values1) {
                    String title = value.get("title").toLowerCase();
                    if (title.contains(prefixString)) {
                        newValues.add(value);
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        }
    }
}
