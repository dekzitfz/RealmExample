package id.dekz.code.realmexample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.dekz.code.realmexample.R;
import id.dekz.code.realmexample.model.User;

/**
 * Created by DEKZ on 6/27/2016.
 */
public class ListUserAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<User> userlist;

    public ListUserAdapter(Context context, List<User> userlist) {
        this.context = context;
        this.userlist = userlist;
    }

    @Override
    public int getCount() {
        return userlist.size();
    }

    @Override
    public Object getItem(int position) {
        return userlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null){
            convertView = inflater.inflate(R.layout.row_list_user,null);
        }

        TextView fullName = (TextView) convertView.findViewById(R.id.tvUserFullName);
        TextView userName = (TextView) convertView.findViewById(R.id.tvUserName);

        User user = userlist.get(position);
        fullName.setText(user.getFullName());
        userName.setText(user.getUserName());

        return convertView;
    }
}
