package cl.inacap.kabban_02.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import cl.inacap.kabban_02.Class.Models.Users;
import cl.inacap.kabban_02.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private TextView profile_name;
    private CircleImageView profile_user_image;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        buildProfile(view);
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
                FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).setValue(new Users(firebaseUser.getUid(),firebaseUser.getDisplayName(),firebaseUser.getPhotoUrl().toString()));
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
}
