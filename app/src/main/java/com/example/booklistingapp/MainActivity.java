package com.example.booklistingapp;


import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final int BOOK_LOADER_ID = 1;
    private String mQueryUrl;

    private BooksAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private SearchView mSearch;
    private Button mGoBtn;

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearch = (SearchView) findViewById(R.id.search);
        mSearch.onActionViewExpanded();
        mSearch.setIconified(true);
        mSearch.setQueryHint("Enter a book keyword");


        final ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
            i++;

        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
            mEmptyStateTextView.setText(R.string.welcome);
        }

        mGoBtn = findViewById(R.id.go_btn);

        mGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQueryUrl = updateUrl(mSearch.getQuery().toString());
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    restartLoader();
                } else {
                    View loadingIndicator = findViewById(R.id.loading_indicator);
                    loadingIndicator.setVisibility(View.GONE);
                    mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });


        ListView booksListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        booksListView.setEmptyView(mEmptyStateTextView);
        mAdapter = new BooksAdapter(this, new ArrayList<Book>());
        booksListView.setAdapter(mAdapter);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Book currentBook = mAdapter.getItem(position);
                Uri bookUri = Uri.parse(currentBook.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
                startActivity(websiteIntent);

            }
        });
    }

    private void restartLoader() {
        mEmptyStateTextView.setVisibility(View.GONE);
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
    }

    private String updateUrl(String query) {
        if (query.contains(" ")) {
            query.replace(" ", "+");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("https://www.googleapis.com/books/v1/volumes?q=").append(query).append("&maxResults=8");
        return sb.toString();
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, mQueryUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        if (i == 1) {//welcome message for the first time
            mEmptyStateTextView.setText(R.string.welcome);
        } else {
            mEmptyStateTextView.setText(R.string.no_books);
        }

        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

}
