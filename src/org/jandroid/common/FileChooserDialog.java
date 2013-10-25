package org.jandroid.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:jfox.young@gmail.com">Young Yang</a>
 */
public class FileChooserDialog {

/*
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        fileDialog = new FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".txt");
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                Log.d(getClass().getName(), "selected file " + file.toString());
            }
        });
        //fileDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
        //  public void directorySelected(File directory) {
        //      Log.d(getClass().getName(), "selected dir " + directory.toString());
        //  }
        //});
        //fileDialog.setSelectDirectoryOption(false);
        fileDialog.showDialog();
    }
*/
    private static final String PARENT_DIR = "..";
    private final String TAG = getClass().getName();
    private List<String> fileList = new ArrayList<String>();
    private File currentPath;

    public interface OnFileSelectedListener {
        void onFileSelected(File file);
    }

    public interface OnDirectorySelectedListener {
        void onDirectorySelected(File directory);
    }

    private boolean selectDirectoryOption;
    private String[] fileSuffixes;

    private OnFileSelectedListener onFileSelectedListener;
    private OnDirectorySelectedListener onDirectorySelectedListener;

    /**
     * @param path
     */
    public FileChooserDialog(File path, String... fileSuffixes) {
        if (!path.exists()) {
            path = Environment.getExternalStorageDirectory();
        }
        this.currentPath = path;
        setFileSuffixes(fileSuffixes);
    }

    public void setFileSuffixes(String... fileSuffixes) {
        this.fileSuffixes = fileSuffixes != null ? fileSuffixes : null;
    }

    public void setSelectDirectoryOption(boolean selectDirectoryOption, OnDirectorySelectedListener onDirectorySelectedListener) {
        this.selectDirectoryOption = selectDirectoryOption;
        this.onDirectorySelectedListener = onDirectorySelectedListener;
    }

    public void setOnFileSelectedListener(OnFileSelectedListener onFileSelectedListener) {
        this.onFileSelectedListener = onFileSelectedListener;
    }

    /**
     * @return file dialog
     */
    protected Dialog createFileDialog(final Context theContext) {

        loadFileList(currentPath);

        AlertDialog.Builder builder = new AlertDialog.Builder(theContext);

        builder.setTitle(currentPath.getPath());
        if (selectDirectoryOption) {
            builder.setPositiveButton("Select directory", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, currentPath.getPath());
                    if(onDirectorySelectedListener != null) {
                        onDirectorySelectedListener.onDirectorySelected(currentPath);
                    }
                }
            });
        }

        builder.setItems(fileList.toArray(new CharSequence[fileList.size()]), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String fileChosen = fileList.get(which);
                File chosenFile = getChosenFile(fileChosen);
                if (chosenFile.isDirectory()) {
                    Log.d(TAG, "open Directory: " + currentPath.getPath());
                    loadFileList(chosenFile);
                    dialog.cancel();
                    dialog.dismiss();
                    showDialog(theContext);
                }
                else {
                    Log.d(TAG, currentPath.getPath());
                    if(onFileSelectedListener != null) {
                        onFileSelectedListener.onFileSelected(chosenFile);
                    }
                }
            }
        });

        return builder.create();
    }

    /**
     * Show file dialog
     */
    public void showDialog(Context theContext) {
        createFileDialog(theContext).show();
    }

    protected void loadFileList(File path) {
        this.currentPath = path;
        fileList.clear();
        if (path.exists()) {
            if (path.getParentFile() != null) {
                fileList.add(PARENT_DIR);
            }
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    if (!sel.canRead()) {
                        return false;
                    }
                    if(sel.isDirectory()) {
                       return true;
                    }
                    else {
                        if(fileSuffixes == null) {
                            return true;
                        }

                        for(String suffix : fileSuffixes) {
                            if(filename.toLowerCase().endsWith(suffix.toLowerCase())) {
                                return true;
                            }
                        }
                        return false;
                    }
                }
            };
            for(File file: path.listFiles(filter)){
                if(file.isDirectory()) {
                    fileList.add(file.getName() + "/");
                }
                else {
                    fileList.add(file.getName());
                }
            }
        }
    }

    private File getChosenFile(String fileChosen) {
        if (fileChosen.equals(PARENT_DIR)) {
            return currentPath.getParentFile();
        }
        else {
            return new File(currentPath, fileChosen);
        }
    }

}
