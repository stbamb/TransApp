package com.strans.transapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.strans.transapp.R;
import com.strans.transapp.activities.ResultsActivity;
import com.strans.transapp.dao.concrete.PurchaseDAO;
import com.strans.transapp.model.City;
import com.strans.transapp.widget.AnimatedExpandableListView.AnimatedExpandableListAdapter;

public class JourneyResultAdapter extends AnimatedExpandableListAdapter
{
    private LayoutInflater inflater;
    private List<GroupItem> items;
    private Context context;
    private ArrayList<City> cities;
    private String userID;
    private ResultsActivity resultsActivity;

    public JourneyResultAdapter(Context context, ArrayList<City> cities, String userID,
                                ResultsActivity resultsActivity)
    {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.cities = cities;
        this.userID = userID;
        this.resultsActivity = resultsActivity;
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
            convertView = inflater.inflate(R.layout.journey_result_adapter_expandable_list_subitem,
                    parent, false);
            holder.from = (TextView) convertView.findViewById(R.id.from);
            holder.to = (TextView) convertView.findViewById(R.id.to);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        } else
            holder = (ChildHolder) convertView.getTag();

        holder.from.setText(item.from);
        holder.to.setText(item.to);
        holder.date.setText(item.date);
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
            convertView = inflater.inflate(R.layout.journey_result_adapter_expandable_list_item,
                    parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.textTitle);
            convertView.setTag(holder);
        } else
            holder = (GroupHolder) convertView.getTag();

        holder.title.setText(item.title);
        Button purchaseButton = (Button) convertView.findViewById(R.id.purchase_button);

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPurchaseDialog(groupPosition);
            }
        });

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
        public String date;
        public String time;
        public String price;
    }

    private static class ChildHolder {
        TextView from;
        TextView to;
        TextView date;
        TextView time;
        TextView price;
    }

    private static class GroupHolder {
        TextView title;
    }

    /**
     * This function is called once the 'Purchase' button has been pressed.
     *
     * @param pos   The position of the element that the user wants to purchase.
     */
    private void openPurchaseDialog(final int pos)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Confirm purchase");
        alertDialog.setMessage("Do you really want to purchase a ticket for this journey?");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Continue",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                purchaseJourney(pos);
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    /**
     * Once the 'Purchase' button has been pressed this function executes.
     * It saves the data to the database.
     *
     * @param pos   The position of the element that the user wants to purchase.
     */
    private void purchaseJourney(int pos)
    {
        GroupItem item = items.get(pos);
        ChildItem childItem = item.items.get(0);
        String date = childItem.date;
        String from = childItem.from;
        String to = childItem.to;
        String time = childItem.time;
        time = time.substring(6);
        String stringPrice = childItem.price;
        date = date.substring(6);
        from = from.substring(6);
        to = to.substring(4);
        stringPrice = stringPrice.substring(7, 9);
        int price = Integer.parseInt(stringPrice);
        PurchaseDAO purchaseDAO = new PurchaseDAO(cities, date, from, to, time,
                userID, price);
        purchaseDAO.execute();
        resultsActivity.openSuccessDialog();
    }
}
