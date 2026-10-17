package com.example.cowbusiness;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    BarChart chart;
    String currentCurrency = "$";
    double currentProfit = 0;
    double currentEmi = 0;
    String currentAiText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 🔹 INIT VIEWS
        TextView profitText = findViewById(R.id.profitText);
        TextView suggestionText = findViewById(R.id.suggestionText);
        TextView aiText = findViewById(R.id.aiText);
        TextView emiText = findViewById(R.id.emiText);
        Button exitBtn = findViewById(R.id.exitBtn);
        Button downloadPdfBtn = findViewById(R.id.downloadPdfBtn);
        chart = findViewById(R.id.chart);

        // 🔹 GET DATA FROM INTENT
        currentCurrency = getIntent().getStringExtra("currency");
        if (currentCurrency == null) currentCurrency = "$";

        currentProfit = getIntent().getDoubleExtra("profit", 0);
        String suggestion = getIntent().getStringExtra("suggestion");
        currentAiText = getIntent().getStringExtra("ai");
        currentEmi = getIntent().getDoubleExtra("emi", 0);

        // 🔹 DYNAMIC UI UPDATES
        profitText.setText(String.format("Total Profit: %s %.2f", currentCurrency, currentProfit));
        emiText.setText(String.format("Monthly EMI: %s %.2f", currentCurrency, currentEmi));
        suggestionText.setText(suggestion);
        aiText.setText(currentAiText);

        if (currentProfit < 0) {
            profitText.setTextColor(Color.parseColor("#FF5252"));
            profitText.setText(String.format("Total Loss: %s %.2f", currentCurrency, Math.abs(currentProfit)));
        } else {
            profitText.setTextColor(Color.parseColor("#4CAF50")); // Green — visible on any background
        }

        // 🔹 BAR CHART LOGIC
        setupChart(currentProfit, currentCurrency);

        // ✅ EXIT BUTTON
        exitBtn.setOnClickListener(v -> finish());

        // ✅ PDF DOWNLOAD BUTTON
        downloadPdfBtn.setOnClickListener(v -> generateReportPDF());
    }

    private void setupChart(double profit, String currency) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f, (float) Math.abs(profit)));

        BarDataSet dataSet = new BarDataSet(entries, "Financial Summary (" + currency + ")");
        dataSet.setColor(profit >= 0 ? Color.parseColor("#4CAF50") : Color.parseColor("#F44336"));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(14f);

        BarData data = new BarData(dataSet);
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void generateReportPDF() {
        PdfDocument document = new PdfDocument();
        // Standard A4 size: 595 x 842
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        TextPaint textPaint = new TextPaint();

        // 1. Header Background
        paint.setColor(currentProfit >= 0 ? Color.parseColor("#004D40") : Color.parseColor("#B71C1C"));
        canvas.drawRect(0, 0, 595, 120, paint);

        // 2. Title
        paint.setColor(Color.WHITE);
        paint.setTextSize(22);
        paint.setFakeBoldText(true);
        canvas.drawText("COW BUSINESS ANALYSIS REPORT", 40, 70, paint);

        // 3. Financial Summary Section
        paint.setColor(Color.BLACK);
        paint.setTextSize(14);
        paint.setFakeBoldText(true);
        int y = 160;
        canvas.drawText("FINANCIAL SUMMARY", 40, y, paint);

        paint.setFakeBoldText(false);
        y += 30;
        canvas.drawText("Project Status: " + (currentProfit >= 0 ? "PROFITABLE" : "LOSS RECOVERY"), 40, y, paint);
        y += 25;
        canvas.drawText("Net Result: " + currentCurrency + String.format("%.2f", currentProfit), 40, y, paint);
        y += 25;
        canvas.drawText("Monthly EMI: " + currentCurrency + String.format("%.2f", currentEmi), 40, y, paint);

        // 4. DRAW CHART IMAGE INTO PDF
        y += 40;
        Bitmap chartBitmap = chart.getChartBitmap(); // Get Bitmap from MPAndroidChart
        if (chartBitmap != null) {
            // Scale bitmap to fit width (leaving margins)
            Bitmap scaledChart = Bitmap.createScaledBitmap(chartBitmap, 450, 250, true);
            canvas.drawBitmap(scaledChart, 70, y, null);
            y += 280;
        }

        // 5. AI ADVISORY (WITH MULTI-LINE SUPPORT)
        paint.setFakeBoldText(true);
        canvas.drawText("AI BUSINESS ADVISORY:", 40, y, paint);
        y += 20;

        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextSize(12);
        StaticLayout staticLayout = new StaticLayout(currentAiText, textPaint, 515,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        canvas.save();
        canvas.translate(40, y);
        staticLayout.draw(canvas);
        canvas.restore();

        // 6. Footer on Page 1
        paint.setColor(Color.parseColor("#004D40"));
        paint.setTextSize(10);
        canvas.drawText("Official Report - Developed by Goutam Gupta", 40, 810, paint);

        document.finishPage(page);

        // --- OPTIONAL: MULTIPLE PAGE EXAMPLE ---
        // If y + staticLayout.getHeight() > 800, you would start a new page here.
        // For this app, the advisory usually fits, but the logic is:
        // PdfDocument.Page page2 = document.startPage(new PdfDocument.PageInfo.Builder(595, 842, 2).create());
        // ... draw more ...
        // document.finishPage(page2);

        // 7. Save and Share
        File file = new File(getExternalFilesDir(null), "CowBusiness_Analysis.pdf");
        try {
            document.writeTo(new FileOutputStream(file));
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Share Business Intelligence..."));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "PDF Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        document.close();
    }
}