package rkan.project.gow_0b;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class MainSwipeAdapter extends FragmentStateAdapter {

    public MainSwipeAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    private MainActivity mParentActivity;

    public MainSwipeAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull MainActivity parentActivity) {
        super(fragmentManager, parentActivity.getLifecycle());
        mParentActivity = parentActivity;
    }
    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return BasketFragment.newInstance(mParentActivity);
            default:
                return BrochureFragment.newInstance(mParentActivity);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
