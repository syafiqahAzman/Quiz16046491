package webservices.rp.edu.quiz16046491;

import android.Manifest;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    Button btnSave, btnRead;
    EditText etCoordinates;
    TextView tv;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                
            }
        });
        btnSave = findViewById(R.id.btnSave);
        etCoordinates = findViewById(R.id.etCoordinates);
        btnRead = findViewById(R.id.btnRead);
        tv = findViewById(R.id.tv);

        if(checkPermission() == true){

            String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";
            File folder = new File(folderLocation);
            if (folder.exists() == false){
                boolean result = folder.mkdir();
                if (result == true){
                    Log.d("File Read/Write", "Folder created");

                }
            }
        } else{

            String msg = "Permission not granted";
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";
                    File targetFile = new File(folderLocation, "quiz.txt");
                    FileWriter writer = new FileWriter(targetFile, false);
                    String getCoordinates = etCoordinates.getText().toString();
                    writer.write(getCoordinates );
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to write!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( checkPermission() == true){
                    String folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";
                    File targetFile = new File(folderLocation, "quiz.txt");
                    if (targetFile.exists() == true){
                        String data ="";
                        try {
                            FileReader reader = new FileReader(targetFile);
//                            can read alot
                            BufferedReader br = new BufferedReader(reader);
//                            read line by line
                            String line = br.readLine();
                            while (line != null){
                                data += line + "\n";
                                line = br.readLine();
                            }
                            br.close();
                            reader.close();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to read!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        Log.d("Content", data);
                        tv.setText(data);
                    }
                }
                else{

                    String msg = "Permission not granted";
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }

            }
        });



    }
    private boolean checkPermission(){
        int permissionCheck_Write = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int permissionCheck_Read = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE
        );

        if (permissionCheck_Write == PermissionChecker.PERMISSION_GRANTED && permissionCheck_Read == PermissionChecker.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }
}
