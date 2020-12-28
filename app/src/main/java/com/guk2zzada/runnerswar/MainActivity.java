package com.guk2zzada.runnerswar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    Animation animSlideInLeft1, animSlideInLeft2, animSlideInLeft3;
    Animation animSlideOutRight1, animSlideOutRight2, animSlideOutRight3;

    Uri uriCapture;
    String absolutePath;

    EasyFlipView idCard;
    LinearLayout laySingle, layMulti;
    LineChart chart1, chart2;
    Button btnLogout;
    TextView txtSingleToday, txtSingleWeek;
    TextView txtMultiToday, txtMultiWeek;
    TextView txtName, txtInfo;
    ImageView imgProfile;
    View cardFront, cardBack;

    int[] arrSingle;
    int[] arrMulti;

    String strHeight;
    String strWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        cardFront = findViewById(R.id.cardFront);
        imgProfile = cardFront.findViewById(R.id.imgProfile);
        cardBack = findViewById(R.id.cardBack);
        idCard = findViewById(R.id.idCard);
        laySingle = findViewById(R.id.laySingle);
        layMulti = findViewById(R.id.layMulti);
        chart1 = findViewById(R.id.chart1);
        chart2 = findViewById(R.id.chart2);
        btnLogout = findViewById(R.id.btnLogout);
        txtSingleToday = findViewById(R.id.txtSingleToday);
        txtSingleWeek = findViewById(R.id.txtSingleWeek);
        txtMultiToday = findViewById(R.id.txtMultiToday);
        txtMultiWeek = findViewById(R.id.txtMultiWeek);
        txtName = cardFront.findViewById(R.id.txtName);
        txtInfo = cardFront.findViewById(R.id.txtInfo);

        animSlideInLeft1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_in_right);
        animSlideInLeft2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_in_right);
        animSlideInLeft3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_in_right);
        animSlideOutRight1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_out_right);
        animSlideOutRight2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_out_right);
        animSlideOutRight3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_out_right);

        animSlideInLeft1.setDuration(500);
        animSlideInLeft2.setStartOffset(250);
        animSlideInLeft2.setDuration(500);
        animSlideInLeft3.setStartOffset(500);
        animSlideInLeft3.setDuration(500);
        animSlideOutRight1.setDuration(500);
        animSlideOutRight2.setStartOffset(250);
        animSlideOutRight2.setDuration(500);
        animSlideOutRight3.setStartOffset(500);
        animSlideOutRight3.setDuration(500);

        //mainAsyncTask.execute("");

        arrSingle = new int[]{8640, 6102, 6416, 7610, 9713, 8124, 8412, 9415};
        arrMulti = new int[]{4248, 2342, 7312, 2354, 7143, 9107, 8113, 4652};

        chart1.setData(arrSingle);
        chart1.setChartStyle(getBaseContext(), LineChart.STYLE_ORANGE);
        chart2.setData(arrMulti);
        chart2.setChartStyle(getBaseContext(), LineChart.STYLE_BLUE);

        strHeight = GlobalVar.height;
        strWeight = GlobalVar.weight;

        CognitoUserPool userPool = new CognitoUserPool(getApplicationContext(), new AWSConfiguration(getApplicationContext()));
        GetDetailsHandler handler = new GetDetailsHandler() {
            @Override
            public void onSuccess(final CognitoUserDetails list) {
                // Successfully retrieved user details
                strHeight = list.getAttributes().getAttributes().get("custom:height");
                Log.e("height", strHeight);
            }

            @Override
            public void onFailure(final Exception exception) {
                // Failed to retrieve the user details, probe exception for the cause
                exception.printStackTrace();
            }
        };

        String who = AWSLoginModel.getSavedUserName(MainActivity.this);
        userPool.getUser(who).getDetails(handler);
        txtName.setText(who);
        txtInfo.setText(strHeight + "cm/" + strWeight + "kg");

        cardFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCard.flipTheView();
            }
        });

        cardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCard.flipTheView();
            }
        });

        laySingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Move move = new Move(MainActivity.this);
                move.startActivity(TestviewpagerActivity.class);
            }
        });

        laySingle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Move move = new Move(MainActivity.this);
                move.startActivity(TestmapActivity.class);

                return false;
            }
        });

        layMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Move move = new Move(MainActivity.this);
                move.startActivity(ModeMultiActivity.class);
            }
        });

        layMulti.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*Intent intent = new Intent(getApplicationContext(), RoomlistActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.hold);*/
                Move move = new Move(MainActivity.this);
                move.startActivity(RoomlistActivity.class);
                return false;
            }
        });

        imgProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPhotoByCamera();
                    }
                };

                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPhotoByAlbum();
                    }
                };

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("이미지 선택")
                        .setPositiveButton("촬영", cameraListener)
                        .setNeutralButton("앨범", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
                return false;
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdentityManager.getDefaultIdentityManager().signOut();
                finish();
            }
        });

        Log.e("AWS_TEST", "ID = " + userPool.getCurrentUser().getUserId());
        GlobalVar.id = userPool.getCurrentUser().getUserId();
        txtName.setText(userPool.getCurrentUser().getUserId());
    }

    /**
     * 카메라 촬영 후 사진 가져오기
     */
    public void getPhotoByCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        uriCapture = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCapture);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     * 앨범에서 이미지 가져오기
     */
    public void getPhotoByAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    /**
     * Bitmap을 저장하는 부분
     */
    private void storeCropImage(Bitmap bitmap, String filePath) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/runnerswar";
        File directory_runnerswar = new File(dirPath);

        if(!directory_runnerswar.exists()) {
            directory_runnerswar.mkdir();
        }

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
            out.flush();
            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode != RESULT_OK) {
            return;
        }

        switch(resultCode) {
            case PICK_FROM_ALBUM:
                uriCapture = data.getData();

            case PICK_FROM_CAMERA:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uriCapture, "image/*");
                intent.putExtra("outputX", 300);
                intent.putExtra("outputY", 400);
                intent.putExtra("aspectX", 3);
                intent.putExtra("aspectY", 4);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;

            case CROP_FROM_IMAGE:
                if(resultCode != RESULT_OK) {
                    return;
                }

                final Bundle extras = data.getExtras();

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/runnerswar/" + System.currentTimeMillis() + ".jpg";

                if(extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    imgProfile.setImageBitmap(photo);
                    storeCropImage(photo, filePath);
                    absolutePath = filePath;
                    break;
                }

                File f = new File(uriCapture.getPath());
                if(f.exists()) {
                    f.delete();
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //IdentityManager.getDefaultIdentityManager().signOut();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            idCard.startAnimation(animSlideInLeft1);
            laySingle.startAnimation(animSlideInLeft2);
            layMulti.startAnimation(animSlideInLeft3);
        }
    }
}
