package com.tarena.allrun.view;

import java.util.ArrayList;

import cn.swu.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView;
import cn.swu.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView.IXListViewListener;
import cn.swu.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView.OnMenuItemClickListener;
import cn.swu.pulltorefreshswipemenulistviewsample.PullToRefreshSwipeMenuListView.OnSwipeListener;
import cn.swu.swipemenulistview.SwipeMenu;
import cn.swu.swipemenulistview.SwipeMenuCreator;
import cn.swu.swipemenulistview.SwipeMenuItem;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.tarena.allrun.R;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.adapter.NearbyUserAdapter;
import com.tarena.allrun.biz.implAsmack.NearbyUserBiz;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ExceptionUtil;
import com.tarena.allrun.util.LogUtil;
import com.tarena.allrun.util.Tools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListView;

public class NearbyUserActivity extends BaseActivity {
	PullToRefreshSwipeMenuListView mListView;
	NearbyUserAdapter nearbyUserAdatper;
	BitmapUtils bitmapUtils = null;
	ShowNearbyUserReceiver showNearbyUserReceiver;
	ArrayList<UserEntity> list;
	int pageIndex = 1;
	int rowNum = 2;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100 && resultCode == 200) {
			// 说明data来之addFriendBiz不是来之系统图库的图片
			int status = data.getIntExtra(Const.KEY_DATA, -1);
			if (status == 0) {
				Tools.showInfo(this, "添加好友信息发送成功，等待好友处理");
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.nearby_user);
			setupView();
			addListener();

			showNearbyUserReceiver = new ShowNearbyUserReceiver();
			this.registerReceiver(showNearbyUserReceiver, new IntentFilter(
					Const.ACTION_GET_NEARBY_USER));
			// NearbyUserBiz nearbyUserBiz = new NearbyUserBiz();
			NearbyUserBiz.query(TApplication.currentUser,
					String.valueOf(pageIndex), String.valueOf(rowNum));
			Tools.showInfo(this, "正在查询");
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(showNearbyUserReceiver);
	}

	private void addListener() {
	}

	private void setupView() {
		mListView = (PullToRefreshSwipeMenuListView) findViewById(R.id.listView);
		bitmapUtils = new BitmapUtils(this);
		nearbyUserAdatper = new NearbyUserAdapter(this, null, bitmapUtils);
		mListView.setAdapter(nearbyUserAdatper);
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {}

			@Override
			public void onLoadMore() {
				new Thread() {
					public void run() {
						try {
							this.sleep(2000);

							pageIndex++;
							NearbyUserBiz nearbyUserBiz = new NearbyUserBiz();
							nearbyUserBiz.query(TApplication.currentUser,
									String.valueOf(pageIndex),
									String.valueOf(rowNum));
						} catch (Exception e) {
							// TODO: handle exception
						}
					};
				}.start();

			}
		});

		SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				int px = Tools.dp2px(NearbyUserActivity.this, 90);
				openItem.setWidth(px);
				// set item title
				openItem.setTitle("delete");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(openItem);
			}
		};

		mListView.setMenuCreator(swipeMenuCreator);
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				if (index == 0) {
					list.remove(position);
					nearbyUserAdatper.update(list);
				}

			}
		});

		mListView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});
		mListView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,
				false, true));
	}

	class ShowNearbyUserReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				Tools.closeProgressDialog();
				if (pageIndex == 1) {
					list = (ArrayList<UserEntity>) intent
							.getSerializableExtra(Const.KEY_DATA);
					nearbyUserAdatper.update(list);
					LogUtil.i("分页", pageIndex+",size="+list.size());
				} else if (pageIndex > 1) {
					ArrayList newList = (ArrayList<UserEntity>) intent
							.getSerializableExtra(Const.KEY_DATA);
					list.addAll(newList);
					nearbyUserAdatper.update(list);
					mListView.stopLoadMore();
					LogUtil.i("分页", "刷新"+pageIndex+",size="+list.size());

				}
			} catch (Exception e) {
			}

		}
	}
}
