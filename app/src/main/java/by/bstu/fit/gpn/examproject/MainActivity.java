package by.bstu.fit.gpn.examproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;

import by.bstu.fit.gpn.examproject.datafiles.fragments.DisciplineFragment;
import by.bstu.fit.gpn.examproject.datafiles.fragments.StudentFragment;

public class MainActivity extends AppCompatActivity{

    public static FragmentTransaction fTrans;
    Fragment frag1, frag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frag1 = new DisciplineFragment();

        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.listFragment, frag1);
        fTrans.addToBackStack(null);
        fTrans.commit();

    }

}