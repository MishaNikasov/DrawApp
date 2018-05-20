package donnu.nikasov.drawapp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private CanvasView canvasView;
    Toolbar myToolbar;
    ColorPickerDialog colorPickerDialog;
    public int color;

    FloatingActionButton undoDraw;
    FloatingActionButton actionB;
    FloatingActionButton actionA;
    FloatingActionButton actionC;
    FloatingActionsMenu menuMultipleActions;

    final int REQUEST_CODE_PICKER = 10;
    ArrayList<Image> images;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(MainActivity.this);

        color = Color.BLACK;

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setSubtitleTextColor(Color.WHITE);

        actionA = (FloatingActionButton) findViewById(R.id.action_a);
        assert actionA != null;
        actionA.setOnClickListener(this);

        actionB = (FloatingActionButton) findViewById(R.id.action_b);
        assert actionB != null;
        actionB.setOnClickListener(this);

        actionC = (FloatingActionButton) findViewById(R.id.action_c);
        assert actionC != null;
        actionC.setOnClickListener(this);

        undoDraw = (FloatingActionButton) findViewById(R.id.undo_button);
        assert undoDraw != null;
        undoDraw.setOnClickListener(this);

        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        canvasView = (CanvasView) findViewById(R.id.canvasView);
        myToolbar.setTitle("Рисунок");
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void initColorCircle(){
        colorPickerDialog = new ColorPickerDialog(MainActivity.this, color);
        colorPickerDialog.setAlphaSliderVisible(true);
        colorPickerDialog.setHexValueEnabled(true);
        colorPickerDialog.setTitle("Выберите цвет");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_a:
                initColorCircle();
                colorPickerDialog.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int i) {
                        color = i;
                        canvasView.setSelectedColor(color);
                    }
                });
                colorPickerDialog.show();
                break;
            case R.id.action_c:
                initColorCircle();
                colorPickerDialog.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int i) {
                        color = i;
                        canvasView.setSelectedBackgroundColor(color);
                    }
                });
                colorPickerDialog.show();
                break;
            case R.id.action_b:

                int a = canvasView.getStrokeWidth();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final SeekBar seekBar = new SeekBar(MainActivity.this);
                final TextView textView = new TextView(MainActivity.this);

                seekBar.setProgress(a);
                seekBar.setMax(110);

                textView.setTextColor(Color.BLACK);
                textView.setText("afs" + String.valueOf(seekBar.getProgress()));

                builder.setTitle("Толщина кисти.")
                        .setIcon(R.drawable.ic_insert_drive_file_white_24dp)
                        .setCancelable(true)
                        .setView(textView)
                        .setView(seekBar)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                canvasView.setStrokeWidth(seekBar.getProgress());
                            }
                        })

                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

                break;
            case R.id.undo_button:
                canvasView.removeLastPath();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        myToolbar.setTitle("Рисунок");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                AlertDialog.Builder delBuilder = new AlertDialog.Builder(MainActivity.this);
                delBuilder.setTitle("Очистить холст?")
                        .setIcon(R.drawable.ic_insert_drive_file_white_24dp)
                        .setCancelable(true)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                canvasView.clearCanvas();
                                dialog.cancel();
                            }
                        })

                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog delAlert = delBuilder.create();
                delAlert.show();

                return true;

            case R.id.action_new:

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final EditText newFileEdit = new EditText(MainActivity.this);
                builder.setTitle("С чистого листа!")
                        .setMessage("Укажите название нового рисунка:")
                        .setIcon(R.drawable.ic_insert_drive_file_white_24dp)
                        .setCancelable(true)

                        .setView(newFileEdit)

                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (!newFileEdit.getText().toString().equals("")) {
                                    myToolbar.setTitle(newFileEdit.getText());
                                    canvasView.clearCanvas();
                                    dialog.cancel();
                                }
                                else
                                    Toast.makeText(MainActivity.this, "Введите название!", Toast.LENGTH_LONG).show();
                            }
                        })

                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

                return true;

            case R.id.action_save:

                AlertDialog.Builder saveBuilder = new AlertDialog.Builder(MainActivity.this);
                final EditText saveEditText = new EditText(MainActivity.this);
                saveBuilder.setTitle("Сохранение")
                        .setMessage("Имя вашего творения:")
                        .setIcon(R.drawable.ic_insert_drive_file_white_24dp)
                        .setCancelable(true)

                        .setView(saveEditText)

                        .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (!saveEditText.getText().toString().equals("")) {
                                    canvasView.saveFile(saveEditText.getText().toString());
                                    dialog.cancel();
                                    Toast.makeText(MainActivity.this, "Сохранено!", Toast.LENGTH_LONG).show();

                                }
                                else
                                    Toast.makeText(MainActivity.this, "Введите название!", Toast.LENGTH_LONG).show();
                            }
                        })

                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert2 = saveBuilder.create();
                alert2.show();

                return true;

            case R.id.action_hide:
                if (item.isChecked()){
                    item.setChecked(false);
                    menuMultipleActions.setVisibility(View.VISIBLE);
                    undoDraw.setVisibility(View.VISIBLE);
                }
                else {
                    item.setChecked(true);
                    undoDraw.setVisibility(View.GONE);
                    menuMultipleActions.setVisibility(View.GONE);
                }
                return true;

            case R.id.gallery:
                Intent intent = new Intent(this, ImagePickerActivity.class);

                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_MODE, true);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_MODE, ImagePickerActivity.MODE_SINGLE);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_LIMIT, 1);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SHOW_CAMERA, false);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES, images);
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_TITLE, "Альбомы");
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_TITLE, "Выберите файл");
                intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_DIRECTORY, "Camera");

                startActivityForResult(intent, REQUEST_CODE_PICKER);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            canvasView.setImageOnView(images.get(0).getPath());
        }
    }
}
