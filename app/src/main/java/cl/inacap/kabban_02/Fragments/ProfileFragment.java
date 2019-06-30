package cl.inacap.kabban_02.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import cl.inacap.kabban_02.Class.Models.Users;
import cl.inacap.kabban_02.Fragments.ProfileFragments.SubProfileGroupFragment;
import cl.inacap.kabban_02.Fragments.ProfileFragments.SubProfileProjectFragment;
import cl.inacap.kabban_02.Fragments.ProfileFragments.SubProfilePublishFragment;
import cl.inacap.kabban_02.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private TextView profile_name;
    private CircleImageView profile_user_image;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tabLayout = view.findViewById(R.id.tab_profile);
        viewPager = view.findViewById(R.id.view_pager_profile);
        buildProfile(view);


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFragment(new SubProfilePublishFragment(),"Publicaciones");
        viewPagerAdapter.addFragment(new SubProfileGroupFragment(),"Grupos");
        viewPagerAdapter.addFragment(new SubProfileProjectFragment(),"Proyectos");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    /**
     * Construye la vista del perfil, agrupando todos los elemtos de la vista en un sólo método
     * @param view (View) Vista que se infla (llenar de datos) del fragment en cuestión, en este caso fragment_profile. Esta acción se lleva a cabo en el método onCreateView de este fragment
     */
    public void buildProfile(View view){
        profile_name = (TextView)view.findViewById(R.id.profile_name);
        profile_user_image = (CircleImageView)view.findViewById(R.id.profile_user_image);

        profile_name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(profile_user_image);

        profile_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase
                        .getInstance()
                        .getReference("Users")
                        .child(firebaseUser
                                .getUid())
                        .setValue(new Users(
                                firebaseUser.getUid(),
                                firebaseUser.getDisplayName(),
                                firebaseUser.getPhotoUrl().toString(),
                                "En línea"));
            }
        });

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            try {
                this.fragments = new ArrayList<>();
                this.titles = new ArrayList<>();
            }catch(Exception e){
                Toast.makeText(getContext(),"ViewPagerAdapter: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public Fragment getItem(int i) {
            try {
                return fragments.get(i);
            }catch(Exception e){
                Toast.makeText(getContext(),"getItem: "+e.getMessage(),Toast.LENGTH_LONG).show();
                return null;
            }

        }

        @Override
        public int getCount() {
            try {
                return fragments.size();
            }catch(Exception e){
                Toast.makeText(getContext(),"getCount: "+e.getMessage(),Toast.LENGTH_LONG).show();
                return 0;
            }
        }

        /**
         * Despliega las pestañas de Chats/Usuarios en la actividad del chat
         * @param fragment (Fragment) Contiene el fragmento de Chats o Usuarios
         * @param title (String) Contiene el título de la pestaña (Chats o Usuarios)
         */
        public void addFragment(Fragment fragment, String title){
            try {
                fragments.add(fragment);
                titles.add(title);
            }catch (Exception e){
                Toast.makeText(getContext(),"addFragment: "+e.getMessage(),Toast.LENGTH_LONG).show();
            }

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            try {
                return titles.get(position);
            }catch (Exception e){
                Toast.makeText(getContext(),"getPageTitle: "+e.getMessage(),Toast.LENGTH_LONG).show();
                return null;
            }

        }


    }
}
