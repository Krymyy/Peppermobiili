package pepper.mobile.peppermobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;


public class GameActivity extends AppCompatActivity {

    Button hitButton;
    Button standButton;
    Button quitButton;
    ImageView cardOne;
    ImageView cardTwo;
    TextView totalView;
    public static final String TAG = "GameActivity: ";
    String activePlayer;
    String stuffFromDB;

    private  FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("games").document("MODnE5O0ol0gIA4NMY8x");

    int totalGlobal = 0;
    String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent i = getIntent();
        mAuth = FirebaseAuth.getInstance();
        playerName = i.getStringExtra("Player");
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if (task.isSuccessful()){
                            Log.d(TAG,"Sign In Successful");
                            FirebaseUser user = mAuth.getCurrentUser();
                        }else{
                            Log.w(TAG,"sign in FAILED", task.getException());
                            Toast.makeText(GameActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        hitButton = (Button) findViewById(R.id.hitButton);
        standButton = (Button) findViewById(R.id.standButton);
        quitButton = (Button) findViewById(R.id.quitButton);
        cardOne = (ImageView) findViewById(R.id.imageView);
        cardTwo = (ImageView) findViewById(R.id.imageView2);
        totalView = (TextView) findViewById(R.id.textView2);
        totalView.setText("Welcome, " + playerName);

        hitButton.setOnClickListener(v ->{
            getDBStuff(v);
            /*card +1 firebaseen id x
            totalGlobal+=5;
            totalView.setText("Total: " + totalGlobal);
            if(totalGlobal > 20) {
                Toast.makeText(getApplicationContext(), playerName + " won the game!", Toast.LENGTH_SHORT).show();
            }
            else
                ;*/
        });

        standButton.setOnClickListener(v->{
            /*card +0 firebaseen id y
            totalGlobal--;
            totalView.setText("Total: " + totalGlobal);*/

        });

        quitButton.setOnClickListener(v-> {
            //nollaa kaiken ja lähettää poistumisviestin firebaseen. Prompti haluaahan varmasti poistua??
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            totalGlobal = 0;
            startActivity(intent);
        });

    }

    public void getDBStuff(View view){
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        stuffFromDB = document.getData().toString();
                        try {

                            JSONObject jsonObject = new JSONObject(stuffFromDB);
                            activePlayer = jsonObject.getString("activePlayer");
                            totalView.setText(activePlayer);
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                } else {
                    Log.w(TAG, "error getting documents", task.getException());
                }
            }
        });
    }
}