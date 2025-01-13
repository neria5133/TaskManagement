package com.example.taskmanagement;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LocalDate myDate = LocalDate.of(2025 , 1 , 13);
        Task task1=new Task(myDate, "math", "scz");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.saveTask(task1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_patient_home_screen, menu);


        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.baseline_add_task_24), getResources().getString(R.string.add_task)));
        menu.add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.baseline_format_list_bulleted_24), getResources().getString(R.string.tasks_list)));
        menu.add(0, 3, 3, menuIconWithText(getResources().getDrawable(R.drawable.baseline_contact_support_24), getResources().getString(R.string.support)));
        menu.add(0, 4, 4, menuIconWithText(getResources().getDrawable(R.drawable.baseline_settings_24), getResources().getString(R.string.settings)));
        return true;
    }

    // הקשבה ללחיצת ITEN בMENU
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case 1:
                Intent intentProfile = new Intent(MainActivity.this, AddTask.class); // כך אני עובר בין מסכים
                startActivity(intentProfile);
                return true;
            case 2:
                Intent intentAddUser = new Intent(MainActivity.this, TasksList.class);// כך אני עובר בין מסכים
                startActivity(intentAddUser);
                return true;
            case 3:
                Intent intentSwitchProfile = new Intent(MainActivity.this, Support.class);// כך אני עובר בין מסכים
                startActivity(intentSwitchProfile);
                return true;
            case 4:
                Intent intentSignOut = new Intent(MainActivity.this, Settings.class);// כך אני עובר בין מסכים
                startActivity(intentSignOut);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }
}