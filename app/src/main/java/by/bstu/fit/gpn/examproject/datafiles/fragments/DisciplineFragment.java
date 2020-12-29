package by.bstu.fit.gpn.examproject.datafiles.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import by.bstu.fit.gpn.examproject.MainActivity;
import by.bstu.fit.gpn.examproject.R;
import by.bstu.fit.gpn.examproject.datafiles.adapters.DisciplineAdapter;
import by.bstu.fit.gpn.examproject.datafiles.databases.DatabaseHelper;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.Discipline;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.Student;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.CourseWork;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.Credit;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.Exam;

public class DisciplineFragment extends Fragment {

    int changeManager;
    View view;
    ArrayList<Discipline> disciplineList;
    RecyclerView.LayoutManager layoutManager;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_discipline_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
                        new DisciplineFragment.DeleteFromBD().doInBackground(i);
                        cursor = new SelectAllFromBD().doInBackground(DatabaseHelper.TABLE_DISCIPLINE_NAME);// cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_RECIPE_NAME, null);
                        if (cursor.moveToFirst()) {
                            do {
                                if(cursor.getString(2).compareTo("Экзамен") == 0)
                                    disciplineList.add(new Exam(cursor.getInt(0), cursor.getString(1)));
                                else if(cursor.getString(2).compareTo("Зачет") == 0)
                                    disciplineList.add(new Credit(cursor.getInt(0), cursor.getString(1)));
                                else if(cursor.getString(2).compareTo("Курсовая") == 0)
                                    disciplineList.add(new CourseWork(cursor.getInt(0), cursor.getString(1)));
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

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                addItem();
                return true;
            case R.id.item2:
                disciplineList = new ArrayList<>();
                cursor = new SelectAllFromBD().doInBackground(DatabaseHelper.TABLE_DISCIPLINE_NAME);
                if (cursor.moveToFirst()) {
                    do {
                        if(cursor.getString(2).compareTo("Экзамен") == 0)
                            disciplineList.add(new Exam(cursor.getInt(0), cursor.getString(1)));
                        else if(cursor.getString(2).compareTo("Зачет") == 0)
                            disciplineList.add(new Credit(cursor.getInt(0), cursor.getString(1)));
                        else if(cursor.getString(2).compareTo("Курсовая") == 0)
                            disciplineList.add(new CourseWork(cursor.getInt(0), cursor.getString(1)));
                    } while (cursor.moveToNext());
                }
                newAdapterForList();
                return true;
            case R.id.item3:
                MainActivity.fTrans = DisciplineFragment.super.getActivity().getSupportFragmentManager().beginTransaction();
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
        view = inflater.inflate(R.layout.fragment_plan_list, container, false);

        changeManager = 1;
        linearLayoutManager = new LinearLayoutManager(super.getActivity());
        gridLayoutManager = new GridLayoutManager(super.getActivity(), 2);
        layoutManager = linearLayoutManager;
        disciplineList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(super.getActivity().getApplicationContext());
        db = databaseHelper.getReadableDatabase();

        cursor = new SelectAllFromBD().doInBackground(DatabaseHelper.TABLE_DISCIPLINE_NAME);
        if (cursor.moveToFirst()) {
            do {

                if(cursor.getString(2).compareTo("Экзамен") == 0)
                    disciplineList.add(new Exam(cursor.getInt(0), cursor.getString(1)));
                else if(cursor.getString(2).compareTo("Зачет") == 0)
                    disciplineList.add(new Credit(cursor.getInt(0), cursor.getString(1)));
                else if(cursor.getString(2).compareTo("Курсовая") == 0)
                    disciplineList.add(new CourseWork(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        newAdapterForList();

        return view;
    }

    public void newAdapterForList() {
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_discipline);
        DisciplineAdapter adapter = new DisciplineAdapter(view.getContext(), disciplineList);
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(layoutManager);
        registerForContextMenu(recyclerView);
    }

    View dialogView;


    public void addItem(){
        LayoutInflater li = LayoutInflater.from(view.getContext());
        dialogView = li.inflate(R.layout.add_discipline, null);
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
                                String title = ((EditText) dialogView.findViewById(R.id.title)).getText().toString();
                                String types = ((Spinner) dialogView.findViewById(R.id.discipline_type)).getSelectedItem().toString();
                                int id;
                                if (!disciplineList.isEmpty())
                                    id = disciplineList.get(disciplineList.size() - 1).get_ID() + 1;
                                else
                                    id = 0;

                                db = databaseHelper.getWritableDatabase();

                                if(types.compareTo("Экзамен") == 0)
                                    new SaveInBD().doInBackground(new Exam(id, title));
                                else if(types.compareTo("Зачет") == 0)
                                    new SaveInBD().doInBackground(new Credit(id, title));
                                else if(types.compareTo("Курсовая") == 0)
                                    new SaveInBD().doInBackground(new CourseWork(id, title));

                                db = databaseHelper.getReadableDatabase();

                                disciplineList = new ArrayList<>();
                                cursor = new SelectAllFromBD().doInBackground(DatabaseHelper.TABLE_DISCIPLINE_NAME);// cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_RECIPE_NAME, null);
                                if (cursor.moveToFirst()) {
                                    do {
                                        if(cursor.getString(2).compareTo("Экзамен") == 0)
                                            disciplineList.add(new Exam(cursor.getInt(0), cursor.getString(1)));
                                        else if(cursor.getString(2).compareTo("Зачет") == 0)
                                            disciplineList.add(new Credit(cursor.getInt(0), cursor.getString(1)));
                                        else if(cursor.getString(2).compareTo("Курсовая") == 0)
                                            disciplineList.add(new CourseWork(cursor.getInt(0), cursor.getString(1)));
                                    } while (cursor.moveToNext());
                                }

                                ArrayList<Student> studentList = new ArrayList<>();
                                cursor = db.rawQuery("select * from Students", null);
                                if (cursor.moveToFirst()) {
                                    do {
                                        studentList.add(new Student(cursor.getInt(0), cursor.getString(1)));
                                    } while (cursor.moveToNext());
                                    for (Student student : studentList) {
                                        databaseHelper.insertStudyPlan(db, student.getID(), disciplineList.size(), "", "");
                                    }
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

    class SaveInBD extends AsyncTask<Discipline, Void, Void> {
        @Override
        protected Void doInBackground(Discipline... disciplines) {
            String type = "";
            if (disciplines[0] instanceof Exam) {
                type += "Экзамен";
                Exam exam = (Exam) disciplines[0];
                databaseHelper.insertDiscipline(db, exam.get_name_of_discipline(), type );
            } else if (disciplines[0] instanceof Credit){
                type += "Зачет";
                Credit credit = (Credit) disciplines[0];
                databaseHelper.insertDiscipline(db, credit.get_name_of_discipline(), type );
            }
            else if(disciplines[0] instanceof CourseWork) {
                type += "Курсовая";
                CourseWork courseWork = (CourseWork) disciplines[0];
                databaseHelper.insertDiscipline(db, courseWork.get_name_of_discipline(), type );

            }
            return null;

        }
    }

    class DeleteFromBD extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            databaseHelper.deleteDiscipline(db, integers[0]);
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
