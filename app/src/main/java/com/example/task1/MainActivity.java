package com.example.task1;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private final String[] conversionTypes = {"Length", "Weight", "Temperature"};
    private final String[] conversionLength = {"cm", "m", "km", "in", "ft", "yd", "mi"};
    private final String[] conversionWeight = {"g", "lb", "kg", "oz", "ton"};
    private final String[] conversionTemperature = {"C", "F", "K"};

    private Spinner unitFromSpinner, unitToSpinner;
    private EditText inputValue;
    private TextView outputValue;
    private int selectedConversionType = 0; // Default to Length

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Reference to spinners and input/output fields
        Spinner conversionTypeSpinner = findViewById(R.id.conversionTypeSpin);
        unitFromSpinner = findViewById(R.id.unitFromSpin);
        unitToSpinner = findViewById(R.id.unitToSpin);
        inputValue = findViewById(R.id.inputValue);
        outputValue = findViewById(R.id.outputValue);
        Button convertButton = findViewById(R.id.convertButton);

        // Set listeners
        conversionTypeSpinner.setOnItemSelectedListener(this);

        // Set up adapter for conversion type spinner
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, conversionTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conversionTypeSpinner.setAdapter(typeAdapter);

        // Set button click listener for conversion
        convertButton.setOnClickListener(v -> performConversion());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.conversionTypeSpin) {
            selectedConversionType = position;
            updateUnitSpinners(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void updateUnitSpinners(int position) {
        String[] selectedUnits;

        switch (position) {
            case 0: selectedUnits = conversionLength; break;
            case 1: selectedUnits = conversionWeight; break;
            case 2: selectedUnits = conversionTemperature; break;
            default: selectedUnits = new String[]{};
        }

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectedUnits);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unitFromSpinner.setAdapter(unitAdapter);
        unitToSpinner.setAdapter(unitAdapter);
    }

    private void performConversion() {
        String fromUnit = unitFromSpinner.getSelectedItem().toString();
        String toUnit = unitToSpinner.getSelectedItem().toString();
        String inputText = inputValue.getText().toString();

        if (inputText.isEmpty()) {
            Toast.makeText(this, "Enter a value to convert", Toast.LENGTH_SHORT).show();
            return;
        }

        double input = Double.parseDouble(inputText);
        double result = convertUnits(selectedConversionType, input, fromUnit, toUnit);
        outputValue.setText(String.format("%.2f", result, toUnit));
    }

    private double convertUnits(int type, double value, String from, String to) {
        if (type == 0) { // Length Conversion
            return lengthConversion(value, from, to);
        } else if (type == 1) { // Weight Conversion
            return weightConversion(value, from, to);
        } else { // Temperature Conversion
            return temperatureConversion(value, from, to);
        }
    }

    private double lengthConversion(double value, String from, String to) {
        double meters;
        switch (from) {
            case "cm": meters = value / 100; break;
            case "m": meters = value; break;
            case "km": meters = value * 1000; break;
            case "in": meters = value * 0.0254; break;
            case "ft": meters = value * 0.3048; break;
            case "yd": meters = value * 0.9144; break;
            case "mi": meters = value * 1609.34; break;
            default: return value;
        }

        switch (to) {
            case "cm": return meters * 100;
            case "m": return meters;
            case "km": return meters / 1000;
            case "in": return meters / 0.0254;
            case "ft": return meters / 0.3048;
            case "yd": return meters / 0.9144;
            case "mi": return meters / 1609.34;
            default: return value;
        }
    }

    private double weightConversion(double value, String from, String to) {
        double kg;
        switch (from) {
            case "g": kg = value / 1000; break;
            case "lb": kg = value * 0.453592; break;
            case "kg": kg = value; break;
            case "oz": kg = value * 0.0283495; break;
            case "ton": kg = value * 907.185; break;
            default: return value;
        }

        switch (to) {
            case "g": return kg * 1000;
            case "lb": return kg / 0.453592;
            case "kg": return kg;
            case "oz": return kg / 0.0283495;
            case "ton": return kg / 907.185;
            default: return value;
        }
    }

    private double temperatureConversion(double value, String from, String to) {
        if (from.equals(to)) return value;

        double celsius;
        switch (from) {
            case "C": celsius = value; break;
            case "F": celsius = (value - 32) * 1.8; break;
            case "K": celsius = value - 273.15; break;
            default: return value;
        }

        switch (to) {
            case "C": return celsius;
            case "F": return celsius * 1.8 + 32;
            case "K": return celsius + 273.15;
            default: return value;
        }
    }
}
