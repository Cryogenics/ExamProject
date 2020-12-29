package by.bstu.fit.gpn.examproject.datafiles.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import by.bstu.fit.gpn.examproject.R;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.Student;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Student> studentList;
    private OnStudentClickListener OnStudentStudentListener;

    public StudentAdapter(Context context, List<Student> students, OnStudentClickListener onStudentClickListener) {
        this.studentList = students;
        this.inflater = LayoutInflater.from(context);
        this.OnStudentStudentListener = onStudentClickListener;
    }

    @NonNull
    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.student_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.ViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.viewTitle.setText(student.get_fullname());
        holder.viewID = student.getID();
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        final TextView viewTitle;
        int viewID;

        ViewHolder(View view) {
            super(view);
            this.viewTitle = view.findViewById(R.id.student_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Student student = studentList.get(getLayoutPosition());
                    OnStudentStudentListener.onStudentClick(student);
                }
            });
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, 1, this.viewID, "Удалить");
        }
    }

    public interface OnStudentClickListener {
        void onStudentClick(Student student);
    }
}
