package cl.inacap.kabban_02;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int SIGN_IN_REQUEST_CODE = 1;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            verifySession();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            showToolbar("Perfil",false);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

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
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
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
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
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
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View nav_header = navigationView.getHeaderView(0);
            TextView displayname = nav_header.findViewById(R.id.displayname);
            ImageView userimage = nav_header.findViewById(R.id.userimage);

            displayname.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            Glide.with(nav_header.getContext()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userimage);

            TextView profile_name = (TextView)findViewById(R.id.profile_name);
            CircleImageView profile_user_image = (CircleImageView)findViewById(R.id.profile_user_image);

            profile_name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(profile_user_image);

            userimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(view.getContext(),MainActivity.class));
                }
            });
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
                Toast.makeText(this,
                        "Inicio de sesión exitosa ¡bienvenido!",
                        Toast.LENGTH_LONG)
                        .show();
                try {
                    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    navigationView.setNavigationItemSelectedListener(this);

                    View nav_header = navigationView.getHeaderView(0);
                    TextView displayname = nav_header.findViewById(R.id.displayname);
                    ImageView userimage = nav_header.findViewById(R.id.userimage);

                    displayname.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    Glide.with(nav_header.getContext()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userimage);

                    TextView profile_name = (TextView)findViewById(R.id.profile_name);
                    CircleImageView profile_user_image = (CircleImageView)findViewById(R.id.profile_user_image);

                    profile_name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(profile_user_image);

                    userimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(view.getContext(),MainActivity.class));
                        }
                    });
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

        if(id == R.id.nav_project){

        }else if(id == R.id.nav_groups){

        }else if(id == R.id.nav_news){

        }else if(id == R.id.nav_chat){

        }else if(id == R.id.nav_singout){
            closeSession();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
