package com.ysr.fiyatgor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysr.barcodes.Barcode;
import com.ysr.db.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;

public class MainActivity extends Activity {

    private ImageView logo;
    private EditText editText;
    private ImageView okutunImage;
    private ImageView barkod;
    private ImageView indirimImage;
    private ImageView cizgi;
    private EditText subeEditText;
    private EditText ipEditText;
    private EditText dbEditText;
    private EditText klnEditText;
    private EditText sifEditText;
    private TextView subeAdiView;
    private TextView subeView;
    private TextView ipView;
    private TextView dbView;
    private TextView klnView;
    private TextView sifView;

    private Button button;
    private String sube = "";
    private String ip = "";
    private String ins = "";
    private String db = "";
    private String kln = "";
    private String sifre = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION   |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        subeAdiView = findViewById(R.id.subeAdiView);
        subeEditText = findViewById(R.id.subeAdi);
        ipEditText = findViewById(R.id.ip);
        dbEditText = findViewById(R.id.dbAdi);
        klnEditText = findViewById(R.id.klnAdi);
        sifEditText = findViewById(R.id.sifre);

        button = findViewById(R.id.kaydet);
        editText = findViewById(R.id.editText);
        okutunImage = findViewById(R.id.okutun);
        editText.setVisibility(View.INVISIBLE);
        okutunImage.setVisibility(View.INVISIBLE);

        indirimImage = findViewById(R.id.indirimImg);
        indirimImage.setVisibility(View.INVISIBLE);
        cizgi = findViewById(R.id.indirimcizgi);
        cizgi.setVisibility(View.INVISIBLE);
        barkod = findViewById(R.id.barkod);
        barkod.setVisibility(View.INVISIBLE);

        SharedPreferences preferences = getSharedPreferences("prefs.xml", MODE_PRIVATE);
        sube = preferences.getString("sube", "Maltepe");
        ip = preferences.getString("ip", "192.168.2.200:1453");
        db = preferences.getString("db", "SMARTPOS_V44_2019");
        kln = preferences.getString("kln", "sa");
        sifre = preferences.getString("sif", "sa123");
        subeEditText.setText(sube);
        ipEditText.setText(ip);
        dbEditText.setText(db);
        klnEditText.setText(kln);
        sifEditText.setText(sifre);

        Log.i(ip + db + kln + sifre, "<<<>>>");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAyar();
            }
        });

        editText.requestFocus();
        editText.addTextChangedListener(
                new TextWatcher() {
                    private final long DELAY = 200; // milliseconds
                    private Timer timer = new Timer();

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //Log.i("onTextChanged", "onTextChanged");
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //Log.i("beforeTextChanged", "beforeTextChanged");
                    }

                    @Override
                    public void afterTextChanged(final Editable s) {
                        Log.i("afterTextChanged", "afterTextChanged");
                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        // TODO: do what you need here (refresh list)
                                        // you will probably need to use runOnUiThread(Runnable action) for some specific actions

                                        Log.i(":edittext::>", ":okunan:>" + editText.getText().toString());

                                        if (!editText.getText().toString().equals("")) {

                                            ConnectionDB conDB = null;
                                            Connection con = null;
                                            PreparedStatement pstmt = null;
                                            //Statement stmt = null;
                                            ResultSet rs = null;
                                            try {
                                                conDB = new ConnectionDB();
                                                SharedPreferences preferences = getSharedPreferences("prefs.xml", MODE_PRIVATE);
                                                ip = preferences.getString("ip", "192.168.2.200:1453");
                                                db = preferences.getString("db", "SMARTPOS_V44_2019");
                                                kln = preferences.getString("kln", "sa");
                                                sifre = preferences.getString("sif", "sa123");
                                                ConnectionDB.ip = ip;
                                                ConnectionDB.db = db;
                                                ConnectionDB.un = kln;
                                                ConnectionDB.password = sifre;
                                                con = conDB.getConnection();
                                                String sql = "";
                                                if (db.startsWith("SMART"))
                                                    //sql = "SELECT a.STOK_ID, a.BARKODU, a.FIYAT1, b.ADI, '12.3 TL' as INDIRIM, '2.34 TL' as KARTFIYAT FROM dbo.BARKOD a, dbo.STOK b where a.STOK_ID = b.ID and a.BARKODU = '" + editText.getText() + "'";
                                                    //sql = "SELECT a.STOK_ID, a.BARKODU, a.FIYAT1, b.ADI, '12.3'  as INDIRIM, null as KARTFIYAT FROM dbo.BARKOD a, dbo.STOK b where a.STOK_ID = b.ID and a.BARKODU = ?";
                                                    //sql = Util.getProperty("SmartposSQL", getApplicationContext());
                                                    sql = "SELECT a.STOK_ID as STOK, a.BARKODU as BARKOD, a.FIYAT1 as FIYAT, b.ADI as ADI, '12.3'  as INDIRIM, null as KARTFIYAT FROM dbo.BARKOD a, dbo.STOK b where a.STOK_ID = b.ID and a.BARKODU = ? ";
                                                    //sql = "SELECT a.STOK_ID, a.BARKODU, a.FIYAT1, b.ADI, '' as INDIRIM, '' as KARTFIYAT FROM dbo.BARKOD a, dbo.STOK b where a.STOK_ID = b.ID and a.BARKODU = '" + editText.getText() + "'";
                                                else if (db.startsWith("Genius"))
                                                    //sql = Util.getProperty("Genius3SQL", getApplicationContext());
                                                    sql = "SELECT top 1 GENIUS3.STOCK_CARD.DESCRIPTION as ADI, GENIUS3.DEPARTMENT_VAT.DESCRIPTION as Expr1, GENIUS3.STOCK_BARCODE.BARCODE as BARKOD,\n" +
                                                "(select top 1 GENIUS3.STOCK_PRICE.UNIT_PRICE from  GENIUS3.STOCK_PRICE  where GENIUS3.STOCK_CARD.ID = GENIUS3.STOCK_PRICE.FK_STOCK_CARD and num=1) as FIYAT,\n" +
                                                "(select GENIUS3.STOCK_PRICE.UNIT_PRICE from  GENIUS3.STOCK_PRICE  where GENIUS3.STOCK_CARD.ID = GENIUS3.STOCK_PRICE.FK_STOCK_CARD and num in(2,3)) as INDIRIM,\n" +
                                                "(select GENIUS3.STOCK_PRICE.UNIT_PRICE from  GENIUS3.STOCK_PRICE  where GENIUS3.STOCK_CARD.ID = GENIUS3.STOCK_PRICE.FK_STOCK_CARD and num=4) as KARTFIYAT\n" +
                                                "  FROM GENIUS3.STOCK_CARD INNER JOIN GENIUS3.STOCK_BARCODE ON GENIUS3.STOCK_CARD.ID = GENIUS3.STOCK_BARCODE.FK_STOCK_CARD\n" +
                                                "  INNER JOIN GENIUS3.DEPARTMENT_VAT ON GENIUS3.STOCK_CARD.FK_DEPARTMENT_VAT = GENIUS3.DEPARTMENT_VAT.ID\n" +
                                                "  where  GENIUS3.STOCK_BARCODE.BARCODE = ? ";
                                                else
                                                    sql = "SELECT TOP 1 C.BARKOD_NO as BARKOD, B.STOK_ADI as ADI , B.STOK_KISA_ADI as KISAADI, A.FIYAT1 as FIYAT, '' as INDIRIM, '' as KARTFIYAT FROM BY001.STOK_KISIM A, BY001.STOK B, BY001.STOK_BARKOD C WHERE A.STOK_ID = B.STOK_ID AND A.STOK_ID = C.STOK_ID AND C.BARKOD_NO = ?";

                                                Log.i("sql::", "::>>" + sql);
                                                //stmt = con.createStatement();
                                                pstmt = con.prepareStatement(sql);
                                                pstmt.setString(1, editText.getText().toString());
                                                Log.i("sql::" + sql, pstmt.toString());
                                                rs = pstmt.executeQuery();

                                                final TextView subeAdiView = findViewById(R.id.subeAdiView);
                                                final TextView barkodu = findViewById(R.id.barkodu);
                                                final ImageView barkod = findViewById(R.id.barkod);
                                                final TextView fiyati = findViewById(R.id.fiyati);
                                                final TextView adi = findViewById(R.id.adi);
                                                final TextView indirim = findViewById(R.id.indirim);
                                                final Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Omnes.ttf");
                                                //Log.i("rsss", rs.toString());

                                                boolean rsDolu = false;

                                                while (rs.next()) {
                                                    rsDolu = true;
                                                    Log.i("rs dolu", "");
                                                    final String barkod1;
                                                    final String fiyat1;
                                                    final String ad1;
                                                    final String indirimStr;
                                                    final String kartFiyatStr;
                                                    //if (db.startsWith("SMART")) {
                                                        if (rs.getString("BARKOD") == null)
                                                            barkod1 = "null";
                                                        else barkod1 = rs.getString("BARKOD");
                                                        if (rs.getString("FIYAT") == null)
                                                            fiyat1 = "null";
                                                        else
                                                            fiyat1 = rs.getFloat("FIYAT") + "";
                                                        if (rs.getString("ADI") == null)
                                                            ad1 = "null";
                                                        else
                                                            ad1 = rs.getString("ADI");
                                                        if (rs.getString("INDIRIM") == null)
                                                            indirimStr = "";
                                                        else
                                                            indirimStr = rs.getString("INDIRIM");
                                                        if (rs.getString("KARTFIYAT") == null)
                                                            kartFiyatStr = "";
                                                        else
                                                            kartFiyatStr = rs.getString("KARTFIYAT");
                                                    //} else {
                                                    //    if (rs.getString("BARCODE") == null)
                                                    //        barkod1 = rs.getString("BARCODE");
                                                    //    else
                                                    //        barkod1 = rs.getString("BARCODE");
                                                    //    if (rs.getString("fiyat") == null)
                                                    //        fiyat1 = "null";
                                                    //    else
                                                    //        fiyat1 = rs.getFloat("fiyat") + "";
                                                    //    if (rs.getString("DESCRIPTION") == null)
                                                    //        ad1 = "null";
                                                    //    else
                                                    //        ad1 = rs.getString("DESCRIPTION");
                                                    //    if (rs.getString("indi") == null)
                                                    //        indirimStr = "";
                                                    //    else
                                                    //        indirimStr = rs.getString("indi");
                                                    //    if (rs.getString("kartfiyat") == null)
                                                    //        kartFiyatStr = "";
                                                    //    else
                                                    //        kartFiyatStr = rs.getString("kartfiyat");
                                                    //}
                                                    //Log.i("rs:::", barkod1 + fiyat1 + ad1);
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            barkodu.setText(barkod1);
                                                            fiyati.setText(fiyat1);
                                                            adi.setText(ad1);
                                                            adi.setTypeface(type);
                                                            fiyati.setTypeface(type);
                                                            barkodu.setTypeface(type);
                                                            indirim.setTypeface(type);
                                                            subeAdiView.setTypeface(type);
                                                            int nokta = fiyat1.indexOf(".");
                                                            String sonFiyat = "";
                                                            String noktaSonrasi = "";
                                                            //Log.i("::::>>>>>>>>>" + fiyat1, nokta + "");
                                                            if (nokta > 0) {
                                                                noktaSonrasi = fiyat1.substring(nokta + 1);
                                                                if (noktaSonrasi.length() == 1) {
                                                                    noktaSonrasi = noktaSonrasi + "0";
                                                                } else if (noktaSonrasi.length() > 2) {
                                                                    noktaSonrasi = noktaSonrasi.substring(0, 2);
                                                                }
                                                                sonFiyat = fiyat1.substring(0, nokta) + "," + noktaSonrasi;
                                                                fiyati.setText(sonFiyat + " TL");
                                                                //Log.i(sonFiyat, "nokta 1 den buyuk sonfiyat ::>>>>" + fiyati.getText());
                                                            } else {
                                                                //Log.i("","nokta else");
                                                                fiyati.setText(fiyat1 + " TL");
                                                            }

                                                            //Bitmap bitmap = null;
                                                            //try {
                                                            //    bitmap = createBarcode(barkod1);
                                                            //} catch (WriterException e) {
                                                            //    e.printStackTrace();
                                                            //}

                                                            if (indirimStr != null && !indirimStr.equals("")) {
                                                                int inokta = indirimStr.indexOf(".");
                                                                String isonFiyat = "";
                                                                String inoktaSonrasi = "";
                                                                if (inokta > 0) {
                                                                    inoktaSonrasi = indirimStr.substring(inokta + 1);
                                                                    if (inoktaSonrasi.length() == 1) {
                                                                        inoktaSonrasi = inoktaSonrasi + "0";
                                                                    } else if (inoktaSonrasi.length() > 2) {
                                                                        inoktaSonrasi = inoktaSonrasi.substring(0, 2);
                                                                    }
                                                                    isonFiyat = indirimStr.substring(0, inokta) + "," + inoktaSonrasi;
                                                                    indirim.setText(fiyati.getText());
                                                                    fiyati.setText(isonFiyat + " TL");
                                                                    //Log.i(isonFiyat, "inokta 1 den buyuk indi:>>>>" + indirimStr.substring(0, inokta));
                                                                } else {
                                                                    //Log.i("","inokta else");
                                                                    indirim.setText(fiyati.getText());
                                                                    fiyati.setText(indirimStr + " TL");
                                                                }
                                                            } else if (kartFiyatStr != null && !kartFiyatStr.equals("")) {
                                                                int knokta = kartFiyatStr.indexOf(".");
                                                                String ksonFiyat = "";
                                                                String knoktaSonrasi = "";
                                                                if (knokta > 0) {
                                                                    knoktaSonrasi = kartFiyatStr.substring(knokta + 1);
                                                                    if (knoktaSonrasi.length() == 1) {
                                                                        knoktaSonrasi = knoktaSonrasi + "0";
                                                                    } else if (knoktaSonrasi.length() > 2) {
                                                                        knoktaSonrasi = knoktaSonrasi.substring(0, 2);
                                                                    }
                                                                    ksonFiyat = kartFiyatStr.substring(0, knokta) + "," + knoktaSonrasi;
                                                                    indirim.setText(fiyati.getText());
                                                                    fiyati.setText(ksonFiyat + " TL");
                                                                    //Log.i(ksonFiyat, "knokta 1 den buyuk kart:::>>>" + kartFiyatStr.substring(0, knokta));
                                                                } else {
                                                                    //Log.i("","knokta else");
                                                                    indirim.setText(fiyati.getText());
                                                                    fiyati.setText(kartFiyatStr + " TL");
                                                                }
                                                            } else indirim.setText("");

                                                            if (indirim.getText().equals("")) {
                                                                indirimImage.setVisibility(View.INVISIBLE);
                                                                cizgi.setVisibility(View.INVISIBLE);
                                                            } else {
                                                                // text üstü çizili basar
                                                                //indirim.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                                                indirimImage.setVisibility(View.VISIBLE);
                                                                cizgi.setVisibility(View.VISIBLE);
                                                            }
                                                            Bitmap bitmap = null;
                                                            Barcode barcode = new Barcode();
                                                            if (barkod1.length() == 13) {
                                                                //EAN13 code = new EAN13(barkod1);
                                                                //bitmap = code.getBitmap(860, 300);
                                                                bitmap = barcode.Ean13_Encode(barkod1, 40);
                                                            } else if (barkod1.length() == 8) {
                                                                bitmap = barcode.Ean8_Encode(barkod1, 30);
                                                            }
                                                            barkod.setImageBitmap(bitmap);
                                                            barkod.setVisibility(View.VISIBLE);
                                                        }
                                                    });
                                                }
                                                if (!rsDolu) {
                                                    final String barkodHata = editText.getText().toString();
                                                    final String ad = "Ürün bulunamadı.Lütfen görevliye bilgi veriniz.";
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            //barkodu.setText(barkodHata);
                                                            Bitmap bitmap = null;
                                                            Barcode barcode = new Barcode();
                                                            if (barkodHata.length() == 13) {
                                                                //EAN13 code = new EAN13(barkod1);
                                                                //bitmap = code.getBitmap(860, 300);
                                                                bitmap = barcode.Ean13_Encode(barkodHata, 35);
                                                            } else if (barkodHata.length() == 8) {
                                                                bitmap = barcode.Ean8_Encode(barkodHata, 35);
                                                            }
                                                            barkod.setImageBitmap(bitmap);
                                                            barkod.setVisibility(View.VISIBLE);
                                                            adi.setText(ad);
                                                            indirim.setText("");
                                                            fiyati.setText("");
                                                            barkodu.setText(barkodHata);
                                                            indirimImage.setVisibility(View.INVISIBLE);
                                                            cizgi.setVisibility(View.INVISIBLE);
                                                            //barkod.setVisibility(View.INVISIBLE);
                                                        }
                                                    });
                                                }
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        editText.setText("");
                                                        editText.requestFocus();
                                                    }
                                                });

                                            } catch (SQLException e) {
                                                Log.e("hata***:::" + e.getMessage(), e.toString());
                                                final TextView adi1 = findViewById(R.id.adi);
                                                final TextView fiyati1 = findViewById(R.id.fiyati);
                                                final TextView indirim = findViewById(R.id.indirim);
                                                final TextView barkodu = findViewById(R.id.barkodu);
                                                final String hata = e.getMessage();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        adi1.setText(hata);
                                                        barkodu.setText(editText.getText());
                                                        indirimImage.setVisibility(View.INVISIBLE);
                                                        cizgi.setVisibility(View.INVISIBLE);
                                                        barkod.setVisibility(View.INVISIBLE);
                                                        fiyati1.setText("");
                                                        indirim.setText("");
                                                        editText.setText("");
                                                        editText.requestFocus();
                                                    }
                                                });
                                            } catch (ClassNotFoundException e) {
                                                Log.e("hata***:::" + e.getMessage(), e.toString());
                                                final TextView adi1 = findViewById(R.id.adi);
                                                final TextView fiyati1 = findViewById(R.id.fiyati);
                                                final TextView indirim = findViewById(R.id.indirim);
                                                final TextView barkodu = findViewById(R.id.barkodu);
                                                final String hata = e.getMessage();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        adi1.setText(hata);
                                                        barkodu.setText(editText.getText());
                                                        indirimImage.setVisibility(View.INVISIBLE);
                                                        cizgi.setVisibility(View.INVISIBLE);
                                                        barkod.setVisibility(View.INVISIBLE);
                                                        fiyati1.setText("");
                                                        indirim.setText("");
                                                        editText.setText("");
                                                        editText.requestFocus();
                                                    }
                                                });
                                            //} catch (IOException e) {
                                            //    Log.e(e.getMessage(), e.toString());
                                            } finally {
                                                if (rs != null) {
                                                    try {
                                                        rs.close();
                                                    } catch (SQLException e) {
                                                        Log.e("hata***" + e.getMessage(), e.toString());
                                                    }
                                                }
                                                if (pstmt != null) {
                                                    try {
                                                        pstmt.close();
                                                    } catch (SQLException e) {
                                                        Log.e("hata***" + e.getMessage(), e.toString());
                                                    }
                                                }
                                                if (con != null) {
                                                    try {
                                                        con.close();
                                                    } catch (SQLException e) {
                                                        Log.e("hata***" + e.getMessage(), e.toString());
                                                    }
                                                }

                                            }
                                        }
                                    }
                                },
                                DELAY
                        );
                    }
                }
        );
    }

    public void setAyar() {

        SharedPreferences.Editor editor = getSharedPreferences("prefs.xml", MODE_PRIVATE).edit();

        editor.putString("sube", subeEditText.getText().toString());
        editor.putString("ip", ipEditText.getText().toString());
        editor.putString("db", dbEditText.getText().toString());
        editor.putString("kln", klnEditText.getText().toString());
        editor.putString("sif", sifEditText.getText().toString());
        editor.commit();

        subeView = findViewById(R.id.subeView);
        ipView = findViewById(R.id.ipView);
        dbView = findViewById(R.id.dbAdiView);
        klnView = findViewById(R.id.klnView);
        sifView = findViewById(R.id.sifreView);
        subeView.setVisibility(View.INVISIBLE);
        ipView.setVisibility(View.INVISIBLE);
        dbView.setVisibility(View.INVISIBLE);
        klnView.setVisibility(View.INVISIBLE);
        sifView.setVisibility(View.INVISIBLE);
        subeEditText.setVisibility(View.INVISIBLE);
        ipEditText.setVisibility(View.INVISIBLE);
        dbEditText.setVisibility(View.INVISIBLE);
        klnEditText.setVisibility(View.INVISIBLE);
        sifEditText.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);

        editText.setVisibility(View.VISIBLE);
        okutunImage.setVisibility(View.VISIBLE);

        subeAdiView.setText("      " + subeEditText.getText().toString());

        logo = findViewById(R.id.logo);
        if (dbEditText.getText().toString().startsWith("Genius"))
            logo.setImageDrawable(getResources().getDrawable(R.drawable.logocarrefour));
        else if (dbEditText.getText().toString().startsWith("BYBS")) {
            logo.setImageDrawable(getResources().getDrawable(R.drawable.logosistem));
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void disableView(View v) {

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                disableView(child);
            }
        }
    }

    /*public Bitmap createBarcode(String data) throws WriterException {
        int size1 = 1400;
        int size2 = 400;
        MultiFormatWriter barcodeWriter = new MultiFormatWriter();
        BitMatrix barcodeBitMatrix = null;
        switch (data.length())  {
            case 8 : barcodeBitMatrix = barcodeWriter.encode(data, BarcodeFormat.EAN_8, size1, size2);
            case 13 : barcodeBitMatrix = barcodeWriter.encode(data, BarcodeFormat.EAN_13, size1, size2);
        }

        Bitmap barcodeBitmap = Bitmap.createBitmap(size1, size2, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < size1; x++) {
            for (int y = 0; y < size2; y++) {
                barcodeBitmap.setPixel(x, y, barcodeBitMatrix.get(x, y) ?
                        Color.BLACK : Color.TRANSPARENT);
            }
        }
        return barcodeBitmap;
    }*/
}
