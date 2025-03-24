package com.zaki.ecommerce_white_label_template2.Adapter;

import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.zaki.ecommerce_white_label_template2.Activities.HomePageActivity;
import com.zaki.ecommerce_white_label_template2.Fragment.*;

public class TabLayoutAdapter extends FragmentStateAdapter {
    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public TabLayoutAdapter(HomePageActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = registeredFragments.get(position);
        if (fragment == null) {
            switch (position) {
                case 1:
                    fragment = new ChairsFragment();
                    break;
                case 2:
                    fragment = new TablesFragment();
                    break;
                case 3:
                    fragment = new SofasFragment();
                    break;
                case 4:
                    fragment = new LampsFragment();
                    break;
                case 5:
                    fragment = new CrockeryFragment();
                    break;
                case 6:
                    fragment = new CeramicsFragment();
                    break;
                case 7:
                    fragment = new PlantPotsFragment();
                    break;
                default:
                    fragment = new AllProductFragment();
                    break;
            }
            registeredFragments.put(position, fragment); // Store the fragment
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 8; // Number of tabs
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
