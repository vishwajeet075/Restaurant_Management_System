
package restaurant;


public class Meals {
 private int id;
 private float cost;
 private String type;
 private float name;

    public Meals(int id, float cost, String type, float name) {
        this.id = id;
        this.cost = cost;
        this.type = type;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Float.parseFloat(name);
    }
 
 
}
