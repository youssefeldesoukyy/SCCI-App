package com.example.scciapp.AC;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.scciapp.HR.AttendanceAppsplashFragment;
import com.example.scciapp.HR.AttendanceDevologyFragment;
import com.example.scciapp.HR.AttendanceInvestFragment;
import com.example.scciapp.HR.AttendanceMarkativeFragment;
import com.example.scciapp.HR.AttendanceTechsolveFragment;

public class WorkshopViewPagerAdapter extends FragmentStateAdapter {
	public WorkshopViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
		super(fragmentManager, lifecycle);
	}
	
	@NonNull
	@Override
	public Fragment createFragment(int position) {
		if(position == 0) {
			return new ACWorkshopAnnouncementsFragment();
		} else {
			return new ACWorkshopTasksFragment();
		}
	}
	
	@Override
	public int getItemCount() {
		return 2;
	}
}