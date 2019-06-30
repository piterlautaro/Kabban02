package cl.inacap.kabban_02.Fragments.ProfileFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cl.inacap.kabban_02.R;

public class SubProfilePublishFragment extends Fragment {

    private EditText profile_publish;
    private Button btn_profile_publish;

    private FirebaseUser fuser;
    private DatabaseReference reference;

    public SubProfilePublishFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_profile_publish, container, false);

        profile_publish = view.findViewById(R.id.profile_publish);
        btn_profile_publish = view.findViewById(R.id.btn_profile_publish);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        btn_profile_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publish(profile_publish.getText().toString().trim(),fuser.getUid(),fuser.getPhotoUrl().toString());
                profile_publish.setText("");
            }
        });
        return view;
    }

    private void publish(String publish,String owner,String imageURL) {
        if(!publish.equals("")){

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("News");

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("type",1);
            hashMap.put("publish",publish);
            hashMap.put("owner",owner);
            hashMap.put("imageURL",imageURL);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            hashMap.put("date",dateFormat.format(new Date()));

            databaseReference.push().setValue(hashMap);
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
}
