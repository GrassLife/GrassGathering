package life.grass.grassgathering;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResourceJsonContainer {
    private static final String FISH_DIR_PATH = GrassGathering.getInstance().getDataFolder().getPath() + File.separator + "fishes";
    private static final String MINE_DIR_PATH = GrassGathering.getInstance().getDataFolder().getPath() + File.separator + "mines";

    private static Gson gson;
    private static ResourceJsonContainer resourceJsonContainer;

    private Map<String, JsonObject> fishJsonMap;
    private Map<String, JsonObject> mineJsonMap;

    static {
        gson = new Gson();
        resourceJsonContainer = new ResourceJsonContainer();
    }

    private ResourceJsonContainer() {
        refillContainer();
    }

    public static ResourceJsonContainer getInstance() {
        return resourceJsonContainer;
    }

    public Map<String, JsonObject> getFishJsonMap() {
        return fishJsonMap;
    }

    public Map<String, JsonObject> getMineJsonMap() {
        return mineJsonMap;
    }

    public void refillContainer() {
        fishJsonMap = new HashMap<>();
        mineJsonMap = new HashMap<>();

        File fishFolder = new File(FISH_DIR_PATH);
        if (!fishFolder.exists()) fishFolder.mkdirs();

        File mineFolder = new File(MINE_DIR_PATH);
        if (!mineFolder.exists()) mineFolder.mkdirs();

        Arrays.stream(fishFolder.listFiles())
                .filter(file -> file.getName().endsWith(".json"))
                .forEach(json -> loadJsonFromFile(json).ifPresent(jsonObject -> fishJsonMap.put(jsonObject.get("fishName").getAsString(), jsonObject)));

        Arrays.stream(mineFolder.listFiles())
                .filter(file -> file.getName().endsWith(".json"))
                .forEach(json -> loadJsonFromFile(json).ifPresent(jsonObject -> mineJsonMap.put(jsonObject.get("mineName").getAsString(), jsonObject)));
    }

    private static Optional<JsonObject> loadJsonFromFile(File file) {
        JsonObject root;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            root = gson.fromJson(br, JsonObject.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            root = null;
        }

        return Optional.ofNullable(root);
    }

    public void setResourceJsonContainer() {
        resourceJsonContainer = new ResourceJsonContainer();
    }
}
