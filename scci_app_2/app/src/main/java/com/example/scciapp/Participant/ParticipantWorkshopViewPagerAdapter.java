package com.example.scciapp.Participant;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.scciapp.AC.ACWorkshopAnnouncementsFragment;
import com.example.scciapp.AC.ACWorkshopTasksFragment;

public class ParticipantWorkshopViewPagerAdapter extends FragmentStateAdapter {
	public ParticipantWorkshopViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
		super(fragmentManager, lifecycle);
	}
	
	@NonNull
	@Override
	public Fragment createFragment(int position) {
		if(position == 0) {
			return new ParticipantWorkshopAnnouncementsFragment();
		} else {
			return new ParticipantWorkshopTasksFragment();
		}
	}
	
	@Override
	public int getItemCount() {
		return 2;
	}
}
