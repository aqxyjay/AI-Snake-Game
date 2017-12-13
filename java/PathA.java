import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class PathA {

    private static int[][] map;
    private static int width;
    private static int height;
    private static Point snakeHead;
    private static Point food;


    public static List<String> findPath() {
        LinkedList<Point> moves = new LinkedList<>();
        LinkedList<Node> openList = new LinkedList<>();
        int[][] closedList = new int[width][height];

        for (int i = 0; i < closedList.length; i++) {
            for (int j = 0; j < closedList[i].length; j++) {
                closedList[i][j] = 0;
            }
        }

        openList.add(new Node(null, snakeHead, new LinkedList<>(), 0, heuristicEstimate(snakeHead, food)));

        while (openList.size() != 0) {
            openList.sort(Comparator.comparingDouble(Node::getfScore));
            Node n = openList.pop();

            // 1 is path
            if (closedList[n.point.x][n.point.y] == 1) {
                continue;
            }

            // 2 is food
            if (map[n.point.x][n.point.y] == 2) {
                // if we reached food, climb up the tree until the root to obtain path
                do {
                    moves.push(n.point);
                    n = n.parent;
                } while (n.parent != null);
                break;
            }
            // Add current node to closedList
            closedList[n.point.x][n.point.y] = 1;

            // Add adjacent nodes to openlist to be processed.
            if (closedList[n.point.x][n.point.y - 1] == 0 && (map[n.point.x][n.point.y - 1] == 0 || map[n.point.x][n.point.y - 1] == 2))
                n.children.push(new Node(n, new Point(n.point.x, n.point.y - 1), new LinkedList<>(), n.gScore + 1, heuristicEstimate(new Point(n.point.x, n.point.y - 1), food)));
            if (closedList[n.point.x + 1][n.point.y] == 0 && (map[n.point.x + 1][n.point.y] == 0 || map[n.point.x + 1][n.point.y] == 2))
                n.children.push(new Node(n, new Point(n.point.x + 1, n.point.y), new LinkedList<>(), n.gScore + 1, heuristicEstimate(new Point(n.point.x + 1, n.point.y), food)));
            if (closedList[n.point.x][n.point.y + 1] == 0 && (map[n.point.x][n.point.y + 1] == 0 || map[n.point.x][n.point.y + 1] == 2))
                n.children.push(new Node(n, new Point(n.point.x, n.point.y + 1), new LinkedList<>(), n.gScore + 1, heuristicEstimate(new Point(n.point.x, n.point.y + 1), food)));
            if (closedList[n.point.x - 1][n.point.y] == 0 && (map[n.point.x - 1][n.point.y] == 0 || map[n.point.x - 1][n.point.y] == 2))
                n.children.push(new Node(n, new Point(n.point.x - 1, n.point.y), new LinkedList<>(), n.gScore + 1, heuristicEstimate(new Point(n.point.x - 1, n.point.y), food)));
            for (int i = 0; i < n.children.size(); i++) {
                int index = in_openlist(openList, n.children.get(i));
                if (index < 0) {
                    //node not in openList, add it.
                    openList.push(n.children.get(i));
                } else {
                    //found a node in openlist that we already found earlier. Check if this is a better route
                    if (n.children.get(i).fScore < openList.get(index).fScore) {
                        //better route, use this one instead.
                        //set the new parent for all the old child nodes
                        for (int j = 0; j < openList.get(index).children.size(); j++) {
                            openList.get(index).children.get(j).parent = n.children.get(i);
                        }
                        //give the children to the new parent
                        n.children.get(i).children = openList.get(index).children;
                        //remove the old node from openList
                        openList.remove(index);
                        //add new node to openList
                        openList.add(n.children.get(i));
                        //Update the scores for all child nodes.
                        update_scores(n.children.get(i));
                    }
                }
            }
        }
    }

    //updates scores of child nodes
    private static void update_scores(Node parent) {
        for (int i = 0; i < parent.children.size(); i++) {
            parent.children.get(i).gScore = parent.gScore + 1;
            parent.children.get(i).hScore = heuristicEstimate(parent.children.get(i).point);
            parent.children.get(i).fScore = parent.children.get(i).gScore + parent.children.get(i).hScore;
            //recursively update any child nodes that this child might have.
            update_scores(parent.children.get(i));
        }
    }

    //check is aNode is in openList. If a match is found, return index, -1 if no match
    private static int in_openlist(LinkedList<Node> openList, Node aNode) {
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).point.x == aNode.point.x && openList.get(i).point.y == aNode.point.y)
                return i;
        }
        return -1;
    }

    //H1+H2
    private static double heuristicEstimate(Point point1, Point point2) {
        return (heuristicEstimate1(point1, point2) + heuristicEstimate2(point1, point2)) / 2;
    }

    //H1
    private static double heuristicEstimate1(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
    }

    //H2
    private static double heuristicEstimate2(Point point1, Point point2) {
        return Math.abs(point1.x - point2.x) + Math.abs(point1.y - point2.y);
    }
}
