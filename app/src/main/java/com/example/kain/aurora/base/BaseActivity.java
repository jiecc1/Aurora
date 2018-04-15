package com.example.kain.aurora.base;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.kain.aurora.R;
import com.example.kain.aurora.api.SerialPortFinder;
import com.example.kain.aurora.bean.Ray;
import com.example.kain.aurora.utils.Application;
import com.example.kain.aurora.utils.FileUtil;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileInputStream;
import com.github.mjdev.libaums.partition.Partition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private Fragment currentFragment;

    private static UsbManager usbManager;
    private HashMap<String, UsbDevice> deviceList;  //设备列表
    private UsbDevice usbDevice;   //找到的USB设备

    //自定义U盘读写权限
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    //当前处接U盘列表
    private UsbMassStorageDevice[] storageDevices;
    //当前U盘所在文件目录
    private UsbFile cFolder;
    private final static String U_DISK_FILE_NAME = "u_disk.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        IntentFilter attachedUSBFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
//        attachedUSBFilter.addDataScheme("file");
//        registerReceiver(mUsbReceiver, attachedUSBFilter);
//
//        IntentFilter detachedUSBFilter = new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED);
//        registerReceiver(mUsbReceiver, detachedUSBFilter);

        registerUDiskReceiver();

//        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//        registerReceiver(mUsbPermissionActionReceiver, filter);

//        read();
//        initUsbDevice();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mUsbReceiver);
        unregisterReceiver(mOtgReceiver);
    }

//    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            makeToast("收到广播");
//            if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
//                makeToast("插入U盘");
//
//                Iterator<UsbDevice> iterator = deviceList.values().iterator();
//                while (iterator.hasNext()) {
//                    UsbDevice device = iterator.next();
//                    makeToast("vendor id:" + device.getVendorId()
//                            + "\nproduct id:" + device.getProductId());
//                    if (device.getProductId() == 25925 && device.getVendorId() == 2352) {
//                        if (usbManager.hasPermission(device)) {
//                            makeToast("U盘已授权");
////                            TODO 通信
//                        } else {
//                            makeToast("U盘未授权");
////                            tryGetUsbPermission();
//                        }
//                    }
//                }
//            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
//                makeToast("拔出U盘");
//            }
//        }
//    };

//    private BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            makeToast("请求USB权限中");
//            if (ACTION_USB_PERMISSION.equals(action)) {
//                context.unregisterReceiver(this);//解注册
//                synchronized (this) {
//                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                        if (null != usbDevice) {
//                            makeToast("已获取USB权限");
//                        }
//                    } else {
//                        //user choose NO for your previously popup window asking for grant permission for this usb device
//                        makeToast("USB权限已被拒绝");
//                    }
//                }
//            }
//        }
//    };

    //获取USB权限
//    private void tryGetUsbPermission() {
//        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
//
//        for (final UsbDevice usbDevice : usbManager.getDeviceList().values()) {
//            if (usbDevice.getVendorId() == 2352 && usbDevice.getProductId() == 25925) {
//                makeToast("已找到身份证USB");
//                makeToast("请求获取USB权限");
//                usbManager.requestPermission(usbDevice, mPermissionIntent);
//            } else {
//                makeToast("未找到身份证USB");
//            }
//        }
//    }

//    private void read() {
//        String[] result = null;
//        StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
//        try {
//            Method method = StorageManager.class.getMethod("getVolumePaths");
//            method.setAccessible(true);
//            try {
//                result = (String[]) method.invoke(storageManager);
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//            for (int i = 0; i < result.length; i++) {
//                Log.i("path", "read: " + result[i] + "\n");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void initUsbDevice() {
//        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        deviceList = usbManager.getDeviceList();
//        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
//        while (deviceIterator.hasNext()) {
//            UsbDevice device = deviceIterator.next();
//            //找到指定的设备
//            if (device.getVendorId() == 2352 && device.getProductId() == 25925) {
//                usbDevice = device;
//            }
//        }
//        findInterface();
//    }
//
//    private void findInterface() {
//
//    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    makeToast("保存成功");
                    break;
                case 101:
                    String txt = msg.obj.toString();
                    makeToast("读取到的数据是：" + txt);
                    break;
            }
        }
    };

    public void saveToUSB() {
        final String content = "测试保存数据";
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                saveText2UDisk(content);
            }
        });
    }

    private void saveText2UDisk(String content) {
        //项目中也把文件保存在了SD卡，其实可以直接把文本读取到U盘指定文件
        File file = FileUtil.getSaveFile(getPackageName()
                        + File.separator + FileUtil.DEFAULT_BIN_DIR,
                U_DISK_FILE_NAME);
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != cFolder) {
            FileUtil.saveSDFile2OTG(file, cFolder);
            mHandler.sendEmptyMessage(100);
        }
    }


    public void readFromUSB() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                readFromUDisk();
            }
        });
    }

    private void readFromUDisk() {
        UsbFile[] usbFiles = new UsbFile[0];
        try {
            usbFiles = cFolder.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != usbFiles && usbFiles.length > 0) {
            for (UsbFile usbFile : usbFiles) {
                if (usbFile.getName().equals(U_DISK_FILE_NAME)) {
                    readTxtFromUDisk(usbFile);
                }
            }
        }
    }

    private void readTxtFromUDisk(UsbFile usbFile) {
        UsbFile descFile = usbFile;
        //读取文件内容
        InputStream is = new UsbFileInputStream(descFile);
        //读取秘钥中的数据进行匹配
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String read;
            while ((read = bufferedReader.readLine()) != null) {
                sb.append(read);
            }
            Message msg = mHandler.obtainMessage();
            msg.what = 101;
            msg.obj = read;
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerUDiskReceiver() {
        //监听otg插入 拔出
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mOtgReceiver, usbDeviceStateFilter);
        //注册监听自定义广播
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mOtgReceiver, filter);
    }

    private BroadcastReceiver mOtgReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case ACTION_USB_PERMISSION://接受到自定义广播
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    //允许权限申请
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbDevice != null) {
                            //用户已授权，可以进行读取操作
                            readDevice(getUsbMass(usbDevice));
                        } else {
                            makeToast("没有插入U盘");
                        }
                    } else {
                        makeToast("未获取到U盘权限");
                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_ATTACHED://接收到U盘设备插入广播
                    UsbDevice device_add = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device_add != null) {
                        //接收到U盘插入广播，尝试读取U盘设备数据
                        redUDiskDevsList();
                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED://接收到U盘设设备拔出广播
                    makeToast("U盘已拔出");
                    break;
            }
        }
    };

    private void redUDiskDevsList() {
        //设备管理器
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        //获取U盘存储设备
        storageDevices = UsbMassStorageDevice.getMassStorageDevices(this);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        //一般手机只有1个OTG插口
        for (UsbMassStorageDevice device : storageDevices) {
            //读取设备是否有权限
            if (usbManager.hasPermission(device.getUsbDevice())) {
                readDevice(device);
            } else {
                //没有权限，进行申请
                usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
            }
        }
        if (storageDevices.length == 0) {
            makeToast("请插入可用的U盘");
        }
    }

    private UsbMassStorageDevice getUsbMass(UsbDevice usbDevice) {
        for (UsbMassStorageDevice device : storageDevices) {
            if (usbDevice.equals(device.getUsbDevice())) {
                return device;
            }
        }
        return null;
    }

    private void readDevice(UsbMassStorageDevice device) {
        try {
            device.init();//初始化
            //设备分区
            Partition partition = device.getPartitions().get(0);
            //文件系统
            FileSystem currentFs = partition.getFileSystem();
            currentFs.getVolumeLabel();//可以获取到设备的标识
            //通过FileSystem可以获取当前U盘的一些存储信息，包括剩余空间大小，容量等等
            Log.e("Capacity: ", currentFs.getCapacity() + "");
            Log.e("Occupied Space: ", currentFs.getOccupiedSpace() + "");
            Log.e("Free Space: ", currentFs.getFreeSpace() + "");
            Log.e("Chunk size: ", currentFs.getChunkSize() + "");
            cFolder = currentFs.getRootDirectory();//设置当前文件对象为根目录
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Application mApplication;
    private SerialPortFinder mSerialPortFinder;

    public void findSerialPort(){
        mApplication = (Application) getApplication();
        mSerialPortFinder = mApplication.mSerialPortFinder;

        // Devices
        String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
        for (int i = 0; i < entries.length; i++) {
            Log.i("entry",entries[i]);
            Log.i("entry",entryValues[i]);
        }
    }

    byte mValueToSend;
    protected OutputStream mOutputStream;
    boolean mByteReceivedBack;
    Object mByteReceivedBackSemaphore = new Object();

    Integer mOutgoing = new Integer(0);

    private class SendingThread extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                synchronized (mByteReceivedBackSemaphore) {
                    mByteReceivedBack = false;
                    try {
                        if (mOutputStream != null) {
                            mOutputStream.write(mValueToSend);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    mOutgoing++;
                    // Wait for 100ms before sending next byte, or as soon as
                    // the sent byte has been read back.
                    try {
                        mByteReceivedBackSemaphore.wait(100);
                        if (mByteReceivedBack == true) {
                            // Byte has been received
                        } else {
                            // Timeout
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                            }
                        });
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }



    public void addOrShowFragments(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (currentFragment != null && fragment.isAdded()) {
            if (currentFragment != fragment) {
                transaction.hide(currentFragment).show(fragment).commit();
            }
        } else {
            if (currentFragment == null && currentFragment != fragment) {
                transaction.add(R.id.layout_frag, fragment).commit();
            } else if (currentFragment != null && currentFragment != fragment) {
                transaction.add(R.id.layout_frag, fragment).hide(currentFragment).commit();
            }
        }
        currentFragment = fragment;
    }

    public List<Ray> findRays(int type) {
        List<Ray> rays;
        rays = DataSupport.where("type = ?", String.valueOf(type)).find(Ray.class);
        return rays;
    }

    public List<Ray> findSelectRays(int type) {
        List<Ray> rays;
        rays = DataSupport.where("type = ? and select = ?", String.valueOf(type), "1").find(Ray.class);
        return rays;
    }

    public void updateAllRays(int type, ContentValues values) {
        DataSupport.updateAll(Ray.class, values, "type = ?", String.valueOf(type));
    }

    public void deleteRays(int type) {
        DataSupport.deleteAll(Ray.class, "type = ?", String.valueOf(type));
    }

    public void deleteSelectRays(int type) {
        DataSupport.deleteAll(Ray.class, "type = ? and select = ?", String.valueOf(type), "1");
    }

    public void saveToSDCard(String fileName, List<Ray> rays) {

        File outPutFile = new File("sdcard/Aurora/" + fileName + ".txt");
        Environment.getExternalStorageDirectory();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Gson gson = new Gson();
            String outPutJsonObject = gson.toJson(rays);
            try {
                FileOutputStream fos = new FileOutputStream(outPutFile);
                fos.write(outPutJsonObject.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "SD card has not inserted", Toast.LENGTH_SHORT).show();
        }
    }

    public List getFromSDCard(String fileName) {

        String json = null;
        List<Ray> rays;
        File inPutFile = new File("sdcard/Aurora/" + fileName + ".txt");
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                FileInputStream fis = new FileInputStream(inPutFile);
                //把字节流转化为字符流
                BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
                //读取文件
                json = buffer.readLine();
                Toast.makeText(this, "读取成功啦！！！", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            rays = gson.fromJson(json, new TypeToken<List<Ray>>() {
            }.getType());
            return rays;
        } else {
            Toast.makeText(this, "SD card has not inserted", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
