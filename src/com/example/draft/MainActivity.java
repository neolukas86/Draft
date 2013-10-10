package com.example.draft;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends Activity{
    private List<Player> player_values = new ArrayList<Player>();
    private ArrayList<String> playerList = new ArrayList<String>();
    private ArrayList<String> teamList = new ArrayList<String>();
    private ArrayList<String> playersTeamsList = new ArrayList<String>();
    private PlayerAdapter playerAdapter = null;
    private ArrayAdapter teamAdapter = null, playersTeamsAdapter = null;
    private ListView listViewPlayers, listViewTeams, listViewPlayersTeams;
    private Dialog dialogNewPlayer, dialogNewTeam;
    private boolean showDialogPlayer = false, showDialogTeam = false;
    private boolean sorteado = false;
    private String inputDialogPlayer, inputDialogTeam;
    private Integer teamSelected;
    private TextView textFinal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

//        ListView lv = (ListView) findViewById(R.id.listViewPlayers);
//        lv.setAdapter(new PlayerAdapter(this,playerList));

        ImageButton new_player = (ImageButton) findViewById(R.id.buttonNewPlayer);
        ImageButton new_team = (ImageButton) findViewById(R.id.buttonNewTeam);
        ImageButton shuffle = (ImageButton) findViewById(R.id.buttonShuffle);
        ImageButton pick = (ImageButton) findViewById(R.id.buttonPick);

        new_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNewPlayer = launchDialogNewPlayer();
            }
        });

        new_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNewTeam = launchDialogNewTeam();
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sorteado = shufflePlayers(player_values, playerAdapter, sorteado);
            }
        });

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleChoiceTeamListDialog(teamList,player_values, playersTeamsList,
                                            teamAdapter, playerAdapter, playersTeamsAdapter,
                        player_values.get(0).name + " " + getString(R.string.pick_team));
            }
        });

        listViewPlayers = (ListView) findViewById(R.id.listViewPlayers);
        listViewTeams = (ListView) findViewById(R.id.listViewTeams);
        listViewPlayersTeams = (ListView) findViewById(R.id.listViewPlayersTeams);

        View header = getLayoutInflater().inflate(R.layout.header, null);
        TextView headerText = (TextView) header.findViewById(R.id.headerTextView);
        headerText.setText(R.string.players);
        listViewPlayers.addHeaderView(header);

        View headerT = getLayoutInflater().inflate(R.layout.header, null);
        headerText = (TextView) headerT.findViewById(R.id.headerTextView);
        headerText.setText(R.string.teams);
        listViewTeams.addHeaderView(headerT);
    }


    @Override
    public void onResume(){
        super.onResume();
//        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

        playerAdapter = new PlayerAdapter(this,R.layout.row_listviewplayer, player_values);
        listViewPlayers.setAdapter(playerAdapter);

        teamAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, teamList);
        listViewTeams.setAdapter(teamAdapter);

        playersTeamsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, playersTeamsList);
        listViewPlayersTeams.setAdapter(playersTeamsAdapter);



        if(showDialogPlayer){
            dialogNewPlayer = launchDialogNewPlayer();

            EditText text = (EditText) dialogNewPlayer.findViewById(R.id.username);
            text.setText(inputDialogPlayer);
        }

        if(showDialogTeam){
            dialogNewTeam = launchDialogNewTeam();

            EditText text = (EditText) dialogNewTeam.findViewById(R.id.username);
            text.setText(inputDialogTeam);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
//        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop(){
        super.onStop();
//        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
//        Toast.makeText(this, "onSaveInstanceState", Toast.LENGTH_SHORT).show();

        savedInstanceState.putStringArrayList("pList",playerList);
        savedInstanceState.putStringArrayList("tList",teamList);
        savedInstanceState.putBoolean("showDialogPlayer",showDialogPlayer);
        savedInstanceState.putBoolean("showDialogTeam",showDialogTeam);

        EditText textP = (EditText) dialogNewPlayer.findViewById(R.id.username);

        Editable textInputPlayer = textP.getText();
        if(textInputPlayer != null){
            savedInstanceState.putString("inputDialogPlayer",textInputPlayer.toString());
        }
        else{
            EditText textT = (EditText) dialogNewPlayer.findViewById(R.id.username);

            Editable textInputTeam = textT.getText();
            if(textInputTeam != null){
                savedInstanceState.putString("inputDialogTeam",textInputTeam.toString());
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
//        Toast.makeText(this, "onRestoreInstanceState", Toast.LENGTH_SHORT).show();

        playerList = savedInstanceState.getStringArrayList("pList");
        teamList = savedInstanceState.getStringArrayList("tList");
        showDialogPlayer = savedInstanceState.getBoolean("showDialogPlayer");
        showDialogTeam = savedInstanceState.getBoolean("showDialogTeam");
        inputDialogPlayer = savedInstanceState.getString("inputDialogPlayer");
        inputDialogTeam = savedInstanceState.getString("inputDialogTeam");
    }


    public Dialog launchDialogNewPlayer(){
        return launchDialogNewItem(R.string.new_player, true);
    }

    public Dialog launchDialogNewTeam(){
        return launchDialogNewItem(R.string.new_team, false);
    }

    public Dialog launchDialogNewItem(int title, final boolean isPlayer){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View layout = inflater.inflate(R.layout.dialog_new_item, null);
        builder.setView(layout);

        final EditText input = (EditText) layout.findViewById(R.id.username);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String name = input.getText().toString().trim();
                if(name != null && name !=""){
                    if(isPlayer){
                        playerList.add(name);

                        player_values.add(new Player(playerList.get(playerList.size()-1).toString()));
                        playerAdapter.notifyDataSetChanged();
                    }
                    else{
                        teamList.add(name);
                        teamAdapter.notifyDataSetChanged();
                    }
                    //Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                }

                if(isPlayer){
                    showDialogPlayer = false;
                }
                else{
                    showDialogTeam = false;
                }

                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(isPlayer){
                    showDialogPlayer = false;
                }
                else{
                    showDialogTeam = false;
                }

                dialog.cancel();
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        dialog.show();

        if(isPlayer){
            showDialogPlayer = false;
        }
        else{
            showDialogTeam = false;
        }

        return dialog;
    }



    public boolean shufflePlayers(final List<Player> list, final PlayerAdapter adapter, boolean sorteado){
        if(sorteado){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.shuffle_again_question)
                    .setTitle(R.string.shuffle_again)

                    // Add the buttons
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Collections.shuffle(list);
                            adapter.notifyDataSetChanged();

                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            // Create the AlertDialog
            AlertDialog dialog = builder.create();

            dialog.show();
        }
        else{
            Collections.shuffle(list);
            adapter.notifyDataSetChanged();

            sorteado = true;
        }

        return sorteado;
    }


    public Dialog singleChoiceTeamListDialog(final ArrayList<String> listT, final List<Player> listP,
                                             final ArrayList<String> listPT,
                                             final ArrayAdapter adapterT, final PlayerAdapter adapterP,
                                             final ArrayAdapter adapterPT,
                                             String title){
        teamSelected = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        CharSequence[] cs = listT.toArray(new CharSequence[listT.size()]);

        builder.setTitle(title)
                .setSingleChoiceItems(cs, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        teamSelected = i;
                    }
                })

                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        list.remove(teamSelected);
//                        adapter.notifyDataSetChanged();


                        Player p = listP.remove(0);

//                        textFinal.setText(textFinal.getText() + p.name + " - "
//                                + listT.remove(listT.get(teamSelected)));

                        String team = listT.get(teamSelected);
                        listT.remove(team);

                        listPT.add(p.name + " - " + team );

//                        listT.remove(listT.get(teamSelected));

                        adapterP.notifyDataSetChanged();
                        adapterT.notifyDataSetChanged();
                        adapterPT.notifyDataSetChanged();

                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        dialog.show();

        return dialog;
    }

}

