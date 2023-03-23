import processing.core.PImage;

import java.util.List;

class House extends Entity{
    public House(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
            super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){}
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){}

}

