import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * A simple class representing a location in 2D space.
 */
public final class Point {
    public final int x;
    public final int y;

    private static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000; // have to be in sync since grows and gains health at same time
    private static final int SAPLING_HEALTH_LIMIT = 5;


    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public double distance(Point other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point && ((Point) other).x == this.x && ((Point) other).y == this.y;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }

    public boolean adjacent(Point p2) {
        return (this.x == p2.x && Math.abs(this.y - p2.y) == 1) || (this.y == p2.y && Math.abs(this.x - p2.x) == 1);
    }

    public static int getIntFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max-min);
    }

    public static double getNumFromRange(double max, double min) {
        Random rand = new Random();
        return min + rand.nextDouble() * (max - min);
    }


    public Optional<Entity> nearestEntity(List<Entity> entities) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.position.distanceSquared(this);

            for (Entity other : entities) {
                int otherDistance = other.position.distanceSquared(this);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public int distanceSquared(Point p2) {
        int deltaX = this.x - p2.x;
        int deltaY = this.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }

    public static int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public Entity createHouse(String id, List<PImage> images) {
        return new House( id, this, images, 0, 0, 0, 0, 0, 0);
    }
    public Entity createBurning(String id, List<PImage> images) {
        return new HouseBurning( id, this, images, 0, 0, 0, 0, 0, 0);
    }


    public Entity createObstacle(String id, double animationPeriod, List<PImage> images) {
        return new Obstacle(id, this, images, 0, 0, 0, animationPeriod, 0, 0);
    }

    public Entity createTree(String id, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Tree(id, this, images, 0, 0, actionPeriod, animationPeriod, health, 0);
    }

    public Entity createStump(String id, List<PImage> images) {
        return new Stump( id, this, images, 0, 0, 0, 0, 0, 0);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public Entity createSapling(String id, List<PImage> images, int health) {
        return new Sapling( id, this, images, 0, 0, SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
    }

    public Entity createFairy(String id, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Fairy( id, this, images, 0, 0, actionPeriod, animationPeriod, 0, 0);
    }

    // need resource count, though it always starts at 0
    public Entity createDudeNotFull(String id, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new DudeNotFull( id, this, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
    }

    // don't technically need resource count ... full
    public Entity createDudeFull(String id, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new Dudefull( id, this, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
    }
    public Point shift(int dx, int dy) { //Written for project 4
        return new Point(x + dx, y + dy);
    }

    public Entity createRat(String id, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new Rat(id, this, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
    }

}
