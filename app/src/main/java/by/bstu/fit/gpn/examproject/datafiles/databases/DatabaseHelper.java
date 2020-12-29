package by.bstu.fit.gpn.examproject.datafiles.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLInput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME = "ExamProject.db";
    static int SCHEMA = 1;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private Date date = new Date();

    public static String TABLE_STUDY_PLAN_NAME = "StudyPlan";
    public static String TABLE_DISCIPLINE_NAME = "Discipline";
    public static String TABLE_STUDENT_NAME = "Students";

    static String DISCIPLINE_COLUMN_ID = "ID";
    static String DISCIPLINE_COLUMN_NAME = "Discipline";
    static String DISCIPLINE_COLUMN_TYPE = "Type";

    static String STUDENT_COLUMN_ID = "ID";
    static String STUDENT_COLUMN_NAME = "FullName";

    static String STUDY_PLAN_COLUMN_ID = "ID";
    static String STUDY_PLAN_COLUMN_ID_DISCIPLINE = "ID_Discipline";
    static String STUDY_PLAN_COLUMN_ID_STUDENT = "ID_Student";
    static String STUDY_PLAN_COLUMN_MARK = "Mark";
    static String STUDY_PLAN_COLUMN_DATE = "Date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableDiscipline(db);
        createTableStudent(db);
        createTableStudyPlan(db);
    }

    private void createTableDiscipline(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_DISCIPLINE_NAME + " ("
                + DISCIPLINE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DISCIPLINE_COLUMN_NAME + " TEXT,"
                + DISCIPLINE_COLUMN_TYPE + " TEXT"
                + ");");
    }

    private void createTableStudent(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_STUDENT_NAME + " ("
                + STUDENT_COLUMN_ID + " INTEGER PRIMARY KEY,"
                + STUDENT_COLUMN_NAME + " TEXT"
                + ");");
    }

    private void createTableStudyPlan(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_STUDY_PLAN_NAME + " ("
                + STUDY_PLAN_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + STUDY_PLAN_COLUMN_ID_DISCIPLINE + " INTEGER,"
                + STUDY_PLAN_COLUMN_ID_STUDENT + " INTEGER,"
                + STUDY_PLAN_COLUMN_MARK + " TEXT,"
                + STUDY_PLAN_COLUMN_DATE + " TEXT,"
                + "FOREIGN KEY(" + STUDY_PLAN_COLUMN_ID_DISCIPLINE + ") REFERENCES " + TABLE_DISCIPLINE_NAME + "(" + DISCIPLINE_COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE,"
                + "FOREIGN KEY(" + STUDY_PLAN_COLUMN_ID_STUDENT + ") REFERENCES " + TABLE_STUDENT_NAME + "(" + STUDENT_COLUMN_ID + ") ON UPDATE CASCADE ON DELETE CASCADE"
                + ");");

        db.execSQL("CREATE TRIGGER IF NOT EXISTS UPDATE_DATE_TRIGGER" +
                " AFTER UPDATE ON " + TABLE_STUDY_PLAN_NAME +
                " BEGIN " +
                " UPDATE " + TABLE_STUDY_PLAN_NAME + " SET Date = '"+ dateFormat.format(date) + "' WHERE StudyPlan.Mark = NEW.Mark; " +
                "END");
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertDiscipline(SQLiteDatabase db, String Name, String Type){
        db.execSQL("INSERT INTO " + TABLE_DISCIPLINE_NAME + "(" + DISCIPLINE_COLUMN_NAME + ", " + DISCIPLINE_COLUMN_TYPE + ")" +
                " VALUES ('" + Name + "', '" + Type + "');");
    }

    public void insertStudent(SQLiteDatabase db, int ID, String Name){
        db.execSQL("INSERT INTO " + TABLE_STUDENT_NAME +
                " VALUES (" + ID + ", '" + Name + "');");
    }

    public void insertStudyPlan(SQLiteDatabase db, int IDDiscipline, int IDStudent, String Mark, String Date){
        db.execSQL("INSERT INTO " + TABLE_STUDY_PLAN_NAME  +
                "(" + STUDY_PLAN_COLUMN_ID_STUDENT + ", " + STUDY_PLAN_COLUMN_ID_DISCIPLINE + ", " + STUDY_PLAN_COLUMN_MARK + ", " + STUDY_PLAN_COLUMN_DATE + ")" +
                " VALUES (" + IDDiscipline + ", " + IDStudent + ", '" + Mark + "', '" + Date + "');");
    }

    public void  updateStudyPlan(SQLiteDatabase db, int id, String mark, String Date){
        db.execSQL("UPDATE " + TABLE_STUDY_PLAN_NAME +
                " SET " + STUDY_PLAN_COLUMN_MARK + " = '" + mark + "', "
                + STUDY_PLAN_COLUMN_DATE + " = '" + Date +
                "' Where " + STUDY_PLAN_COLUMN_ID + " = " + id);
    }

    public Cursor selectStudent(SQLiteDatabase db, int id){
        return db.rawQuery("select * from " + TABLE_STUDENT_NAME + " where ID=?", new String[]{String.valueOf(id)});
    }

    public Cursor selectStudyPlan(SQLiteDatabase db, int id){
        return db.rawQuery("select * from " + TABLE_STUDY_PLAN_NAME + " where ID=?", new String[]{String.valueOf(id)});
    }

    public Cursor selectDiscipline(SQLiteDatabase db, int id) {
        return db.rawQuery("select * from " + TABLE_DISCIPLINE_NAME + " where ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteDiscipline(SQLiteDatabase db, int id) {
        db.execSQL("Delete FROM " + TABLE_DISCIPLINE_NAME +
                " Where ID=" + id + ";");
    }

    public void deleteStudent(SQLiteDatabase db, int id) {
        db.execSQL("Delete FROM " + TABLE_DISCIPLINE_NAME +
                " Where ID=" + id + ";");
    }
}
