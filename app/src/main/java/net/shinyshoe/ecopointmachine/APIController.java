package net.shinyshoe.ecopointmachine;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIController {
    public static String BACKEND_URL = "https://ecopoint-backend-production.up.railway.app";

    public static String postSampah(String kategoriSampah, String beratSampah, int poin) {
        String urlString = BACKEND_URL + "/api/sampah";
        String response = "";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format(
                    "{\"kategori_sampah\":\"%s\", \"berat_sampah\":\"%s\", \"poin\":%d}",
                    kategoriSampah, beratSampah, poin
            );

            try (OutputStream os = new BufferedOutputStream(conn.getOutputStream())) {
                os.write(jsonInputString.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            responseCode >= 200 && responseCode < 300
                                    ? conn.getInputStream()
                                    : conn.getErrorStream()
                    )
            );

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            response = sb.toString();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            response = "{\"error\":\"" + e.getMessage() + "\"}";
        }

        return response;
    }
    public static String getMesin() {
        String urlString = BACKEND_URL + "/api/mesin";
        String response = "";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            responseCode >= 200 && responseCode < 300
                                    ? conn.getInputStream()
                                    : conn.getErrorStream()
                    )
            );

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            response = sb.toString();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            response = "{\"error\":\"" + e.getMessage() + "\"}";
        }

        return response;
    }
    public static void ensureRegistered(String machineName) {
        String machineID = getMesinIdByName(machineName);
        if(machineID.isEmpty()) {
            Log.d("API", "NEW MACHINE");

            String response = postMesin(machineName);
            Log.d("API",  response);
        }
    }
    public static String getMesinIdByName(String machineName) {
        try {
            String jsonString = getMesin();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray dataArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject mesin = dataArray.getJSONObject(i);
                String namaMesin = mesin.getString("nama_mesin");

                if (namaMesin.equals(machineName)) {
                    return mesin.getString("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getKodeVerifikasi(String jsonString) {
        try {
            org.json.JSONObject json = new org.json.JSONObject(jsonString);
            return json.getJSONObject("data").getString("kode_verifikasi");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getSampahId(String jsonString) {
        try {
            org.json.JSONObject json = new org.json.JSONObject(jsonString);
            return json.getJSONObject("data").getString("id");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String postMesin(String namaMesin) {
        String urlString = BACKEND_URL + "/api/mesin";
        String response = "";

        // For testing only — don't use on main thread in production!
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = String.format("{\"nama_mesin\": \"%s\"}", namaMesin);

            try (OutputStream os = new BufferedOutputStream(conn.getOutputStream())) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            responseCode >= 200 && responseCode < 300
                                    ? conn.getInputStream()
                                    : conn.getErrorStream()
                    )
            );

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            response = sb.toString();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            response = "{\"error\":\"" + e.getMessage() + "\"}";
        }

        return response;
    }
    public static String postPermintaan(String mesinId, String[] daftarSampah) {
        String urlString = BACKEND_URL + "/api/permintaan";
        String response = "";

        // Allow network call on main thread for testing (not recommended for production)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Build JSON string manually
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{");
            jsonBuilder.append("\"mesin_id\": \"").append(mesinId).append("\",");
            jsonBuilder.append("\"daftar_sampah\": [");

            for (int i = 0; i < daftarSampah.length; i++) {
                jsonBuilder.append("\"").append(daftarSampah[i]).append("\"");
                if (i < daftarSampah.length - 1) {
                    jsonBuilder.append(", ");
                }
            }

            jsonBuilder.append("]}");

            String jsonInput = jsonBuilder.toString();

            try (OutputStream os = new BufferedOutputStream(conn.getOutputStream())) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            responseCode >= 200 && responseCode < 300
                                    ? conn.getInputStream()
                                    : conn.getErrorStream()
                    )
            );

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            response = sb.toString();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            response = "{\"error\":\"" + e.getMessage() + "\"}";
        }

        return response;
    }
}
