package com.ysr.barcodes;

//import anywheresoftware.b4a.BA;
//import anywheresoftware.b4a.BA.*;
//import anywheresoftware.b4a.BA.ActivityObject;
//import anywheresoftware.b4a.BA.DontInheritEvents;
//import anywheresoftware.b4a.BA.Events;
//import anywheresoftware.b4a.BA.Hide;
//import anywheresoftware.b4a.BA.Permissions;
//import anywheresoftware.b4a.BA.ShortName;
//import anywheresoftware.b4a.BA.Version;
//import anywheresoftware.b4a.BA.DependsOn;
//import anywheresoftware.b4a.objects.ViewWrapper;
//import anywheresoftware.b4a.AbsObjectWrapper;

import android.provider.ContactsContract;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class Barcode {

    public void initialize() {
        // WHAT ???
    };

    public Bitmap QR_Encode(String qrData,int qrCodeDimention) {

        Bitmap bitmap= Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimention);
        try {
            bitmap = qrCodeEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        };
        return bitmap;
    };

    public Bitmap Ean13_Encode(String qrData,int qrCodeDimention) {

        Bitmap bitmap= Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null,
                Contents.Type.TEXT, BarcodeFormat.EAN_13.toString(), qrCodeDimention);
        try {
            bitmap = qrCodeEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        };
        return bitmap;
    };

    public Bitmap Ean8_Encode(String qrData,int qrCodeDimention) {

        Bitmap bitmap= Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null,
                Contents.Type.TEXT, BarcodeFormat.EAN_8.toString(), qrCodeDimention);
        try {
            bitmap = qrCodeEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        };
        return bitmap;
    };



    private static class Contents {
        private Contents() {
        }

        private class Type {

            // Plain text. Use Intent.putExtra(DATA, string). This can be used for URLs too, but string
            // must include "http://" or "https://".
            public static final String TEXT = "TEXT_TYPE";

            // An email type. Use Intent.putExtra(DATA, string) where string is the email address.
            public static final String EMAIL = "EMAIL_TYPE";

            // Use Intent.putExtra(DATA, string) where string is the phone number to call.
            public static final String PHONE = "PHONE_TYPE";

            // An SMS type. Use Intent.putExtra(DATA, string) where string is the number to SMS.
            public static final String SMS = "SMS_TYPE";

            public static final String CONTACT = "CONTACT_TYPE";

            public static final String LOCATION = "LOCATION_TYPE";

            private Type() {
            }
        }

        public static final String URL_KEY = "URL_KEY";

        public static final String NOTE_KEY = "NOTE_KEY";

        // When using Type.CONTACT, these arrays provide the keys for adding or retrieving multiple phone numbers and addresses.
        public static final String[] PHONE_KEYS = {
                ContactsContract.Intents.Insert.PHONE, ContactsContract.Intents.Insert.SECONDARY_PHONE,
                ContactsContract.Intents.Insert.TERTIARY_PHONE
        };

        public static final String[] PHONE_TYPE_KEYS = {
                ContactsContract.Intents.Insert.PHONE_TYPE,
                ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE,
                ContactsContract.Intents.Insert.TERTIARY_PHONE_TYPE
        };

        public static final String[] EMAIL_KEYS = {
                ContactsContract.Intents.Insert.EMAIL, ContactsContract.Intents.Insert.SECONDARY_EMAIL,
                ContactsContract.Intents.Insert.TERTIARY_EMAIL
        };

        public static final String[] EMAIL_TYPE_KEYS = {
                ContactsContract.Intents.Insert.EMAIL_TYPE,
                ContactsContract.Intents.Insert.SECONDARY_EMAIL_TYPE,
                ContactsContract.Intents.Insert.TERTIARY_EMAIL_TYPE
        };
    }
    // -------------------------------------------- Fim
    public final class QRCodeEncoder {
        private static final int WHITE = 0xFFFFFFFF;
        private static final int BLACK = 0xFF000000;

        private int dimension = Integer.MIN_VALUE;
        private String contents = null;
        private String displayContents = null;
        private String title = null;
        private BarcodeFormat format = null;
        private boolean encoded = false;

        public QRCodeEncoder(String data, Bundle bundle, String type, String format, int dimension) {
            this.dimension = dimension;
            encoded = encodeContents(data, bundle, type, format);
        }

        public String getContents() {
            return contents;
        }

        public String getDisplayContents() {
            return displayContents;
        }

        public String getTitle() {
            return title;
        }

        private boolean encodeContents(String data, Bundle bundle, String type, String formatString) {
            // Default to QR_CODE if no format given.
            format = null;
            if (formatString != null) {
                try {
                    format = BarcodeFormat.valueOf(formatString);
                } catch (IllegalArgumentException iae) {
                    // Ignore it then
                }
            }
            if (format == null || format == BarcodeFormat.QR_CODE) {
                this.format = BarcodeFormat.QR_CODE;
                encodeQRCodeContents(data, bundle, type);
            } else if (data != null && data.length() > 0) {
                contents = data;
                displayContents = data;
                title = "Text";
            }
            return contents != null && contents.length() > 0;
        }

        private void encodeQRCodeContents(String data, Bundle bundle, String type) {
            if (type.equals(Contents.Type.TEXT)) {
                if (data != null && data.length() > 0) {
                    contents = data;
                    displayContents = data;
                    title = "Text";
                }
            } else if (type.equals(Contents.Type.EMAIL)) {
                data = trim(data);
                if (data != null) {
                    contents = "mailto:" + data;
                    displayContents = data;
                    title = "E-Mail";
                }
            } else if (type.equals(Contents.Type.PHONE)) {
                data = trim(data);
                if (data != null) {
                    contents = "tel:" + data;
                    displayContents = PhoneNumberUtils.formatNumber(data);
                    title = "Phone";
                }
            } else if (type.equals(Contents.Type.SMS)) {
                data = trim(data);
                if (data != null) {
                    contents = "sms:" + data;
                    displayContents = PhoneNumberUtils.formatNumber(data);
                    title = "SMS";
                }
            } else if (type.equals(Contents.Type.CONTACT)) {
                if (bundle != null) {
                    StringBuilder newContents = new StringBuilder(100);
                    StringBuilder newDisplayContents = new StringBuilder(100);

                    newContents.append("MECARD:");

                    String name = trim(bundle.getString(ContactsContract.Intents.Insert.NAME));
                    if (name != null) {
                        newContents.append("N:").append(escapeMECARD(name)).append(';');
                        newDisplayContents.append(name);
                    }

                    String address = trim(bundle.getString(ContactsContract.Intents.Insert.POSTAL));
                    if (address != null) {
                        newContents.append("ADR:").append(escapeMECARD(address)).append(';');
                        newDisplayContents.append('\n').append(address);
                    }

                    Collection<String> uniquePhones = new HashSet<String>(Contents.PHONE_KEYS.length);
                    for (int x = 0; x < Contents.PHONE_KEYS.length; x++) {
                        String phone = trim(bundle.getString(Contents.PHONE_KEYS[x]));
                        if (phone != null) {
                            uniquePhones.add(phone);
                        }
                    }
                    for (String phone : uniquePhones) {
                        newContents.append("TEL:").append(escapeMECARD(phone)).append(';');
                        newDisplayContents.append('\n').append(PhoneNumberUtils.formatNumber(phone));
                    }

                    Collection<String> uniqueEmails = new HashSet<String>(Contents.EMAIL_KEYS.length);
                    for (int x = 0; x < Contents.EMAIL_KEYS.length; x++) {
                        String email = trim(bundle.getString(Contents.EMAIL_KEYS[x]));
                        if (email != null) {
                            uniqueEmails.add(email);
                        }
                    }
                    for (String email : uniqueEmails) {
                        newContents.append("EMAIL:").append(escapeMECARD(email)).append(';');
                        newDisplayContents.append('\n').append(email);
                    }

                    String url = trim(bundle.getString(Contents.URL_KEY));
                    if (url != null) {
                        // escapeMECARD(url) -> wrong escape e.g. http\://zxing.google.com
                        newContents.append("URL:").append(url).append(';');
                        newDisplayContents.append('\n').append(url);
                    }

                    String note = trim(bundle.getString(Contents.NOTE_KEY));
                    if (note != null) {
                        newContents.append("NOTE:").append(escapeMECARD(note)).append(';');
                        newDisplayContents.append('\n').append(note);
                    }

                    // Make sure we've encoded at least one field.
                    if (newDisplayContents.length() > 0) {
                        newContents.append(';');
                        contents = newContents.toString();
                        displayContents = newDisplayContents.toString();
                        title = "Contact";
                    } else {
                        contents = null;
                        displayContents = null;
                    }

                }
            } else if (type.equals(Contents.Type.LOCATION)) {
                if (bundle != null) {
                    // These must use Bundle.getFloat(), not getDouble(), it's part of the API.
                    float latitude = bundle.getFloat("LAT", Float.MAX_VALUE);
                    float longitude = bundle.getFloat("LONG", Float.MAX_VALUE);
                    if (latitude != Float.MAX_VALUE && longitude != Float.MAX_VALUE) {
                        contents = "geo:" + latitude + ',' + longitude;
                        displayContents = latitude + "," + longitude;
                        title = "Location";
                    }
                }
            }
        }

        public Bitmap encodeAsBitmap() throws WriterException {
            if (!encoded) return null;

            Map<EncodeHintType, Object> hints = null;
            String encoding = guessAppropriateEncoding(contents);
            if (encoding != null) {
                hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
                hints.put(EncodeHintType.CHARACTER_SET, encoding);
            }
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix result = writer.encode(contents, format, dimension, dimension, hints);
            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        }

        private String guessAppropriateEncoding(CharSequence contents) {
            // Very crude at the moment
            for (int i = 0; i < contents.length(); i++) {
                if (contents.charAt(i) > 0xFF) { return "UTF-8"; }
            }
            return null;
        }

        private  String trim(String s) {
            if (s == null) { return null; }
            String result = s.trim();
            return result.length() == 0 ? null : result;
        }

        private String escapeMECARD(String input) {
            if (input == null || (input.indexOf(':') < 0 && input.indexOf(';') < 0)) { return input; }
            int length = input.length();
            StringBuilder result = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                char c = input.charAt(i);
                if (c == ':' || c == ';') {
                    result.append('\\');
                }
                result.append(c);
            }
            return result.toString();
        }
    }
}