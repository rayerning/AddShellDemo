package com.example.classloaderstudy;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.interfaces.IDynamic;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MainActivity extends Activity {

	// 动态类加载接口
	private IDynamic lib;
	private boolean isDexClassLoader;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化组件
		Button showBannerBtn = (Button) findViewById(R.id.show_banner_btn);
		Button showDialogBtn = (Button) findViewById(R.id.show_dialog_btn);
		Button showFullScreenBtn = (Button) findViewById(R.id.show_fullscreen_btn);
		// Button showAppWallBtn = (Button) findViewById(R.id.show_appwall_btn);


		/** 使用DexClassLoader方式加载类 */
		// dex压缩文件的路径(可以是apk,jar,zip格式)
		String dexPath = Environment.getExternalStorageDirectory()
				+ File.separator + "dynamic_temp.jar";
		// dex解压释放后的目录
		// String dexOutputDirs = getApplicationInfo().dataDir;
		File file = this.getDir("dex", 0);
		String dexOutputDirs = file.getAbsolutePath();
		// String dexOutputDirs =
		// Environment.getExternalStorageDirectory().toString();
		// 定义DexClassLoader
		// 第一个参数：是dex压缩文件的路径
		// 第二个参数：是dex解压缩后存放的目录
		// 第三个参数：是C/C++依赖的本地库文件目录,可以为null
		// 第四个参数：是上一级的类加载器
		DexClassLoader cl = new DexClassLoader(dexPath, dexOutputDirs, null,
				getClassLoader());

		/** 使用PathClassLoader方法加载类 */
		// 创建一个意图，用来找到指定的apk：这里的"com.dynamic.impl是指定apk中在AndroidMainfest.xml文件中定义的<action name="com.dynamic.impl"/>
		Intent intent = new Intent("com.example.impl", null);
		// 获得包管理器
		PackageManager pm = getPackageManager();
		List<ResolveInfo> resolveinfoes = pm.queryIntentActivities(intent, 0);
		// 获得指定的activity的信息
		ActivityInfo actInfo = resolveinfoes.get(0).activityInfo;
		// 获得apk的目录或者jar的目录
		String apkPath = actInfo.applicationInfo.sourceDir;
		// native代码的目录
		String libPath = actInfo.applicationInfo.nativeLibraryDir;
		// 创建类加载器，把dex加载到虚拟机中
		// 第一个参数：是指定apk安装的路径，这个路径要注意只能是通过actInfo.applicationInfo.sourceDir来获取
		// 第二个参数：是C/C++依赖的本地库文件目录,可以为null
		// 第三个参数：是上一级的类加载器
		PathClassLoader pcl = new PathClassLoader(apkPath, libPath,
				this.getClassLoader());

		// 加载类
		try {
			// com.dynamic.impl.Dynamic是动态类名
			Class<?> libProviderClazz;
			if (isDexClassLoader) {
				// 使用DexClassLoader加载类
				libProviderClazz = cl.loadClass("com.example.impl.Dynamic");
			}else {
				// 使用PathClassLoader加载类
				libProviderClazz = pcl.loadClass("com.example.impl.Dynamic");
			}
			lib = (IDynamic) libProviderClazz.newInstance();
			if (lib != null) {
				lib.init(MainActivity.this);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		/** 下面分别调用动态类中的方法 */
		showBannerBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (lib != null) {
					lib.showBanner();
				} else {
					Toast.makeText(getApplicationContext(), "类加载失败", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		showDialogBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (lib != null) {
					lib.showDialog();
				} else {
					Toast.makeText(getApplicationContext(), "类加载失败", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		showFullScreenBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (lib != null) {
					lib.showFullScreen();
				} else {
					Toast.makeText(getApplicationContext(), "类加载失败", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		
		final TextView textView = (TextView) findViewById(R.id.module);
		findViewById(R.id.select_dex).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						textView.setText("DexClassLoader");
						isDexClassLoader = true;
					}
				});
		findViewById(R.id.select_path).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						textView.setText("PathClassLoader");
						isDexClassLoader = false;
					}
				});
		
	}
}
