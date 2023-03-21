import processing.core.PImage;

import java.util.List;

class Tree extends Entity implements Plant
{

    public Tree(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!transform(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), actionPeriod);
        }
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (health <= 0) {
            Entity stump = position.createStump(STUMP_KEY + "_" + id, imageStore.getImageList(STUMP_KEY));

            world.removeEntity( scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }
    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore){
        return this.transform(world, scheduler, imageStore);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), actionPeriod);
        scheduler.scheduleEvent(this, this.createAnimationAction(0), this.getAnimationPeriod());
    }
}