package com.tarena.allrun.biz.implAsmack;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.UUID;

import org.jivesoftware.smack.AccountManager;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tarena.allrun.TApplication;
import com.tarena.allrun.entity.UserEntity;
import com.tarena.allrun.util.Const;
import com.tarena.allrun.util.MD5Util;
import com.tarena.allrun.view.RegisterActivity;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RegisterBiz  {
	int status = Const.STATUS_OK;

	


	public void register(final RegisterActivity registerActivity,UserEntity userEntity ,byte[] imageData) {
		try {
			int threadId = (int) Thread.currentThread().getId();
			
			// ��ע����Ϣ����openfire
			AccountManager accountManager = TApplication.xmppConnection
					.getAccountManager();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", userEntity.getName());
			accountManager.createAccount(userEntity.getUsername(),
					userEntity.getPassword(), map);

			// ��ע����Ϣ����tomcat
			// 1,�ύ��˭
			String url = "http://" + TApplication.host
					+ ":8080/allRunServer/register.jsp";
			// 2,��ʲô����
			// httpClient,asyncHttpClient,java.net.HttpUrlConnection
			HttpUtils httpUtils = new HttpUtils(60000);
			RequestParams requestEntity = new RequestParams();
			requestEntity
					.addBodyParameter("username", userEntity.getUsername());
			String pwd = userEntity.getPassword();
			pwd = MD5Util.getStringMD5(pwd);
			requestEntity.addBodyParameter("md5password", pwd);
			requestEntity.addBodyParameter("nickname", userEntity.getName());

			// ���ļ�
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
					imageData);
			String fileName = UUID.randomUUID().toString() + ".png";
			requestEntity.addBodyParameter("file", byteArrayInputStream,
					byteArrayInputStream.available(), fileName, "image/png");

			httpUtils.send(HttpMethod.POST, url, requestEntity,
					new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							int threadId = (int) Thread.currentThread().getId();

							String jsonString = responseInfo.result;
							Log.i("ע��", jsonString);
							try {
								JSONObject jsonObject = new JSONObject(jsonString);
								String strStatus = jsonObject
										.getString("status");
								if ("0".equals(strStatus)) {
									status = Integer.parseInt(strStatus);
								}
								registerActivity.handleResult(Const.STATUS_OK);

							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								
							}

						}

						@Override
						public void onFailure(HttpException error, String msg) {
							// ���㲥
							Log.i("","");
							registerActivity.handleResult(Const.STATUS_REGISTER_FAILURE);

						}
					});

			// 3,�������������ݴ���
		} catch (Exception e) {
			registerActivity.handleResult(Const.STATUS_REGISTER_FAILURE);

		} finally {

		}

	}

}
