package com.chinalwb.are.demo;

import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ARE_DemoActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager pager;
    List<PageModel> pageModels = new ArrayList<>();

    {
        pageModels.add(new PageModel(R.layout.aredittext_all, R.string.title_aredittext_toolbar));
        pageModels.add(new PageModel(R.layout.areditor_full_bottom, R.string.title_areditor_full_bottom));
        pageModels.add(new PageModel(R.layout.areditor_full_top, R.string.title_areditor_full_top));
        pageModels.add(new PageModel(R.layout.areditor_min_bottom, R.string.title_areditor_min_bottom));
        pageModels.add(new PageModel(R.layout.areditor_min_top, R.string.title_areditor_min_top));
        pageModels.add(new PageModel(R.layout.areditor_min_hide, R.string.title_areditor_min_hide));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_are__demo);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                PageModel pageModel = pageModels.get(position);
                return PageFragment.newInstance(pageModel.layoutRes);
            }

            @Override
            public int getCount() {
                return pageModels.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(pageModels.get(position).titleRes);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);
    }

    private class PageModel {
        @LayoutRes
        int layoutRes;
        @StringRes
        int titleRes;

        PageModel(@LayoutRes int layoutRes, @StringRes int titleRes) {
            this.layoutRes = layoutRes;
            this.titleRes = titleRes;
        }
    }
}
