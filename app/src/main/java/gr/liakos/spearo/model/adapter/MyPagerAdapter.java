package gr.liakos.spearo.model.adapter;


import gr.liakos.spearo.fragment.FrgFishingSessions;
import gr.liakos.spearo.fragment.FrgFishingStats;
import gr.liakos.spearo.fragment.FrgFishingStatsGlobal;
import gr.liakos.spearo.fragment.FrgMyPlaces;
import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Pager adapter class.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    Fragment[] fragments;

    public MyPagerAdapter(FragmentManager supportFragmentManager, int pageCount) {
        super(supportFragmentManager);
        fragments = new Fragment[pageCount];
        for (int i = 0; i < fragments.length; i++)
            fragments[i] = null;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {

                if (fragments[position] == null) {
                    fragments[position] = FrgFishingSessions.init(0);
                }
                break;
            }
            case 1: {
                if (fragments[position] == null) {
                    fragments[position] = FrgFishingStats.init(1);
                }
                break;
            }

            case 2: {
                if (fragments[position] == null) {
                    fragments[position] = FrgFishingStatsGlobal.init(2);
                }
                break;
            }

            case 3: {
                if (fragments[position] == null) {
                    fragments[position] = FrgMyPlaces.init(3);
                }
                break;
            }
        }

        return fragments[position];
    }

}