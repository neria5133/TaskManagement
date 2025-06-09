package com.example.taskmanagement.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.text.Spannable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.taskmanagement.R;

/**
 * מחלקת בסיס לכל האקטיביטיז באפליקציה.
 * מטפלת במצב כהה/בהיר ובתפריט העליון עם אפשרויות ניווט.
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * נקרא ביצירת האקטיביטי.
     * מאתחל את מצב התצוגה (כהה/בהיר) לפי ההגדרות השמורות.
     *
     * @param savedInstanceState מצב שמור אם קיים
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("DarkMode", false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
    }

    /**
     * יוצר את תפריט הניווט עם אייקונים וטקסט.
     *
     * @param menu התפריט שנוצר
     * @return תמיד true להצגה
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.baseline_add_task_24), getResources().getString(R.string.add_task)));
        menu.add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.baseline_format_list_bulleted_24), getResources().getString(R.string.tasks_list)));
        menu.add(0, 3, 5, menuIconWithText(getResources().getDrawable(R.drawable.baseline_contact_support_24), getResources().getString(R.string.support)));
        menu.add(0, 4, 4, menuIconWithText(getResources().getDrawable(R.drawable.baseline_settings_24), getResources().getString(R.string.settings)));
        menu.add(0, 5, 3, menuIconWithText(getResources().getDrawable(R.drawable.baseline_circle_notifications_24), "רשימת התראות"));
        return true;
    }

    /**
     * מטפל בלחיצה על פריט בתפריט הניווט.
     * מפנה לאקטיביטי המתאים לפי הפריט שנבחר.
     *
     * @param item פריט התפריט שנלחץ
     * @return true אם הטיפול התבצע, אחרת ברירת מחדל
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent(this, AddTaskActivity.class));
                return true;
            case 2:
                startActivity(new Intent(this, TaskListActivity.class));
                return true;
            case 3:
                startActivity(new Intent(this, SupportActivity.class));
                return true;
            case 4:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case 5:
                startActivity(new Intent(this, NotificationListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * פונקציה ליצירת טקסט עם אייקון לתפריט.
     *
     * @param r האייקון להצגה
     * @param title הטקסט להצגה לצד האייקון
     * @return CharSequence הכולל את האייקון והטקסט
     */
    private CharSequence menuIconWithText(Drawable r, String title) {
        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("   " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
