package in.co.sevenrocks.tschart.collection;

import java.io.Serializable;
import java.util.ArrayList;

import in.co.sevenrocks.android.collection.Entity;

/**
 * Created by Hardeep on 16-Sep-17.
 */

public class MenuEntity extends Entity implements Serializable
{
    public static MenuEntity root;

    static {
        root = new MenuEntity("-1", "Root", "", "", false);
    }

    public String parentId, type;
    public boolean isPrivate;
    public ArrayList<MenuEntity> children;
    public ArrayList<ReportEntity> reports;

    public MenuEntity(String id, String name, String parent_id, String type, Boolean isPrivate)
    {
        super(id, name);

        this.parentId = parent_id;
        this.type = type;
        this.isPrivate = isPrivate;

        this.children = new ArrayList<>();
        this.reports = new ArrayList<>();
    }

    public void addChild(MenuEntity entity)
    {
        this.children.add(entity);
    }

    public void addReport(ReportEntity entity)
    {
        this.reports.add(entity);
    }

    public void clear()
    {
        this.children.clear();
        this.reports.clear();
    }
}
