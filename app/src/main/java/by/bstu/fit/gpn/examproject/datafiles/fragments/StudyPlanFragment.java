package by.bstu.fit.gpn.examproject.datafiles.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import by.bstu.fit.gpn.examproject.MainActivity;
import by.bstu.fit.gpn.examproject.R;
import by.bstu.fit.gpn.examproject.datafiles.adapters.PaperAdapter;
import by.bstu.fit.gpn.examproject.datafiles.databases.DatabaseHelper;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.Discipline;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.Student;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.StudyPlan;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.CourseWork;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.Credit;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.Exam;

public class StudyPlanFragment extends Fragment {

    int IDStudent;
    int changeManager;
    View view;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private Date date = new Date();
    PaperAdapter adapter;
    ArrayList<StudyPlan> studyPlanList;
    RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        int i = 0;
        for (StudyPlan studyPlan : studyPlanList) {
            studyPlan.getDiscipline().set_mark(adapter.getStrings()[i]);
            i++;
            new StudyPlanFragment.UpdateBD().doInBackground(studyPlan);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        int i = 0;
        for (StudyPlan studyPlan : studyPlanList) {
            studyPlan.getDiscipline().set_mark(adapter.getStrings()[i]);
            i++;
            new StudyPlanFragment.UpdateBD().doInBackground(studyPlan);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_study_plan_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                int i = 0;
                for (StudyPlan studyPlan : studyPlanList) {
                    studyPlan.getDiscipline().set_mark(adapter.getStrings()[i]);
                    i++;
                    new StudyPlanFragment.UpdateBD().doInBackground(studyPlan);
                }



                MainActivity.fTrans = StudyPlanFragment.super.getActivity().getSupportFragmentManager().beginTransaction();
                StudentFragment fragment = new StudentFragment();
                MainActivity.fTrans.replace(R.id.listFragment, fragment);
                MainActivity.fTrans.addToBackStack(null);
                MainActivity.fTrans.commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_students_papers, container, false);

        changeManager = 1;
        linearLayoutManager = new LinearLayoutManager(super.getActivity());
        gridLayoutManager = new GridLayoutManager(super.getActivity(), 2);
        layoutManager = linearLayoutManager;
        studyPlanList = new ArrayList<>();



        Bundle arguments = getArguments();
        if (arguments != null) {
            IDStudent = arguments.getInt("ID");
        }

        databaseHelper = new DatabaseHelper(super.getActivity().getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        cursor = new StudyPlanFragment.SelectAllFromBD().doInBackground();
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getString(5).compareTo("Экзамен") == 0)
                    studyPlanList.add(new StudyPlan(cursor.getInt(0), cursor.getString(1), new Exam(cursor.getInt(6), cursor.getString(2), cursor.getString(3), cursor.getString(4))));
                else if(cursor.getString(5).compareTo("Зачет") == 0)
                    studyPlanList.add(new StudyPlan(cursor.getInt(0), cursor.getString(1), new Exam(cursor.getInt(6), cursor.getString(2), cursor.getString(3), cursor.getString(4))));
                else if(cursor.getString(5).compareTo("Курсовая") == 0)
                    studyPlanList.add(new StudyPlan(cursor.getInt(0), cursor.getString(1), new Exam(cursor.getInt(6), cursor.getString(2), cursor.getString(3), cursor.getString(4))));
            } while (cursor.moveToNext());
        }
        newAdapterForList();

        return view;
    }

    public void newAdapterForList() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_papers);
        adapter = new PaperAdapter(view.getContext(), studyPlanList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(recyclerView);
    }

    class SaveInBD extends AsyncTask<StudyPlan, Void, Void> {
        @Override
        protected Void doInBackground(StudyPlan... studyPlans) {
            ArrayList<Discipline> disciplineList = new ArrayList<>();
            cursor = db.rawQuery("select * from Discipline", null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(2).compareTo("Экзамен") == 0)
                        disciplineList.add(new Exam(cursor.getInt(0), cursor.getString(1)));
                    else if (cursor.getString(2).compareTo("Зачет") == 0)
                        disciplineList.add(new Credit(cursor.getInt(0), cursor.getString(1)));
                    else if (cursor.getString(2).compareTo("Курсовая") == 0)
                        disciplineList.add(new CourseWork(cursor.getInt(0), cursor.getString(1)));
                } while (cursor.moveToNext());

                for (Discipline disc : disciplineList) {
                    databaseHelper.insertStudyPlan(db, IDStudent, disc.get_ID(), "", "");
                }

            }
            return null;
        }
    }


    class UpdateBD extends AsyncTask<StudyPlan, Void, Void>{
        @Override
        protected Void doInBackground(StudyPlan... studyPlans) {
            StudyPlan studyPlan = studyPlans[0];
            databaseHelper.updateStudyPlan(db, studyPlan.getID(), studyPlan.getDiscipline().get_mark(), studyPlan.getDiscipline().get_date());
            return null;
        }
    }

    class SelectAllFromBD extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... voids) {
            return db.rawQuery("select StudyPlan.ID, Students.FullName, Discipline.Discipline, "
                    + "StudyPlan.Mark, StudyPlan.Date, Discipline.Type, Discipline.ID From StudyPlan inner join Students "
                    + "on StudyPlan.ID_Student = Students.ID "
                    + "inner join Discipline "
                    + "on StudyPlan.ID_Discipline = Discipline.ID "
                    + "where Students.ID = " + IDStudent, null);
        }
    }
}
