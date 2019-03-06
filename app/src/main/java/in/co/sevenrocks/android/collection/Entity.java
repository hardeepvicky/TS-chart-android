package in.co.sevenrocks.android.collection;

import java.util.LinkedHashMap;

/**
 * Created by Tech on 18-Aug-15.
 */
public class Entity implements Comparable<Entity>
{
    private String id,name;
    public LinkedHashMap data;

    //Constructor
    public Entity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    //Constructor
    public Entity(String id, String name, LinkedHashMap data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    //Constructor
    public Entity(Object id, Object name) {
        this.id = id.toString();
        this.name = name.toString();
    }

    //Constructor
    public Entity(Object id, Object name, Object data) {
        this.id = id.toString();
        this.name = name.toString();
        this.data = (LinkedHashMap) data;
    }

    public int compareTo(Entity other){
        // compareTo should return < 0 if this is supposed to be
        // less than other, > 0 if this is supposed to be greater than
        // other and 0 if they are supposed to be equal
        int result = this.name.compareTo(other.name);
        return result == 0 ? this.name.compareTo(other.name) : result;
    }

    //ID getter
    public String getId() {
        return id;
    }

    //ID setter
    public void setId(String id) {
        this.id = id;
    }

    //Name getter
    public String getName() {
        return name;
    }

    //Name setter
    public void setName(String name) {
        this.name = name;
    }
    //Overrides toString
    public String toString() {
        return name;
    }
}

