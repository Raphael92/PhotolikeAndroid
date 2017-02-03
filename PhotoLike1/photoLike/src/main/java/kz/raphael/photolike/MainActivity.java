package kz.raphael.photolike;


import com.astuetz.PagerSlidingTabStrip;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	
	//private final Handler handler = new Handler();

	private static MyPagerAdapter adapter;
	
	private boolean isResumed = false;

	private static final String[] sMyScope = new String[]{
        VKScope.FRIENDS,
        VKScope.WALL,
        VKScope.PHOTOS,
        VKScope.NOHTTPS,
        VKScope.GROUPS
};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		VKSdk.wakeUpSession(this, new VKCallback<VKSdk.LoginState>() {
            @Override
            public void onResult(VKSdk.LoginState res) {
                if (isResumed) {
                    switch (res) {
                        case LoggedOut:
							Log.i("TAG", "Выход");
                          //  showLogin();
                            break;
                        case LoggedIn:
							Log.i("TAG", "Вход");
                            showMain();
                            break;
                        case Pending:
							Log.i("TAG", "Pending");
                            break;
                        case Unknown:
							Log.i("TAG", "Unknown");
                            break;
                    }
                }
            }

            @Override
            public void onError(VKError error) {

            }
        });

        String[] fingerprint = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Log.d("Fingerprint", fingerprint[0]);
		
		/*tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);
		tabs.setOnPageChangeListener(new OnPageChangeListener() {
			
			// This method will be invoked when a new page becomes selected.
			@Override
			public void onPageSelected(int position) {
				Log.i("TAG", "CARD " + position);
			}
			
			// This method will be invoked when the current page is scrolled
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// Code goes here
			}
			
			// Called when the scroll state changes: 
			// SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
			@Override
			public void onPageScrollStateChanged(int state) {
				// Code goes here
			}
		});*/
	}

	private void showMain() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new MainFragment())
                .commitAllowingStateLoss();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        if (VKSdk.isLoggedIn()) {
     //       showLogout();
        } else {
           // showLogin();
        	VKSdk.login(this, sMyScope);
        }
    }

    @Override
    protected void onPause() {
        isResumed = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // User passed Authorization
           //     startTestActivity();
				showMain();
            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
	
    public static class MainFragment extends android.support.v4.app.Fragment {  
    	
    	private PagerSlidingTabStrip tabs;
    	private ViewPager pager;
    	
    	
        public MainFragment() {
            super();
        }
        @Override
        
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.activity_main, container, false);
            tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
    		pager = (ViewPager) v.findViewById(R.id.pager);
    		//adapter = new MyPagerAdapter(getSupportFragmentManager());

    		pager.setAdapter(adapter);

    		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
    				.getDisplayMetrics());
    		pager.setPageMargin(pageMargin);

    		tabs.setViewPager(pager);
    	/*	tabs.setOnPageChangeListener(new OnPageChangeListener() {
    			
    			// This method will be invoked when a new page becomes selected.
    			@Override
    			public void onPageSelected(int position) {

                    Log.i("TAG", "CARD " + position);

                   View v = inflater.inflate(R.layout.switch_layout, null);
                    Switch switcha = (Switch) v.findViewById(R.id.switchForActionBar);
                    switcha.setChecked(false);
    			}
    			
    			// This method will be invoked when the current page is scrolled
    			@Override
    			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    				// Code goes here
    			}
    			
    			// Called when the scroll state changes: 
    			// SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
    			@Override
    			public void onPageScrollStateChanged(int state) {
    				// Code goes here
    			}
    		});
*/


            return v;
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem= menu.findItem(R.id.myswitch);
       View view = MenuItemCompat.getActionView(menuItem);
        Switch switcha = (Switch)view.findViewById(R.id.switchForActionBar);
        switcha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do anything here on check changed
                Toast.makeText(MainActivity.this, "Monitored switch is " + (isChecked ? "on" : "off"),
                        Toast.LENGTH_SHORT).show();

                Intent intent2 = new Intent(SuperAwesomeCardFragment.BROADCAST_ACTION);
                intent2.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent2);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
*/
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
	
 
	
	
	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "Друзья1", "Группы2", "Признания3" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			Log.i("TAG", "CARD get item " + (position));
			return SuperAwesomeCardFragment.newInstance(position);
         //   return null;
		}

	}
	
	

	
}
