package stadnina;

import java.util.List;

public class StableManager {
    private StableDAO stableDAO;

    public StableManager() {
        this.stableDAO = new StableDAO();
    }

    public void addStable(String name, int capacity) {
        if (stableDAO.findByName(name) != null) return;
        Stable s = new Stable(name, capacity);
        stableDAO.saveStable(s);
    }

    // --- NOWA METODA ---
    public void updateHorse(Horse horse) {
        stableDAO.updateHorse(horse);
    }
    // -------------------

    public void removeStable(String name) {
        Stable s = stableDAO.findByName(name);
        if (s != null) stableDAO.deleteStable(s);
    }

    public List<Stable> getAllStables() {
        return stableDAO.getAllStables();
    }

    public Stable getStable(String name) {
        return stableDAO.findByName(name);
    }
    public void removeHorse(Horse horse) {
        stableDAO.deleteHorse(horse);
    }

    public void updateStable(Stable s) {
        stableDAO.saveStable(s);
    }
}