import processing.core.PImage;

import java.util.List;

class House extends Entity{
    public House(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
            super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){}
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){}
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (health <= 0) {
            Entity stump = position.createBurning(id, imageStore.getImageList("burning"));

            world.removeEntity( scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }
}

