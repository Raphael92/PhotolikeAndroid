/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kz.raphael.photolike;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKRequest.VKRequestListener;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


public class SuperAwesomeCardFragment extends Fragment {

	private static final String ARG_POSITION = "position";

	private int position;
	private FrameLayout fl;
	private View linearlayout;
	private TableLayout tableModal;
	private TableRow rowLoveModal;
	//private ScrollView sv;
	private LinearLayout ll;
	//private LinearLayout ll1;
	Boolean eventSwitch = false;
	//int sex = 1;
	SharedPreferences sPref;
//	BroadcastReceiver br;

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	//private Switch switcha;

	public final static String BROADCAST_ACTION = "kz.photolike.raphael";

	public static SuperAwesomeCardFragment newInstance(int position) {
		SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(ARG_POSITION);
		Log.i("TAG", "CARD " + position);
		setHasOptionsMenu(true);


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i("TAG", "обновил " + position);


		//tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
	//	pager = (ViewPager) v.findViewById(R.id.pager);
		//adapter = new MyPagerAdapter(getSupportFragmentManager());

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		/*FrameLayout*/ fl = new FrameLayout(getActivity());
		//fl.setLayoutParams(params);

		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
				.getDisplayMetrics());

		if (position == 0) {
			Log.i("TAG", "CARD postion " + position);
			ScrollView sv = new ScrollView(getActivity());
			sv.setLayoutParams(params);
			sv.setTag("sv1");
			fl.addView(sv);
			ll = new LinearLayout(getActivity());
			//ll1 = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.linear_layout1, null);
			ll.setTag("ll1");
		//	ll.setId(0);
			//ll.setTag("qw");
			//ll.setLayoutParams(params);
			ll.setOrientation(LinearLayout.VERTICAL);
			sv.addView(ll);

			VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,sex,bdate,city,photo_max_orig, photo_100","order","hints"));
			request.registerObject();
			request.executeWithListener(mRequestListener);
		}
		else
		if (position == 1) {
			Log.i("TAG", "CARD postion " + position);

			ScrollView sv = new ScrollView(getActivity());
			sv.setLayoutParams(params);
			fl.addView(sv);
			ll = new LinearLayout(getActivity());
			//ll.setLayoutParams(params);
			ll.setOrientation(LinearLayout.VERTICAL);
			sv.addView(ll);
			VKRequest request = VKApi.groups().get(VKParameters.from(VKApiConst.FIELDS, "id, name, photo_100", VKApiConst.EXTENDED, 1));
			request.registerObject();
			request.executeWithListener(mRequestListenerGroup);
		}
		else {
			Log.i("TAG", "CARD postion " + position);


			View v = getActivity().getLayoutInflater().inflate(R.layout.love_tab, null);
			new TaskGetLoveTab(getActivity()).execute(v);

			fl.addView(v);
		}

		MainActivity activity = (MainActivity) getActivity();
		tabs = (PagerSlidingTabStrip) activity.findViewById(R.id.tabs);
		tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			// This method will be invoked when a new page becomes selected.
			@Override
			public void onPageSelected(int position) {
				Log.i("TAG", "gettag " + getActivity().getSupportFragmentManager().getFragments().get(0). getTag());

				if (position == 0) {
					Fragment frg = null;
					frg = getActivity().getSupportFragmentManager().getFragments().get(1);
					//final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
					//ft.detach(frg);
					//ft.attach(frg);
					ll = (LinearLayout) frg.getView().findViewWithTag("ll1");
					//ft.commit();
					sPref = getActivity().getSharedPreferences("sex", Context.MODE_PRIVATE);
					int sex = sPref.getInt("0", 0);
					int sexMain = sPref.getInt("main", 0);
					if (sex != sexMain) {
						sPref = getActivity().getSharedPreferences("sex", Context.MODE_PRIVATE);
						SharedPreferences.Editor ed = sPref.edit();
						ed.putInt("0", sexMain);
						ed.apply();

						VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,sex,bdate,city,photo_max_orig, photo_100","order","hints"));
						request.registerObject();
						request.executeWithListener(mRequestListener);
					}
				}
				Log.i("TAG", "rabotaet??? " + position);

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

		return fl;
	}

	/*public void getFirends() {
		VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,sex,bdate,city,photo_max_orig, photo_100","order","hints"));
		request.registerObject();
		request.executeWithListener(mRequestListener);
	}*/
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		MenuItem menuItem= menu.findItem(R.id.myswitch);
        View view = MenuItemCompat.getActionView(menuItem);
        Switch switcha = (Switch)view.findViewById(R.id.switchForActionBar);

		switcha.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				//Log.i("TAG", "touch ");
				v.setTag(1);
				return false;
			}
		});
        switcha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do anything here on check changed
				Log.i("TAG", "CARD buttonView " + buttonView.getTag());
				if (buttonView.getTag() == 1 && position == 0) {
					Toast.makeText(getActivity(), "Monitored2 switch is " + position + " " + (isChecked ? "on" : "off"),
							Toast.LENGTH_SHORT).show();
					int sex = 0;
					if (isChecked)
						sex = 1;
					else
						sex = 2;
					sPref = getActivity().getSharedPreferences("sex", Context.MODE_PRIVATE);
					SharedPreferences.Editor ed = sPref.edit();
					ed.putInt(String.valueOf(position), sex);
					ed.apply();
					VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,sex,bdate,city,photo_max_orig, photo_100","order","hints"));
					request.registerObject();
					request.executeWithListener(mRequestListener);
				}
				if (buttonView.getTag() == 1) {
					int sex = 0;
					if (isChecked)
						sex = 1;
					else
						sex = 2;
					sPref = getActivity().getSharedPreferences("sex", Context.MODE_PRIVATE);
					SharedPreferences.Editor ed = sPref.edit();
					ed.putInt("main", sex);
					ed.apply();
				}
			/*	ScrollView sv = (ScrollView) fl.findViewWithTag("sv1");
				LinearLayout ll = (LinearLayout) sv.findViewWithTag("ll1");
				TextView tv = new TextView(getActivity());
				tv.setText("TEST");
				ll.addView(tv);*/
				/*if (position == 1) {
					ScrollView sv = (ScrollView) fl.findViewWithTag("sv1");
					LinearLayout ll = (LinearLayout) sv.findViewWithTag("ll1");
					TextView tv = new TextView(getActivity());
					tv.setText("TEST");
					ll.addView(tv);
				}*/

				//buttonView.setTag(0);
            }
        });
		super.onCreateOptionsMenu(menu,inflater);
	}

	VKRequestListener mRequestListenerGroup = new VKRequestListener() {
		@Override
		public void onComplete(VKResponse response) {
			//setResponseText(response.json.toString());
			//	ImageView iv = new ImageView;

			//URL url = new URL("");
			//InputStream content = (InputStream)url.getContent();
			//Drawable d = Drawable.createFromStream(content , "src");
			//iv.setImageDrawable(d);

			FrameLayout.LayoutParams imageViewParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);

			try {
				JSONObject obj = new JSONObject(response.json.toString());
				JSONObject obj2 = obj.getJSONObject("response");
				JSONArray arr = obj2.getJSONArray("items");
				/*ScrollView sv = (ScrollView) fl.findViewWithTag("sv1");
				LinearLayout ll = (LinearLayout) sv.findViewWithTag("ll1");*/
				String post_id = "не удалось";
				for (int i = 0; i <4; i++)
				{
					//post_id = arr.getJSONObject(i).getString("first_name");
				/*	Log.i("TAG", "post_id = " + arr.getJSONObject(i).getString("photo_100"));
					URL url = null;
					ImageView iv = new ImageView(getContext());
					try {
						url = new URL(///rr.getJSONObject(i).getString("photo_max_orig")
								"https://pp.vk.me/c630229/v630229698/1ab49/lBbc1d3gUSI.jpg");
						Log.i("TAG", "url " + url);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						Log.i("TAG", "ошибка " + i);
						e.printStackTrace();
					}
					InputStream content = null;*/

					ImageView iv2 = new ImageView(getActivity());

					//iv.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length));
					iv2.setImageDrawable(getResources().getDrawable(R.drawable.nast));
					iv2.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Toast toast = Toast.makeText(getActivity(),
									"Из группы",
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();

							//ll.removeAllViews();

							VKRequest request = VKApi.groups().getMembers(VKParameters.from(VKApiConst.FIELDS, "first_name, sex, last_name, photo_max_orig, photo_100", VKApiConst.GROUP_ID, "104052691", //id группы, у которой получаю подписчиков
									VKApiConst.COUNT, 10));
							request.registerObject();
							request.executeWithListener(mRequestListener);
						}
					});
					//if (i ==0)
						iv2.setTag(i);
					//simpleWaitDialog.dismiss();
					if (i == 0) {
						iv2.setPadding(0, 0, 0, 10);
					}
					else {
						iv2.setPadding(0, 10, 0, 10);
					}
					ll.addView(iv2);
					new MyAsync(getActivity()).execute(arr.getJSONObject(i).getString("photo_100"), Integer.toString(i), "1" /*"https://pp.vk.me/c630229/v630229698/1ab4a/tEiUtwMWTyQ.jpg"*/);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		@Override
		public void onError(VKError error) {
			//setResponseText(error.toString());
		}

		@Override
		public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded,
							   long bytesTotal) {
			// you can show progress of the request if you want
		}

		@Override
		public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
			/*getFragment().textView.append(
					String.format("Attempt %d/%d failed\n", attemptNumber, totalAttempts));*/
		}
	};

	VKRequestListener mRequestListener = new VKRequestListener() {
		@Override
		public void onComplete(VKResponse response) {
			//setResponseText(response.json.toString());
			//	ImageView iv = new ImageView;

			//URL url = new URL("");
			//InputStream content = (InputStream)url.getContent();
			//Drawable d = Drawable.createFromStream(content , "src"); 
			//iv.setImageDrawable(d);
			/*ScrollView sv = (ScrollView) fl.findViewWithTag("sv1");
			LinearLayout ll = (LinearLayout) sv.findViewWithTag("ll1");*/
			ll.removeAllViews();
			FrameLayout.LayoutParams imageViewParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.WRAP_CONTENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);

			try {
				JSONObject obj = new JSONObject(response.json.toString());

				//JSONArray arr = obj.getJSONArray("response");
				JSONObject obj2 = obj.getJSONObject("response");
				JSONArray arr = obj2.getJSONArray("items");

				//String post_id = "не удалось";
				for (int i = 0; i < arr.length(); i++)
				{
				//	post_id = arr.getJSONObject(i).getString("first_name");
				//	Log.i("TAG", "post_id = " + arr.getJSONObject(i).getString("photo_100"));
				//	URL url = null;
				//	ImageView iv = new ImageView(getContext());
				/*	try {
						url = new URL(///rr.getJSONObject(i).getString("photo_max_orig")
								"https://pp.vk.me/c630229/v630229698/1ab49/lBbc1d3gUSI.jpg");
						Log.i("TAG", "url " + url);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						Log.i("TAG", "ошибка " + i);
						e.printStackTrace();
					}
					InputStream content = null;*/

					//Здесь ошибки -------------------

					//	content = (InputStream)url.getContent();
/*	SSLContext context = SSLContext.getInstance("TLS");
   context.init(null, tmf.getTrustManagers(), null);

HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
   urlConnection.setSSLSocketFactory(context.getSocketFactory());
   InputStream in = urlConnection.getInputStream();*/
//	URLConnection urlConnection = url.openConnection();
//content = (InputStream) url.getContent();
// URL url = new URL("http://www.android.com/");   
					// HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

					sPref = getActivity().getSharedPreferences("sex", Context.MODE_PRIVATE);
					int sex = sPref.getInt("0", 0);
					if (arr.getJSONObject(i).getInt("sex") == sex || arr.getJSONObject(i).getInt("sex") == 0) {
						ImageView iv2 = new ImageView(getActivity());
						final String firstName = arr.getJSONObject(i).getString("first_name");
						final String lastName = arr.getJSONObject(i).getString("last_name");
						final String userid = arr.getJSONObject(i).getString("id");
						//iv2.setTag("id" + userid);
						//iv.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length));
						iv2.setImageDrawable(getResources().getDrawable(R.drawable.nast));

						iv2.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								//	Log.i("TAG", "clickListener " + v.getTag());
								/*Switch anonSwitch = new Switch(getActivity());
								anonSwitch.setText("Анонимно");
								TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
								params.setMargins(0,0,0,0);
								anonSwitch.setGravity(Gravity.CENTER_HORIZONTAL);*/

									AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
									//TextView vText = (TextView) v;
									ad.setTitle(firstName + " " + lastName);
									// ad.ti
									linearlayout = getActivity().getLayoutInflater().inflate(R.layout.modal_lovepage, null);
									tableModal = (TableLayout) linearlayout.findViewById(R.id.tableModal);
								//	tableModal.addView(anonSwitch);
									new GetLoveAsync(getActivity()).execute("https://photolike.info/example_test/getSexType.php", userid);
									ad.setView(linearlayout);
									//  ad.setMessage(message); // сообщение
									ad.setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int arg1) {

											//	db.insertToGis(equipcode, mlat, mlon);
											//setEquipOnMap(mlat, mlon, appTitle);
										}
									});
									//        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
									//          public void onClick(DialogInterface dialog, int arg1) {

									//            }
									//      });
									ad.setCancelable(true);
									ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
										public void onCancel(DialogInterface dialog) {

										}
									});
									ad.show();


							}
						});

						iv2.setTag(i);
						//simpleWaitDialog.dismiss();
						if (i == 0) {
							iv2.setPadding(0, 0, 0, 10);
						} else {
							iv2.setPadding(0, 10, 0, 10);
						}
						ll.addView(iv2);
						new MyAsync(getActivity()).execute(arr.getJSONObject(i).getString("photo_100"), Integer.toString(i), "0" /*"https://pp.vk.me/c630229/v630229698/1ab4a/tEiUtwMWTyQ.jpg"*/);
					}
						//new MyAsync(getActivity()).execute(arr);
					//Drawable d = Drawable.createFromStream(content , "src"); 
					
					/*Toast toast = Toast.makeText(getApplicationContext(),
							post_id,
		    	            Toast.LENGTH_SHORT);
		    	    toast.setGravity(Gravity.CENTER, 0, 0);*/
					
				/*	PlaceholderFragment fragment = getFragment();
					if (fragment != null && fragment.linLayout != null) {
						// toast.show();
						iv.setImageDrawable(d);
						iv.setPadding(0, -40, 0, -40);
						fragment.linLayout.addView(iv);
					}*/
					/*FrameLayout fl = new FrameLayout(getActivity());
					iv.setImageDrawable(d);
					iv.setPadding(0, -40, 0, -40);
					fl.addView(iv);*/
				}

				//new MyAsync(getActivity()).execute(arr.getJSONObject(1).getString("photo_max_orig"), Integer.toString(1) /*"https://pp.vk.me/c630229/v630229698/1ab4a/tEiUtwMWTyQ.jpg"*/);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


		@Override
		public void onError(VKError error) {
			//setResponseText(error.toString());
		}

		@Override
		public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded,
							   long bytesTotal) {
			// you can show progress of the request if you want
		}

		@Override
		public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
			/*getFragment().textView.append(
					String.format("Attempt %d/%d failed\n", attemptNumber, totalAttempts));*/
		}
	};

		public class TaskLoadImage extends AsyncTask<String, Void, Bitmap> {
			ImageView v;
			public TaskLoadImage() {

			}

			@Override
			protected Bitmap doInBackground(String... params) {
				HttpsURLConnection conn= null;
				InputStream is = null;

				try {
					URL url = new URL(params[0]);
					Log.i("log_tag", "bitmap " + url);
					SSLContext context = SSLContext.getInstance("TLS");
					//connection = (HttpsURLConnection) url.openConnection();

					CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
					InputStream caInput = SuperAwesomeCardFragment.this.getResources().openRawResource(R.raw.photolike);//new BufferedInputStream(new FileInputStream("photolike.crt"));
					Certificate ca;
					try {
						ca = cf.generateCertificate(caInput);
						System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
					} finally {
						caInput.close();
					}

// Create a KeyStore containing our trusted CAs
					String keyStoreType = KeyStore.getDefaultType();
					KeyStore keyStore = KeyStore.getInstance(keyStoreType);
					keyStore.load(null, null);
					keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
					String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
					TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
					tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
					//SSLContext context = SSLContext.getInstance("TLS");
					context.init(null, tmf.getTrustManagers(), null);

// Tell the URLConnection to use a SocketFactory from our SSLContext
					//URL url = new URL("https://certs.cac.washington.edu/CAtest/");
					conn =
							(HttpsURLConnection)url.openConnection();
					conn.setSSLSocketFactory(context.getSocketFactory());

					conn.setUseCaches(true);
					is = conn.getInputStream();
					Bitmap myBitmap = BitmapFactory.decodeStream(is);
					return myBitmap;
				} catch (IOException e) {
					Log.i("log_tag", "ошибка love " + e.getMessage());
					e.printStackTrace();
				} catch (CertificateException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (KeyStoreException e) {
					e.printStackTrace();
				} catch (KeyManagementException e) {
					e.printStackTrace();
				} finally{
					conn.disconnect();
				}
				return null;
			}

			public void onPostExecute(Bitmap string) {
				//	iv.setImageBitmap(string);
				//Log.i("log_tag", "bitmap " + string);
				//ImageLove.setImageBitmap(string);

			}

		}

	public class MyAsync extends AsyncTask<String, Void, Bitmap> {

		private Activity mActivity;
		//private ProgressDialog simpleWaitDialog;
		private int nomRecord = 0;
		Boolean isGroup = false;
		public MyAsync(Activity activity) {
			mActivity = activity;
		}

		@Override
		protected void onPreExecute() {
			Log.i("Async-Example", "onPreExecute Called");
		/*	simpleWaitDialog = ProgressDialog.show(mActivity,
					"Wait", "Downloading Image");*/

		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Log.i("TAG", "Запускаю поток" );
			HttpsURLConnection connection = null;
			//StringBuilder src = new StringBuilder();
			InputStream is = null;

			if (params[2].equals("1"))
				isGroup = true;
			try {
				/*JSONArray itemsArr = params[0];
				URL url = null;
				//for (int i = 0; i <4; i++)
				try {
					url = new URL(itemsArr.getJSONObject(1).getString("photo_max_orig"));
				} catch (JSONException e) {
					e.printStackTrace();
				}*/
				URL url = new URL(params[0]);
				nomRecord = Integer.parseInt(params[1]);
				connection = (HttpsURLConnection) url.openConnection();
				connection.setUseCaches(true);
				is = connection.getInputStream();
				// return is;
	         /*   while (true) {
	                byte[] line = new byte[1024];
	                int size = is.read(line);
	                if (size <= 0)
	                    break;
	                src.append(new String(line,"windows-1251"));
	            }
	            Log.i("TAG", "src " + new String(src));*/
				Log.i("TAG", "До битмапа" );
			/*	BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;*/
				Bitmap myBitmap = BitmapFactory.decodeStream(is/*, null, options*/);

				Log.i("TAG", "myBitmap.getByteCount() = ");
				return myBitmap;
			} catch (IOException e) {
				Log.i("TAG", "ошибка =  " + e.getMessage());
				e.printStackTrace();
			} finally{
				Log.i("TAG", "дошел до финала");
				connection.disconnect();
			}
			// return new String(src);
			Log.i("TAG", "так не должно" );
			return null;
		}

		public void onPostExecute(Bitmap string) {
			super.onPostExecute(string);
	       /* TextView textView = (TextView) mActivity.findViewById(R.id.responce);
	        textView.setText(string);*/
			//	InputStream is = null;
			//	is = new ByteArrayInputStream(string.getBytes());
		//	ScrollView sv = (ScrollView) fl.findViewWithTag("sv1");
		//	final LinearLayout ll = (LinearLayout) sv.findViewWithTag("ll1");
			//Drawable d = Drawable.createFromStream(is, "src");
			if (!isGroup)
				Log.i("TAG", "картинка = " + string.getByteCount());
			else
				Log.i("TAG", "картинка группы = " + string.getByteCount());
		//	if (!isGroup) {
				ImageView iv = (ImageView) ll.findViewWithTag(nomRecord); //new ImageView(getActivity());

				//iv.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length));
				iv.setImageBitmap(string);
				//simpleWaitDialog.dismiss();
				if (nomRecord == 0) {
					iv.setPadding(0, 0, 0, 10);
				} else {
					iv.setPadding(0, 10, 0, 10);
				}

				//int id = getResources().getIdentifier("kz.raphael.photolike:drawable/nast.jpg", null, null);
				//Resources res = getResources(); // need this to fetch the drawable
				//	Drawable draw = getResources().getDrawable(R.drawable.nast);
				//iv.setImageDrawable(draw);
				//	iv.setImageDrawable(d);
				//	iv.setPadding(0, -40, 0, -40);
				//iv.setTag(nomRecord);

				//ll.addView(iv);
			/*	iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//	Log.i("TAG", "clickListener " + v.getTag());

						if (isGroup) {
							Toast toast = Toast.makeText(getActivity(),
									"Из группы",
									Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();

							ll.removeAllViews();

							VKRequest request = VKApi.groups().getMembers(VKParameters.from(VKApiConst.FIELDS, "first_name, last_name, photo_max_orig, photo_100", VKApiConst.GROUP_ID, "104052691", //id группы, у которой получаю подписчиков
									VKApiConst.COUNT, 10));
							request.registerObject();
							request.executeWithListener(mRequestListener);

						} else {
							AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
							//TextView vText = (TextView) v;
							ad.setTitle("Дефектная карточка объекта");
							// ad.ti
							linearlayout = getActivity().getLayoutInflater().inflate(R.layout.modal_lovepage, null);
							tableModal = (TableLayout) linearlayout.findViewById(R.id.tableModal);
							new GetLoveAsync(getActivity()).execute("https://photolike.info/example_test/getSexType.php");
							ad.setView(linearlayout);
							//  ad.setMessage(message); // сообщение
							ad.setPositiveButton("Закрыть", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int arg1) {
									//	db.insertToGis(equipcode, mlat, mlon);
									//setEquipOnMap(mlat, mlon, appTitle);
								}
							});
							//        ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
							//          public void onClick(DialogInterface dialog, int arg1) {

							//            }
							//      });
							ad.setCancelable(true);
							ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
								public void onCancel(DialogInterface dialog) {

								}
							});
							ad.show();
						}

					}
				});*/


		//	}
		/*	TextView v = new TextView(getActivity());
			v.setGravity(Gravity.CENTER);
			v.setBackgroundResource(R.drawable.background_card);
			v.setText("CARD q");
			fl.addView(v);
	    	Log.i("TAG", "task " + string);*/
		}
	}

	public class SexClickAsync extends AsyncTask<String, Void, String> {
		//private Activity loveActivity;
		String sexType = "0";
		String userId = "0";
		String sexNew = "0";
		public SexClickAsync(Activity activity) {
			//loveActivity = activity;
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			Log.i("log_tag", "url =2 ");
			InputStream is = null;

			//HttpsURLConnection conn=null;
			try{

				//	content = (InputStream)url.getContent();

//	URLConnection urlConnection = url.openConnection();
//content = (InputStream) url.getContent();
// URL url = new URL("http://www.android.com/");
				// HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

				URL url=new URL(params[0]);
				userId = params[1];
				String anonim = params[2];
				sexType = params[3];
				sexNew = params[4];
				SSLContext context = SSLContext.getInstance("TLS");
				// context.init(null, tmf.getTrustManagers(), null);



				Log.i("log_tag", "url get = " + url);
				String agent="Applet";
				//  String query="query=" + r[0];
				String query = "viewer_id=186332067&auth_key=eb2ca2e8df6ffc18daf33d07bdf119ac&whom_id=" + userId +
						"&anonim=" + anonim + "&type=" + sexType + "&New=" + sexNew;
				String type="application/x-www-form-urlencoded";
				//	conn=(HttpsURLConnection)url.openConnection();

				// Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
				InputStream caInput = SuperAwesomeCardFragment.this.getResources().openRawResource(R.raw.photolike);//new BufferedInputStream(new FileInputStream("photolike.crt"));
				Certificate ca;
				try {
					ca = cf.generateCertificate(caInput);
					System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
				} finally {
					caInput.close();
				}

// Create a KeyStore containing our trusted CAs
				String keyStoreType = KeyStore.getDefaultType();
				KeyStore keyStore = KeyStore.getInstance(keyStoreType);
				keyStore.load(null, null);
				keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
				String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
				tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
				//SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, tmf.getTrustManagers(), null);

// Tell the URLConnection to use a SocketFactory from our SSLContext
				//URL url = new URL("https://certs.cac.washington.edu/CAtest/");
				HttpsURLConnection conn =
						(HttpsURLConnection)url.openConnection();
				conn.setSSLSocketFactory(context.getSocketFactory());
				//InputStream in = conn.getInputStream();
				//copyInputStreamToOutputStream(in, System.out);
				Log.i("log_tag", "poka rabotaet");
				//InputStream in = conn.getInputStream();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				Log.i("log_tag", "poka rabotaet1 ");
				conn.setRequestMethod("POST");
				conn.setRequestProperty( "User-Agent", agent );
				conn.setRequestProperty( "Content-Type", type );
				//conn.setRequestProperty("viewer_id", "raphael_z");
				conn.setRequestProperty( "Content-Length", ""+query.length());
				Log.i("log_tag", "poka rabotaet2 ");
				OutputStream out=conn.getOutputStream();
				out.write(query.getBytes());
				Log.i("log_tag", "poka rabotaet3");
				is = conn.getInputStream();
				Log.i("log_tag", "rabotaet");
			}catch(Exception e){
				e.printStackTrace();
				Log.e("log_tag", "Error in http connection "+e.toString());
			}/*finally{
		    	conn.disconnect();
		    }*/


			//convert response to string
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is/*,"iso-8859-1"*/),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				//Log.i("log_tag", "proverka 1" + reader.readLine());
				while ((line = reader.readLine()) != null) {
					Log.i("log_tag", "proverka 11");
					sb.append(line + "\n");
					Log.i("log_tag","proverka2 " + sb.toString());
				}
				is.close();
				Log.i("log_tag","proverka " + sb.toString());
				result=sb.toString();
			}catch(Exception e){
				Log.e("log_tag", "Error converting result "+e.toString());
			}

			//parse json data
			/*JSONArray jArray = null;
			try{
				jArray = new JSONArray(result);
			}
			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
			}
			return jArray;*/
			return result;
		}

		public void onPostExecute(String result) {
			Toast.makeText(getActivity(), "click",
					Toast.LENGTH_SHORT).show();
			int num = Integer.parseInt(sexType) - 1;
			int newType = 1 - Integer.parseInt(sexNew);
			/*new GetImageLoveAsync(getActivity()).execute("https://photolike.info/example_test/images/" +
					json_data.getString("imgUrl"), Integer.toString(i), userId, "0");*/
			new GetImageLoveAsync(getActivity()).execute("https://photolike.info/example_test/" + result, Integer.toString(num),
					userId, Integer.toString(newType), "0");
				/*	if (json_data.getString("wantSex").equals("1") && json_data.getString("mutualSex").equals("1"))
						new GetImageLoveAsync(getActivity()).execute("https://photolike.info/example_test/images/" + json_data.getString("imgUrl"), Integer.toString(i));
					else if (json_data.getString("wantSex").equals("1"))
						new GetImageLoveAsync(getActivity()).execute("https://photolike.info/example_test/images/" + json_data.getString("imgUrl_YesNo"), Integer.toString(i));
					else
						new GetImageLoveAsync(getActivity()).execute("https://photolike.info/example_test/images/" + json_data.getString("imgUrl_No"), Integer.toString(i));
*/
		}
	}

	public class GetLoveAsync extends AsyncTask<String, Void, JSONArray> {
		//private Activity loveActivity;
		private String userId = "0";
		public GetLoveAsync(Activity activity) {
		//	loveActivity = activity;
		}

		@Override
		protected JSONArray doInBackground(String... params) {
			String result = "";
			Log.i("log_tag", "url =2 ");
			InputStream is = null;

			//HttpsURLConnection conn=null;
			try{

				//	content = (InputStream)url.getContent();

//	URLConnection urlConnection = url.openConnection();
//content = (InputStream) url.getContent();
// URL url = new URL("http://www.android.com/");
				// HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

				URL url=new URL(params[0]);
				userId = params[1];
				SSLContext context = SSLContext.getInstance("TLS");
				// context.init(null, tmf.getTrustManagers(), null);



				Log.i("log_tag", "url get = " + url);
				String agent="Applet";
				//  String query="query=" + r[0];
				String query = "viewer_id=186332067&auth_key=eb2ca2e8df6ffc18daf33d07bdf119ac&whom_id=" + userId;
				String type="application/x-www-form-urlencoded";
			//	conn=(HttpsURLConnection)url.openConnection();

				// Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
				InputStream caInput = SuperAwesomeCardFragment.this.getResources().openRawResource(R.raw.photolike);//new BufferedInputStream(new FileInputStream("photolike.crt"));
				Certificate ca;
				try {
					ca = cf.generateCertificate(caInput);
					System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
				} finally {
					caInput.close();
				}

// Create a KeyStore containing our trusted CAs
				String keyStoreType = KeyStore.getDefaultType();
				KeyStore keyStore = KeyStore.getInstance(keyStoreType);
				keyStore.load(null, null);
				keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
				String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
				tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
				//SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, tmf.getTrustManagers(), null);

// Tell the URLConnection to use a SocketFactory from our SSLContext
				//URL url = new URL("https://certs.cac.washington.edu/CAtest/");
				HttpsURLConnection conn =
						(HttpsURLConnection)url.openConnection();
				conn.setSSLSocketFactory(context.getSocketFactory());
				//InputStream in = conn.getInputStream();
				//copyInputStreamToOutputStream(in, System.out);
				Log.i("log_tag", "poka rabotaet");
				//InputStream in = conn.getInputStream();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				Log.i("log_tag", "poka rabotaet1 ");
				conn.setRequestMethod("POST");
				conn.setRequestProperty( "User-Agent", agent );
				conn.setRequestProperty( "Content-Type", type );
				//conn.setRequestProperty("viewer_id", "raphael_z");
				conn.setRequestProperty( "Content-Length", ""+query.length());
				Log.i("log_tag", "poka rabotaet2 ");
				OutputStream out=conn.getOutputStream();
				out.write(query.getBytes());
				Log.i("log_tag", "poka rabotaet3");
				is = conn.getInputStream();
				Log.i("log_tag", "rabotaet");
			}catch(Exception e){
				e.printStackTrace();
				Log.e("log_tag", "Error in http connection "+e.toString());
			}/*finally{
		    	conn.disconnect();
		    }*/


			//convert response to string
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is/*,"iso-8859-1"*/),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				 //Log.i("log_tag", "proverka 1" + reader.readLine());
				while ((line = reader.readLine()) != null) {
					Log.i("log_tag", "proverka 11");
					sb.append(line + "\n");
					Log.i("log_tag","proverka2 " + sb.toString());
				}
				is.close();
				Log.i("log_tag","proverka " + sb.toString());
				result=sb.toString();
			}catch(Exception e){
				Log.e("log_tag", "Error converting result "+e.toString());
			}

			//parse json data
			JSONArray jArray = null;
			try{
				jArray = new JSONArray(result);
		         /*   for(int i=0;i<jArray.length();i++){
		                    JSONObject json_data = jArray.getJSONObject(i);
		                    Log.i("log_tag","id: "+json_data.getInt("id")+
		                            ", name: "+json_data.getString("name")+
		                            ", wantSex: "+json_data.getString("wantSex")+
		                            ", mutualSex: "+json_data.getString("mutualSex")+
		                            ", imgUrl: "+json_data.getString("imgUrl")
		                    );
		            }*/
			}
			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
			}
			return jArray;
		}

		public void onPostExecute(JSONArray string) {
	       /* TextView textView = (TextView) mActivity.findViewById(R.id.responce);
	        textView.setText(string);*/
			//TableRow rowLoveModal = null;
			Log.i("log_tag","string: " + string);
			try {
				for(int i=0;i<string.length();i++){
					JSONObject json_data = string.getJSONObject(i);
					Log.i("log_tag","id: "+json_data.getInt("id")+
									", name: "+json_data.getString("name")+
									", wantSex: "+json_data.getString("wantSex")+
									", mutualSex: "+json_data.getString("mutualSex")+
									", imgUrl: "+json_data.getString("imgUrl")
					);
               	/*	TableLayout tableModal = (TableLayout) linearlayout.findViewById(R.id.tableModal);
               		if (i % 2 == 0) {
               			
               			rowLoveModal = new TableRow(getActivity());
					//	rowPower.setWeightSum(10);
				   //     rowRepairName.setBackgroundResource(R.drawable.roundtable);
				        
				        ImageView ImageLove = new ImageView(getActivity());*/
					if (json_data.getString("wantSex").equals("1") && json_data.getString("mutualSex").equals("1"))
						new GetImageLoveAsync(getActivity()).execute("https://photolike.info/example_test/images/" +
								json_data.getString("imgUrl"), Integer.toString(i), userId, "0", "1");
				    else if (json_data.getString("wantSex").equals("1"))
						new GetImageLoveAsync(getActivity()).execute("https://photolike.info/example_test/images/" +
								json_data.getString("imgUrl_YesNo"), Integer.toString(i), userId, "0", "1");
					else
						new GetImageLoveAsync(getActivity()).execute("https://photolike.info/example_test/images/" +
								json_data.getString("imgUrl_No"), Integer.toString(i), userId, "1", "1");
					/*   rowLoveModal.addView(ImageLove);

						tableModal.addView(rowLoveModal);
               		}
               		else {
               			ImageView ImageLove = new ImageView(getActivity());
				        rowLoveModal.addView(ImageLove);
               		}*/
				}
			}
			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
			}



		}
	}

	public class GetImageLoveAsync extends AsyncTask<String, Void, Bitmap> {
		private Activity loveImageActivity;
		private ImageView ImageLove;
		private int nomRec = 0;
		private String userId = "0";
		private String sexNew = "0";
		private String newModal = "1";
		public GetImageLoveAsync(Activity activity) {
			loveImageActivity = activity;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			HttpsURLConnection conn= null;
			InputStream is = null;

			nomRec = Integer.parseInt(params[1]);
			userId = params[2];
			sexNew = params[3];
			newModal = params[4];
			//	if (i % 2 == 0) {
       		/*	rowLoveModal = new TableRow(getActivity());
       			ImageLove = new ImageView(getActivity());
		        rowLoveModal.addView(ImageLove);
				tableModal.addView(rowLoveModal); //здесь ошибка*/
       		/*}
       		else {
       			ImageLove = new ImageView(getActivity());
		        rowLoveModal.addView(ImageLove);
       		}*/
			try {
				URL url = new URL(params[0]);
				Log.i("log_tag", "bitmap " + url);
				SSLContext context = SSLContext.getInstance("TLS");
				//connection = (HttpsURLConnection) url.openConnection();

				CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
				InputStream caInput = SuperAwesomeCardFragment.this.getResources().openRawResource(R.raw.photolike);//new BufferedInputStream(new FileInputStream("photolike.crt"));
				Certificate ca;
				try {
					ca = cf.generateCertificate(caInput);
					System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
				} finally {
					caInput.close();
				}

// Create a KeyStore containing our trusted CAs
				String keyStoreType = KeyStore.getDefaultType();
				KeyStore keyStore = KeyStore.getInstance(keyStoreType);
				keyStore.load(null, null);
				keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
				String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
				tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
				//SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, tmf.getTrustManagers(), null);

// Tell the URLConnection to use a SocketFactory from our SSLContext
				//URL url = new URL("https://certs.cac.washington.edu/CAtest/");
				conn =
						(HttpsURLConnection)url.openConnection();
				conn.setSSLSocketFactory(context.getSocketFactory());

				conn.setUseCaches(true);
				is = conn.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(is);
				return myBitmap;
			} catch (IOException e) {
				Log.i("log_tag", "ошибка love " + e.getMessage());
				e.printStackTrace();
			} catch (CertificateException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyStoreException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} finally{
				conn.disconnect();
			}
			return null;
		}

		public void onPostExecute(Bitmap string) {
			//	iv.setImageBitmap(string);
			//Log.i("log_tag", "bitmap " + string);
			//ImageLove.setImageBitmap(string);
			if (newModal.equals("1")) {
				if (nomRec % 2 == 0) {
					rowLoveModal = new TableRow(getActivity());
					rowLoveModal.setTag("rowmodal" + nomRec);
					ImageLove = new ImageView(getActivity());
					ImageLove.setTag("modal" + nomRec);
					ImageLove.setImageBitmap(string);
					ImageLove.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							new SexClickAsync(getActivity()).execute("https://photolike.info/example_test/sexClick.php",
									userId, "0", String.valueOf((nomRec + 1)), sexNew);
						}
					});
					rowLoveModal.addView(ImageLove);
					tableModal.addView(rowLoveModal); //здесь ошибка*/
				} else {
					ImageLove = new ImageView(getActivity());
					ImageLove.setTag("modal" + nomRec);
					ImageLove.setImageBitmap(string);
					ImageLove.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							new SexClickAsync(getActivity()).execute("https://photolike.info/example_test/sexClick.php",
									userId, "0", String.valueOf((nomRec + 1)), sexNew);
						}
					});
					rowLoveModal.addView(ImageLove);
				}
			}
			else {
				if (nomRec % 2 == 0) {
					TableRow tr = (TableRow) tableModal.findViewWithTag("rowmodal" + nomRec);
					ImageLove = (ImageView) tr.findViewWithTag("modal" + nomRec);
					ImageLove.setImageBitmap(string);
					ImageLove.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							new SexClickAsync(getActivity()).execute("https://photolike.info/example_test/sexClick.php",
									userId, "0", String.valueOf((nomRec + 1)), sexNew);
						}
					});
				} else {
					TableRow tr = (TableRow) tableModal.findViewWithTag("rowmodal" + (nomRec - 1));
					ImageLove = (ImageView) tr.findViewWithTag("modal" + nomRec);
					ImageLove.setImageBitmap(string);
					ImageLove.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							new SexClickAsync(getActivity()).execute("https://photolike.info/example_test/sexClick.php",
									userId, "0", String.valueOf((nomRec + 1)), sexNew);
						}
					});
				}
			}
		}

	}

	public class TaskGetLoveTab extends AsyncTask<View, Void, JSONArray> {
		//private Activity loveActivity;
		View v;
		public TaskGetLoveTab(Activity activity) {
			//loveActivity = activity;
		}

		@Override
		protected JSONArray doInBackground(View... params) {
			//String result = "";
			v = params[0];
			Log.i("log_tag", "url =2 ");
			InputStream is = null;

			//HttpsURLConnection conn=null;
			try{

				//	content = (InputStream)url.getContent();

//	URLConnection urlConnection = url.openConnection();
//content = (InputStream) url.getContent();
// URL url = new URL("http://www.android.com/");
				// HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

				URL url=new URL("https://photolike.info/example_test/getLikes.php");

				SSLContext context = SSLContext.getInstance("TLS");
				// context.init(null, tmf.getTrustManagers(), null);



				Log.i("log_tag", "url get = " + url);
				String agent="Applet";
				//  String query="query=" + r[0];
				String query = "viewer_id=186332067&auth_key=eb2ca2e8df6ffc18daf33d07bdf119ac";
				String type="application/x-www-form-urlencoded";
				//	conn=(HttpsURLConnection)url.openConnection();

				// Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
				InputStream caInput = SuperAwesomeCardFragment.this.getResources().openRawResource(R.raw.photolike);//new BufferedInputStream(new FileInputStream("photolike.crt"));
				Certificate ca;
				try {
					ca = cf.generateCertificate(caInput);
					System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
				} finally {
					caInput.close();
				}

// Create a KeyStore containing our trusted CAs
				String keyStoreType = KeyStore.getDefaultType();
				KeyStore keyStore = KeyStore.getInstance(keyStoreType);
				keyStore.load(null, null);
				keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
				String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
				tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
				//SSLContext context = SSLContext.getInstance("TLS");
				context.init(null, tmf.getTrustManagers(), null);

// Tell the URLConnection to use a SocketFactory from our SSLContext
				//URL url = new URL("https://certs.cac.washington.edu/CAtest/");
				HttpsURLConnection conn =
						(HttpsURLConnection)url.openConnection();
				conn.setSSLSocketFactory(context.getSocketFactory());
				//InputStream in = conn.getInputStream();
				//copyInputStreamToOutputStream(in, System.out);
				Log.i("log_tag", "poka rabotaet");
				//InputStream in = conn.getInputStream();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				Log.i("log_tag", "poka rabotaet1 ");
				conn.setRequestMethod("POST");
				conn.setRequestProperty( "User-Agent", agent );
				conn.setRequestProperty( "Content-Type", type );
				//conn.setRequestProperty("viewer_id", "raphael_z");
				conn.setRequestProperty( "Content-Length", ""+query.length());
				Log.i("log_tag", "poka rabotaet2 ");
				OutputStream out=conn.getOutputStream();
				out.write(query.getBytes());
				Log.i("log_tag", "poka rabotaet3");
				is = conn.getInputStream();
				Log.i("log_tag", "rabotaet");
			}catch(Exception e){
				e.printStackTrace();
				Log.e("log_tag", "Error in http connection "+e.toString());
			}/*finally{
		    	conn.disconnect();
		    }*/


			//convert response to string
			String result = null;
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is/*,"iso-8859-1"*/),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				//Log.i("log_tag", "proverka 1" + reader.readLine());
				while ((line = reader.readLine()) != null) {
					Log.i("log_tag", "proverka 11");
					sb.append(line + "\n");
					Log.i("log_tag","proverka2 " + sb.toString());
				}
				is.close();
				Log.i("log_tag","proverka " + sb.toString());
				result=sb.toString();
			}catch(Exception e){
				Log.e("log_tag", "Error converting result "+e.toString());
			}

			//parse json data
			JSONArray jArray = null;
			try{
				jArray = new JSONArray(result);
		           /* for(int i=0;i<jArray.length();i++){
		                    JSONObject json_data = jArray.getJSONObject(i);
		                    Log.i("log_tag","id: "+json_data.getInt("id")+
		                            ", who_id: "+json_data.getString("who_id")+
		                            ", likeDate: "+json_data.getString("likeDate")+
		                            ", anonim: "+json_data.getString("anonim")+
		                            ", imgUrl: "+json_data.getString("imgUrl")
		                    );
		            }*/
			}
			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
			}
			return jArray;
		}

		public void onPostExecute(JSONArray string) {
	       /* TextView textView = (TextView) mActivity.findViewById(R.id.responce);
	        textView.setText(string);*/
			//TableRow rowLoveModal = null;
			Log.i("log_tag","string: " + string);
			try {
				for(int i=0;i<string.length();i++){
					JSONObject json_data = string.getJSONObject(i);
					Log.i("log_tag","id: "+json_data.getInt("id") +
							", who_id: "+json_data.getString("who_id") +
							", likeDate: "+json_data.getString("likeDate") +
							", anonim: "+json_data.getString("anonim") +
							", imgUrl: "+json_data.getString("imgUrl")
					);
					TableLayout tl = (TableLayout) v.findViewById(R.id.lovetab);
					TableRow tr = new TableRow(getActivity());

					TextView tv = new TextView(getActivity());
					tv.setText(json_data.getString("likeDate"));
					tv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

					final TextView tv2 = new TextView(getActivity());
					tv2.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

					ImageView iv = new ImageView(getActivity());
					iv.setImageDrawable(getResources().getDrawable(R.drawable.nast));
					new TaskLoadImage().execute("https://photolike.info/example_test/images/" + json_data.getString("imgUrl"));
					VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, json_data.getString("who_id"), VKApiConst.FIELDS, "photo_100"));
					request.registerObject();
					request.executeWithListener(new VKRequestListener() {
						@Override
						public void onComplete(VKResponse response) {
							try {
								JSONObject obj = new JSONObject(response.json.toString());
								JSONArray arr = obj.getJSONArray("response");
								//	JSONArray arr = obj2.getJSONArray("items");
								tv2.setText(arr.getJSONObject(0).getString("first_name") + " " + arr.getJSONObject(0).getString("last_name"));

								//new MyAsync(getActivity()).execute(arr.getJSONObject(0).getString("photo_100"), Integer.toString(0), "0");
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						@Override
						public void onError(VKError error) {

						}
						@Override
						public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {

						}
					});

					tr.addView(tv);
					tr.addView(tv2);
					tr.addView(iv);
					tl.addView(tr);
				}
			}
			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
			}



		}
	}


}