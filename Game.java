
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Game extends JFrame {
    private Font font = new Font("Arial", Font.BOLD, 25);
    private final int SIDE = 4;          //сторона поля
    private int emptyButton;          //пустая клетка
    private final JButton[][] button;           //массив кнопок
    private final String[] winComb = new String[(SIDE * SIDE) - 1];          //выигрышная комбинация
    private JPanel field;          //поле игры

    Game() {

        super("Пятнашки");

        for (int i = 1; i < (SIDE * SIDE); i++)
            winComb[i - 1] = Integer.toString(i);

        button = new JButton[SIDE][SIDE];          //массив кнопок

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        Dimension frameSIZE = new Dimension(screenHeight / 2, screenHeight / 2);

        setSize(frameSIZE);
        setJMenuBar(Menu());
        Field();
    }

    private void Field() {
        if (field != null) {
            font = field.getFont();
            field.setFocusable(false);
            remove(field);
        }

        field = new JPanel(new GridLayout(SIDE, SIDE));

        ArrayList<Integer> randomButton = new ArrayList<>(SIDE * SIDE);
        for (int i = 0; i < (SIDE * SIDE); i++)
            randomButton.add(i);                                    //добавление чисел в массив

        Collections.shuffle(randomButton);                          //перемешать числа

        for (int index = 0; index < (SIDE * SIDE); index++) {       //кнопки на поле
            int i = index / SIDE;          //строка
            int j = index % SIDE;          //столбец
            button[i][j] = new JButton(String.valueOf(randomButton.get(index)));     //создание кнопки
            button[i][j].setBackground(new Color(255, 191, 65));
            button[i][j].setForeground(Color.WHITE);
            button[i][j].setFocusable(false);
            button[i][j].setFont(font);
            button[i][j].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    flipEmptyWithNearButton(i, j);
                }
            });

            field.add(button[i][j]);
            if (randomButton.get(index) == 0) {
                emptyButton = index;
                button[i][j].setVisible(false);
            }
        }

        change(SIDE - 1, SIDE - 1);

        for (int i = 0; i < getKeyListeners().length; i++) {
            KeyListener keyListener = getKeyListeners()[i];
            removeKeyListener(keyListener);
        }


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent press) {
                int line = emptyButton / SIDE;
                int column = emptyButton % SIDE;

                if (press.getKeyCode() == KeyEvent.VK_UP)
                    flipEmptyWithNearButton(line + 1, column);

                if (press.getKeyCode() == KeyEvent.VK_DOWN)
                    flipEmptyWithNearButton(line - 1, column);

                if (press.getKeyCode() == KeyEvent.VK_LEFT)
                    flipEmptyWithNearButton(line, column + 1);

                if (press.getKeyCode() == KeyEvent.VK_RIGHT)
                    flipEmptyWithNearButton(line, column - 1);
            }
        });
        setFocusable(true);

        add(field);
        field.setFont(font);
    }

    private void flipEmptyWithNearButton(int i, int j) {
        int emptyButtonX = emptyButton / SIDE;
        int emptyButtonY = emptyButton % SIDE;
        int reX = Math.abs(i - emptyButtonX);
        int reY = Math.abs(j - emptyButtonY);
        boolean i1 = (i == emptyButtonX);
        boolean j1 = (j == emptyButtonY);
        boolean canSwap = (i1 || j1) && (reX == 1 || reY == 1) && (i >= 0 && i < SIDE) && (j >= 0 && j < SIDE);

        if (canSwap)
            change(i, j);

        if (checkVictory())
            won();
    }

    private void change(int i1, int j1) {               //перемещение квадратика на другое место поля
        int i = emptyButton / SIDE;
        int j = emptyButton % SIDE;
        button[i][j].setText(button[i1][j1].getText());
        button[i][j].setVisible(true);
        button[i1][j1].setText(String.valueOf(0));
        button[i1][j1].setVisible(false);
        emptyButton = (i1 * SIDE) + j1;
    }

    private boolean checkVictory() {                    //проверка на победу
        for (int i = winComb.length - 1; i >= 0; i--) {
            String value = button[i / SIDE][i % SIDE].getText();
            if (!value.equals(winComb[i]))
                return false;
        }
        return true;
    }

    private void won() {
        JOptionPane.showMessageDialog(null, "Это победа!",
                "Выйгрыш", JOptionPane.INFORMATION_MESSAGE);
    }

    private void victoryInit() {          //Выйгрыш
        for (int index = 0; index < (SIDE * SIDE) - 2; index++) {
            int i = index / SIDE;
            int j = index % SIDE;
            button[i][j].setText(String.valueOf(index + 1));
            button[i][j].setVisible(true);
        }
        button[SIDE - 1][SIDE - 2].setText(String.valueOf(0));
        button[SIDE - 1][SIDE - 2].setVisible(false);
        emptyButton = ((SIDE - 1) * SIDE) + (SIDE - 2);
        button[SIDE - 1][SIDE - 1].setText(String.valueOf((SIDE * SIDE) - 1));
        button[SIDE - 1][SIDE - 1].setVisible(true);

        setFocusable(true);

        change(SIDE - 1, SIDE - 1);

        if (checkVictory())
            won();
    }


    private JMenuBar Menu() {

        JMenuBar Menu = new JMenuBar();

        JMenu menu = new JMenu("Меню");
        Menu.add(menu);
        menu.setMnemonic(KeyEvent.VK_C);        //мнемоника alt+C открывает меню

        JMenuItem newGame = new JMenuItem("Новая игра");
        menu.add(newGame);
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));         //акселераатор ctrl+E создает новую игру
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Field();
            }
        });

        menu.add(new JPopupMenu.Separator());          

        JMenuItem easyVictory = new JMenuItem("Выйграть");
        menu.add(easyVictory);
        easyVictory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));          //акселераатор ctrl+X создает уже выйгрышную комбинацию
        easyVictory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                victoryInit();
            }
        });

        menu.add(new JPopupMenu.Separator());

        JMenuItem exit = new JMenuItem("Выход");
        menu.add(exit);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));           //акселераатор ctrl+S выходит из игры
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenu _author_ = new JMenu("Автор");
        Menu.add(_author_);
        _author_.setMnemonic(KeyEvent.VK_R);         //мнемоника alt+R открывает справку об авторе

        JMenuItem author = new JMenuItem("Автор", 'Q');           //мнемоника Q открывет автор
        _author_.add(author);
        author.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Агаев Багир Русланович, P3168", "Университет ИТМО:", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return Menu;

    }
}



