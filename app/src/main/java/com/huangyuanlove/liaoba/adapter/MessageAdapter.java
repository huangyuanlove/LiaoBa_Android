package com.huangyuanlove.liaoba.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangyuanlove.liaoba.R;
import com.huangyuanlove.liaoba.entity.MessageBean;

import java.util.List;


/**
 * Created
 * Author: huangyuan_xuan
 * Date: 2015/10/16
 */
public class MessageAdapter extends BaseAdapter  {

    private int resourceID;
    private Context context;
    private List<MessageBean> datas;
    public MessageAdapter(Context context, int resource, List<MessageBean> objects) {
//        super(context, resource, objects);

        this.resourceID = resource;
        this.context = context;
        this.datas = objects;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        MessageBean msg = datas.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceID, null);
            viewHolder = new ViewHolder();
            viewHolder.leftLayout = (RelativeLayout) view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = (RelativeLayout) view.findViewById(R.id.right_layout);
            viewHolder.leftMessage = (TextView) view.findViewById(R.id.left_message);
            viewHolder.leftMessage.setTag(position);
            viewHolder.rightMessage = (TextView) view.findViewById(R.id.right_message);
            viewHolder.rightMessage.setTag(position);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (msg.getType() == MessageBean.MSG_TYPE_RECEIVED) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMessage.setText(msg.getContent());

        } else if (msg.getType() == MessageBean.MSG_TYPE_SENT) {
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.rightMessage.setText(msg.getContent());
        }

        return view;
    }



    class ViewHolder {
        RelativeLayout leftLayout;
        RelativeLayout rightLayout;
        TextView leftMessage;
        TextView rightMessage;
    }


}
