public interface Plant {
    String TREE_KEY = "tree";
    double TREE_ACTION_MAX = 1.400;
    double TREE_ACTION_MIN = 1.000;
    double TREE_ANIMATION_MAX = 0.600;
    double TREE_ANIMATION_MIN = 0.050;
    int TREE_HEALTH_MAX = 3;
    int TREE_HEALTH_MIN = 1;
    String SAPLING_KEY = "sapling";
    String STUMP_KEY = "stump";

    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}
