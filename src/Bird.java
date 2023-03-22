import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Bird extends LiveObj{
    private ImageStore imageStore;

    public Bird(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit,
                ImageStore imageStore){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
        this.imageStore = imageStore;
    }
    public void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Entity> targetEntities = new ArrayList<>();
        targetEntities.add(new HouseBurning(null, null, null, 0, 0, 0, 0, 0, 0));
        Optional<Entity> birdTarget = world.findNearest(position, targetEntities);

        if (birdTarget.isPresent()) {
            Point tgtPos = birdTarget.get().position;

            if (this.moveTo(world, birdTarget.get(), scheduler)) {

                Entity house = tgtPos.createHouse("house" + "_" + birdTarget.get().id, imageStore.getImageList("house"));

                world.addEntity(house);
                house.scheduleActions(scheduler, world, imageStore);
            }
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
                world.setBackgroundCell(position, new Background("egg", imageStore.getImageList("egg")));

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
