package by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines;

import by.bstu.fit.gpn.examproject.datafiles.datamodels.Discipline;

public class CourseWork extends Discipline {

    public CourseWork(int _ID, String _name_of_discipline) {
        super(_ID, _name_of_discipline);
    }

    public CourseWork(int _ID, String _name_of_discipline, String _mark, String _date) {
        super(_ID, _name_of_discipline, _mark, _date);
    }
}

