package com.vazquez.carlos.myfirstappuprb;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText studentIdInput, studentNameInput, phoneNumberInput;
    Spinner concentrationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.studentIdInput = (EditText)findViewById(R.id.studentIdInput);
        this.studentNameInput = (EditText)findViewById(R.id.studentNameInput);
        this.phoneNumberInput = (EditText)findViewById(R.id.phoneNumberInput);

        this.phoneNumberInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        this.concentrationSpinner = (Spinner)findViewById(R.id.concentrationSpinner);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.concentration_list, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        concentrationSpinner.setAdapter(adapter);
    }

    public void save(View view) {
        JSONObject jsonObject = new JSONObject(), jsonData = new JSONObject();

        try {
            jsonObject.put("File", "studentInfo");

            jsonData.put("studentId", studentIdInput.getText().toString());
            jsonData.put("studentName", studentNameInput.getText().toString());
            jsonData.put("studentConcentration", concentrationSpinner.getSelectedItem().toString());
            jsonData.put("studentPhoneNumber", phoneNumberInput.getText().toString());

            jsonObject.put("data", jsonData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        writeFile(jsonObject);
    }

    public void writeFile(JSONObject obj) {
        final String FOLDER_NAME = "Student_Info", FILE_NAME = "studentInformation.json";

        File dir = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
        if (!dir.exists())
            dir.mkdir();

        File file = new File(dir, FILE_NAME);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(obj.toString());
            fileWriter.flush();
            fileWriter.close();
            saveFeedback(true);
        } catch (IOException e) {
            e.printStackTrace();
            saveFeedback(false);
        }
    }

    public void reset(View view) {
        studentIdInput.setText("");
        studentNameInput.setText("");
        phoneNumberInput.setText("");
        concentrationSpinner.setSelection(0);
    }

    public void saveFeedback(boolean result){
        Toast feedback = (result) ?
                Toast.makeText(this, getResources().getString(R.string.save_positive_feedback), Toast.LENGTH_SHORT):
                Toast.makeText(this, getResources().getString(R.string.save_negative_feedback), Toast.LENGTH_SHORT);

        feedback.show();
    }
}