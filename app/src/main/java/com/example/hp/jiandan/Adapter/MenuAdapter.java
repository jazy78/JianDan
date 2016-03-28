package com.example.hp.jiandan.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hp.jiandan.MainActivity;
import com.example.hp.jiandan.R;
import com.example.hp.jiandan.model.MenuItem;

import java.util.ArrayList;

/**
 * Created by hp on 2016/3/8.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private ArrayList<MenuItem> menuItems;
    private MainActivity mainActivity;
    private MenuItem.FragmentType currentFragmentType= MenuItem.FragmentType.FreshNews;

    public MenuAdapter(ArrayList<MenuItem> list, MainActivity mainActivity) {
        menuItems = list;
       this.mainActivity=mainActivity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MenuItem menuItem = menuItems.get(position);
        holder.textView.setText(menuItem.getTitle());
        holder.imageView.setImageResource(menuItem.getResourceId());
        holder.rl_containter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentFragmentType!=menuItem.getType()){
                    try {
                        //通过通配符定义的参数 获取实例
                        Fragment fragment=(Fragment)Class.forName(menuItem.getFragment().getName()).newInstance() ;
                        mainActivity.replaceFragment(R.id.frame_container,fragment);
                        currentFragmentType=menuItem.getType();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                  mainActivity.closeDrawer();
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuItems==null?0:menuItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        private RelativeLayout rl_containter;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_menu);
            textView = (TextView) itemView.findViewById(R.id.tv_title);
            rl_containter = (RelativeLayout) itemView.findViewById(R.id.rl_container);
        }
    }

}
