package com.example.yllaaa.aidlclienttest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.yangl.androidsample.Book;
import com.example.yangl.androidsample.BookController;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String TAG = "client";
    private BookController bookController;
    private boolean connected;
    private List<Book> booklist;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bookController = BookController.Stub.asInterface(iBinder);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            connected = false;
        }
    };

    private void bindService(){
        Intent intent = new Intent();
        intent.setPackage("com.example.yangl.androidsample");
        intent.setAction("com.example.yangl.androidsample.action");
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindService();

        View getBookListView = findViewById(R.id.get_book_list);
        View addBookView = findViewById(R.id.add_book);

        getBookListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connected){
                    try {
                        booklist = bookController.getBookList();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    log();
                }
            }
        });

        addBookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connected){
                    Book book = new Book("这是一本新书 InOut");
                    try {
                        bookController.addBookInout(book);
                        Log.d(TAG, "向服务器以InOut方式添加了一本新书");
                        Log.d(TAG, "新书名：" + book.getName());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void log() {
        for (Book book : booklist) {
            Log.d(TAG, book.getName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(connected){
            unbindService(serviceConnection);
        }
    }
}
