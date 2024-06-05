package har.makawet.jvm.Home;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import har.makawet.jvm.Adapters.RecyclerViewAdapter;
import har.makawet.jvm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatRoomFragment extends Fragment {

    private InterstitialAd mInterstitialAd;


    private RecyclerView recFriendsView;
    private DatabaseReference databseRef;
    private DatabaseReference userdatabseRef;
    private FirebaseAuth mAuth;
    public static boolean tv0ChatsBol;
    private String current_user_id;
    private View mainView;


    private ArrayList<Integer> countryImgv = new ArrayList<>();
    private ArrayList<String> countryName = new ArrayList<>();

    public ChatRoomFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_chatroom, container, false);

        initImageBitmaps();
        // Inflate the layout for this fragment
        return  mainView;
    }


    private void initImageBitmaps(){


        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.Cooking));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.Hiking));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.Painting));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.dji));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.egy));


        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.irq));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.jord));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.kut));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.leb));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.lib));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.maur));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.mar));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.omar));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.pals));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.Poker));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.saud));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.sud));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.syr));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.sola));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.tun));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.Traveling));

        countryImgv.add(R.drawable.chatroom);
        countryName.add(getString(R.string.yem));



        initRecyclerView(mainView);
    }

    private void initRecyclerView(View view){
        GridLayoutManager gridLayoutManager;
        RecyclerView recyclerView = view.findViewById(R.id.recyclerv_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), countryName, countryImgv);
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getActivity(),3
                ,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

    }


}
