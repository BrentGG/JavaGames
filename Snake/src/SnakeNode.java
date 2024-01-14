public class SnakeNode {
    private int $xpos;
    private int $ypos;
    private int $prevxpos;
    private int $prevypos;

    public SnakeNode(int x, int y) {
        $xpos = x;
        $ypos = y;
        $prevxpos = 0;
        $prevypos = 0;
    }

    public void moveTo(int x, int y) {
        $prevxpos = $xpos;
        $prevypos = $ypos;

        if (x >= Snake.COLS)
            $xpos = 0;
        else if (x < 0)
            $xpos = Snake.COLS-1;
        else
            $xpos = x;
        if (y >= Snake.ROWS)
            $ypos = 0;
        else if (y < 0)
            $ypos = Snake.ROWS-1;
        else
            $ypos = y;
    }

    public int getX() { return $xpos; }
    public int getY() { return $ypos; }
    public int getPrevX() { return $prevxpos; }
    public int getPrevY() { return $prevypos; }
}
