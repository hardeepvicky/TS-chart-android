package in.co.techformation.android.collection;

/**
 * Created by Tech on 11-Sep-15.
 */
public class FileEntity
{
    public String filename, filepath;
    public boolean willSave;

    public FileEntity(String filepath, String filename, boolean willSave)
    {
        this.filename = filename;
        this.filepath = filepath;
        this.willSave = willSave;
    }
}
