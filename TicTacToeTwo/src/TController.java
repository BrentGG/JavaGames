/**
 * The controller for TModel and TView
 */
public class TController {
    private TModel $model;

    public TController(TModel model) {
        $model = model;
    }

    public void onReset() {
        $model.reset();
    }

    public void onClick(int row, int col) {
        $model.doMove(row, col);
    }
}
