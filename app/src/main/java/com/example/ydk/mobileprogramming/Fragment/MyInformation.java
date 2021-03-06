package com.example.ydk.mobileprogramming.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.MobileProgramming.R;
import com.example.ydk.mobileprogramming.Activity.SetImageActivity;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyInformation extends Fragment {

    private String IP = "61.255.8.214:27922";

    private Button btnUpdateUserImage, btnUpdateUserInfo;
    private TextView ID, KAKAO, SEX, TEXT;
    private ImageView imageView;
    private String mUserInfo;

    String cur_ID, cur_SEX;
    Bitmap bmImg;
    String PATH;

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "ID";
    private static final String TAG_NAME = "NAME";
    private static final String TAG_SEX = "SEX";
    private static final String TAG_INTRO = "INTRO";
    JSONArray peoples = null;

    boolean changing_intro;

    FTPClient client;
    String[] REQUIRED_PERMISSIONS  = { Manifest.permission.CAMERA, // 카메라
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};  // 외부 저장소

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.activity_my_information,container,false);

        ActivityCompat.requestPermissions( getActivity(), REQUIRED_PERMISSIONS, 0);

        final Intent get = getActivity().getIntent();

        changing_intro = false;
        cur_ID = getArguments().getString("myId");
        cur_SEX = getArguments().getString("SEX");

        btnUpdateUserImage = (Button) rootView.findViewById(R.id.change_image);
        btnUpdateUserInfo = (Button) rootView.findViewById(R.id.change_intro);

        ID = (TextView) rootView.findViewById(R.id.my_ID);
        KAKAO = (TextView) rootView.findViewById(R.id.my_KAKAO);
        SEX = (TextView) rootView.findViewById(R.id.my_SEX);
        TEXT = (TextView) rootView.findViewById(R.id.my_INTRO);

        getData("http://" + IP + "/mp/MyInfo.php?ID=" + cur_ID);

        imageView = (ImageView)rootView.findViewById(R.id.imageView);

        back imageload = new back();
        imageload.execute("http://" + IP + "/mp/image/" + cur_ID + ".jpg");

        btnUpdateUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),SetImageActivity.class);

                startActivityForResult(intent, 1001);
            }
        });

        btnUpdateUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                View layout = inflater.inflate(R.layout.updateinfo,null);
                builder.setView(layout);
                final EditText mEdit = (EditText)layout.findViewById(R.id.updateInfo);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mUserInfo = mEdit.getText().toString();
                        TEXT.setText("한 줄 소개 : " + mUserInfo);
                        changing_intro = true;
                        getData("http://" + IP + "/mp/ChangeIntro.php?ID=" + cur_ID + "&INTRO=" + mUserInfo);
                    }
                });
                builder.show();
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001)
        {	//if image change
            if(resultCode == -1)
            {
                PATH = data.getStringExtra("PATH");
                bmImg=(Bitmap) data.getParcelableExtra("bitmap");
                imageView.setImageBitmap(bmImg);
                Log.d("Connect", PATH);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean status = false;
                        status = ftpConnectAndUpload(PATH);
                        if(status == true) {
                            Log.d("Connect", "Upload Success");
                            return ;
                        }
                        else {
                            Log.d("Connect", "Upload failed");
                        }
                    }
                }).start();
            }
        }
    }

    public class back extends AsyncTask<String, Integer,Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try
            {
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);

                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return bmImg;
        }
    }

    final Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            imageView.setImageBitmap(bmImg);
        }
    };

    protected void showList()
    {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++)
            {
                JSONObject c = peoples.getJSONObject(i);
                ID.setText("ID : " + c.getString(TAG_ID));
                KAKAO.setText("카카오톡 ID : " + c.getString(TAG_NAME));
                SEX.setText("성별 : " + c.getString(TAG_SEX));
                TEXT.setText("한 줄 소개 : " + c.getString(TAG_INTRO));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if(changing_intro)
                {
                    changing_intro = false;
                }
                else
                {
                    myJSON = result;
                    showList();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public boolean ftpConnectAndUpload(String path)
    {
        client = new FTPClient();
        FileInputStream fis = null;
        Log.d("Connect", "Connect Call");
        try {
            client.connect("61.255.8.214", 27925);
            client.login("ghdms", "ghdms789");
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.setBufferSize(5 * 1024 * 1024);
            Log.d("Connect", "Connect true");

            File file = new File(path);
            fis = new FileInputStream(file);
            client.storeFile("/" + cur_ID + ".jpg", fis);
            fis.close();
            Log.d("Connect", "Upload true");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Connect", "Connect false");
        return false;
    }
}