package com.quy.buvape;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.BubbleToggleView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.quy.buvape.view.ChatFragment;
import com.quy.buvape.view.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    BubbleNavigationLinearView bottom_navigation_view_linear;

    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment selectFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom_navigation_view_linear = findViewById(R.id.bottom_navigation_view_linear);

        frameLayout = findViewById(R.id.frame_container);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ContactFragment contactFragment = new ContactFragment();
        fragmentTransaction.replace(frameLayout.getId(),contactFragment).commit();

        bottom_navigation_view_linear.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                switch (position){
                    case 0:
                        selectFragment = new ContactFragment();
                        break;
  //                  case 1:
 //                       selectFragment = new ChatFragment();
//                        break;
   //                 case 2:
//                        selectFragment = new ContactFragment();
//                        break;
                    case 1:
                        selectFragment = new ChatFragment();
                        break;
                    case 2:
                        selectFragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(frameLayout.getId(),selectFragment).commit();
            }
        });

    }
}