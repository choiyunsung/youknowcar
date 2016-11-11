package org.androidtown.camera;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import android.Manifest;



public class MainActivity extends AppCompatActivity {
    public static  final int REQUEST_IMAGE_CAPTURE = 1001;
    private static final int RE = 1002;


    File file =null;
    ImageView imageView1;
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView1 = (ImageView)findViewById(R.id.imageView1);

        try {
            file = createFile();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        checkDangerousPermissions();
    }

    private  void checkDangerousPermissions(){
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i=0; i< permissions.length;i++){
            permissionCheck = ContextCompat.checkSelfPermission(this,permissions[i]);
             if (permissionCheck == PackageManager.PERMISSION_DENIED){
                 break;
             }
        }
        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"앱을 실행합니다.",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"확인이 안되었습니다.",Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,permissions[0])){
                Toast.makeText(this,"확인이 되었습니다",Toast.LENGTH_LONG).show();
            }else {
                ActivityCompat.requestPermissions(this,permissions,1);
            }
        }
    }
    public  void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        if(requestCode ==1){
            for (int i =0; i<permissions.length;i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 이건뭔가요", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, permissions[i] + " 하하 이건뭔가요?", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
    public void onButton1Clicked(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        if (intent.resolveActivity(getPackageManager()) !=null){
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }


    public void onButton2Clicked(View v) {
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        startActivityForResult(intent, RE);
    }


    private  File createFile() throws  IOException{
        String imageFileName ="test.jpg";
        File storageDir = Environment.getExternalStorageDirectory();
        File curFile = new File(storageDir,imageFileName);

        return  curFile;
    }

    protected void onActivityRequest(int requestCode, int resultCode,Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            if (file !=null){
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                imageView1.setImageBitmap(bitmap);

            }else {
                Toast.makeText(getApplicationContext(), "File is null.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
