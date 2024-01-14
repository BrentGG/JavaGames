public class TicTacToe {
    private TModel $model;
    private TView $view;
    private TController $controller;

    public TicTacToe() {
        $model = new TModel();
        $controller = new TController($model);
        $view = new TView($controller);
        $model.addObserver($view);
    }

    /**
     * Main method, program starts here.
     * @param args the command line arguments (not used)
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TicTacToe tictactoe = new TicTacToe();
            }
        });
    }
}
