package cl.inacap.kabban_02;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

public class AddMyGroupActivity extends AppCompatActivity {

    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;

    private Button btn_mygroup_take;
    private Button btn_mygroup_pic;
    private String name;
    private ImageView mygroup_photo;

    private Button btn_mygroup_add;
    private Uri selectedImage;


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

        selectedImage = null;

        iniciarBotones();
    }

    public void iniciarBotones(){
        name = "";
        btn_mygroup_take = findViewById(R.id.btn_mygroup_take);
        btn_mygroup_pic = findViewById(R.id.btn_mygroup_pic);
        mygroup_photo = findViewById(R.id.mygroup_photo);
        btn_mygroup_add = findViewById(R.id.btn_mygroup_add);

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
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });

        btn_mygroup_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String imageURL = "";
                    if(selectedImage != null){

                        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("uploads").child(selectedImage.getLastPathSegment());
                        UploadTask uploadTask = storageReference.putFile(selectedImage);

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful()){
                                    Toast.makeText(AddMyGroupActivity.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                                }
                                return storageReference.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    selectedImage = task.getResult();
                                }else {
                                    Toast.makeText(AddMyGroupActivity.this, "Algo pasó: "+task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        imageURL = selectedImage.toString();
                    }else{
                        imageURL = "https://images.pexels.com/photos/373912/pexels-photo-373912.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500";
                        Toast.makeText(AddMyGroupActivity.this, "Es nulo", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(AddMyGroupActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == TAKE_PICTURE){
                if (data.hasExtra("data")) {
                    mygroup_photo.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
                }
            }else if(requestCode == SELECT_PICTURE){
                selectedImage = data.getData();
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
        }else{
            Toast.makeText(this, "No ok: " + resultCode, Toast.LENGTH_SHORT).show();
        }

    }
}
