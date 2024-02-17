public class City {
    private int x, y, id, circleNumber;
    private boolean visited;

    public City(int id, int x, int y) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getCircleNumber() {
        return circleNumber;
    }

    public void setCircleNumber(int circleNumber) {
        this.circleNumber = circleNumber;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
