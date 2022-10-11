package dit257.mandalore.uweather;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
/*
This is a simple slider for whenever we get the first layout and to swipe between cities.
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    public SimpleFragmentPagerAdapter(final FragmentManager fm) {
        super(fm);
    }
// Just a getter for the fragments
    @Override
    public Fragment getItem(final int position) {
        if (position == 0) {
            return new FirstFragment();
        } else{
            return new SecondFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}