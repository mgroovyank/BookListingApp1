package com.example.booklistingapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class BooksAdapter extends ArrayAdapter<Book> {

    public BooksAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Book book = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView authorTextView = convertView.findViewById(R.id.author);
        authorTextView.setText(book.getAuthor());

        TextView titleTextView = convertView.findViewById(R.id.title);
        titleTextView.setText(book.getTitle());

        TextView langTextView = convertView.findViewById(R.id.language);
        langTextView.setText(book.getLang());

        TextView categoryTextView = convertView.findViewById(R.id.category);
        categoryTextView.setText(book.getCategory());

        ImageView image = convertView.findViewById(R.id.image);
        new ImageAsyncTask(image).execute(book.getImageUrl());

        return convertView;
    }

    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageAsyncTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
