import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;

public class SnakeModel extends Observable {
    private ArrayList<SnakeNode> $snake;
    private ArrayList<int[]> $obstacles;
    private DIRECTION $direction;
    private Timer $timer;
    private int[] $applePos;
    private int $score;
    private boolean $directionChangeRequested = false;
    private boolean $wallsEnabled = false;
    private boolean $obstaclesEnabled = false;
    private boolean $aiEnabled = false;

    public enum DIRECTION {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }

    public SnakeModel() {
        initSnake();
    }

    private void initSnake() {
        $snake = new ArrayList<>();
        $snake.add(new SnakeNode(Snake.COLS/2, Snake.ROWS/2));
        $snake.add(new SnakeNode((Snake.COLS/2)-1, (Snake.ROWS/2)));
        $snake.add(new SnakeNode((Snake.COLS/2)-2, (Snake.ROWS/2)));
        $snake.add(new SnakeNode((Snake.COLS/2)-3, (Snake.ROWS/2)));
        $snake.add(new SnakeNode((Snake.COLS/2)-4, (Snake.ROWS/2)));
        $obstacles = new ArrayList<>();
        $direction = DIRECTION.EAST;
        $applePos = new int[2];
        randomApplePos();
        $score = 0;
    }

    class TickTask extends TimerTask {
        public void run() {
            tick();
        }
    }

    public void start(int speed, boolean walls, boolean obstacles, boolean ai) {
        initSnake();
        $wallsEnabled = walls;
        $obstaclesEnabled = obstacles;
        if ($obstaclesEnabled)
            randomObstacles();
        $aiEnabled = ai;
        setChanged();
        notifyObservers($snake);
        $timer = new Timer();
        $timer.schedule(new TickTask(), 0, 30*(11-speed));
        //$timer.schedule(new TickTask(), 0, 1);
    }

    public void stop() {
        $timer.cancel();
    }

    private void tick() {
        int[] newPos = getMove($snake.get(0).getX(), $snake.get(0).getY());
        if ($wallsEnabled && checkHitWall(newPos)) {
            lost();
            return;
        }
        $snake.get($snake.size() - 1).moveTo(newPos[0], newPos[1]);
        $snake.add(0, $snake.get($snake.size() - 1));
        $snake.remove($snake.size() - 1);

        if (checkCollision($snake.get(0).getX(), $snake.get(0).getY(), 1)) {
            lost();
            return;
        }

        if (checkCollision($applePos[0], $applePos[1], 0)) {
            addSnakeNode();
            randomApplePos();
            $score += 100;
        }
        setChanged();
        notifyObservers($snake);

        //ai functionality
        if ($aiEnabled)
            doAiMove();
    }

    public void lost() {
        $timer.cancel();
        setChanged();
        notifyObservers("lost");
    }

    private int[] getMove(int x, int y) {
        $directionChangeRequested = false;
        int[] result = new int[2];
        result[0] = x;
        result[1] = y;
        switch ($direction) {
            case NORTH:
                --result[1];
                break;
            case EAST:
                ++result[0];
                break;
            case SOUTH:
                ++result[1];
                break;
            case WEST:
                --result[0];
                break;
        }
        return result;
    }

    public void changeDirection(DIRECTION d) {
        if (allowed($direction, d)) {
            $directionChangeRequested = true;
            $direction = d;
        }
    }

    private boolean allowed(DIRECTION oldD, DIRECTION newD) {
        if ($directionChangeRequested)
            return false;
        if (oldD == newD)
            return false;
        if (oldD == DIRECTION.NORTH && newD == DIRECTION.SOUTH)
            return false;
        if (oldD == DIRECTION.SOUTH && newD == DIRECTION.NORTH)
            return false;
        if (oldD == DIRECTION.WEST && newD == DIRECTION.EAST)
            return false;
        if (oldD == DIRECTION.EAST && newD == DIRECTION.WEST)
            return false;
        return true;
    }

    private boolean checkCollision(int x, int y, int startIndex) {
        boolean collision = false;
        for (int i = startIndex; i < $snake.size(); ++i) {
            if (x == $snake.get(i).getX() && y == $snake.get(i).getY())
                collision = true;
        }
        for (int i = 0; i < $obstacles.size(); ++i) {
            if (x == $obstacles.get(i)[0] && y == $obstacles.get(i)[1])
                collision = true;
        }
        return collision;
    }

    private void randomApplePos() {
        Random rng = new Random();

        ArrayList<int[]> available = new ArrayList<>();
        for (int i = 0; i < Snake.ROWS; ++i) {
            for (int j = 0; j < Snake.COLS; ++j) {
                if (!checkCollision(j, i, 0) && twoFreeSpaces(j, i))
                    available.add(new int[]{j, i});
            }
        }
        $applePos = available.get(rng.nextInt(available.size()));

        /*
        boolean goodPosition = false;
        int x = 0, y = 0;
        while (!goodPosition) {
            x = rng.nextInt(Snake.COLS);
            y = rng.nextInt(Snake.ROWS);
            goodPosition = !checkCollision(x, y, 0);
        }
        $applePos[0] = x;
        $applePos[1] = y;
        */
    }

    private boolean twoFreeSpaces(int x, int y) {
        int freeSpaces = 4;
        if (checkCollision(x, y+1, 0) || checkHitWall(new int[]{x,y+1}))
            --freeSpaces;
        if (checkCollision(x+1, y, 0) || checkHitWall(new int[]{x+1,y}))
            --freeSpaces;
        if (checkCollision(x-1, y, 0) || checkHitWall(new int[]{x-1,y}))
            --freeSpaces;
        if (checkCollision(x, y-1, 0) || checkHitWall(new int[]{x,y-1}))
            --freeSpaces;
        if (freeSpaces >= 2)
            return true;
        return false;
    }

    public int[] getApplePos() {
        return $applePos;
    }

    private void addSnakeNode() {
        SnakeNode last = $snake.get($snake.size()-1);
        SnakeNode secondLast = $snake.get($snake.size()-2);

        int x = 0, y = 0;
        if (last.getY() == secondLast.getY()) {
            y = last.getY();
            if (secondLast.getX() == last.getX() + 1)
                x = last.getX() - 1;
            else
                x = last.getX() + 1;
            if (x < 0)
                x = Snake.COLS-1;
            if (x > Snake.COLS-1)
                x = 0;
            if (checkCollision(x, y, 0) || checkHitWall(new int[]{x, y}))
                addNonCollisionSnakeNode(last);
            else
                $snake.add(new SnakeNode(x, y));
        }
        else {
            x = last.getX();
            if (secondLast.getY() == last.getY()+1)
                y = last.getY()-1;
            else
                y = last.getY()+1;
            if (y < 0)
                y = Snake.ROWS-1;
            if (y > Snake.ROWS-1)
                y = 0;
            if (checkCollision(x, y, 0) || checkHitWall(new int[]{x, y}))
                addNonCollisionSnakeNode(last);
            else
                $snake.add(new SnakeNode(x, y));
        }
        if (checkWon()) {
            setChanged();
            notifyObservers("won");
        }
    }

    private void addNonCollisionSnakeNode(SnakeNode last) {
        int x = last.getX();
        int y = last.getY();
        if (!checkCollision(x+1, y, 0) && !checkHitWall(new int[]{x+1, y}))
            $snake.add(new SnakeNode(x+1, y));
        else if (!checkCollision(x-1, y, 0) && !checkHitWall(new int[]{x-1, y}))
            $snake.add(new SnakeNode(x-1, y));
        else if (!checkCollision(x, y+1, 0) && !checkHitWall(new int[]{x, y+1}))
            $snake.add(new SnakeNode(x, y+1));
        else
            $snake.add(new SnakeNode(x, y-1));
    }

    private boolean checkWon() {
        if (!$obstaclesEnabled)
            return $snake.size() == (Snake.COLS * Snake.ROWS);
        else {
            return $snake.size() == (Snake.COLS * Snake.ROWS) - (getObstacleRows() * Snake.COLS);
        }
    }

    public int getScore() {
        return $score;
    }

    private boolean checkHitWall(int[] pos) {
        return pos[0] < 0 || pos[0] >= Snake.COLS || pos[1] < 0 || pos[1] >= Snake.ROWS;
    }

    private void randomObstacles() {
        Random rng = new Random();

        ArrayList<int[]> available = new ArrayList<>();
        for (int i = 0; i < Snake.ROWS; ++i) {
            for (int j = 0; j < Snake.COLS; ++j) {
                if (!checkCollision(j, i, 0) && i != $snake.get(0).getY())
                    available.add(new int[]{j, i});
            }
        }

        for (int i = 0; i < (Snake.COLS*Snake.ROWS/15); ++i) {
            int index = rng.nextInt(available.size());
            $obstacles.add(available.get(index));
            available.remove(index);
        }
    }

    public ArrayList<int[]> getObstacles() {
        return $obstacles;
    }

    private int getObstacleRows() {
        ArrayList<Integer> rows = new ArrayList<>();
        for (int[] o : $obstacles) {
            if (!rows.contains(o[1]))
                rows.add(o[1]);
        }
        return rows.size();
    }

    private void doAiMove() {
        changeAiAppleDirection();
        changeAiCollisionDirection();
    }

    private void changeAiCollisionDirection() {
        int[] nextPos = getMove($snake.get(0).getX(), $snake.get(0).getY());
        int[] nextNextPos = getMove(nextPos[0], nextPos[1]);
        changeAiCollisionDirectionHelp(nextNextPos);
        changeAiCollisionDirectionHelp(nextPos);
    }

    private boolean changeAiCollisionDirectionHelp(int[] nextPos) {
        boolean collision = false;
        if (!$wallsEnabled) {
            if (checkCollision(nextPos[0], nextPos[1], 1)) {
                collision = true;
                DIRECTION[] directions = {DIRECTION.NORTH, DIRECTION.EAST, DIRECTION.SOUTH, DIRECTION.WEST};
                for (int i = 0; i < 4; ++i) {
                    changeDirection(directions[i]);
                    int[] newNextPos = getMove($snake.get(0).getX(), $snake.get(0).getY());
                    if (!checkCollision(newNextPos[0], newNextPos[1], 1))
                        break;
                }
            }
        }
        else {
            if (checkCollision(nextPos[0], nextPos[1], 1) || checkHitWall(nextPos)) {
                collision = true;
                DIRECTION[] directions = {DIRECTION.NORTH, DIRECTION.EAST, DIRECTION.SOUTH, DIRECTION.WEST};
                for (int i = 0; i < 4; ++i) {
                    changeDirection(directions[i]);
                    int[] newNextPos = getMove($snake.get(0).getX(), $snake.get(0).getY());
                    if (!checkCollision(newNextPos[0], newNextPos[1], 1) && !checkHitWall(newNextPos))
                        break;
                }
            }
        }
        return collision;
    }

    private void changeAiAppleDirection() {
        int applex = $applePos[0];
        int appley = $applePos[1];

        if ($direction == DIRECTION.NORTH || $direction == DIRECTION.SOUTH) {
            if ($snake.get(0).getY() == appley) {
                if (!$wallsEnabled) {
                    if (Math.abs($snake.get(0).getX() - applex) <= Snake.COLS / 2) {
                        if (applex < $snake.get(0).getX())
                            changeDirection(DIRECTION.WEST);
                        else
                            changeDirection(DIRECTION.EAST);
                    } else {
                        if (applex < $snake.get(0).getX())
                            changeDirection(DIRECTION.EAST);
                        else
                            changeDirection(DIRECTION.WEST);
                    }
                }
                else {
                    if (applex < $snake.get(0).getX())
                        changeDirection(DIRECTION.WEST);
                    else
                        changeDirection(DIRECTION.EAST);
                }
            }
        }
        else if ($direction == DIRECTION.EAST || $direction == DIRECTION.WEST) {
            if ($snake.get(0).getX() == applex) {
                if (!$wallsEnabled) {
                    if (Math.abs($snake.get(0).getY() - appley) <= Snake.ROWS / 2) {
                        if (appley < $snake.get(0).getY())
                            changeDirection(DIRECTION.NORTH);
                        else
                            changeDirection(DIRECTION.SOUTH);
                    } else {
                        if (appley < $snake.get(0).getY())
                            changeDirection(DIRECTION.SOUTH);
                        else
                            changeDirection(DIRECTION.NORTH);
                    }
                }
                else {
                    if (appley < $snake.get(0).getY())
                        changeDirection(DIRECTION.NORTH);
                    else
                        changeDirection(DIRECTION.SOUTH);
                }
            }
        }
    }
}
