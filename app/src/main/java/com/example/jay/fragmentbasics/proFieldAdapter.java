package com.example.jay.fragmentbasics;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by armorsun on 2015/8/2.
 */
public class proFieldAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LinkedHashMap<String,List<String>> proFieldHashmap;
    private List<String> proFieldDetail;            // store the professional field titles and subtitles
    private  List<String> childSelected =new ArrayList<String>();


    private ArrayList<String> selectedItems=new ArrayList<String>();    //an arraylist stores what you select
    private ArrayAdapter slctItemsAdapter;                              //an adapter links data and listview

    //Constructor
    public  proFieldAdapter(Context context,LinkedHashMap<String,List<String>> hashMap,List<String> list){
        this.context=context;
        this.proFieldHashmap=hashMap;
        this.proFieldDetail=list;
    }

    @Override
    public int getGroupCount() {
        return proFieldDetail.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return proFieldHashmap.get(proFieldDetail.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return proFieldDetail.get(groupPosition);
    }

    @Override
    public Object getChild(int parent, int child) {
        return proFieldHashmap.get(proFieldDetail.get(parent)).get(child);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int parent, int child) {
        return child;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //gets a view to display the given group (our professional field tittle)
    @Override
    public View getGroupView(int parent, boolean isExpanded, View convertView, ViewGroup parentView) {

        String groupTitle =(String)getGroup(parent);
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.pro_field_list_parent,parentView,false);
        }
        TextView parentTextView=(TextView)convertView.findViewById(R.id.textView_parent);
        parentTextView.setTypeface(null, Typeface.BOLD);
        parentTextView.setText(groupTitle);
        return convertView;
    }

    //gets a view to display the given child (our professional field subtitle) within the given group (our professional fielf title)
    @Override
    public View getChildView(final int parent, final int child, boolean isLastChild, View convertView, ViewGroup parentView) {
        String childTitle =(String)getChild(parent, child);
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.pro_field_list_child,parentView,false);
        }

        final CheckedTextView chkTextView=(CheckedTextView)convertView.findViewById(R.id.textView_child);
        chkTextView.setText(childTitle);
        chkTextView.setChecked(childSelected.contains(proFieldHashmap.get(proFieldDetail.get(parent)).get(child)));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean setChildClick(int parent,int child){
        if (childSelected.contains(proFieldHashmap.get(proFieldDetail.get(parent)).get(child))) {
            childSelected.remove(proFieldHashmap.get(proFieldDetail.get(parent)).get(child));
            return false;
        } else {
            childSelected.add(proFieldHashmap.get(proFieldDetail.get(parent)).get(child));
            return true;
        }
    }
}
