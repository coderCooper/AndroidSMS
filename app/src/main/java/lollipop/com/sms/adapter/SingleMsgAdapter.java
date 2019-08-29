package lollipop.com.sms.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lollipop.com.sms.R;

/**
 * Created by lollipop on 2018/11/6.
 */

public class SingleMsgAdapter extends BaseAdapter {

    private Context mCon;
    private ArrayList<SmsModel> smsBeans;

    public SingleMsgAdapter(Context mCon, ArrayList<SmsModel> smsBeans){
        this.mCon = mCon;
        this.smsBeans = smsBeans;
    }

    @Override
    public int getCount() {
        if (smsBeans != null){
            return smsBeans.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return smsBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView leftcontent;       // 详细内容
        TextView rightcontent;       // 详细内容
        TextView time;       // 详细内容
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCon).inflate(R.layout.item_single, null);
            holder = new ViewHolder();
            holder.leftcontent = ((TextView) convertView.findViewById(R.id.tv01));
            holder.rightcontent = ((TextView) convertView.findViewById(R.id.tv03));
            holder.time = ((TextView) convertView.findViewById(R.id.tv02));
            convertView.setTag(holder);
        } else {
            holder = ((ViewHolder) convertView.getTag());
        }
        final SmsModel bean = smsBeans.get(position);

        if (bean.isSend()){
            holder.rightcontent.setText(bean.getContent());
            holder.rightcontent.setVisibility(View.VISIBLE);
            holder.leftcontent.setVisibility(View.GONE);
        } else {
            holder.leftcontent.setText(bean.getContent());
            holder.leftcontent.setVisibility(View.VISIBLE);
            holder.rightcontent.setVisibility(View.GONE);
        }

        holder.time.setText(bean.getTime());

        return convertView;
    }
}
