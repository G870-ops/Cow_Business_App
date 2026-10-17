package com.example.cowbusiness;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText cows, cost, milk, price, expense, years, loan, rate;
    Button calculateBtn, resetBtn, profitAnalysisBtn, lossAnalysisBtn;
    LinearLayout aiCard;
    TextView aiText, developerText;
    BarChart previewChart;
    Switch darkModeSwitch;
    Spinner currencySpinner;

    SharedPreferences prefs;
    String selectedCurrency = "$";

    public static final String[] CURRENCY_ITEMS = {
            "$ - USD (US Dollar)",
            "₹ - INR (Indian Rupee)",
            "€ - EUR (Euro)",
            "£ - GBP (British Pound)",
            "¥ - JPY / CNY (Yen / Yuan)",
            "रू - NPR (Nepalese Rupee)",
            "৳ - BDT (Bangladeshi Taka)",
            "₨ - PKR (Pakistani Rupee)",
            "د.إ - AED (UAE Dirham)",
            "﷼ - SAR (Saudi Riyal)",
            "C$ - CAD (Canadian Dollar)",
            "A$ - AUD (Australian Dollar)",
            "CHF - CHF (Swiss Franc)",
            "₽ - RUB (Russian Ruble)",
            "₩ - KRW (South Korean Won)",
            "₺ - TRY (Turkish Lira)",
            "R$ - BRL (Brazilian Real)",
            "R - ZAR (South African Rand)",
            "KD - KWD (Kuwaiti Dinar)",
            "Rs - LKR (Sri Lankan Rupee)",
            "₱ - PHP (Philippine Peso)",
            "₫ - VND (Vietnamese Dong)",
            "฿ - THB (Thai Baht)",
            "RM - MYR (Malaysian Ringgit)",
            "Rp - IDR (Indonesian Rupiah)",
            "₦ - NGN (Nigerian Naira)",
            "₵ - GHS (Ghanaian Cedi)",
            "zł - PLN (Polish Zloty)",
            "kr - SEK / NOK (Krona)",
            "KSh - KES (Kenyan Shilling)",
            "E£ - EGP (Egyptian Pound)",
            "QAR - QAR (Qatari Riyal)"
    };

    public static final String[] CURRENCY_SYMBOLS = {
            "$", "₹", "€", "£", "¥", "रू", "৳", "₨", "د.إ", "﷼",
            "C$", "A$", "CHF", "₽", "₩", "₺", "R$", "R", "KD", "Rs",
            "₱", "₫", "฿", "RM", "Rp", "₦", "₵", "zł", "kr", "KSh", "E£", "QAR"
    };

    int[] darkColors = {Color.parseColor("#BBDEFB"), Color.parseColor("#C8E6C9"), Color.parseColor("#FFF9C4"), Color.parseColor("#FFCDD2"), Color.parseColor("#D1C4E9")};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupCurrencySpinner();
        prefs = getSharedPreferences("CowData", MODE_PRIVATE);
        loadData();

        darkModeSwitch.setOnCheckedChangeListener((v, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                showColorPickerDialog();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                resetDefaultColors();
            }
        });

        profitAnalysisBtn.setOnClickListener(v -> performFinancialAnalysis(true));
        lossAnalysisBtn.setOnClickListener(v -> performFinancialAnalysis(false));

        calculateBtn.setOnClickListener(v -> {
            if (validateInputs()) {
                processCalculation();
            } else {
                Toast.makeText(this, "Input Required: Populate All Data Fields with valid values", Toast.LENGTH_SHORT).show();
            }
        });

        resetBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this).setTitle("System Reset").setMessage("Wipe all locally stored data?")
                    .setPositiveButton("Proceed", (d, w) -> clearWithAnimation()).show();
        });

        startDeveloperNeonGlow();
    }

    private void initViews() {
        cows = findViewById(R.id.cows); cost = findViewById(R.id.cost);
        milk = findViewById(R.id.milk); price = findViewById(R.id.price);
        expense = findViewById(R.id.expense); years = findViewById(R.id.years);
        loan = findViewById(R.id.loan); rate = findViewById(R.id.rate);
        calculateBtn = findViewById(R.id.calculateBtn); resetBtn = findViewById(R.id.resetBtn);
        profitAnalysisBtn = findViewById(R.id.profitAnalysisBtn); lossAnalysisBtn = findViewById(R.id.lossAnalysisBtn);
        aiCard = findViewById(R.id.aiCard); aiText = findViewById(R.id.aiText);
        previewChart = findViewById(R.id.previewChart); darkModeSwitch = findViewById(R.id.darkModeSwitch);
        developerText = findViewById(R.id.developerText); currencySpinner = findViewById(R.id.currencySpinner);
    }

    private void setupCurrencySpinner() {
        currencySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CURRENCY_ITEMS));
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                if (pos >= 0 && pos < CURRENCY_SYMBOLS.length) {
                    selectedCurrency = CURRENCY_SYMBOLS[pos];
                    saveData();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });
    }

    private boolean validateInputs() {
        if (cows.getText().toString().trim().isEmpty() ||
            cost.getText().toString().trim().isEmpty() ||
            milk.getText().toString().trim().isEmpty() ||
            price.getText().toString().trim().isEmpty() ||
            expense.getText().toString().trim().isEmpty() ||
            years.getText().toString().trim().isEmpty()) {
            return false;
        }
        try {
            int c = Integer.parseInt(cows.getText().toString().trim());
            int y = Integer.parseInt(years.getText().toString().trim());
            double cCost = Double.parseDouble(cost.getText().toString().trim());
            double m = Double.parseDouble(milk.getText().toString().trim());
            double p = Double.parseDouble(price.getText().toString().trim());
            double e = Double.parseDouble(expense.getText().toString().trim());
            return c > 0 && y > 0 && cCost >= 0 && m >= 0 && p >= 0 && e >= 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void performFinancialAnalysis(boolean isProfitRequest) {
        if (!validateInputs()) {
            Toast.makeText(this, "Input Required: Populate All Data Fields with valid values", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int totalCows = Integer.parseInt(cows.getText().toString().trim());
            double costPerCow = Double.parseDouble(cost.getText().toString().trim());
            double dailyMilk = Double.parseDouble(milk.getText().toString().trim());
            double milkPrice = Double.parseDouble(price.getText().toString().trim());
            double monthlyExp = Double.parseDouble(expense.getText().toString().trim());
            int totalYrs = Integer.parseInt(years.getText().toString().trim());

            double loanPrincipal = loan.getText().toString().trim().isEmpty() ? 0 : Double.parseDouble(loan.getText().toString().trim());
            double interestRate = rate.getText().toString().trim().isEmpty() ? 0 : Double.parseDouble(rate.getText().toString().trim());

            // Accurate calculation matching BusinessCalculator
            double totalNetProfit = BusinessCalculator.calculateProfit(
                    totalCows, costPerCow, dailyMilk, milkPrice, monthlyExp, totalYrs, loanPrincipal, interestRate);
            double netAnnualProfit = totalNetProfit / totalYrs;

            if (isProfitRequest) {
                if (totalNetProfit >= 0) {
                    showGraph(totalYrs, netAnnualProfit, "Net Profit", Color.parseColor("#00C853"));
                    String aiMsg = PredictionHelper.predict(totalCows, dailyMilk, monthlyExp, totalNetProfit, selectedCurrency);
                    aiText.setText(aiMsg);
                    generateDetailedPDF("Profit_Analytics_Report", netAnnualProfit, true);
                } else {
                    Toast.makeText(this, "Analysis Mismatch: System calculated a Net Loss of " + selectedCurrency + " " + String.format("%,.2f", Math.abs(totalNetProfit)), Toast.LENGTH_LONG).show();
                }
            } else {
                if (totalNetProfit < 0) {
                    showGraph(totalYrs, Math.abs(netAnnualProfit), "Net Loss", Color.parseColor("#FF1744"));
                    String aiMsg = PredictionHelper.predict(totalCows, dailyMilk, monthlyExp, totalNetProfit, selectedCurrency);
                    aiText.setText(aiMsg);
                    generateDetailedPDF("Loss_Analytics_Report", netAnnualProfit, false);
                } else {
                    Toast.makeText(this, "Analysis Mismatch: System calculated a Net Profit of " + selectedCurrency + " " + String.format("%,.2f", totalNetProfit), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Data Error: Verify all numerical inputs", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateDetailedPDF(String fileName, double annualAmount, boolean isProfit) {
        PdfDocument doc = new PdfDocument();
        int pageWidth = 595;
        int pageHeight = 842;
        int margin = 40;

        // Initial Page Setup
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
        PdfDocument.Page page = doc.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // --- Background & Header (Page 1) ---
        paint.setColor(Color.parseColor("#F0F4F8"));
        canvas.drawRect(0, 0, pageWidth, pageHeight, paint);

        paint.setColor(isProfit ? Color.parseColor("#004D40") : Color.parseColor("#B71C1C"));
        canvas.drawRect(0, 0, pageWidth, 120, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(26);
        paint.setFakeBoldText(true);
        canvas.drawText("COW BUSINESS INTELLIGENCE", margin, 65, paint);

        paint.setTextSize(12);
        paint.setFakeBoldText(false);
        paint.setAlpha(180);
        canvas.drawText("SYSTEM ANALYSIS REPORT • REAL-TIME AI PREDICTION", margin, 90, paint);
        paint.setAlpha(255);

        int x = margin;
        int y = 160;

        // --- 1. BUSINESS PARAMETERS ---
        paint.setColor(Color.BLACK);
        paint.setTextSize(15);
        paint.setFakeBoldText(true);
        canvas.drawText("Business Financial Parameters", x, y, paint);

        y += 10;
        paint.setStrokeWidth(2);
        paint.setColor(Color.parseColor("#CCCCCC"));
        canvas.drawLine(x, y, 555, y, paint);

        y += 30;
        paint.setTextSize(13);
        paint.setFakeBoldText(false);
        paint.setColor(Color.DKGRAY);

        canvas.drawText("Total Cows: " + cows.getText(), x, y, paint);
        canvas.drawText("Cost per Unit Cow: " + selectedCurrency + " " + cost.getText(), x + 250, y, paint);
        y += 25;
        canvas.drawText("Milk/Day: " + milk.getText() + "L", x, y, paint);
        canvas.drawText("Price/Liter: " + selectedCurrency + " " + price.getText(), x + 250, y, paint);
        y += 25;
        canvas.drawText("Monthly Expense: " + selectedCurrency + " " + expense.getText(), x, y, paint);
        canvas.drawText("Loan Amount: " + selectedCurrency + " " + (loan.getText().toString().isEmpty() ? "0" : loan.getText()) + " , Rate = " + rate.getText() + "%", x + 250, y, paint);

        // --- DRAWABLE QR CODE INTEGRATION ---
        int qrSize = 80;
        int qrX = pageWidth - margin - qrSize;
        int qrY = 700;
        try {
            android.graphics.drawable.Drawable drawable = ContextCompat.getDrawable(this, R.drawable.qr_code);
            if (drawable != null) {
                Bitmap qrBitmap = Bitmap.createBitmap(qrSize, qrSize, Bitmap.Config.ARGB_8888);
                Canvas qrCanvas = new Canvas(qrBitmap);
                drawable.setBounds(0, 0, qrCanvas.getWidth(), qrCanvas.getHeight());
                drawable.draw(qrCanvas);
                canvas.drawBitmap(qrBitmap, qrX, qrY, null);
                paint.setColor(Color.GRAY); paint.setTextSize(7);
                canvas.drawText("SECURE VERIFICATION", qrX, qrY + qrSize + 10, paint);
            }
        } catch (Exception e) { e.printStackTrace(); }
        paint.setTextSize(7);
        // Footer placeholder reserved for future QR annotation text

        // --- 2. YEAR-WISE FORECAST (With Page Overflow) ---
        y += 60;
        paint.setFakeBoldText(true);
        paint.setTextSize(16);
        paint.setColor(isProfit ? Color.parseColor("#2E7D32") : Color.parseColor("#C62828"));
        canvas.drawText(isProfit ? "PROFIT FORECAST (Yearly)" : "LOSS FORECAST (Yearly)", x, y, paint);

        y += 30;
        int totalYears = Integer.parseInt(years.getText().toString());

        for (int i = 1; i <= totalYears; i++) {
            // OVERFLOW CHECK: Start new page if y exceeds 720
            if (y > 720) {
                doc.finishPage(page);
                pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, doc.getPages().size() + 1).create();
                page = doc.startPage(pageInfo);
                canvas = page.getCanvas();
                y = 60; // Reset Y for new page
                paint.setColor(Color.parseColor("#F0F4F8"));
                canvas.drawRect(0, 0, pageWidth, pageHeight, paint);
            }

            paint.setColor(Color.BLACK);
            paint.setTextSize(12);
            paint.setFakeBoldText(false);
            String entry = "Year " + i + ": " + selectedCurrency + " " + String.format("%,.2f", Math.abs(annualAmount * i));
            canvas.drawText(entry, x, y, paint);

            // Neon Aesthetic Bar
            paint.setColor(isProfit ? Color.GREEN : Color.RED);
            paint.setAlpha(50);
            canvas.drawRect(x + 200, y - 12, x + 200 + (i * 15) + 4, y + 2, paint);

            paint.setAlpha(255);
            paint.setColor(isProfit ? Color.parseColor("#00C853") : Color.parseColor("#FF1744"));
            canvas.drawRect(x + 200, y - 10, x + 200 + (i * 15), y, paint);
            y += 35;
        }

        // --- 3. SUMMARY & AI ADVISORY (Check for Space) ---
        if (y > 600) { // New page for footer if current one is full
            doc.finishPage(page);
            page = doc.startPage(new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, doc.getPages().size() + 1).create());
            canvas = page.getCanvas();
            y = 60;
            paint.setColor(Color.parseColor("#F0F4F8"));
            canvas.drawRect(0, 0, pageWidth, pageHeight, paint);
        }

        y += 20;
        double totalAmount = Math.abs(annualAmount * totalYears);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(isProfit ? Color.parseColor("#E8F5E9") : Color.parseColor("#FFEBEE"));
        canvas.drawRect(x - 10, y, 555, y + 60, paint);
        paint.setColor(isProfit ? Color.parseColor("#00C853") : Color.parseColor("#FF1744"));
        canvas.drawRect(x - 10, y, x - 5, y + 60, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(16);
        paint.setFakeBoldText(true);
        y += 35;
        canvas.drawText((isProfit ? "NET PROJECTED PROFIT: " : "NET PROJECTED LIABILITY: ") + selectedCurrency + " " + String.format("%,.2f", totalAmount), x + 15, y, paint);

        y += 60;
        paint.setColor(Color.parseColor("#455A64"));
        paint.setTextSize(14);
        canvas.drawText("AI Strategic Advisory:", x, y, paint);

        y += 10;
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new android.graphics.DashPathEffect(new float[]{10, 10}, 0));
        paint.setColor(Color.GRAY);
        canvas.drawRect(x - 5, y, 555, y + 55, paint);

        paint.setPathEffect(null);
        paint.setStyle(Paint.Style.FILL);
        paint.setFakeBoldText(false);
        paint.setTextSize(11);
        y += 25;
        String suggestion = isProfit ?
                "Success Matrix: Reinvest 20% of profit into better fodder to increase yield by 10% next cycle." :
                "Risk Alert: Optimize labor costs or utilize manure as bio-fertilizer to bridge the fiscal gap.";
        canvas.drawText(suggestion, x + 5, y, paint);
        canvas.drawText("Calculated inclusive of compounding interest rates and operational cash flow volatility.", x + 5, y + 12, paint);


        y = 780;
        paint.setColor(Color.LTGRAY);
        canvas.drawLine(margin, y, 555, y, paint);
        y += 20;
        paint.setTextSize(9);
        paint.setColor(Color.GRAY);
        canvas.drawText("REPORT ID: CB-IX-" + System.currentTimeMillis() / 1000, margin, y, paint);

        paint.setFakeBoldText(true);
        paint.setColor(Color.parseColor("#004D40"));
        canvas.drawText("VERIFIED BY GOUTAM GUPTA", 410, y, paint);

        y += 12;
        paint.setFakeBoldText(false);
        paint.setColor(Color.GRAY);
        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());
        canvas.drawText("TIMESTAMP: " + timestamp, margin, y, paint);
        canvas.drawText("OFFICIAL DOCUMENT - GOUTAM COMPUTER INSTITUTE", margin, y + 15, paint);

        // --- 5. FINALIZE AND EXPORT ---
        doc.finishPage(page);
        File file = new File(getExternalFilesDir(null), fileName + ".pdf");
        try {
            doc.writeTo(new FileOutputStream(file));
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            Intent it = new Intent(Intent.ACTION_SEND);
            it.setType("application/pdf").putExtra(Intent.EXTRA_STREAM, uri).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(it, "Syncing Business Intelligence..."));
        } catch (IOException e) { e.printStackTrace(); }
        doc.close();
    }

    private void processCalculation() {
        int totalCows = Integer.parseInt(cows.getText().toString().trim());
        double costPerCow = Double.parseDouble(cost.getText().toString().trim());
        double dailyMilk = Double.parseDouble(milk.getText().toString().trim());
        double milkPrice = Double.parseDouble(price.getText().toString().trim());
        double monthlyExp = Double.parseDouble(expense.getText().toString().trim());
        int totalYrs = Integer.parseInt(years.getText().toString().trim());

        double loanPrincipal = loan.getText().toString().trim().isEmpty() ? 0 : Double.parseDouble(loan.getText().toString().trim());
        double interestRate = rate.getText().toString().trim().isEmpty() ? 0 : Double.parseDouble(rate.getText().toString().trim());

        double p = BusinessCalculator.calculateProfit(
                totalCows, costPerCow, dailyMilk, milkPrice, monthlyExp, totalYrs, loanPrincipal, interestRate
        );

        double emi = LoanCalculator.calculateEMI(
                loanPrincipal, interestRate, totalYrs * 12
        );

        String aiPrediction = PredictionHelper.predict(totalCows, dailyMilk, monthlyExp, p, selectedCurrency);
        aiText.setText(aiPrediction);

        saveData();
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("profit", p);
        i.putExtra("currency", selectedCurrency);
        i.putExtra("emi", emi);
        i.putExtra("suggestion", BusinessCalculator.getSuggestion(p, selectedCurrency));
        i.putExtra("ai", aiPrediction);
        i.putExtra("years", totalYrs);
        i.putExtra("cows", totalCows);
        i.putExtra("cost", costPerCow);
        i.putExtra("milk", dailyMilk);
        i.putExtra("price", milkPrice);
        i.putExtra("expense", monthlyExp);
        i.putExtra("loan", loanPrincipal);
        i.putExtra("rate", interestRate);
        startActivity(i);
    }

    private void showGraph(int y, double amt, String l, int col) {
        List<BarEntry> ent = new ArrayList<>();
        for (int i = 1; i <= y; i++) ent.add(new BarEntry(i, (float) Math.abs(amt * i)));
        BarDataSet ds = new BarDataSet(ent, l + " (" + selectedCurrency + ")");
        ds.setColor(col);
        ds.setValueTextColor(Color.BLACK);
        ds.setValueTextSize(10f);
        previewChart.setData(new BarData(ds));
        previewChart.getDescription().setEnabled(false);
        previewChart.animateY(1000);
        previewChart.invalidate();
    }

    private void startDeveloperNeonGlow() {
        int[] cols = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};
        final int[] idx = {0};
        developerText.post(new Runnable() {
            @Override public void run() {
                developerText.setTextColor(cols[idx[0]]);
                developerText.animate().alpha(0.5f).setDuration(500).withEndAction(() ->
                        developerText.animate().alpha(1f).setDuration(500).withEndAction(this).start()).start();
                idx[0] = (idx[0] + 1) % cols.length;
            }
        });
    }

    private void clearWithAnimation() {
        EditText[] f = {cows, cost, milk, price, expense, years, loan, rate};
        for (EditText et : f) et.setText("");
        previewChart.clear(); previewChart.invalidate();
        aiText.setText("Awaiting Neural Input...");
    }

    private void saveData() {
        SharedPreferences.Editor e = prefs.edit();
        e.putString("cows", cows.getText().toString()); e.putString("cost", cost.getText().toString());
        e.putString("milk", milk.getText().toString()); e.putString("price", price.getText().toString());
        e.putString("expense", expense.getText().toString()); e.putString("years", years.getText().toString());
        e.putString("loan", loan.getText().toString()); e.putString("rate", rate.getText().toString());
        e.putString("currency", selectedCurrency);
        e.apply();
    }

    private void loadData() {
        cows.setText(prefs.getString("cows", "")); cost.setText(prefs.getString("cost", ""));
        milk.setText(prefs.getString("milk", "")); price.setText(prefs.getString("price", ""));
        expense.setText(prefs.getString("expense", "")); years.setText(prefs.getString("years", "5"));
        loan.setText(prefs.getString("loan", "")); rate.setText(prefs.getString("rate", ""));
        selectedCurrency = prefs.getString("currency", "$");
        for (int i = 0; i < CURRENCY_SYMBOLS.length; i++) {
            if (CURRENCY_SYMBOLS[i].equals(selectedCurrency)) {
                currencySpinner.setSelection(i);
                break;
            }
        }
    }

    private void showColorPickerDialog() {
        String[] names = {"Cyan Protocol", "Emerald Grid", "Amber Glow", "Crimson Core"};
        new AlertDialog.Builder(this).setTitle("Select Aesthetic Matrix").setItems(names, (d, w) -> aiCard.setBackgroundColor(darkColors[w])).show();
    }

    private void resetDefaultColors() { aiCard.setBackgroundColor(Color.parseColor("#E8F0FE")); }
}