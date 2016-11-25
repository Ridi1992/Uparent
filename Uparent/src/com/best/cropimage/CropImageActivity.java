package com.best.cropimage;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lester.uparent.Childinfo;
import com.lester.uparent.MainActivity;
import com.lester.uparent.R;
import com.lingwei.materialcalculator.view.CropImage;
import com.lingwei.materialcalculator.view.CropImageView;

public class CropImageActivity extends Activity implements OnClickListener {

    private CropImageView mImageView;
    private Bitmap mBitmap;
    private CropImage mCrop;
    private TextView mSave;
    private Button rotateLeft, rotateRight;
    private String mPath = "CropImageActivity";
    public int screenWidth = 0;
    public int screenHeight = 0;

    private ProgressBar mProgressBar;

    public static final int SHOW_PROGRESS = 2000;

    public static final int REMOVE_PROGRESS = 2001;

    private String TAG = "CropImageActivity";
    
    private String info="";

    /**
     * <pre>
     * 分发消息
     * </pre>
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
            case SHOW_PROGRESS:
                mProgressBar.setVisibility(View.VISIBLE);
                break;
            case REMOVE_PROGRESS:
                mHandler.removeMessages(SHOW_PROGRESS);
                mProgressBar.setVisibility(View.INVISIBLE);
                break;
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gl_modify_avatar);
        init(); // 初始化信息
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBitmap != null) {
            mBitmap = null;
        }
    }

    /**
     * <pre>
     * 初始化信息
     * </pre>
     */
    @SuppressLint("ShowToast")
    private void init() {
        getWindowWH();
        mPath = getIntent().getStringExtra("path");
        info=getIntent().getStringExtra("info");
        mImageView = (CropImageView) findViewById(R.id.gl_modify_avatar_image);
        mSave = (TextView) this.findViewById(R.id.gl_modify_avatar_save);
        findViewById(R.id.back).setOnClickListener(this);
//        mCancel = (Button) this.findViewById(R.id.gl_modify_avatar_cancel);
        rotateLeft = (Button) this.findViewById(R.id.gl_modify_avatar_rotate_left);
        rotateRight = (Button) this.findViewById(R.id.gl_modify_avatar_rotate_right);
        mSave.setOnClickListener(this);
//        mCancel.setOnClickListener(this);
        rotateLeft.setOnClickListener(this);
        rotateRight.setOnClickListener(this);
        try {
            mBitmap = createBitmap(mPath, screenWidth, screenHeight);
            if (mBitmap == null) {
//                UIHelper.ToastMessage(getApplicationContext(), "图片没有找到");
                finish();
            } else {
                resetImageView(mBitmap);
            }
        } catch (Exception e) {
//            UIHelper.ToastMessage(getApplicationContext(), "图片没有找到");
            finish();
        }
        addProgressbar(); // 加载progress
    }

    /**
     * <pre>
     * 获取屏幕的高和宽
     * </pre>
     * 
     */
    private void getWindowWH() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    /**
     * <pre>
     * 重置ImgeView
     * </pre>
     * 
     * @param b
     *            图片
     */
    private void resetImageView(Bitmap b) {
        mImageView.clear();
        mImageView.setImageBitmap(b);
        mImageView.setImageBitmapResetBase(b, true);
        mCrop = new CropImage(this, mImageView, mHandler);
        mCrop.crop(b);
    }

    /**
     * <pre>
     * 点击事件
     * </pre>
     */
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.back: 
            mCrop.cropCancel();
            finish();
            break;
        case R.id.gl_modify_avatar_save: // 保存
            String path = mCrop.saveToLocal(mCrop.cropAndSave());
//            Intent intent = new Intent();
//            intent.putExtra("path", path);
//            setResult(RESULT_OK, intent);
            if(info.equals("1")){
            	MainActivity.activity.mmeFragment.setImg(path);
            }else if(info.equals("2")){
            	Childinfo.activity.setImg(path);
            }
            finish();
            break;
        case R.id.gl_modify_avatar_rotate_left: // 左旋转
            mCrop.startRotate(270.f);
            break;
        case R.id.gl_modify_avatar_rotate_right: // 右旋转
            mCrop.startRotate(90.f);
            break;
        }
    }

    /**
     * <pre>
     * 加载progress
     * </pre>
     */
    protected void addProgressbar() {
        mProgressBar = new ProgressBar(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addContentView(mProgressBar, params);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * <pre>
     * 创建图片
     * </pre>
     * 
     * @param path
     *            图片地址
     * @param w
     *            宽度
     * @param h
     *            高度
     * @return 所需的图片
     */
    public Bitmap createBitmap(String path, int w, int h) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            Bitmap bitmap=BitmapFactory.decodeFile(path, newOpts);
            return rotaingBitmap(readPictureDegree(path),bitmap);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
    
    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    public static Bitmap rotaingBitmap(int angle , Bitmap bitmap) {  
        //旋转图片 动作   
        Matrix matrix = new Matrix();;  
        matrix.postRotate(angle);  
        System.out.println("angle2=" + angle);  
        // 创建新的图片   
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
        return resizedBitmap;  
    }
}