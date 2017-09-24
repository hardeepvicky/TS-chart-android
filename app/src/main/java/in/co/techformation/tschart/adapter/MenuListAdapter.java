package in.co.techformation.tschart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.techformation.android.Constant;
import in.co.techformation.tschart.R;
import in.co.techformation.tschart.collection.MenuEntity;
import in.co.techformation.tschart.collection.ReportEntity;

/**
 * Created by Tech on 19-Apr-17.
 */

public class MenuListAdapter extends BaseAdapter
{
    ArrayList<MenuEntity> list;
    Context context;

    public interface MenuListAdapterCallback{
        void onSelect(MenuEntity entity);
        void onSelect(ArrayList<ReportEntity> reportList);
    }

    MenuListAdapterCallback callBack;

    private static LayoutInflater inflater=null;

    public MenuListAdapter(Context context, ArrayList<MenuEntity> list, MenuListAdapterCallback callback)
    {
        this.context = context;
        this.list = list;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.callBack = callback;
    }

    public void update(ArrayList<MenuEntity> list)
    {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View rowView;

        rowView = inflater.inflate(R.layout.adapter_menu_list, null);

        TextView lblName = (TextView) rowView.findViewById(R.id.lblName);
        TextView lblChildCount = (TextView) rowView.findViewById(R.id.lblChildCount);

        final MenuEntity entity = list.get(position);

        lblName.setText(entity.getName());

        int c = 0;
        if (entity.children.size() > 0)
        {
            c = entity.children.size();
        }

        if (entity.reports.size() > 0)
        {
            c = entity.reports.size();
        }

        lblChildCount.setText("(" + c + ")");

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (entity.children.size() > 0)
                {
                    callBack.onSelect(entity);
                }
                else if (entity.reports.size() > 0)
                {
                    callBack.onSelect(entity.reports);
                }
            }
        });

        return rowView;
    }
}