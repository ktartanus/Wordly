package com.example.ktartanus.wordly;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ktartanus.wordly.model.GlobalParamDAO;
import com.example.ktartanus.wordly.model.Word;
import com.example.ktartanus.wordly.model.WordDAO;
import com.example.ktartanus.wordly.services.RandomStateGenerator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView mTextWord;
    private TextView mTextTranslation;
    private WordDAO wordDAO;
    private GlobalParamDAO globalParamDAO;
    private ProgressBar progressBar;
    private TextView progressBarTextView;
    private Button translateButton;
    private Button possitiveButton;
    private Button negativeButton;

    private MainActivity self;

    Word actualWord;
    int loadedWordCounter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        int empty = 0, not_known = 0, know = 0, just_knew = 0, just_rememmbered = 0, known_well = 0;
//        for (int i = 0; i<1000; i++) {
//            Word.WORD_STATE randomState = RandomStateGenerator.getRamdomState();
//            if (randomState == Word.WORD_STATE.EMPTY) empty++;
//            if (randomState == Word.WORD_STATE.JUST_KNEW) just_knew++;
//            if (randomState == Word.WORD_STATE.JUST_REMEMBERED) just_rememmbered++;
//            if (randomState == Word.WORD_STATE.KNOWN) know++;
//            if (randomState == Word.WORD_STATE.WELL_KNOWN) known_well++;
//            if (randomState == Word.WORD_STATE.NOT_KNOWN) not_known++;
//        }
//
//           Log.i("RANDOM_STATE", "empty : " + empty + "/1000");
//           Log.i("RANDOM_STATE", "just_knew : " + just_knew + "/1000");
//           Log.i("RANDOM_STATE", "just_rememmbered : " + just_rememmbered + "/1000");
//           Log.i("RANDOM_STATE", "know : " + know + "/1000");
//           Log.i("RANDOM_STATE", "known_well : " + known_well+ "/1000");
//           Log.i("RANDOM_STATE", "not_known : " + not_known + "/1000");



        setContentView(R.layout.activity_main);
        mTextWord = (TextView) findViewById(R.id.textWord);
        mTextTranslation = (TextView) findViewById(R.id.textTranslation);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar01);
        progressBarTextView = (TextView) findViewById(R.id.progressBarText);
        translateButton = (Button) findViewById(R.id.buttonTranslate);
        possitiveButton = (Button) findViewById(R.id.buttonPossitive);
        negativeButton = (Button) findViewById(R.id.buttonNegative);
        self = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        wordDAO = new WordDAO(getApplicationContext());
        globalParamDAO = new GlobalParamDAO(getApplicationContext());
        globalParamDAO.open();
        wordDAO.open();
        loadedWordCounter = getLoadedWordCount();
        if(isFirstEntry()){

            new loadWordsFromFile().execute();
            addFirstEnryParam();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setNewRandomWord();
        translateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mTextTranslation.setText(actualWord.getTranslation());
                mTextTranslation.setVisibility(View.VISIBLE);
                translateButton.setVisibility(View.INVISIBLE);
                possitiveButton.setVisibility(View.VISIBLE);
                negativeButton.setVisibility(View.VISIBLE);
            }
        });
        possitiveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                self.wordChangedClick(true);
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                self.wordChangedClick(false);
            }
        });
    }

    private int getLoadedWordCount() {
        String laodedWordsCount = globalParamDAO.getSinlgeEntry("laodedWordsCount");
        if ( laodedWordsCount == null || laodedWordsCount.equals("")|| laodedWordsCount.equals("NOT EXIST")) {
            globalParamDAO.insertEntry("laodedWordsCount", "0");
            return 0;
        }
        else
            return Integer.valueOf(laodedWordsCount);
    }

    private void wordChangedClick(boolean isPossitive){
        possitiveButton.setVisibility(View.INVISIBLE);
        negativeButton.setVisibility(View.INVISIBLE);
        mTextTranslation.setVisibility(View.INVISIBLE);
        translateButton.setVisibility(View.VISIBLE);
        if (isPossitive){
            upgradeWordState();
        } else {
            downgradeWordState();
        }
        setNewRandomWord();
    }

    private void downgradeWordState() {
        Word.WORD_STATE oldstate = actualWord.getState();
        if((oldstate == Word.WORD_STATE.EMPTY)){
            updateWordWithNewState(Word.WORD_STATE.NOT_KNOWN);
        }
        else if (!(oldstate == Word.WORD_STATE.NOT_KNOWN)){
            int newOrdinal =  oldstate.ordinal() -1 ;
            Word.WORD_STATE newState = Word.WORD_STATE.values()[newOrdinal];
            Log.i("NEW_STATE", "old state was : " + oldstate.name()+ "but new state is : " + newState.name());
            updateWordWithNewState(newState);
        }
    }

    private void upgradeWordState() {
        Word.WORD_STATE oldstate = actualWord.getState();
        if((oldstate == Word.WORD_STATE.EMPTY)){
            updateWordWithNewState(Word.WORD_STATE.KNOWN);
        }
        else if(!(oldstate == Word.WORD_STATE.WELL_KNOWN)){
            int newOrdinal =  oldstate.ordinal() +1 ;
            Word.WORD_STATE newState = Word.WORD_STATE.values()[newOrdinal];
            Log.i("NEW_STATE", "old state was : " + oldstate.name()+ "but new state is : " + newState.name());
            updateWordWithNewState(newState);
        }
    }

    private void updateWordWithNewState(Word.WORD_STATE newState) {
        actualWord.setState(newState);
        android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        String formattedDate = dateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString();
        actualWord.setState_last_modyfication_date(formattedDate);
        wordDAO.updateEntry(actualWord);
    }

    private void setNewRandomWord() {
        Word newWord = wordDAO.selectRandomWordByState(getRandomState());
        if (newWord !=null){
            actualWord = newWord;
        } else {
            setNewRandomWord();
        }
        mTextWord.setText(actualWord.getContent());
    }

    private Word.WORD_STATE getRandomState() {
        return RandomStateGenerator.getRamdomState();
    }

    private void addWordToDatabase(String word, String wordTranslation) {
        android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
        String formattedDate = dateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString();
        wordDAO.insertWord(new Word(word, Word.WORD_STATE.EMPTY, formattedDate, wordTranslation));
    }

    private boolean validRawFileLine(String line) {

        if( line ==null || line.equals("") )
            return false;
        char firstLetter = line.charAt(0);
        if( firstLetter == '#')
            return false;
        return true;
    }

    private void addFirstEnryParam() {
        globalParamDAO.insertEntry("isFirstEntry", "true");
        }

    private boolean isFirstEntry() {
        String param = globalParamDAO.getSinlgeEntry("isFirstEntry");
        return !(param==null) && !param.equals("");

    }

    private class loadWordsFromFile extends AsyncTask<Object, Integer, Long> {

        protected void onProgressUpdate(Integer... progress) {
            progressBarTextView.setText("Ładuje dane : załadowano " + progress[0] + "/100000");
        }

        @Override
        protected Long doInBackground(Object... objects) {
            try {
                Resources res = getResources();
                InputStream in_s = res.openRawResource(R.raw.dictionary);
                InputStream in_sPL = res.openRawResource(R.raw.dictionary_pl);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in_s));
                BufferedReader readerPL = new BufferedReader(new InputStreamReader(in_sPL));
                for(int i = 0; i< loadedWordCounter; i++){
                    reader.readLine();
                    readerPL.readLine();
                }

                String line = reader.readLine();
                String linePL = readerPL.readLine();
                int counterAdded = 0;
                while (line != null && linePL != null) {
                    if(validRawFileLine(line) && validRawFileLine(linePL) && !line.toLowerCase().equals(linePL.toLowerCase())){
                        addWordToDatabase(line, linePL);
                        counterAdded++;
                    }
                    loadedWordCounter++;
                    globalParamDAO.updateEntry("laodedWordsCount", String.valueOf(loadedWordCounter));
                    line = reader.readLine();
                    linePL = readerPL.readLine();
                    if (loadedWordCounter % 100 ==0)  {
                        publishProgress(loadedWordCounter);
                        Log.i("Progress","Pushed " + loadedWordCounter + "/100000");
                    }
                }
                progressBar.setVisibility(View.GONE);
                Log.i("InitWords","Pushed " + counterAdded + " from all " + loadedWordCounter + "words.");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("InitWords","Error: can't load init words.");
            } finally {
                wordDAO.close();
            }            return 1l;        }

        protected void onPostExecute(Long result) {
            progressBar.setVisibility(View.GONE);
        }
    }



}
