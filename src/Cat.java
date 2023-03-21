import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

public class Cat extends LiveObj{
    public Cat(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public boolean moveTo(WorldModel world, Entity rat, EventScheduler scheduler){
        if (this.position.adjacent(rat.position)) {
            world.removeEntity(scheduler, rat);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, rat.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
    public Point nextPosition(WorldModel world, Point destPos) {
        AStarPathingStrategy pathingStrategy = new AStarPathingStrategy();
        List<Point> path = pathingStrategy.computePath(position, destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                Point::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() >= 1) {
            return path.get(0);
        } else {
            return position;
        }
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
        List<Entity> targetEntities = new ArrayList<>();
        targetEntities.add(new Rat(null, null, null, 0, 0, 0, 0, 0, 0));
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

    }

    public void transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity Rat = position.createRat(id, actionPeriod, animationPeriod, resourceLimit, images);

        world.removeEntity(scheduler, this);

        world.addEntity(Rat);
        Rat.scheduleActions(scheduler, world, imageStore);
    }
}
