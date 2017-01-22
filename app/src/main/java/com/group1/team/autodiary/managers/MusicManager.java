package com.group1.team.autodiary.managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.group1.team.autodiary.objects.Music;
import com.group1.team.autodiary.objects.Weather;

import java.util.HashMap;
import java.util.List;

/**
 * Created by q on 2017-01-22.
 */

public class MusicManager {

    public interface Callback {
        void callback(Music music);
    }

    public interface Callback2 {
        void callback(List<Music> musics);
    }

    Context mContext;

    public MusicManager(Context context) { mContext = context; }

    public void getPlayingMusicInfo(Callback callback) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.music.metachanged");
        intentFilter.addAction("com.android.music.playstatechanged");
        intentFilter.addAction("com.android.music.playbackcomplete");
        intentFilter.addAction("com.android.music.queuechanged");

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                //String cmd = intent.getStringExtra("command");
                String artist = intent.getStringExtra("artist");
                String album = intent.getStringExtra("album");
                String track = intent.getStringExtra("track");

                Music playingMusicInfo = new Music(action, artist, album, track);
                callback.callback(playingMusicInfo);
            }
        };

        mContext.registerReceiver(mReceiver, intentFilter);
    }

    public String[] getMostFrequentlyPlayedMusic(List<Music> musics) {
        if (musics.size() == 0) return null;

        HashMap<String, Integer> hashMap = new HashMap<>();

        Music mostFrequentlyPlayedMusic = musics.get(0);
        int playedNum = 1;

        for (int i = 0; i < musics.size(); i++) {
            Music temp = musics.get(i);
            String key = temp.getArtist() + temp.getTrack();
            if (hashMap.containsKey(key))
                hashMap.put(key, hashMap.get(key) + 1);
            else
                hashMap.put(key, 1);
            if (playedNum < hashMap.get(key)) {
                mostFrequentlyPlayedMusic = temp;
                playedNum = hashMap.get(key);
            }
        }

        return new String[] {
                mostFrequentlyPlayedMusic.getArtist(),
                mostFrequentlyPlayedMusic.getTrack(),
                playedNum + ""
        };
    }
}