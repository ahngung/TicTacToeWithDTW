import java.awt.Point;
import java.util.List;

public class Template {
    private String name;
    private List<Point> points;

    public Template(String name, List<Point> points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setName(String name) {
        this.name = name;
    }
}
