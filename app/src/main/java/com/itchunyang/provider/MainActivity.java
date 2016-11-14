package com.itchunyang.provider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * ContentProvider中的URI
 * content://  com.xxx.myapp/ table/100
 * 固定的       Authority      path
 *
 * Authority：授权信息，用以区别不同的ContentProvider；
 * Path：可以用来表示我们要操作的数据，路径的构建应根据业务而定 例如：
 * 要操作person表中id为10的记录，可以构建这样的路径:/person/10
 * 要操作person表中id为10的记录的name字段， person/10/name
 * 要操作person表中的所有记录，可以构建这样的路径:/person
 * 要操作xxx表中的记录，可以构建这样的路径:/xxx
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
