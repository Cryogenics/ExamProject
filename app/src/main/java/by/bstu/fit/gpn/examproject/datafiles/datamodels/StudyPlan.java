package by.bstu.fit.gpn.examproject.datafiles.datamodels;

import java.util.ArrayList;

public class StudyPlan {
    private int ID;
    private String student;
    private Discipline discipline;

    public StudyPlan(int ID, String student, Discipline discipline) {
        this.ID = ID;
        this.student = student;
        this.discipline = discipline;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
}
