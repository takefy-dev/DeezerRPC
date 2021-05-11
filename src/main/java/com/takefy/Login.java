package com.takefy;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;


public class Login {
    public static ArrayList login() {
        WebDriver driver = new ChromeDriver();
//
        ArrayList secretCode = new ArrayList<String>();
        String OAUTH_URL = "https://connect.deezer.com/oauth/auth.php?app_id=439422&redirect_uri=https://github.com/takefy-dev/&perms=basic_access,listening_history,offline_access";

        driver.get(OAUTH_URL);
        while (!driver.getCurrentUrl().contains("code") && !driver.getCurrentUrl().contains("error")) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                driver.close();
            }
        }
        String finalUrl = driver.getCurrentUrl();
        String code = null;
        if (finalUrl.contains("code")) {
            code = finalUrl.split("code=")[1];
            String secretCodeURL  = "https://connect.deezer.com/oauth/access_token.php?app_id=439422&secret=773bc5301adeb56ad190654b32a15148&code=" + code;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("accept", "application/json")
                    .uri(URI.create(secretCodeURL))
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String accessToken = response.body().split("access_token=")[1].split("&expires=0")[0];
                try{
                    String path = System.getProperty("java.io.tmpdir") + "\\deezerRPC.txt";
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path, false));
                    bw.write(accessToken);
                    bw.close();
                }catch(Exception e){
                }
                secretCode.add(accessToken);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return secretCode;
    }


}
