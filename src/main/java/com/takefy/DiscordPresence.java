package com.takefy;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import org.json.JSONArray;
import org.json.JSONObject;

public class DiscordPresence {
    public static void startPresence(String token) {
        DiscordRPC discord = DiscordRPC.INSTANCE;
        String appId = "765160745209167882";
        String steamId = "";

        DiscordEventHandlers handlers = new DiscordEventHandlers();
        discord.Discord_Initialize(appId, handlers, true, steamId);
        DiscordRichPresence presence = new DiscordRichPresence();

        try {
            while (true) {

                refreshSong(discord, presence, token);

                Thread.sleep(5000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void refreshSong(DiscordRPC discord, DiscordRichPresence presence, String accessToken) {
        JSONObject history = new JSONObject(DeezerApi.history(accessToken));
        JSONArray lastMusics = history.getJSONArray("data");
        JSONObject lastSong = lastMusics.getJSONObject(0);
        JSONObject artist = lastSong.getJSONObject("artist");
        Integer timestamp = (Integer) lastSong.get("timestamp");
        Integer duration = (Integer) lastSong.get("duration");
        String title = (String) lastSong.get("title");
        if (presence.details == null || !presence.details.contains(title)) {
            String artistName = (String) artist.get("name");
            presence.largeImageKey = "big";
            presence.largeImageText = title + " - " + artistName;
            presence.smallImageKey = "small";
            presence.startTimestamp = System.currentTimeMillis() / 1000;
            presence.smallImageText = duration.toString() + "s";
            presence.details = title;
            presence.state = artistName;
            discord.Discord_UpdatePresence(presence);
        }
        ;

    }
}
