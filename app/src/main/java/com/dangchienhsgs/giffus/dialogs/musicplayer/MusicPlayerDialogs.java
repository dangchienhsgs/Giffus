package com.dangchienhsgs.giffus.dialogs.musicplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dangchienhsgs.giffus.R;
import com.dangchienhsgs.giffus.map.Song;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MusicPlayerDialogs extends DialogFragment implements ImageButton.OnClickListener {
    private final String TAG = "Music Player Dialogs";


    private TextView txtSongLyric;
    private EditText txtSongUrl;
    private ImageButton buttonPlay;
    private ImageButton buttonPrevious;
    private ImageButton buttonNext;
    private ProgressBar progressBar;
    private Spinner spinner;

    private OnReturnedListener mListener;

    private ArrayAdapter<String> mAdapter;

    private List<Song> listSongs;
    private boolean isPlaying[];
    private boolean isDownloaded[];
    private String listSongPath[];


    private MediaPlayer mPlayer;

    private int currentPosition = 0;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_play_music, null);

        txtSongUrl = (EditText) view.findViewById(R.id.song_url);
        txtSongLyric = (TextView) view.findViewById(R.id.song_lyrics);
        spinner = (Spinner) view.findViewById(R.id.music_player_spinner_songs);
        buttonPlay = (ImageButton) view.findViewById(R.id.button_play);
        buttonPrevious = (ImageButton) view.findViewById(R.id.button_previous);
        buttonNext = (ImageButton) view.findViewById(R.id.button_next);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        buttonPlay.setOnClickListener(this);


        progressBar.setVisibility(View.INVISIBLE);

        isPlaying = new boolean[listSongs.size()];
        listSongPath = new String[listSongs.size()];

        Arrays.fill(isPlaying, false);


        // Create the Adapter for Spinner
        List<String> listTitle = new ArrayList<String>();
        for (Song song : listSongs) {
            listTitle.add(song.getTitle());
        }
        mAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.row_spinner_songs,
                R.id.text_title,
                listTitle
        );
        spinner.setAdapter(mAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // Set Song URL and Song Lyric
                currentPosition = spinner.getSelectedItemPosition();
                txtSongUrl.setText(listSongs.get(currentPosition).getUrl());
                if (listSongs.get(currentPosition).getLyrics() != null) {
                    txtSongLyric.setText(listSongs.get(currentPosition).getLyrics());
                }

                if (!isDownloaded[currentPosition]) {
                    // If the song have not been downloaded
                    buttonPlay.setImageResource(R.drawable.download);
                } else {
                    if (isPlaying[currentPosition]) {
                        buttonPlay.setImageResource(R.drawable.ic_action_pause);
                    } else {
                        buttonPlay.setImageResource(R.drawable.ic_action_play);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Set listener for Button next
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = currentPosition + 1;
                if (position == listSongs.size()) {
                    position = 0;
                }
                spinner.setSelection(position);
            }
        });


        // Set Listener for Button previous
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = currentPosition - 1;
                if (currentPosition < 0) {
                    currentPosition = listSongs.size();
                }
                spinner.setSelection(position);
            }
        });


        // Create Positive Button
        builder.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getDialog().cancel();
                mPlayer.stop();
                Arrays.fill(isPlaying, false);
                mListener.onReturned(listSongPath, isDownloaded, listSongPath);
            }
        });

        builder.setView(view);
        builder.setTitle("Play Music");
        return builder.create();
    }

    public void setListSongs(List<Song> listSongs) {
        this.listSongs = listSongs;
    }

    public void setIsDownloaded(boolean[] isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public void setListSongPath(String[] listSongPath) {
        this.listSongPath = listSongPath;
    }

    @Override
    public void onClick(View view) {
        if (isDownloaded[currentPosition]) {

            if (isPlaying[currentPosition]) {
                mPlayer.stop();
                isPlaying[currentPosition] = false;
                buttonPlay.setImageResource(R.drawable.ic_action_play);
            } else {
                playMusic(getActivity(), listSongPath[currentPosition]);
                buttonPlay.setImageResource(R.drawable.ic_action_pause);
                isPlaying[currentPosition] = true;
            }


        } else {

            // If the current song has not been downloaded
            String url = listSongs.get(currentPosition).getUrl();
            new DownloadFileTask(getActivity(), progressBar, String.valueOf("gift" + currentPosition + ".mp3"), currentPosition)
                    .execute(url);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnReturnedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity have not implements the override method of listener");
        }


    }

    public boolean playMusic(Context context, String filePath) {
        Log.d(TAG, filePath);
        Uri uri = Uri.parse(filePath);
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }
        mPlayer.reset();
        try {
            mPlayer.setDataSource(context, uri);
            mPlayer.prepare();
            mPlayer.start();
            return true;
        } catch (IOException e) {
            Log.d(TAG, "Link of data is fail");
            return false;
        }

    }

    public interface OnReturnedListener {
        public void onReturned(String listSingPath[], boolean isDownloaded[], String filePath[]);
    }

    /**
     * Background Async Task to download file
     */
    public class DownloadFileTask extends AsyncTask<String, String, String> {
        int position;
        private String TAG = "Download File Task";
        private ProgressBar progressBar;
        private String filename;
        private Context context;

        public DownloadFileTask(Context context, ProgressBar progressBar, String filename, int position) {
            this.context = context;
            this.progressBar = progressBar;
            this.filename = filename;
            this.position = position;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }


        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // Get Music file length
                int lenghtOfFile = conection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 10 * 1024);
                // Output stream to write file in SD card
                String path = context.getFilesDir() + "/" + filename;
                OutputStream output = new FileOutputStream(path);
                Log.d(TAG, path);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress which triggers onProgressUpdate method
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // Write data to file
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();
                // Close streams
                output.close();
                input.close();
                return path;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return null;
            }
        }


        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            progressBar.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * *
         */
        @Override
        protected void onPostExecute(String filePath) {

            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Download xong !", Toast.LENGTH_SHORT).show();


            if (filePath == null) {
                Log.d(TAG, "Download Fail");
            } else {
                isDownloaded[position] = true;
                listSongPath[position] = filePath;

                if (position == currentPosition) {
                    buttonPlay.setImageResource(R.drawable.ic_action_play);
                }
            }
        }

    }


}
