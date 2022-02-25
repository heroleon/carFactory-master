package com.leon.carfixfactory.ui.adapter.base;

/**
 * Created by lenovo on 2018/5/13.
 */

import android.content.Context;


import androidx.recyclerview.widget.RecyclerView;

import com.leon.carfixfactory.bean.ContactsInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public abstract class ContactsListAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    protected ArrayList<ContactsInfo> items = new ArrayList<>();

    protected Context mContext;

    public ContactsListAdapter() {
        setHasStableIds(true);
    }

    public void add(ContactsInfo object) {
        items.add(object);
        notifyDataSetChanged();
    }

    public void add(int index, ContactsInfo object) {
        items.add(index, object);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends ContactsInfo> collection) {
        if (collection != null) {
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(ContactsInfo... items) {
        addAll(Arrays.asList(items));
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(String object) {
        items.remove(object);
        notifyDataSetChanged();
    }

    public ContactsInfo getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
