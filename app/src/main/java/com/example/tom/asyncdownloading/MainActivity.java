package com.example.tom.asyncdownloading;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button asyncButton;
    private ImageView downloadImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        asyncButton = (Button)findViewById(R.id.asyncButton);
        downloadImageView = (ImageView)findViewById(R.id.imageView);

        asyncButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == asyncButton.getId()) {
            downloadAsync();
        }
    }

    public void downloadAsync() {
        new FileDownloaderTask().execute("https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");
    }

    private class FileDownloaderTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            downloadImageView.setImageBitmap(result);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadFile(params[0]);
        }

        private Bitmap downloadFile(String urlString) {
            URL downloadURL= null;
            try {
                downloadURL = new URL(urlString);
                URLConnection conn = downloadURL.openConnection();

                InputStream is = conn.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead, totalByteRead = 0;
                byte[] data = new byte[2048];
                while((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                    totalByteRead = nRead;
                }
                byte[] image = buffer.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                Log.v("bitmap", String.valueOf(bitmap));
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
