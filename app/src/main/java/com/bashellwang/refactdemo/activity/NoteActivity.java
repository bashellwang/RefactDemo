package com.bashellwang.refactdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bashellwang.refactdemo.R;
import com.bashellwang.refactdemo.business.Note;
import com.bashellwang.refactdemo.business.NoteType;
import com.bashellwang.refactdemo.business.NotesAdapter;
import com.bashellwang.refactdemo.storage.dbmodule.DBManager;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private View addNoteButton;

    //    private NoteDao noteDao;
//    private Query<Note> notesQuery;
    private NotesAdapter notesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        setUpViews();

        // get the note DAO
//        DaoSession daoSession = ((App) getApplication()).getDaoSession();
//        DaoSession daoSession = DaoManager.getInstance().getDaoSession();
//        noteDao = daoSession.getNoteDao();

        // query all notes, sorted a-z by their text
//        notesQuery = noteDao.queryBuilder().orderAsc(NoteDao.Properties.Text).build();
        updateNotes();
    }

    private void updateNotes() {
//        List<Note> notes = notesQuery.list();
        List<Note> notes = DBManager.getInstance().getmDataBase().queryAll(Note.class);
        notesAdapter.setNotes(notes);
    }

    protected void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotes);
        //noinspection ConstantConditions
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notesAdapter = new NotesAdapter(noteClickListener);
        recyclerView.setAdapter(notesAdapter);

        addNoteButton = findViewById(R.id.buttonAdd);
        //noinspection ConstantConditions
        addNoteButton.setEnabled(false);

        editText = (EditText) findViewById(R.id.editTextNote);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addNote();
                    return true;
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                addNoteButton.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void onAddButtonClick(View view) {
        addNote();
    }

    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");

        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());

        Note note = new Note();
        note.setText(noteText);
        note.setComment(comment);
        note.setDate(new Date());
        note.setType(NoteType.TEXT);

        DBManager.getInstance().getmDataBase().insert(note);
        Log.d("DaoExample", "Inserted new note, ID: " + note.getId());

        updateNotes();
    }

    NotesAdapter.NoteClickListener noteClickListener = new NotesAdapter.NoteClickListener() {
        @Override
        public void onNoteClick(int position) {
            Note note = notesAdapter.getNote(position);
            Long noteId = note.getId();

//            noteDao.deleteByKey(noteId);
//            DaoManager.getInstance().delete(note);
            DBManager.getInstance().getmDataBase().delete(note);
            Log.d("DaoExample", "Deleted note, ID: " + noteId);

            updateNotes();
        }
    };
}
