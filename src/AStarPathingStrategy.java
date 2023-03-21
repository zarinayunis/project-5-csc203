import java.util.List;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new LinkedList<>();
        List<Point> visited = new LinkedList<>();

        NodePath startNode = new NodePath(start, new LinkedList<>(Arrays.asList(start)), 0);

        List<NodePath> openList = new LinkedList<>();
        openList.add(startNode);

        boolean foundEnd = false;

        while (!openList.isEmpty() && !foundEnd) {
            NodePath current = getLowestFScoreNode(openList);
            openList.remove(current);
            if (withinReach.test(current.getPoint(), end)) {
                path = current.getPath();
                foundEnd = true;
            } else {
                visited.add(current.getPoint());
                for (Point neighbor : potentialNeighbors.apply(current.getPoint())
                        .filter(canPassThrough)
                        .filter(pt -> !visited.contains(pt))
                        .toList()) {
                    int gScore = current.getPath().size();
                    int hScore = (int)Math.round(neighbor.distance(end)); //Added distance function to Point
                    int fScore = gScore + hScore;
                    List<Point> newPath = new LinkedList<>(current.getPath());
                    newPath.add(neighbor);
                    NodePath newNode = new NodePath(neighbor, newPath, fScore);
                    addToOpenList(openList, newNode);
                }
            }
        }
        if (path.size() > 0){
            path.remove(0);
        }

        return path;
    }

    private NodePath getLowestFScoreNode(List<NodePath> nodes) {
        NodePath lowest = nodes.get(0);
        for (NodePath node : nodes) {
            if (node.getfScore() < lowest.getfScore()) {
                lowest = node;
            }
        }
        return lowest;
    }

    private void addToOpenList(List<NodePath> openList, NodePath node) {
        boolean added = false;
        for (int i = 0; i < openList.size(); i++) {
            NodePath openNode = openList.get(i);
            if (openNode.getPoint().equals(node.getPoint())) {
                if (node.getfScore() < openNode.getfScore()) {
                    openList.set(i, node);
                }
                added = true;
                break;
            } else if (node.getfScore() < openNode.getfScore()) {
                openList.add(i, node);
                added = true;
                break;
            }
        }
        if (!added) {
            openList.add(node);
        }
    }
}