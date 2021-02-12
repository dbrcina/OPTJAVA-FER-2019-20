package hr.fer.zemris.optjava.dz12.engine;

import hr.fer.zemris.optjava.dz12.gui.actionlistener.ActionListener;

import java.util.ArrayList;
import java.util.List;

public class GPEngine {

    private List<ActionListener> listeners;

    private boolean[][] initialPlatform;
    private boolean[][] platform;
    private int platformWidth;
    private int platformHeight;

    private int currentX;
    private int currentY;
    private int foodAte;

    private int maxActions;
    private int currentAction;

    // 0 - right
    // 1 - down
    // 2 - left
    // 3 - up
    private int direction;

    public GPEngine(boolean[][] initialPlatform, int maxActions) {
        this.initialPlatform = initialPlatform;
        this.maxActions = maxActions;
        platformWidth = initialPlatform[0].length;
        platformHeight = initialPlatform.length;
        platform = new boolean[platformHeight][platformWidth];
        copyPlatform();
    }

    public void reset() {
        copyPlatform();
        currentX = currentY = direction = foodAte = currentAction = 0;
    }

    private void copyPlatform() {
        for (int i = 0; i < platformHeight; i++) {
            if (platformWidth >= 0)
                System.arraycopy(initialPlatform[i], 0, platform[i], 0, platformWidth);
        }
    }

    public boolean isFoodAhead() {
        int[] nextPosition = nextPosition(); // [x, y]
        return platform[nextPosition[0]][nextPosition[1]];
    }

    public boolean hasMoreActions() {
        return currentAction < maxActions;
    }

    public void moveFunction() {
        if (!hasMoreActions()) return;
        int[] nextPosition = nextPosition();
        currentX = nextPosition[0];
        currentY = nextPosition[1];
        if (platform[currentX][currentY]) {
            foodAte++;
            platform[currentX][currentY] = false;
        }
        currentAction++;
        notifyListeners();
    }

    // helper method, calculates next position
    private int[] nextPosition() {
        int x = currentX;
        int y = currentY;
        switch (direction) {
            case 0:
                x++;
                if (x == platformWidth) x = 0;
                break;
            case 1:
                y++;
                if (y == platformHeight) y = 0;
                break;
            case 2:
                x--;
                if (x < 0) x = platformWidth - 1;
                break;
            case 3:
                y--;
                if (y < 0) y = platformHeight - 1;
                break;
            default:
                throw new RuntimeException("Wrong direction, expected 0, 1, 2, or 3");
        }
        return new int[]{x, y};
    }

    public void leftFunction() {
        if (!hasMoreActions()) return;
        direction--;
        if (direction < 0) direction = 3;
        currentAction++;
        notifyListeners();
    }

    public void rightFunction() {
        if (!hasMoreActions()) return;
        direction++;
        if (direction > 3) direction = 0;
        currentAction++;
        notifyListeners();
    }

    public boolean[][] getPlatform() {
        return platform;
    }

    public int getPlatformWidth() {
        return platformWidth;
    }

    public int getPlatformHeight() {
        return platformHeight;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public int getFoodAte() {
        return foodAte;
    }

    public int getDirection() {
        return direction;
    }

    public boolean addListener(ActionListener l) {
        if (listeners == null) listeners = new ArrayList<>();
        return listeners.add(l);
    }

    public boolean removeListener(ActionListener l) {
        return listeners.remove(l);
    }

    private void notifyListeners() {
        if (listeners == null) return;
        listeners.forEach(ActionListener::actionExecuted);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
