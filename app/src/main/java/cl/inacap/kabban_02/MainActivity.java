package cl.inacap.kabban_02;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import cl.inacap.kabban_02.Fragments.ChatFragment;
import cl.inacap.kabban_02.Fragments.GroupFragment;
import cl.inacap.kabban_02.Fragments.NewsFragment;
import cl.inacap.kabban_02.Fragments.ProfileFragment;
import cl.inacap.kabban_02.Fragments.ProjectFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ProfileFragment.OnFragmentInteractionListener,
        ProjectFragment.OnFragmentInteractionListener,
        GroupFragment.OnFragmentInteractionListener,
        NewsFragment.OnFragmentInteractionListener,
        ChatFragment.OnFragmentInteractionListener{

    private static final int SIGN_IN_REQUEST_CODE = 1;
    DatabaseReference reference;
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            verifySession();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Construye el toolbar
     * @param title (String) Título del fragment activo
     * @param upButton (boolean) Especifica si el toolbar tendrá o no UpButton (retroceso jerárquico)
     */
    public void showToolbar(String title, boolean upButton){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Cierra la sesión actual
     */
    public void closeSession(){
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    /**
     * Verifica si hay sesión iniciada, de lo contrario solicitará al usuario iniciar sesión con una de sus cuentas registradas en el dispositivo
     * @return TRUE si hay sesión iniciada, FALSE si no la hay
     */
    public boolean verifySession(){
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build()
            );
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTheme(R.style.AppTheme)
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
            return false;
        }else{
            Toast.makeText(this,
                    "Bienvenido " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();
            loadComplements();
            return true;
        }
    }

    /**
     * Obtiene el resutado de la actividad actual al verificar el inicio de sesión por parte del usuario
     * @param requestCode Código solicitado, es comparado con el código que se enviía por verifySession()
     * @param resultCode Codigo resultante, generado internamente para verificar que la operación es existosa
     * @param data Actividad
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id",firebaseUser.getUid());
                hashMap.put("username",firebaseUser.getDisplayName());

                try {
                    
                    hashMap.put("imageURL",firebaseUser.getPhotoUrl().toString());

                }catch (Exception e){

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse("https://cooptena.fin.ec/imagenes/2019/02/AVATAR-GENERICO.jpg"))
                            .build();

                    firebaseUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                    }else{
                                        Toast.makeText(MainActivity.this, "No foto: "+task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    hashMap.put("imageURL","https://cooptena.fin.ec/imagenes/2019/02/AVATAR-GENERICO.jpg");
                }

                hashMap.put("status","Desconectado(a)");
                hashMap.put("search",firebaseUser.getDisplayName().toLowerCase());

                reference.setValue(hashMap);

                Toast.makeText(this,
                        "Inicio de sesión exitosa ¡bienvenido!",
                        Toast.LENGTH_LONG)
                        .show();
                try {
                    loadComplements();
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Error de carga de complementos: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this,
                        "No pudimos iniciar sesión. Inténtalo de nuevo más tarde.",
                        Toast.LENGTH_LONG)
                        .show();
                // Cerramos la aplicación
                finish();
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Agrega funcionalidades a los ítems del menú lateral
     * @param item Objeto de la clase MenuItem
     * @return TRUE
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if(id == R.id.nav_profile){
            fragment = new ProfileFragment();
            showToolbar("Perfil",false);
        }else if(id == R.id.nav_project){
            fragment = new ProjectFragment();
            showToolbar("Proyectos",false);
        }else if(id == R.id.nav_groups){
            fragment = new GroupFragment();
            showToolbar("Grupos",false);
        }else if(id == R.id.nav_news){
            fragment = new NewsFragment();
            showToolbar("Noticias",false);
        }else if(id == R.id.nav_chat){
            fragment = new ChatFragment();
            showToolbar("Mensajes",false);
        }else if(id == R.id.nav_singout){
            closeSession();
        }

        if(fragment != null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main,fragment);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        //showToolbar(title,false);
        return true;
    }

    /**
     * Carga los complementos del DrawerNavigationView (menú lateral deslizable)
     */
    public void loadComplements(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nav_header = navigationView.getHeaderView(0);
        TextView displayname = nav_header.findViewById(R.id.displayname);
        ImageView userimage = nav_header.findViewById(R.id.userimage);

        displayname.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        Glide.with(nav_header.getContext()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userimage);

        Fragment fragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content_main,fragment).commit();
        navigationView.getMenu().getItem(0).setChecked(true);

        showToolbar("Perfil",false);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status",status);

        reference.updateChildren(hashMap);
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();
        status("En línea");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("Desconectado(a)");
    }*/
}
