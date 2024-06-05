package har.makawet.jvm.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;



import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import har.makawet.jvm.Chat.ChatUserActivity;
import har.makawet.jvm.Utils.DialogUtils;
import har.makawet.jvm.R;
import har.makawet.jvm.Models.UsersSearch;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import javax.net.ssl.SSLSession;

public class SearchFrag extends Fragment {



    private InterstitialAd mInterstitialAd;

    private View view;
    private RecyclerView rcvSearchUsers;
    private FirebaseRecyclerAdapter<UsersSearch,userSearchViewHolder> userSearchRecyclerAdapte;
    private DatabaseReference userdatabseRef;
    private DatabaseReference dbrefSearch;
    FirebaseUser currentUser;
    String idUser;
    private GridLayoutManager mLayoutManager;
    private View mViewInflate;
    private ImageView imgvUser;
    private TextView tvAge,tvRelationShip,tvCountry,tvUsername;
    private android.widget.Button btnSendMsg;
    private ImageView imgvCloseUserProfile,imgvBlock;
    private int valueFive;
    private Button btnSHowMore;
    private Query qury;
    private static int numberuser = 50;
    private int indexxx = 2;
    private DatabaseReference dbrefSearchFemale;
    private DatabaseReference dbrefSearchMale;
    private View mViewInflateReportuser;
    private DatabaseReference dbReportAbuseOfContent;
    private DatabaseReference dbBlock;
    private View mViewInflatedialogLogout;
    private DatabaseReference dbMessagingHis;
    private DatabaseReference dbrefMyChat;
    private DatabaseReference dbMessagingMy;
    private DatabaseReference dbrefHischat;

    //InterstitialAd mInterstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_search,container,false);
        dbReportAbuseOfContent = FirebaseDatabase.getInstance().getReference().child("ReportAbuse");
        dbBlock = FirebaseDatabase.getInstance().getReference().child("block");

        widgets();




        return view;
    }

    private void widgets() {
        // rcv
        rcvSearchUsers = view.findViewById(R.id.rcSearchUsers);
        btnSHowMore = view.findViewById(R.id.btnSHowMore);

        rcvSearchUsers.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(),3
                ,LinearLayoutManager.VERTICAL,false);
        rcvSearchUsers.setLayoutManager(mLayoutManager);
    }
    private void dialogBlock(String userid){
        dbrefMyChat = FirebaseDatabase.getInstance().getReference().child("Chat").child(idUser).child(userid);
        dbrefHischat = FirebaseDatabase.getInstance().getReference().child("Chat").child(userid).child(idUser);
        dbMessagingMy = FirebaseDatabase.getInstance().getReference().child("Messaging").child(idUser).child(userid);
        dbMessagingHis = FirebaseDatabase.getInstance().getReference().child("Messaging").child(userid).child(idUser);

        mViewInflatedialogLogout= getLayoutInflater().inflate(R.layout.dialog_report_user,null);
        TextView tv_dialog_logout_cancel = mViewInflatedialogLogout.findViewById(R.id.tv_dialog_cancel);
        TextView tv_dialog_logout_confirm = mViewInflatedialogLogout.findViewById(R.id.tv_dialog_yes_confirm);
        final AlertDialog.Builder alertDialogBuilder = DialogUtils.CustomAlertDialog(mViewInflatedialogLogout, getActivity());
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(!getActivity().isFinishing()) {

            alertDialog.setCancelable(true);
            alertDialog.show();
        }

        tv_dialog_logout_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbBlock.child(idUser).child(userid).child("blocked").setValue("true");
                dbBlock.child(userid).child(idUser).child("blocked").setValue("true");
                dbMessagingHis.removeValue();
                dbMessagingMy.removeValue();
                dbrefMyChat.removeValue();
                dbrefHischat.removeValue();

                alertDialog.cancel();
                alertDialog.dismiss();
            }
        });

        tv_dialog_logout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                alertDialog.dismiss();
            }
        });

    }


    private void firebaseRecyclerview(Query qury){

        valueFive = 5;
        userSearchRecyclerAdapte =
                new FirebaseRecyclerAdapter<UsersSearch,
                        userSearchViewHolder>(

                        UsersSearch.class,
                        R.layout.layout_pack_user_search,
                        userSearchViewHolder.class,
                        qury

                ) {
                    @Override
                    protected void populateViewHolder(final userSearchViewHolder viewHolder, UsersSearch model, int position) {


                        final String list_user_id = getRef(position).getKey();



                        userdatabseRef.child(list_user_id).addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {

                                    String userName = String.valueOf(dataSnapshot.child("username").getValue());
                                    String image = String.valueOf(dataSnapshot
                                            .child(String.valueOf(getString(R.string.fb_thumb_image))).getValue());
                                    viewHolder.setusername(userName);
                                    viewHolder.setUserImage(image,getContext());

                                }catch(Exception e){}
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        //// set id user and click ////
                        final String userIDvisit = getRef(position).getKey();
                        if(userIDvisit.equals(idUser))
                        {

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent userProfileIntent = new Intent(getActivity(),AccountFrag.class);
                                    startActivity(userProfileIntent);
                                }
                            });
                        }
                        else {

                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Toast.makeText(getActivity(),getString(R.string.waiting), Toast.LENGTH_SHORT).show();

                                    userdatabseRef.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {


                                            mViewInflate = getLayoutInflater().inflate(R.layout.customdialog,null);
                                            imgvUser = mViewInflate.findViewById(R.id.imgvUser);
                                            tvAge = mViewInflate.findViewById(R.id.tvAgeUser);
                                            tvRelationShip = mViewInflate.findViewById(R.id.tvRelationshipUser);
                                            tvCountry = mViewInflate.findViewById(R.id.tvCountryProfileUser);
                                            tvUsername = mViewInflate.findViewById(R.id.tvUsernameUser);
                                            btnSendMsg = mViewInflate.findViewById(R.id.btnSendMsg);
                                            imgvCloseUserProfile = mViewInflate.findViewById(R.id.imgvCloseUserProfile);
                                            imgvBlock = mViewInflate.findViewById(R.id.imgvBlock);

                                            ImageView imgvReport = mViewInflate.findViewById(R.id.imgvReport);

                                            imgvReport.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    dialogReportuser(userIDvisit);
                                                }
                                            });

                                            imgvBlock.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialogBlock(userIDvisit);
                                                }
                                            });

                                            btnSendMsg.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    dbBlock.child(idUser).child(userIDvisit).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()){
                                                                Toast.makeText(getActivity(), getString(R.string.stg_clocked), Toast.LENGTH_SHORT).show();
                                                            }else{
                                                                Intent intmessage = new Intent(getActivity(), ChatUserActivity.class);
                                                                intmessage.putExtra("userIDvisited",userIDvisit);
                                                                startActivity(intmessage);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });


                                                }
                                            });

                                            dbBlock.child(idUser).child(userIDvisit).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()){
                                                        Toast.makeText(getActivity(), getString(R.string.stg_clocked), Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        AlertDialog.Builder alertDialogBuilder = DialogUtils.CustomAlertDialog(mViewInflate,getActivity());
                                                        final AlertDialog alertDialog = alertDialogBuilder.create();
                                                        alertDialog.setCancelable(false);
                                                        alertDialog.show();

                                                        imgvCloseUserProfile.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                alertDialog.dismiss();
                                                            }
                                                        });

                                                        userdatabseRef.child(userIDvisit).addValueEventListener(new ValueEventListener() {

                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                /// Stock information from firebase to Setting activity .
                                                                String username = dataSnapshot.child(String.valueOf(getString(R.string.fb_username))).getValue().toString();
                                                                final String image = dataSnapshot.child(String.valueOf(getString(R.string.fb_thumb_image))).getValue().toString();
                                                                String age = dataSnapshot.child(String.valueOf(getString(R.string.fb_age))).getValue().toString();
                                                                String country = dataSnapshot.child(String.valueOf(getString(R.string.fb_country))).getValue().toString();
                                                                String relationship = dataSnapshot.child(String.valueOf(getString(R.string.fb_relationship))).getValue().toString();

                                                                tvAge.setText(age);
                                                                tvUsername.setText(username);
                                                                if(relationship.equals("0")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_single)));else
                                                                if(relationship.equals("1")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_relatio)));else
                                                                if(relationship.equals("2")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_engeg)));else
                                                                if(relationship.equals("3")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_inopen)));else
                                                                if(relationship.equals("4")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_itcomp)));else
                                                                if(relationship.equals("6")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_in)));else
                                                                if(relationship.equals("7")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_sep)));else
                                                                if(relationship.equals("8")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_marr)));else
                                                                if(relationship.equals("9")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_indo)));else
                                                                if(relationship.equals("10")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_wid)));else
                                                                if(relationship.equals("11")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_div)));else
                                                                if(relationship.equals("12")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_none)));



                                                                if(country.equals("Writing")) tvCountry.setText(String.valueOf(getString(R.string.egy)));
                                                                else if(country.equals("Cooking")) tvCountry.setText(String.valueOf(getString(R.string.Cooking)));
                                                                else if(country.equals("Amateur Radio")) tvCountry.setText(String.valueOf(getString(R.string.sud)));
                                                                else if(country.equals("Hiking")) tvCountry.setText(String.valueOf(getString(R.string.Hiking)));
                                                                else if(country.equals("Painting")) tvCountry.setText(String.valueOf(getString(R.string.Painting)));
                                                                else if(country.equals("Sculpture")) tvCountry.setText(String.valueOf(getString(R.string.dji)));
                                                                else if(country.equals("Running")) tvCountry.setText(String.valueOf(getString(R.string.irq)));
                                                                else if(country.equals("Dancing")) tvCountry.setText(String.valueOf(getString(R.string.jord)));
                                                                else if(country.equals("Yoga")) tvCountry.setText(String.valueOf(getString(R.string.kut)));
                                                                else if(country.equals("Meditating")) tvCountry.setText(String.valueOf(getString(R.string.leb)));
                                                                else if(country.equals("Reading")) tvCountry.setText(String.valueOf(getString(R.string.lib)));
                                                                else if(country.equals("Video Games")) tvCountry.setText(String.valueOf(getString(R.string.maur)));
                                                                else if(country.equals("Gardening")) tvCountry.setText(String.valueOf(getString(R.string.mar)));
                                                                else if(country.equals("Knitting")) tvCountry.setText(String.valueOf(getString(R.string.omar)));
                                                                else if(country.equals("Woodwork")) tvCountry.setText(String.valueOf(getString(R.string.pals)));
                                                                else if(country.equals("Poker")) tvCountry.setText(String.valueOf(getString(R.string.Poker)));
                                                                else if(country.equals("Acting Arabia")) tvCountry.setText(String.valueOf(getString(R.string.saud)));
                                                                else if(country.equals("Other")) tvCountry.setText(String.valueOf(getString(R.string.yem)));
                                                                else if(country.equals("Bodybuilding")) tvCountry.setText(String.valueOf(getString(R.string.syr)));
                                                                else if(country.equals("swimming")) tvCountry.setText(String.valueOf(getString(R.string.sola)));
                                                                else if(country.equals("Daydreaming")) tvCountry.setText(String.valueOf(getString(R.string.tun)));
                                                                else if(country.equals("Traveling")) tvCountry.setText(String.valueOf(getString(R.string.Traveling)));
                                                                else
                                                                    tvCountry.setText("none");


                                                                // add in gradle app picasso
                                                                // compile 'com.squareup.picasso:picasso:2.5.2'
                                                                if(!image.equals("imageDefault")) {
                                                                    ///Picasso.with(SettingActivity.this).load(image).placeholder(R.drawable.no_image_profile).into(displayImageProfile);

                                                                    //// Offline Capabilities: networkPolicy(NetworkPolicy.OFFLINE)
                                                                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE)
                                                                            .placeholder(R.drawable.no_image_profile).into(imgvUser, new Callback() {
                                                                                @Override
                                                                                public void onSuccess() {

                                                                                }
                                                                                @Override
                                                                                public void onError(Exception e) {
                                                                                    Picasso.get().load(image).placeholder(R.drawable.no_image_profile).into(imgvUser);
                                                                                }
                                                                            });
                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });



                                        }
                                        @Override public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }
                            });

                        }

                    }

                };
        rcvSearchUsers.setAdapter(userSearchRecyclerAdapte);

        userSearchRecyclerAdapte.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int testimonycount = userSearchRecyclerAdapte.getItemCount();
                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (testimonycount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rcvSearchUsers.scrollToPosition(positionStart);
                }
            }
        });

    }


//    private void firebaseRecyclerviewone(Query qury){
//
//        valueFive = 5;
//        userSearchRecyclerAdapte =
//                new FirebaseRecyclerAdapter<UsersSearch,
//                        userSearchViewHolder>(
//
//                        UsersSearch.class,
//                        R.layout.layout_pack_user_search,
//                        userSearchViewHolder.class,
//                        qury
//
//                ) {
//                    @Override
//                    protected void populateViewHolder(final userSearchViewHolder viewHolder, UsersSearch model, int position) {
//
//
//                        final String list_user_id = getRef(position).getKey();
//
//                        userdatabseRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                try {
//
//                                    String userName = String.valueOf(dataSnapshot.child("username").getValue());
//                                    String image = String.valueOf(dataSnapshot
//                                            .child(String.valueOf(getString(R.string.fb_thumb_image))).getValue());
//                                    viewHolder.setusername(userName);
//                                    viewHolder.setUserImage(image,getContext());
//
//                                }catch(Exception e){}
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//                        //// set id user and click ////
//                        final String userIDvisit = getRef(position).getKey();
//                        if(userIDvisit.equals(idUser))
//                        {
//                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent userProfileIntent = new Intent(getActivity(),AccountFrag.class);
//                                    startActivity(userProfileIntent);
//                                }
//                            });
//                        }
//                        else {
//
//                            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                    Toast.makeText(getActivity(),getString(R.string.waiting), Toast.LENGTH_SHORT).show();
//
//                                    userdatabseRef.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                                                mViewInflate = getLayoutInflater().inflate(R.layout.customdialog,null);
//                                                imgvUser = mViewInflate.findViewById(R.id.imgvUser);
//                                                tvAge = mViewInflate.findViewById(R.id.tvAgeUser);
//                                                tvRelationShip = mViewInflate.findViewById(R.id.tvRelationshipUser);
//                                                tvCountry = mViewInflate.findViewById(R.id.tvCountryProfileUser);
//                                                tvUsername = mViewInflate.findViewById(R.id.tvUsernameUser);
//                                                btnSendMsg = mViewInflate.findViewById(R.id.btnSendMsg);
//                                                imgvCloseUserProfile = mViewInflate.findViewById(R.id.imgvCloseUserProfile);
//
//
//                                                ImageView imgvReport = mViewInflate.findViewById(R.id.imgvReport);
//
//                                                imgvReport.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View view) {
//
//                                                        dialogReportuser(userIDvisit);
//                                                    }
//                                                });
//                                                btnSendMsg.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View view) {
//
//                                                        Intent intmessage = new Intent(getActivity(),ChatUserActivity.class);
//                                                        intmessage.putExtra("userIDvisited",userIDvisit);
//                                                        startActivity(intmessage);
//                                                    }
//                                                });
//
//
//
//                                                AlertDialog.Builder alertDialogBuilder = DialogUtils.CustomAlertDialog(mViewInflate,getActivity());
//                                                final AlertDialog alertDialog = alertDialogBuilder.create();
//                                                alertDialog.setCancelable(false);
//                                                alertDialog.show();
//
//                                                imgvCloseUserProfile.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View view) {
//                                                        alertDialog.dismiss();
//                                                    }
//                                                });
//
//                                                userdatabseRef.child(userIDvisit).addValueEventListener(new ValueEventListener() {
//
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                                        /// Stock information from firebase to Setting activity .
//                                                        String username = dataSnapshot.child(String.valueOf(getString(R.string.fb_username))).getValue().toString();
//                                                        final String image = dataSnapshot.child(String.valueOf(getString(R.string.fb_thumb_image))).getValue().toString();
//                                                        String age = dataSnapshot.child(String.valueOf(getString(R.string.fb_age))).getValue().toString();
//                                                        String country = dataSnapshot.child(String.valueOf(getString(R.string.fb_country))).getValue().toString();
//                                                        String relationship = dataSnapshot.child(String.valueOf(getString(R.string.fb_relationship))).getValue().toString();
//
//                                                        tvAge.setText(age);
//                                                        tvUsername.setText(username);
//                                                        if(relationship.equals("0")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_single)));else
//                                                        if(relationship.equals("1")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_relatio)));else
//                                                        if(relationship.equals("2")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_engeg)));else
//                                                        if(relationship.equals("3")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_inopen)));else
//                                                        if(relationship.equals("4")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_itcomp)));else
//                                                        if(relationship.equals("6")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_in)));else
//                                                        if(relationship.equals("7")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_sep)));else
//                                                        if(relationship.equals("8")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_marr)));else
//                                                        if(relationship.equals("9")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_indo)));else
//                                                        if(relationship.equals("10")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_wid)));else
//                                                        if(relationship.equals("11")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_div)));else
//                                                        if(relationship.equals("12")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_none)));
//
//
//
//                                                        if(country.equals("Writing")) tvCountry.setText(String.valueOf(getString(R.string.egy)));
//                                                        else if(country.equals("Cooking")) tvCountry.setText(String.valueOf(getString(R.string.Cooking)));
//                                                        else if(country.equals("Amateur Radio")) tvCountry.setText(String.valueOf(getString(R.string.sud)));
//                                                        else if(country.equals("Hiking")) tvCountry.setText(String.valueOf(getString(R.string.Hiking)));
//                                                        else if(country.equals("Painting")) tvCountry.setText(String.valueOf(getString(R.string.Painting)));
//                                                        else if(country.equals("Sculpture")) tvCountry.setText(String.valueOf(getString(R.string.dji)));
//                                                        else if(country.equals("Running")) tvCountry.setText(String.valueOf(getString(R.string.irq)));
//                                                        else if(country.equals("Dancing")) tvCountry.setText(String.valueOf(getString(R.string.jord)));
//                                                        else if(country.equals("Yoga")) tvCountry.setText(String.valueOf(getString(R.string.kut)));
//                                                        else if(country.equals("Meditating")) tvCountry.setText(String.valueOf(getString(R.string.leb)));
//                                                        else if(country.equals("Reading")) tvCountry.setText(String.valueOf(getString(R.string.lib)));
//                                                        else if(country.equals("Video Games")) tvCountry.setText(String.valueOf(getString(R.string.maur)));
//                                                        else if(country.equals("Gardening")) tvCountry.setText(String.valueOf(getString(R.string.mar)));
//                                                        else if(country.equals("Knitting")) tvCountry.setText(String.valueOf(getString(R.string.omar)));
//                                                        else if(country.equals("Woodwork")) tvCountry.setText(String.valueOf(getString(R.string.pals)));
//                                                        else if(country.equals("Poker")) tvCountry.setText(String.valueOf(getString(R.string.Poker)));
//                                                        else if(country.equals("Acting Arabia")) tvCountry.setText(String.valueOf(getString(R.string.saud)));
//                                                        else if(country.equals("Other")) tvCountry.setText(String.valueOf(getString(R.string.yem)));
//                                                        else if(country.equals("Bodybuilding")) tvCountry.setText(String.valueOf(getString(R.string.syr)));
//                                                        else if(country.equals("swimming")) tvCountry.setText(String.valueOf(getString(R.string.sola)));
//                                                        else if(country.equals("Daydreaming")) tvCountry.setText(String.valueOf(getString(R.string.tun)));
//                                                        else if(country.equals("Traveling")) tvCountry.setText(String.valueOf(getString(R.string.Traveling)));
//                                                        else
//                                                            tvCountry.setText("none");
//
//
//                                                        // add in gradle app picasso
//                                                        // compile 'com.squareup.picasso:picasso:2.5.2'
//                                                        if(!image.equals("imageDefault")) {
//                                                            ///Picasso.with(SettingActivity.this).load(image).placeholder(R.drawable.no_image_profile).into(displayImageProfile);
//
//                                                            //// Offline Capabilities: networkPolicy(NetworkPolicy.OFFLINE)
//                                                            Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE)
//                                                                    .placeholder(R.drawable.no_image_profile).into(imgvUser, new Callback() {
//                                                                @Override
//                                                                public void onSuccess() {
//
//                                                                }
//                                                                @Override
//                                                                public void onError(Exception e) {
//                                                                    Picasso.get().load(image).placeholder(R.drawable.no_image_profile).into(imgvUser);
//                                                                }
//                                                            });
//                                                        }
//
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(DatabaseError databaseError) {
//
//                                                    }
//                                                });
//
//                                        }
//                                        @Override public void onCancelled(DatabaseError databaseError) {
//                                        }
//                                    });
//                                }
//                            });
//
//                        }
//
//                    }
//
//                };
//        rcvSearchUsers.setAdapter(userSearchRecyclerAdapte);
//        userSearchRecyclerAdapte.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//
//
//                    rcvSearchUsers.scrollToPosition(numberuser);
//
//            }
//        });
//
//    }

    public static class userSearchViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageView imgvUsersSearch;
        TextView tvUsername ;

        public userSearchViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tvUsername =  mView.findViewById(R.id.tvUsernameSaerchWorld);
            imgvUsersSearch =  mView.findViewById(R.id.civUserSearch);

        }



        public void setusername(String username){
            tvUsername.setText(username);
        }


        public void setUserImage(String thumb_image, Context ctx){

            Picasso.get().load(thumb_image).placeholder(R.drawable.no_image_profile).into(imgvUsersSearch);

        }


    }

    @Override
    public void onStart() {
        super.onStart();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dbrefSearchMale = FirebaseDatabase.getInstance().getReference().child("Search").child("Male");
        dbrefSearchFemale = FirebaseDatabase.getInstance().getReference().child("Search").child("Female");

        if(currentUser != null) {
            idUser = currentUser.getUid();
            userdatabseRef = FirebaseDatabase.getInstance().getReference().child("Users");


            userdatabseRef.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String sex = String.valueOf(dataSnapshot.child("sex").getValue());

                    if(sex.equals("Female")) {
                        Query qq = dbrefSearchMale.orderByChild("number").limitToLast(numberuser);
                        firebaseRecyclerview(qq);
                    }
                    else
                    {
                        Query qq = dbrefSearchFemale.orderByChild("number").limitToLast(numberuser);
                        firebaseRecyclerview(qq);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

//            btnSHowMore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //setAdsinstforShowMore(view);
//                    //if(isshowed) {
//
//                    userdatabseRef.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            String sex = String.valueOf(dataSnapshot.child("sex").getValue());
//
//                            if (sex.equals("Female")) {
//                                Query qq = dbrefSearchMale.orderByChild("number").limitToLast(numberuser * indexxx);
//                                firebaseRecyclerviewone(qq);
//                            } else {
//                                Query qq = dbrefSearchFemale.orderByChild("number").limitToLast(numberuser * indexxx);
//                                firebaseRecyclerviewone(qq);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
//                    indexxx++;
//                    btnSHowMore.setVisibility(View.GONE);
//                    //}
//
//                }
//            });


            // for show button when i scroll in the end //
//            rcvSearchUsers.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    //dx horizontal distance scrolled in pixels
//                    //dy vertical distance scrolled in pixels
//                    super.onScrolled(recyclerView, dx, dy);
//
//                    if (dy > 0 ) {
//
////                        int visibleItemCount = mLayoutManager.getChildCount();
////                        int totalItemCount = mLayoutManager.getItemCount();
//                        int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
//
////                        Toast.makeText(getActivity(), "visibleItemCount : "+String.valueOf(visibleItemCount), Toast.LENGTH_SHORT).show();
////                        Toast.makeText(getActivity(), "total : "+String.valueOf(totalItemCount), Toast.LENGTH_SHORT).show();
////                        Toast.makeText(getActivity(), "pastvisible : "+ String.valueOf(pastVisiblesItems), Toast.LENGTH_SHORT).show();
//
//
//                        if (pastVisiblesItems == 0) {
//                            Log.v("...", " Reached Last Item");
//                            btnSHowMore.setVisibility(View.VISIBLE);
//                        }else{
//                            btnSHowMore.setVisibility(View.GONE);
//                        }
//
//                    }else{
//                        btnSHowMore.setVisibility(View.GONE);
//                    }
//
//                }
//            });
////
//
//
        }



    }



    private void dialogReportuser(final String fromuser){

        mViewInflateReportuser = getLayoutInflater().inflate(R.layout.dialog_abusecontent,null);
        TextView btnSentReport = mViewInflateReportuser.findViewById(R.id.btnSentReport);
        TextView btnCancel = mViewInflateReportuser.findViewById(R.id.btnCancel);
        final AlertDialog.Builder alertDialogBuilder = DialogUtils.CustomAlertDialog(mViewInflateReportuser,getActivity());
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        btnSentReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keypush =dbReportAbuseOfContent.push().getKey();
                dbReportAbuseOfContent.child(keypush).child("userID").setValue(idUser);
                dbReportAbuseOfContent.child(keypush).child("reportUserID").setValue(fromuser);
                dbReportAbuseOfContent.child(keypush).child("time").setValue(System.currentTimeMillis()*-1);
                alertDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.send_succes), Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }


    private void testREMOVEADS(final String userIDvisit){

        mViewInflate = getLayoutInflater().inflate(R.layout.customdialog,null);
        imgvUser = mViewInflate.findViewById(R.id.imgvUser);
        tvAge = mViewInflate.findViewById(R.id.tvAgeUser);
        tvRelationShip = mViewInflate.findViewById(R.id.tvRelationshipUser);
        tvCountry = mViewInflate.findViewById(R.id.tvCountryProfileUser);
        tvUsername = mViewInflate.findViewById(R.id.tvUsernameUser);
        btnSendMsg = mViewInflate.findViewById(R.id.btnSendMsg);
        imgvCloseUserProfile = mViewInflate.findViewById(R.id.imgvCloseUserProfile);

        btnSendMsg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                dbBlock.child(idUser).child(userIDvisit).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Toast.makeText(getActivity(), getString(R.string.stg_clocked), Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intmessage = new Intent(getActivity(),ChatUserActivity.class);
                            intmessage.putExtra("userIDvisited",userIDvisit);
                            startActivity(intmessage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        AlertDialog.Builder alertDialogBuilder = DialogUtils.CustomAlertDialog(mViewInflate,getActivity());
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        imgvCloseUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

        userdatabseRef.child(userIDvisit).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /// Stock information from firebase to Setting activity .
                String username = dataSnapshot.child(String.valueOf(getString(R.string.fb_username))).getValue().toString();
                final String image = dataSnapshot.child(String.valueOf(getString(R.string.fb_thumb_image))).getValue().toString();
                String age = dataSnapshot.child(String.valueOf(getString(R.string.fb_age))).getValue().toString();
                String country = dataSnapshot.child(String.valueOf(getString(R.string.fb_country))).getValue().toString();
                String relationship = dataSnapshot.child(String.valueOf(getString(R.string.fb_relationship))).getValue().toString();

                tvAge.setText(age);
                tvUsername.setText(username);
                if(relationship.equals("0")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_single)));else
                if(relationship.equals("1")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_relatio)));else
                if(relationship.equals("2")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_engeg)));else
                if(relationship.equals("3")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_inopen)));else
                if(relationship.equals("4")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_itcomp)));else
                if(relationship.equals("6")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_in)));else
                if(relationship.equals("7")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_sep)));else
                if(relationship.equals("8")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_marr)));else
                if(relationship.equals("9")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_indo)));else
                if(relationship.equals("10")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_wid)));else
                if(relationship.equals("11")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_div)));else
                if(relationship.equals("12")) tvRelationShip.setText(String.valueOf(getString(R.string.sp_none)));



                if(country.equals("Writing")) tvCountry.setText(String.valueOf(getString(R.string.egy)));
                else if(country.equals("Cooking")) tvCountry.setText(String.valueOf(getString(R.string.Cooking)));
                else if(country.equals("Amateur Radio")) tvCountry.setText(String.valueOf(getString(R.string.sud)));
                else if(country.equals("Hiking")) tvCountry.setText(String.valueOf(getString(R.string.Hiking)));
                else if(country.equals("Painting")) tvCountry.setText(String.valueOf(getString(R.string.Painting)));
                else if(country.equals("Sculpture")) tvCountry.setText(String.valueOf(getString(R.string.dji)));
                else if(country.equals("Running")) tvCountry.setText(String.valueOf(getString(R.string.irq)));
                else if(country.equals("Dancing")) tvCountry.setText(String.valueOf(getString(R.string.jord)));
                else if(country.equals("Yoga")) tvCountry.setText(String.valueOf(getString(R.string.kut)));
                else if(country.equals("Meditating")) tvCountry.setText(String.valueOf(getString(R.string.leb)));
                else if(country.equals("Reading")) tvCountry.setText(String.valueOf(getString(R.string.lib)));
                else if(country.equals("Video Games")) tvCountry.setText(String.valueOf(getString(R.string.maur)));
                else if(country.equals("Gardening")) tvCountry.setText(String.valueOf(getString(R.string.mar)));
                else if(country.equals("Knitting")) tvCountry.setText(String.valueOf(getString(R.string.omar)));
                else if(country.equals("Woodwork")) tvCountry.setText(String.valueOf(getString(R.string.pals)));
                else if(country.equals("Poker")) tvCountry.setText(String.valueOf(getString(R.string.Poker)));
                else if(country.equals("Acting Arabia")) tvCountry.setText(String.valueOf(getString(R.string.saud)));
                else if(country.equals("Other")) tvCountry.setText(String.valueOf(getString(R.string.yem)));
                else if(country.equals("Bodybuilding")) tvCountry.setText(String.valueOf(getString(R.string.syr)));
                else if(country.equals("swimming")) tvCountry.setText(String.valueOf(getString(R.string.sola)));
                else if(country.equals("Daydreaming")) tvCountry.setText(String.valueOf(getString(R.string.tun)));
                else if(country.equals("Traveling")) tvCountry.setText(String.valueOf(getString(R.string.Traveling)));
                else
                    tvCountry.setText("none");


                // add in gradle app picasso
                // compile 'com.squareup.picasso:picasso:2.5.2'
                if(!image.equals("imageDefault")) {
                    ///Picasso.with(SettingActivity.this).load(image).placeholder(R.drawable.no_image_profile).into(displayImageProfile);

                    //// Offline Capabilities: networkPolicy(NetworkPolicy.OFFLINE)
                    Picasso.get().load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.no_image_profile).into(imgvUser, new Callback() {
                                @Override
                                public void onSuccess() {

                                }
                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(image).placeholder(R.drawable.no_image_profile).into(imgvUser);
                                }
                            });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//
//
//
//
//                }
//            }
//        });


    }

}
