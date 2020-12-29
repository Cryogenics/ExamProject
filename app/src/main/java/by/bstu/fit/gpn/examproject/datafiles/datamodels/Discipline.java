package by.bstu.fit.gpn.examproject.datafiles.datamodels;

public abstract class Discipline {

    int _ID;
    String _name_of_discipline;
    String _mark;
    String _date;

    public Discipline(int _ID, String _name_of_discipline) {
        this._ID = _ID;
        this._name_of_discipline = _name_of_discipline;
    }

    public Discipline(int _ID, String _name_of_discipline, String _mark, String _date) {
        this._ID = _ID;
        this._name_of_discipline = _name_of_discipline;
        this._mark = _mark;
        this._date = _date;
    }

    public String get_mark() {
        return _mark;
    }

    public void set_mark(String _mark) {
        this._mark = _mark;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String get_name_of_discipline() {
        return _name_of_discipline;
    }

    public void set_name_of_discipline(String _name_of_discipline) {
        this._name_of_discipline = _name_of_discipline;
    }
}


