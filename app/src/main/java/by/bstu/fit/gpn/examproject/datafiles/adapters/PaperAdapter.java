package by.bstu.fit.gpn.examproject.datafiles.adapters;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import by.bstu.fit.gpn.examproject.R;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.StudyPlan;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.CourseWork;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.Credit;
import by.bstu.fit.gpn.examproject.datafiles.datamodels.disciplines.Exam;

public class PaperAdapter extends RecyclerView.Adapter<PaperAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<StudyPlan> studyPlanList;
    private String[] strings;


    public PaperAdapter(Context context, List<StudyPlan> studyPlans) {
        this.studyPlanList = studyPlans;
        this.inflater = LayoutInflater.from(context);
        this.strings = new String[studyPlans.size()];
    }

    @NonNull
    @Override
    public PaperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.study_plan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaperAdapter.ViewHolder holder, int position) {
        StudyPlan studyPlan = studyPlanList.get(position);
        holder.viewName.setText(studyPlan.getDiscipline().get_name_of_discipline());
        holder.viewID = studyPlan.getID();
        if(studyPlan.getDiscipline().get_mark() != null && studyPlan.getDiscipline().get_mark().compareTo("") != 0)
        {
            holder.viewMark.setText(studyPlan.getDiscipline().get_mark());
            holder.viewDate.setText(studyPlan.getDiscipline().get_date());
        }
        else{
            holder.viewDate.setText("");
            holder.viewMark.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return studyPlanList.size();
    }


    public String[] getStrings() {
        return strings;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView viewName, viewDate;
        int viewID;
        final EditText viewMark;

        ViewHolder(View view) {
            super(view);
            this.viewName = view.findViewById(R.id.discipline_name_study_plan);
            this.viewDate = view.findViewById(R.id.date);
            this.viewMark = view.findViewById(R.id.mark);
            viewMark.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    strings[getAdapterPosition()] = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }
}

