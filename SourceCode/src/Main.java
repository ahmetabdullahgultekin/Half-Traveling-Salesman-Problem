import java.io.*;
import java.util.*;

@SuppressWarnings("unchecked")
public class Main {
    static String inputString, outputString;
    static File inputFile;
    static int maxVal, minVal, range;
    static long pathDistance;
    static ArrayList<City> path = new ArrayList<>();
    static ArrayList<City> cities = new ArrayList<>();
    static ArrayList<City> sortedCities = new ArrayList<>();
    static ArrayList<City> currentCities = new ArrayList<>();

    //Calculates the distance between two given points
    private static long calculateDistance(long x1, long y1, long x2, long y2) {
        return Math.round(Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
    }

    //Calculates the difference between the smallest and largest value in the given input
    private static void calculateInputRange() {
        maxVal = Integer.MIN_VALUE;
        minVal = Integer.MAX_VALUE;
        int x, y;
        for (City city : cities) {
            x = city.getX();
            y = city.getY();
            if (maxVal < x)
                maxVal = x;
            if (maxVal < y)
                maxVal = y;
            if (minVal > x)
                minVal = x;
            if (minVal > y)
                minVal = y;
        }
        range = maxVal - minVal;
    }

    // Reads the input file and generates a list of cities
    private static void readInput() throws FileNotFoundException {
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String trimmedInputLine = line.trim();
            String[] tokens = trimmedInputLine.split("\\s+");
            int id = Integer.parseInt(tokens[0]);
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            City city = new City(id, x, y);
            cities.add(city);
        }
        scanner.close();
    }

    // Prints the requested output to the given file
    private static void printResults() throws IOException {
        FileWriter outputFile = new FileWriter(outputString);
        PrintWriter printWriter = new PrintWriter(outputFile);
        printWriter.println(pathDistance);
        System.out.println(pathDistance);
        for (City city : path) {
            printWriter.println(city.getId());
            System.out.println(city.getId());
        }
        printWriter.close();
    }

    //Finds the closest point to the given point in the same region and returns its index
    private static int findNearestPoint(int id, int x1, int y1) {
        long minDistance = Integer.MAX_VALUE;
        long currentDistance;
        int nearestPoint = -1;
        int x2, y2;

        for (City city : currentCities) {
            //If this city is the same city or has been visited, continue to the next iteration
            if ((city.getId() == id) || city.isVisited())
                continue;

            x2 = city.getX();
            y2 = city.getY();
            currentDistance = calculateDistance(x1, y1, x2, y2);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                nearestPoint = city.getId();
            }
        }
        if (nearestPoint != -1)
            cities.get(nearestPoint).setVisited(true);
        return nearestPoint;
    }

    //Calculates the length of the created path
    private static void calculatePathDistance() {
        int x1, x2, y1, y2;
        int pathSize = path.size();
        for (int i = 1; i < pathSize; i++) {
            x1 = path.get(i - 1).getX();
            y1 = path.get(i - 1).getY();
            x2 = path.get(i).getX();
            y2 = path.get(i).getY();
            pathDistance += calculateDistance(x1, y1, x2, y2);
        }
        x1 = path.get(0).getX();
        y1 = path.get(0).getY();
        x2 = path.get(pathSize - 1).getX();
        y2 = path.get(pathSize - 1).getY();
        pathDistance += calculateDistance(x1, y1, x2, y2);
    }

    //creates a path starting from the given point
    private static void createPath(int startingPoint) {
        int nearestPoint,currentCitySize;
        long radius;
        int citySize = cities.size();

        //calculate the distance of all cities from the starting point and set a circleNumber value for all of them
        for (City city : cities) {
            radius = (calculateDistance(city.getX(), city.getY(), cities.get(startingPoint).getX(), cities.get(startingPoint).getY()));
            city.setCircleNumber((int) (radius / (range / 3)));
        }

        //Sort cities arraylist by circle number
        path.add(cities.get(startingPoint));
        currentCities.add(cities.get(startingPoint));
        cities.get(startingPoint).setVisited(true);
        sortedCities = (ArrayList<City>) cities.clone();
        sortedCities.sort(Comparator.comparingInt(City::getCircleNumber));
        int circleNumber = 0;


        //Add the cities in each circle to the currentCities arraylist, respectively. and find the shortest path in that circle
        outerLoop:
        for (int i = 1; i < citySize; i++) {
            City city = sortedCities.get(i);
            if (circleNumber == city.getCircleNumber()) {
                currentCities.add(city);
            } else if (circleNumber < city.getCircleNumber()) {
                currentCitySize = currentCities.size();
                for (int j = 0; j < currentCitySize; j++) {
                    City lastCity = path.get(path.size() - 1);
                    nearestPoint = findNearestPoint(lastCity.getId(), lastCity.getX(), lastCity.getY());
                    if (nearestPoint != -1)
                        path.add(cities.get(nearestPoint));
                    if (path.size() == (int) Math.ceil(citySize / 2.0))
                        break outerLoop;
                }
                currentCities.clear();
                circleNumber++;
                i--;
            }
        }
        // this part is for the outermost circle
        if (path.size() != (int) Math.ceil(citySize / 2.0)) {
            for (City ignored : currentCities) {
                City lastCity = path.get(path.size() - 1);
                nearestPoint = findNearestPoint(lastCity.getId(), lastCity.getX(), lastCity.getY());
                path.add(cities.get(nearestPoint));
                if (path.size() == (int) Math.ceil(citySize / 2.0))
                    break;
            }
        }
    }

    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter the input file");
            inputString = input.next();
            inputFile = new File(inputString);
            System.out.println("Enter the output file");
            outputString = input.next();

            readInput();
            calculateInputRange();

            int citySize = cities.size();
            long minDist = Integer.MAX_VALUE;
            int bestStart = -1;

            // This loop creates a path with all cities as the starting point one by one
            // and finds the starting point that gives the best result
            for (int i = 0; i < citySize; i++) {
                createPath(i);
                calculatePathDistance();
                if (pathDistance < minDist) {
                    minDist = pathDistance;
                    bestStart = i;
                }
                //reset variables to try a new point
                currentCities.clear();
                path.clear();
                sortedCities.clear();
                pathDistance = 0;
                for (City city : cities)
                    city.setVisited(false);
            }

            //create path for best starting point, calculate pathLength and print results
            createPath(bestStart);
            calculatePathDistance();
            printResults();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}