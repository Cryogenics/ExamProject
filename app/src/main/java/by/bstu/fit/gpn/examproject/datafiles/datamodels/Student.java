package by.bstu.fit.gpn.examproject.datafiles.datamodels;

public class Student {

    private int ID;
    private String _fullname;

    public Student(int ID, String _fullname) {
        this.ID = ID;
        this._fullname = _fullname;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String get_fullname() {
        return _fullname;
    }

    public void set_fullname(String _fullname) {
        this._fullname = _fullname;
    }
}
