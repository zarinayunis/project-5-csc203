import processing.core.PImage;

import java.util.List;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity {

    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;
    public final int resourceLimit;
    public int resourceCount;
    public final double actionPeriod;
    public final double animationPeriod;
    public int health;
    public final int healthLimit;

    public Entity(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit){
        this.id =id;
        this.position =position;
        this.images =images;
        this.imageIndex =0;
        this.resourceLimit =resourceLimit;
        this.resourceCount =resourceCount;
        this.actionPeriod =actionPeriod;
        this.animationPeriod =animationPeriod;
        this.health =health;
        this.healthLimit =healthLimit;
    }

    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    public String log() {
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
    }

    public double getAnimationPeriod() {
        return animationPeriod;
    }

    public void nextImage() {
        imageIndex = imageIndex + 1;
    }

    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, null, null, repeatCount);
    }

    public Action createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore, 0);
    }

}

