package by.bstu.fit.gpn.examproject.datafiles.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import by.bstu.fit.gpn.examproject.MainActivity;
import by.bstu.fit.gpn.examproject.R;
import by.bstu.fit.gpn.examproject.datafiles.adapters.DisciplineAdapter;
import by.bstu.fit.gpn.examproject.datafiles.adapters.StudentAdapter;
import by.bstu.fit.gpn.examproject.datafiles.databases.DatabaseHelper;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.Discipline;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.Student;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.StudyPlan;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.CourseWork;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.Credit;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.Exam;

public class StudentFragment extends Fragment {
    int changeManager;
    DisciplineFragment disciplineFragment;
    StudentFragment studentFragment;
    View view;
    String path;
    ArrayList<Student> studentList;
    FragmentTransaction fragmentTransaction;
    RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Уверены, что хотите удалить рецепт?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new StudentFragment.DeleteFromBD().doInBackground(i);
                        studentList = new ArrayList<>();
                        cursor = new StudentFragment.SelectAllFromBD().doInBackground(DatabaseHelper.TABLE_STUDENT_NAME);// cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_RECIPE_NAME, null);
                        if (cursor.moveToFirst()) {
                            do {
                                studentList.add(new Student(cursor.getInt(0), cursor.getString(1)));
                            } while (cursor.moveToNext());
                        }
                        newAdapterForList();

                    }
                });
                builder.setNegativeButton("Нет", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_student_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                addItem();
                return true;
            case R.id.item2:
                studentList = new ArrayList<>();
                cursor = new StudentFragment.SelectAllFromBD().doInBackground(DatabaseHelper.TABLE_STUDENT_NAME);
                if (cursor.moveToFirst()) {
                    do {
                        studentList.add(new Student(cursor.getInt(0), cursor.getString(1)));
                    } while (cursor.moveToNext());
                }
                newAdapterForList();
                return true;
            case R.id.item3:
                MainActivity.fTrans = StudentFragment.super.getActivity().getSupportFragmentManager().beginTransaction();
                DisciplineFragment fragment = new DisciplineFragment();
                MainActivity.fTrans.replace(R.id.listFragment, fragment);
                MainActivity.fTrans.addToBackStack(null);
                MainActivity.fTrans.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student_list, container, false);

        changeManager = 1;
        linearLayoutManager = new LinearLayoutManager(super.getActivity());
        gridLayoutManager = new GridLayoutManager(super.getActivity(), 2);
        layoutManager = linearLayoutManager;
        studentList = new ArrayList<>();

        path = super.getActivity().getFilesDir().getPath();

        databaseHelper = new DatabaseHelper(super.getActivity().getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        cursor = new StudentFragment.SelectAllFromBD().doInBackground(DatabaseHelper.TABLE_STUDENT_NAME);
        if (cursor.moveToFirst()) {
            do {
                studentList.add(new Student(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        newAdapterForList();

        return view;
    }

    public void newAdapterForList() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_student);
        StudentAdapter adapter = new StudentAdapter(view.getContext(), studentList, new StudentAdapter.OnStudentClickListener() {
            @Override
            public void onStudentClick(Student student) {
                MainActivity.fTrans = StudentFragment.super.getActivity().getSupportFragmentManager().beginTransaction();
                StudyPlanFragment fragment = new StudyPlanFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("ID", student.getID());
                bundle.putString("Name", student.get_fullname());
                fragment.setArguments(bundle);
                MainActivity.fTrans.replace(R.id.listFragment, fragment);
                MainActivity.fTrans.addToBackStack(null);
                MainActivity.fTrans.commit();
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(recyclerView);
    }

    View dialogView;


    public void addItem(){
        LayoutInflater li = LayoutInflater.from(view.getContext());
        dialogView = li.inflate(R.layout.add_student, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(view.getContext());

        mDialogBuilder.setView(dialogView);


        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Сохранить", null)
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        final AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(dialogView.getContext());
                        builder.setMessage("Уверены, что хотите добавить?");
                        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String title = ((EditText) dialogView.findViewById(R.id.fullname)).getText().toString();
                                int id;
                                if (!studentList.isEmpty())
                                    id = studentList.get(studentList.size() - 1).getID() + 1;
                                else
                                    id = 0;

                                db = databaseHelper.getWritableDatabase();

                                new SaveInBD().doInBackground(new Student(id, title));

                                db = databaseHelper.getReadableDatabase();

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
                                        databaseHelper.insertStudyPlan(db, id, disc.get_ID(), "", "");
                                    }
                                }

                                studentList = new ArrayList<>();
                                cursor = new StudentFragment.SelectAllFromBD().doInBackground(DatabaseHelper.TABLE_STUDENT_NAME);// cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_RECIPE_NAME, null);
                                if (cursor.moveToFirst()) {
                                    do {
                                        studentList.add(new Student(cursor.getInt(0), cursor.getString(1)));
                                    } while (cursor.moveToNext());
                                }
                                newAdapterForList();
                                alertDialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("Нет", null);
                        AlertDialog alertDialogForCheck = builder.create();
                        alertDialogForCheck.show();
                    }
                });
            }
        });
        alertDialog.show();
    }

    class SaveInBD extends AsyncTask<Student, Void, Void> {
        @Override
        protected Void doInBackground(Student... students) {
            Student student = students[0];
            databaseHelper.insertStudent(db, student.getID(), student.get_fullname());
            return null;
        }
    }

    class DeleteFromBD extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            databaseHelper.deleteStudent(db, integers[0]);
            return null;
        }
    }

    class SelectAllFromBD extends AsyncTask<String, Void, Cursor> {
        @Override
        protected Cursor doInBackground(String... strings) {
            return db.rawQuery("select * from " + strings[0], null);
        }
    }
}
