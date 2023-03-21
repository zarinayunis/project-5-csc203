import java.util.List;
public class NodePath {
    private final Point point;
    private final List<Point> path;
    private final int fScore;

    public NodePath(Point point, List<Point> path, int fScore) {
        this.point = point;
        this.path = path;
        this.fScore = fScore;
    }

    public Point getPoint() {
        return point;
    }
    public List<Point> getPath(){
        return path;
    }
    public int getfScore(){
        return fScore;
    }
}
