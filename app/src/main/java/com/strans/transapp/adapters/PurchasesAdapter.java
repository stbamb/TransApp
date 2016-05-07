package com.strans.transapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.strans.transapp.R;
import com.strans.transapp.widget.AnimatedExpandableListView.AnimatedExpandableListAdapter;

public class PurchasesAdapter extends AnimatedExpandableListAdapter
{
    private LayoutInflater inflater;
    private List<GroupItem> items;

    public PurchasesAdapter(Context context)
    {
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<GroupItem> items) {
        this.items = items;
    }

    @Override
    public ChildItem getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).items.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent)
    {
        ChildHolder holder;
        ChildItem item = getChild(groupPosition, childPosition);
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = inflater.inflate(R.layout.purchase_adapter_expandable_list_subitem,
                    parent, false);
            holder.from = (TextView) convertView.findViewById(R.id.from);
            holder.to = (TextView) convertView.findViewById(R.id.to);
            holder.journeyDate = (TextView) convertView.findViewById(R.id.journey_date);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else
            holder = (ChildHolder) convertView.getTag();

        holder.from.setText(item.from);
        holder.to.setText(item.to);
        holder.journeyDate.setText(item.journeyDate);
        holder.time.setText(item.time);
        holder.price.setText(item.price);
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return items.get(groupPosition).items.size();
    }

    @Override
    public GroupItem getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHolder holder;
        GroupItem item = getGroup(groupPosition);
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = inflater.inflate(R.layout.purchase_adapter_expandable_list_item,
                    parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.textTitle);
            convertView.setTag(holder);
        } else
            holder = (GroupHolder) convertView.getTag();

        holder.title.setText(item.title);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    public static class GroupItem {
        public String title;
        public List<ChildItem> items = new ArrayList<>();
    }

    public static class ChildItem {
        public String from;
        public String to;
        public String journeyDate;
        public String time;
        public String price;
    }

    private static class ChildHolder {
        TextView from;
        TextView to;
        TextView journeyDate;
        TextView time;
        TextView price;
    }

    private static class GroupHolder {
        TextView title;
    }
}
