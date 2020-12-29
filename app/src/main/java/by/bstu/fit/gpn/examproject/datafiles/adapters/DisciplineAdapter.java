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
import by.bstu.fit.gpn.examproject.datafiles.datamodels.Discipline;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.CourseWork;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.Credit;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.Exam;

public class DisciplineAdapter extends RecyclerView.Adapter<DisciplineAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Discipline> disciplineList;

    public DisciplineAdapter(Context context, List<Discipline> disciplines) {
        this.disciplineList = disciplines;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DisciplineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.discipline_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisciplineAdapter.ViewHolder holder, int position) {
        if(disciplineList.get(position) instanceof Exam)
            holder.viewType.setText("Экзамен");
        else if(disciplineList.get(position) instanceof Credit)
            holder.viewType.setText("Зачет");
        else if(disciplineList.get(position) instanceof CourseWork)
            holder.viewType.setText("Курсовая");
        Discipline discipline = disciplineList.get(position);
        holder.viewName.setText(discipline.get_name_of_discipline());
        holder.viewID = discipline.get_ID();
    }

    @Override
    public int getItemCount() {
        return disciplineList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        final TextView viewName, viewType;
        int viewID;

        ViewHolder(View view) {
            super(view);
            this.viewName = view.findViewById(R.id.discipline_name);
            this.viewType = view.findViewById(R.id.type);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, 1, this.viewID, "Удалить");
        }
    }
}
