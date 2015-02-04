package com.andrewsosa.bounce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;


public class TaskViewActivity extends Activity implements Toolbar.OnMenuItemClickListener {

    Task task;
    TaskDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        // Extract Task
        task = (Task) getIntent().getSerializableExtra("Task");

        // Toolbar craziness
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.inflateMenu(R.menu.menu_item_view);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitleTextColor(getResources().getColor(R.color.abc_primary_text_material_dark));
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get a datasource
        dataSource = new TaskDataSource(this);
        dataSource.open();


        // Set up actual task stuff
        TextView taskName = (TextView) findViewById(R.id.task_name);
        taskName.setText(task.getName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_view, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_complete:
                Toast.makeText(TaskViewActivity.this, "Action Complete", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_delete:
                launchDeleteDialog();
                return true;
            case R.id.action_postpone:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchDeleteDialog() {

        new MaterialDialog.Builder(this)
                .content("Delete task?")
                .positiveText("Delete")
                .negativeText("Cancel")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {

                        dataSource.deleteTask(task);

                        Intent data = new Intent();
                        data.putExtra("Task", task);
                        data.putExtra("Action", "delete");
                        setResult(RESULT_OK, data);
                        TaskViewActivity.this.finish();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        Toast.makeText(TaskViewActivity.this, "Object will not be deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

    }
}
