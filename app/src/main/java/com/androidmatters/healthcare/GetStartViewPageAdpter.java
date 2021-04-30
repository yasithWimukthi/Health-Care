package com.androidmatters.healthcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class GetStartViewPageAdpter extends PagerAdapter {
    Context context;
    ArrayList<ScreenItem> scrList;

    public GetStartViewPageAdpter(Context context, ArrayList<ScreenItem> scrList) {
        this.context = context;
        this.scrList = scrList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View layoutscreen = inflater.inflate(R.layout.layout_screen,null);

        ImageView imageView = layoutscreen.findViewById(R.id.into_img);
        TextView title = layoutscreen.findViewById(R.id.intro_title);
        TextView des = layoutscreen.findViewById(R.id.intro_des);

        title.setText(this.scrList.get(position).getTitle());
        des.setText(this.scrList.get(position).getDescription());
        imageView.setImageResource(this.scrList.get(position).getImg());

        container.addView(layoutscreen);

        return layoutscreen;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return scrList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
