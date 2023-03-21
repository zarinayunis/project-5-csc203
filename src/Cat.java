import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cat extends LiveObj{
    public Cat(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public boolean moveTo(WorldModel world, Rat rat, EventScheduler scheduler){
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
        Optional<Entity> fullTarget = world.findNearest(position, targetEntities);

        if (fullTarget.isEmpty()) {
            transform(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), actionPeriod);
        }
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
