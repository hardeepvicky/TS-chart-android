package in.co.sevenrocks.android.collection;

/**
 * Created by Tech on 09-Oct-15.
 */

public class FileRuleEntity
{
    public boolean autoIncreament;

    public String path, filePrefix;

    public FileRuleEntity(String path, String filePrefix)
    {
        this.path = path;
        this.filePrefix = filePrefix;
        this.autoIncreament = false;
    }

    public FileRuleEntity(String path)
    {
        this.path = path;
        this.filePrefix = null;
        this.autoIncreament = true;
    }
}
