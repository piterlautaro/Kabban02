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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import cl.inacap.kabban_02.Fragments.ChatFragments.SubChatFragment;
import cl.inacap.kabban_02.Fragments.ChatFragments.SubUsersFragment;
import cl.inacap.kabban_02.R;


public class ChatFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    DatabaseReference reference;
    FirebaseUser fuser;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_chat, container, false);
            tabLayout = view.findViewById(R.id.tab_chats);
            viewPager = view.findViewById(R.id.view_pager_chat);

            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
            viewPagerAdapter.addFragment(new SubChatFragment(),"Chats");
            viewPagerAdapter.addFragment(new SubUsersFragment(),"Usuarios");

            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            // Inflate the layout for this fragment
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            reference = FirebaseDatabase.getInstance().getReference();
            return view;
        }catch(Exception e){
            Toast.makeText(getContext(),"onCreateView(ChatFragment): "+e.getMessage(),Toast.LENGTH_LONG).show();
            return null;
        }

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

    class ViewPagerAdapter extends FragmentPagerAdapter{

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
