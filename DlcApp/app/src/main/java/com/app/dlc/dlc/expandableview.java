package com.app.dlc.dlc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class expandableview extends AppCompatActivity {
    ExpandableRelativeLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_expandable_layout_listview_example);
    }
    public void expandableButton1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        tv = (TextView) findViewById(R.id.tv);
        expandableLayout1.toggle(); // toggle expand and collapse
        tv.setText("Test");
    }

    public void expandableButton2(View view) {
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle(); // toggle expand and collapse
    }

    public void expandableButton3(View view) {
        expandableLayout3 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout3);
        expandableLayout3.toggle(); // toggle expand and collapse
    }

    public void expandableButton4(View view) {
        expandableLayout4 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout4);
        expandableLayout4.toggle(); // toggle expand and collapse
    }
}
