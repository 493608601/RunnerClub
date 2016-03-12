package com.tarena.allrun.view;

import java.io.ByteArrayOutputStream;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.tarena.allrun.R;
import com.tarena.allrun.biz.implAsmack.RegisterBiz;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.ImageCompress;
import com.tarena.allrun.util.ImageCompress.CompressOptions;
import com.tarena.allrun.util.Tools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends Activity {
	@ViewById
	EditText et_register_username, et_register_password,
			et_register_confirm_password, et_register_name;
	@ViewById
	Button btn_register_submit;
	@ViewById
	ImageView iv_register_selectIcon;
	private Bitmap bitmap;
	String username,pwd;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			
			Intent data=new Intent();
			data.putExtra("username", username);
			data.putExtra("pwd", pwd);
			//finish必须在后面
			setResult(0, data);
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void selectIcon(View view) {
		// 打开系统图库，选择图片
		Intent intent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, 200);
	}

	@Click(R.id.btn_register_submit)
	public void btn_register_submit() {
		try {
			btn_register_submit.setEnabled(false);

			Tools.showProgressDialog(RegisterActivity.this, "亲，正在为你注册");
			 username = et_register_username.getText().toString();
			 pwd = et_register_password.getText().toString();
			String name = et_register_name.getText().toString();
			// 检查学员完成

			// 带数据
			UserEntity userEntity = new UserEntity();
			userEntity.setUsername(username);
			userEntity.setPassword(pwd);
			userEntity.setName(name);

			// 用intent不能直接带图片，带byte[]
//			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//			bitmap.compress(CompressFormat.PNG, 10, byteArrayOutputStream);
//			byte[] imageData = byteArrayOutputStream.toByteArray();
			callBiz(userEntity);
		} catch (Exception e) {
		}
	}

	@Background
	public void callBiz(UserEntity userEntity) {
		RegisterBiz registerBiz = new RegisterBiz();
		registerBiz.register(this, userEntity);
	}

	/**
	 * 执行startActivityForResult，打开系统图库，选择完图片后，执行onActivityResult
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == 200) {
				// bitmap = MediaStore.Images.Media.getBitmap(
				// getContentResolver(), data.getData());
				// ivSelectIcon.setImageBitmap(bitmap);

				ImageCompress.CompressOptions options = new CompressOptions();
				options.maxHeight = 40;
				options.maxWidth = 40;
				options.uri = data.getData();

				ImageCompress imageCompress = new ImageCompress();
				bitmap = imageCompress.compressFromUri(this, options);
				iv_register_selectIcon.setImageBitmap(bitmap);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();		
	}

	@UiThread
	public void handleResult(int status) {
		int threadId = (int) Thread.currentThread().getId();

		Tools.closeProgressDialog();
		btn_register_submit.setEnabled(true);
		if (status == Const.STATUS_OK) {
			Toast.makeText(this, "注册成功", 2000).show();
		} else {
			Toast.makeText(this, "注册失败", 2000).show();
		}
	}

}
