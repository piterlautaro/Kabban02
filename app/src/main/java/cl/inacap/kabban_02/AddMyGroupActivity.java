package cl.inacap.kabban_02;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddMyGroupActivity extends AppCompatActivity {

    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;

    private Button btn_mygroup_take;
    private Button btn_mygroup_pic;
    private String name;
    private ImageView mygroup_photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_group);

        Toolbar toolbar = findViewById(R.id.mygroup_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CREAR NUEVO GRUPO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iniciarBotones();
    }

    public void iniciarBotones(){
        name = "";
        btn_mygroup_take = findViewById(R.id.btn_mygroup_take);
        btn_mygroup_pic = findViewById(R.id.btn_mygroup_pic);
        mygroup_photo = findViewById(R.id.mygroup_photo);

        btn_mygroup_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                int code = TAKE_PICTURE;
                Uri output = Uri.fromFile(new File(name));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                startActivityForResult(intent, code);
            }
        });

        btn_mygroup_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                int code = SELECT_PICTURE;
                startActivityForResult(intent, code);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == TAKE_PICTURE){
            if (data.hasExtra("data")) {
                mygroup_photo.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
            }
        }else if(requestCode == SELECT_PICTURE){
            Uri selectedImage = data.getData();
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                mygroup_photo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "No hay archivo: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
