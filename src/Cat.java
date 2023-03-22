import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cat extends LiveObj{
    public Cat(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Entity> targetEntities = new ArrayList<>();
        targetEntities.add(new Rat(null, null, null, 0, 0, 0, 0, 0, 0));
        Optional<Entity> catTarget = world.findNearest(position, targetEntities);

        if (catTarget.isPresent()) {
            Point tgtPos = catTarget.get().position;

            if (this.moveTo(world, catTarget.get(), scheduler)) {

                world.removeEntity(scheduler, catTarget.get());
            }
        }
        else{
            world.removeEntity(scheduler, this);
        }

        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), actionPeriod);
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.position)) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);

            }
            return false;
        }
    }
    public Point nextPosition(WorldModel world, Point destPos) {
        AStarPathingStrategy pathingStrategy = new AStarPathingStrategy();
        List<Point> path = pathingStrategy.computePath(position, destPos,
                p -> world.withinBounds(p) && (!world.isOccupied(p) || ((world.isOccupied(p)) && (world.getOccupancyCell(p) instanceof Stump))),
                Point::adjacent,
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.isEmpty()) {
            return position;
        }

        Point nextPos = path.get(0);
        if (world.isOccupied(nextPos) && !(world.getOccupancyCell(nextPos) instanceof Stump)) {
            return position;
        }

        return nextPos;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, this.createAnimationAction(0), this.getAnimationPeriod());
    }


}
