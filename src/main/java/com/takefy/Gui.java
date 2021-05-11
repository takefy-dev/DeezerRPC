package com.takefy;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class  Gui implements ActionListener {

    private static  JButton button;
    private static JLabel success;
    private static JLabel title;
    public static void init(){
        JPanel panel = new JPanel();

        JFrame frame = new JFrame();
        frame.setTitle("DeezerRPC");
        frame.setSize(350,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);



        panel.setLayout(null);

        title = new JLabel("Welcome to deezerRPC");
        title.setBounds(90,10,300,25);
        panel.add(title);

        button = new JButton("Login to deezer");
        button.setBounds(75,90,200,25);
        button.addActionListener(new Gui());
        panel.add(button);

        success = new JLabel("");
        success.setBounds(75,120,200,25);
        panel.add(success);
        frame.setVisible(true);

        String path = System.getProperty("java.io.tmpdir") + "\\deezerRPC.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String accessToken = br.readLine();


            System.out.println(accessToken);
            if(accessToken != ""){
                JSONObject me = new JSONObject(DeezerApi.me(accessToken));
                success.setText("You're now logged in as " + me.get("name") + " !");
                title.setText("Welcome back " + me.get("name") + " to deezerRPC");
                DiscordPresence.startPresence(accessToken);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ArrayList<String> isLogging =  Login.login();
        if(isLogging.size() > 0){
            JSONObject me = new JSONObject(DeezerApi.me(isLogging.get(0)));
            success.setText("You're now logged in as " + me.get("name") + " !");
            title.setText("Welcome " + me.get("name") + " to deezerRPC");
            DiscordPresence.startPresence(isLogging.get(0));
        }else{
            success.setText("Authentication failed please retry !");
        }
    }
}
