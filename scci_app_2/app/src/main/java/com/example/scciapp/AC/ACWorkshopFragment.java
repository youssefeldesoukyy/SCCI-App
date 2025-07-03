package com.example.scciapp.AC;



import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scciapp.HR.AttendanceWorkshopsViewPagerAdapter;
import com.example.scciapp.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ACWorkshopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ACWorkshopFragment extends Fragment {
	
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	
	public ACWorkshopFragment() {
		// Required empty public constructor
	}
	
	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment ACWorkshopFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ACWorkshopFragment newInstance(String param1, String param2) {
		ACWorkshopFragment fragment = new ACWorkshopFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_a_c_workshop, container, false);
	}
	private TabLayout tabLayout;
	private ViewPager2 viewPager2;
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
		viewPager2 = (ViewPager2) view.findViewById(R.id.viewpager2);
		FragmentManager fragmentManager = getChildFragmentManager();
		WorkshopViewPagerAdapter vpAdapter = new WorkshopViewPagerAdapter(fragmentManager, getLifecycle());
		viewPager2.setAdapter(vpAdapter);
		
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager2.setCurrentItem(tab.getPosition());
			}
			
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			
			}
			
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			
			}
		});
		
		viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			@Override
			public void onPageSelected(int position) {
				tabLayout.selectTab(tabLayout.getTabAt(position));
				super.onPageSelected(position);
			}
		});
	}
}