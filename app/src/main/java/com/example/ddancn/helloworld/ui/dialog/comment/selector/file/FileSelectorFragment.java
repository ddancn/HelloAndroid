package com.example.ddancn.helloworld.ui.dialog.comment.selector.file;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ddancn.helloworld.MyApplication;
import com.example.ddancn.helloworld.R;
import com.example.ddancn.helloworld.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;

public class FileSelectorFragment extends Fragment {

    public static String ARG_TAB = "fileType";
    public static String[][] TYPES = {{"doc", "docx"}, {"xls", "xlsx", "csv"}, {"ppt", "pptx"}, {"pdf"}, {"txt", "rar", "zip", "mp3", "m4a", "wav"}};
    public static int[] ICON_IDS = {R.drawable.ic_doc, R.drawable.ic_xls, R.drawable.ic_ppt, R.drawable.ic_pdf, R.drawable.ic_unknown};

    private ArrayList<FileInfo> fileList;
    private FileRvAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private ContentResolver contentResolver = MyApplication.getContext().getContentResolver();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_selector, container, false);

        recyclerView = view.findViewById(R.id.rv_file);
        progressBar = view.findViewById(R.id.progress_bar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        fileList = new ArrayList<>();
        adapter = new FileRvAdapter(getActivity(), fileList);
        recyclerView.setAdapter(adapter);

        new LoadFileTask().execute(getArguments().getInt(ARG_TAB));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static FileSelectorFragment newInstance(int tab) {
        FileSelectorFragment fragment = new FileSelectorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TAB, tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    class LoadFileTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... tab) {
            String[] types = TYPES[tab[0]];
            String[] columns = new String[]{
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.MIME_TYPE,
                    MediaStore.Files.FileColumns.SIZE,
                    MediaStore.Files.FileColumns.DATE_MODIFIED,
                    MediaStore.Files.FileColumns.DATA};
            String name = MediaStore.Files.FileColumns.DATA;

            StringBuilder selection = new StringBuilder("(");
            for (int i = 0; i < types.length; i++) {
                selection.append(name).append(" LIKE '%.").append(types[i]).append("'");
                if (i < types.length - 1) selection.append(" or ");
            }
            selection.append(")");

            Cursor cursor = contentResolver.query(
                    MediaStore.Files.getContentUri("external"),
                    columns,
                    selection.toString(),
                    null,
                    null);
            int dataColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);

            while (cursor.moveToNext()) {
                String path = cursor.getString(dataColumnIndex);
                FileInfo document = FileUtil.getFileInfoFromFile(new File(path));
                document.setImageId(ICON_IDS[tab[0]]);
                fileList.add(document);
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
}
