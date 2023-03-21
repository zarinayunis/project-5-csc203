import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    public int numRows;
    public int numCols;
    public Background[][] background;
    public Entity[][] occupancy;
    public Set<Entity> entities;
    private static final int SAPLING_HEALTH = 0;

    private static final int SAPLING_NUM_PROPERTIES = 1;

    private static final String SAPLING_KEY = "sapling";
    private static final String DUDE_KEY = "dude";
    private static final int DUDE_ACTION_PERIOD = 0;
    private static final int DUDE_ANIMATION_PERIOD = 1;

    private static final int DUDE_NUM_PROPERTIES = 3;
    private static final String FAIRY_KEY = "fairy";
    private static final int FAIRY_ANIMATION_PERIOD = 0;
    private static final int FAIRY_ACTION_PERIOD = 1;
    private static final int FAIRY_NUM_PROPERTIES = 2;

    private static final String RAT_KEY = "rat";
    private static final int RAT_ANIMATION_PERIOD = 0;
    private static final int RAT_ACTION_PERIOD = 1;
    private static final int RAT_NUM_PROPERTIES = 2;

    private static final int DUDE_LIMIT = 2;
    private static final String OBSTACLE_KEY = "obstacle";
    private static final int OBSTACLE_ANIMATION_PERIOD = 0;
    private static final int OBSTACLE_NUM_PROPERTIES = 1;


    private static final int RAT_LIMIT = 1;
    private static final String TREE_KEY = "tree";
    private static final int TREE_ANIMATION_PERIOD = 0;
    private static final int TREE_ACTION_PERIOD = 1;
    private static final int TREE_HEALTH = 2;
    private static final int TREE_NUM_PROPERTIES = 3;

    private static final String HOUSE_KEY = "house";
    private static final int HOUSE_NUM_PROPERTIES = 0;

    private static final String STUMP_KEY = "stump";
    private static final int STUMP_NUM_PROPERTIES = 0;

    private static final int PROPERTY_KEY = 0;


    private static final int PROPERTY_ID = 1;
    private static final int PROPERTY_COL = 2;
    private static final int PROPERTY_ROW = 3;
    private static final int ENTITY_NUM_PROPERTIES = 4;

    public WorldModel() {

    }
    public List<String> log() {
        List<String> list = new ArrayList<>();
        for (Entity entity : entities) {
            String log = entity.log();
            if (log != null) list.add(log);
        }
        return list;
    }

        private void parseSapling(String[] properties, Point pt, String id, ImageStore imageStore) {
            if (properties.length == SAPLING_NUM_PROPERTIES) {
                int health = Integer.parseInt(properties[SAPLING_HEALTH]);
                Entity entity = pt.createSapling(id, imageStore.getImageList( SAPLING_KEY), health);
                this.tryAddEntity(entity);
            } else {
                throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", SAPLING_KEY, SAPLING_NUM_PROPERTIES));
            }
        }

        private void parseDude(String[] properties, Point pt, String id, ImageStore imageStore) {
            if (properties.length == DUDE_NUM_PROPERTIES) {
                Entity entity = pt.createDudeNotFull(id, Double.parseDouble(properties[DUDE_ACTION_PERIOD]), Double.parseDouble(properties[DUDE_ANIMATION_PERIOD]), Integer.parseInt(properties[DUDE_LIMIT]), imageStore.getImageList( DUDE_KEY));
                this.tryAddEntity(entity);
            } else {
                throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", DUDE_KEY, DUDE_NUM_PROPERTIES));
            }
        }

        private void parseFairy(String[] properties, Point pt, String id, ImageStore imageStore) {
            if (properties.length == FAIRY_NUM_PROPERTIES) {
                Entity entity = pt.createFairy(id, Double.parseDouble(properties[FAIRY_ACTION_PERIOD]), Double.parseDouble(properties[FAIRY_ANIMATION_PERIOD]), imageStore.getImageList(FAIRY_KEY));
                this.tryAddEntity(entity);
            } else {
                throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", FAIRY_KEY, FAIRY_NUM_PROPERTIES));
            }
        }

        private void parseTree(String[] properties, Point pt, String id, ImageStore imageStore) {
            if (properties.length == TREE_NUM_PROPERTIES) {
                Entity entity = pt.createTree(id, Double.parseDouble(properties[TREE_ACTION_PERIOD]), Double.parseDouble(properties[TREE_ANIMATION_PERIOD]), Integer.parseInt(properties[TREE_HEALTH]), imageStore.getImageList(TREE_KEY));
                this.tryAddEntity(entity);
            } else {
                throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", TREE_KEY, TREE_NUM_PROPERTIES));
            }
        }

        private void parseObstacle(String[] properties, Point pt, String id, ImageStore imageStore) {
            if (properties.length == OBSTACLE_NUM_PROPERTIES) {
                Entity entity = pt.createObstacle(id, Double.parseDouble(properties[OBSTACLE_ANIMATION_PERIOD]), imageStore.getImageList(OBSTACLE_KEY));
                this.tryAddEntity(entity);
            } else {
                throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", OBSTACLE_KEY, OBSTACLE_NUM_PROPERTIES));
            }
        }

        private void parseHouse(String[] properties, Point pt, String id, ImageStore imageStore) {
            if (properties.length == HOUSE_NUM_PROPERTIES) {
                Entity entity = pt.createHouse(id, imageStore.getImageList(HOUSE_KEY));
                this.tryAddEntity(entity);
            } else {
                throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", HOUSE_KEY, HOUSE_NUM_PROPERTIES));
            }
        }

        private void parseStump(String[] properties, Point pt, String id, ImageStore imageStore) {
            if (properties.length == STUMP_NUM_PROPERTIES) {
                Entity entity = pt.createStump(id, imageStore.getImageList(STUMP_KEY));
                this.tryAddEntity(entity);
            } else {
                throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", STUMP_KEY, STUMP_NUM_PROPERTIES));
            }
        }

        private void tryAddEntity(Entity entity) {
            if (this.isOccupied(entity.position)) {
                // arguably the wrong type of exception, but we are not
                // defining our own exceptions yet
                throw new IllegalArgumentException("position occupied");
            }

            this.addEntity(entity);
        }

        public boolean withinBounds(Point pos) {
            return pos.y >= 0 && pos.y < this.numRows && pos.x >= 0 && pos.x < this.numCols;
        }

        public boolean isOccupied(Point pos) {
            return this.withinBounds(pos) && this.getOccupancyCell(pos) != null;
        }

        public Optional<Entity> findNearest(Point pos, List<Entity> kinds) {
            List<Entity> ofType = new LinkedList<>();
            for (Entity entity : entities) {
                for (Entity kind : kinds) {
                    if (kind.getClass().isInstance(entity)) {
                        ofType.add(entity);
                    }
                }
            }

            return pos.nearestEntity(ofType);
        }

        public void addEntity(Entity entity) {
            if (this.withinBounds(entity.position)) {
                this.setOccupancyCell(entity.position, entity);
                this.entities.add(entity);
            }
        }

        public void moveEntity(EventScheduler scheduler, Entity entity, Point pos) {
            Point oldPos = entity.position;
            if (this.withinBounds(pos) && !pos.equals(oldPos)) {
                this.setOccupancyCell(oldPos, null);
                Optional<Entity> occupant = this.getOccupant(pos);
                occupant.ifPresent(target -> this.removeEntity(scheduler, target));
                this.setOccupancyCell(pos, entity);
                entity.position = pos;
            }
        }

        public void removeEntity(EventScheduler scheduler, Entity entity) {
            scheduler.unscheduleAllEvents(entity);
            this.removeEntityAt(entity.position);
        }

        public void removeEntityAt( Point pos) {
            if (this.withinBounds(pos) && this.getOccupancyCell(pos) != null) {
                Entity entity = this.getOccupancyCell(pos);

                /* This moves the entity just outside of the grid for
                 * debugging purposes. */
                entity.position = new Point(-1, -1);
                entities.remove(entity);
                this.setOccupancyCell(pos, null);
            }
        }


        public Optional<Entity> getOccupant(Point pos) {
            if (this.isOccupied(pos)) {
                return Optional.of(this.getOccupancyCell(pos));
            } else {
                return Optional.empty();
            }
        }

        public Entity getOccupancyCell(Point pos) {
            return occupancy[pos.y][pos.x];
        }

        private void setOccupancyCell(Point pos, Entity entity) {
            occupancy[pos.y][pos.x] = entity;
        }

        public void parseSaveFile(Scanner saveFile, ImageStore imageStore, Background defaultBackground) {
            String lastHeader = "";
            int headerLine = 0;
            int lineCounter = 0;
            while (saveFile.hasNextLine()) {
                lineCounter++;
                String line = saveFile.nextLine().strip();
                if (line.endsWith(":")) {
                    headerLine = lineCounter;
                    lastHeader = line;
                    switch (line) {
                        case "Backgrounds:" -> background = new Background[numRows][numCols];
                        case "Entities:" -> {
                            occupancy = new Entity[numRows][numCols];
                            entities = new HashSet<>();
                        }
                    }
                } else {
                    switch (lastHeader) {
                        case "Rows:" -> numRows = Integer.parseInt(line);
                        case "Cols:" -> numCols = Integer.parseInt(line);
                        case "Backgrounds:" -> this.parseBackgroundRow(line, lineCounter - headerLine - 1, imageStore);
                        case "Entities:" -> this.parseEntity(line, imageStore);
                    }
                }
            }
        }

        private void parseBackgroundRow(String line, int row, ImageStore imageStore) {
            String[] cells = line.split(" ");
            if (row < numRows) {
                int rows = Math.min(cells.length, numCols);
                for (int col = 0; col < rows; col++) {
                    background[row][col] = new Background(cells[col], imageStore.getImageList(cells[col]));
                }
            }
        }

        private void parseEntity(String line, ImageStore imageStore) {
            String[] properties = line.split(" ", ENTITY_NUM_PROPERTIES + 1);
            if (properties.length >= ENTITY_NUM_PROPERTIES) {
                String key = properties[PROPERTY_KEY];
                String id = properties[PROPERTY_ID];
                Point pt = new Point(Integer.parseInt(properties[PROPERTY_COL]), Integer.parseInt(properties[PROPERTY_ROW]));

                properties = properties.length == ENTITY_NUM_PROPERTIES ?
                        new String[0] : properties[ENTITY_NUM_PROPERTIES].split(" ");

                switch (key) {
                    case OBSTACLE_KEY -> this.parseObstacle(properties, pt, id, imageStore);
                    case DUDE_KEY -> this.parseDude(properties, pt, id, imageStore);
                    case FAIRY_KEY -> this.parseFairy(properties, pt, id, imageStore);
                    case HOUSE_KEY -> this.parseHouse(properties, pt, id, imageStore);
                    case TREE_KEY -> this.parseTree(properties, pt, id, imageStore);
                    case SAPLING_KEY -> this.parseSapling(properties, pt, id, imageStore);
                    case STUMP_KEY -> this.parseStump(properties, pt, id, imageStore);
                    default -> throw new IllegalArgumentException("Entity key is unknown");
                }
            } else {
                throw new IllegalArgumentException("Entity must be formatted as [key] [id] [x] [y] ...");
            }
        }

        public Background getBackgroundCell(Point pos) {
            return background[pos.y][pos.x];
        }

        public void setBackgroundCell(Point pos, Background background) {
            this.background[pos.y][pos.x] = background;
        }
        public Optional<PImage> getBackgroundImage( Point pos) {
        if (this.withinBounds(pos)) {
            return Optional.of(Background.getCurrentImage(this.getBackgroundCell(pos)));
        } else {
            return Optional.empty();
        }
    }

    public void load(Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        parseSaveFile(saveFile, imageStore, defaultBackground);
        if(background == null){
            background = new Background[numRows][numCols];
            for (Background[] row : background)
                Arrays.fill(row, defaultBackground);
        }
        if(occupancy == null){
            occupancy = new Entity[numRows][numCols];
            entities = new HashSet<>();
        }
    }

}


