package gameClient.Game_DS;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

import api.*;
import gameClient.Ex2;
import org.json.JSONException;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 *
 * @author Shaked Aviad & Rotem Halbreich
 */

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;
import static java.lang.Integer.parseInt;


public class GameGUI extends JFrame implements MouseListener, ActionListener {
    private GameAlgo gameAlgo;
    private directed_weighted_graph graph;
    private double MinX, MinY, MaxX, MaxY;
    private boolean CMD = true;
    private File soundtrack;
    private JButton button;
    private double BTNSIZE;
    private Clip music;
    private ImageIcon iconBtnMute=new ImageIcon("data/media/mute.png");
    private ImageIcon iconBtnUnmute=new ImageIcon("data/media/unmute.png");
    private BufferedImage background;
    private BufferedImage ash;
    private BufferedImage misty;
    private BufferedImage brock;
    private BufferedImage pichu;
    private BufferedImage pikachu;
    private BufferedImage raichu;
    private BufferedImage raikou;
    private BufferedImage bulbasour;
    private BufferedImage ivysaur;
    private BufferedImage venusaur;
    private BufferedImage rayquaza;

    public GameGUI() {
        super();
        setTitle("Pokemon's Path - The Game");
        setSize(1280, 700);
        playMusic();
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setFrameSize();
        setButton();
        menuBar();
        addMouseListener(this);
        setImages();
        initGame("constructor");
    }

    /**
     * @param g
     */
    public void update(GameAlgo g) {
        this.gameAlgo = g;
        this.graph = gameAlgo.getGraph();
        resizeFrame();
    }


    /**
     * @param g2
     */
    public void paint(Graphics g2) {
        if (graph == null) {
            super.paint(g2);
            return;
        }
        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();

        super.paintComponents(g);
        button.setBounds((int) BTNSIZE - 60, 0, 50, 50);
        drawBackground(g);
        drawGraph(g);
        drawPokemos(g);
        drawAgents(g);
        drawInfo(g);

        Graphics2D orgGraphic = (Graphics2D) g2;
        orgGraphic.drawImage(bufferedImage, null, 0, 0);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String select = e.getActionCommand();
        if (select.equals("New Game")) {
            if (gameAlgo.getGame().isRunning())
                gameAlgo.getGame().stopGame();
            initGame("new");
        } else if (e.getSource() == button) {
            if (select.equals("Mute")) {
                button.setText("Unmute");
                button.setIcon(iconBtnMute);
                music.stop();
            } else {
                button.setText("Mute");
                button.setIcon(iconBtnUnmute);
                music.start();
            }
        } else {
            gameAlgo.getGame().stopGame();
        }
    }

    ///////// Private Methods //////////
    private void setButton() {
        button = new JButton();
        button.setText("Mute");
        button.setFont(new Font("Elephant", Font.PLAIN, 0));
        button.setIcon(iconBtnUnmute);
        button.addActionListener(this);
        button.setIconTextGap(-15);
        button.setBorder(BorderFactory.createEtchedBorder());
        this.add(button);


    }

    /**
     * @param from
     */
    public void initGame(String from) {
        String select = null, key = null;
        boolean with = true;
        if (from.equals("constructor") && Ex2.level != null && Ex2.id != null) CMD = false;
        if (!CMD && from.equals("constructor")) return;
        while (with) {
            key = JOptionPane.showInputDialog("Please enter your ID:  ", "000000000");
            select = JOptionPane.showInputDialog("Please choose level number (0-23):  ", "0");
            int num = -1, key_id = -1;
            try {
                num = parseInt(select);
                key_id = parseInt(key);
                with = false;
            } catch (Exception ex) {
                continue;
            }
            if (from.equals("constructor")) {
                Ex2.level = num;
                Ex2.id = key_id;
            }
            if (from.equals("new")) initNewGame(num, key_id);
        }
    }

    /**
     *
     */
    private void setImages() {
        try {
            this.background = ImageIO.read(new File("data/media/background.png"));
            this.ash = ImageIO.read(new File("data/media/ash.png"));
            this.misty = ImageIO.read(new File("data/media/misty.png"));
            this.brock = ImageIO.read(new File("data/media/brock.png"));
            this.pichu = ImageIO.read(new File("data/media/pichu.png"));
            this.pikachu = ImageIO.read(new File("data/media/pikachu.png"));
            this.raichu = ImageIO.read(new File("data/media/raichu.png"));
            this.raikou = ImageIO.read(new File("data/media/raikou.png"));
            this.bulbasour = ImageIO.read(new File("data/media/bulbasaur.png"));
            this.ivysaur = ImageIO.read(new File("data/media/ivysaur.png"));
            this.venusaur = ImageIO.read(new File("data/media/venusaur.png"));
            this.rayquaza = ImageIO.read(new File("data/media/rayquaza.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param g
     */
    private void drawBackground(Graphics2D g) {
        g.drawImage(background, 0, 50, getWidth(), getHeight(), null);
    }

    /**
     *
     */
    private void playMusic() {
        try {
            this.soundtrack = new File("data/media/soundtrack.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundtrack);
            music = AudioSystem.getClip();
            music.open(audioStream);
            music.start();
            music.loop(Integer.MAX_VALUE);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void setFrameSize() {
        MinX = MAX_VALUE;
        MinY = MAX_VALUE;
        MaxX = MIN_VALUE;
        MaxY = MIN_VALUE;
        BTNSIZE = getWidth();
    }

    /**
     *
     */
    private void resizeFrame() {
        if (graph == null) return;
        for (node_data n : graph.getV()) {
            if (n.getLocation().x() < MinX)
                MinX = n.getLocation().x();

            if (n.getLocation().x() > MaxX)
                MaxX = n.getLocation().x();

            if (n.getLocation().y() < MinY)
                MinY = n.getLocation().y();

            if (n.getLocation().y() > MaxY)
                MaxY = n.getLocation().y();
        }
        BTNSIZE = getWidth();
    }

    /**
     * @param y
     * @return
     */
    private int ratioY(double y) {
        if (y > MaxY || y < MinY)
            resizeFrame();

        return getHeight() - (int) ((y - MinY) / (MaxY - MinY) * (getHeight() - 100) + 30);
    }

    /**
     * @param x
     * @return
     */
    private int ratioX(double x) {
        if (x > MaxX || x < MinX)
            resizeFrame();
        BTNSIZE = getWidth();
        return (int) ((x - MinX) / (MaxX - MinX) * (getWidth() - 50) + 20);
    }

    /**
     * @param g
     */
    private void drawGraph(Graphics2D g) {
        if (graph == null) return;
        g.setFont(new Font("Elephant", Font.PLAIN, 13));
        for (node_data n : graph.getV()) {
            drawNode(g, n);
            for (edge_data e : graph.getE(n.getKey())) {
                drawEdge(g, e);
            }
        }
    }

    /**
     * @param g
     * @param n
     */
    private void drawNode(Graphics2D g, node_data n) {
        int x = ratioX(n.getLocation().x());
        int y = ratioY(n.getLocation().y());
        g.setColor(new Color(59, 124, 191, 255));
        g.setFont(new Font("Elephant", Font.PLAIN, 13));
        g.fill3DRect(x - 8, y - 10, 18, 18, true);
        g.drawString("" + n.getKey(), x, y + 20);
    }

    /**
     * @param g
     * @param e
     */
    private void drawEdge(Graphics2D g, edge_data e) {
        node_data src = graph.getNode(e.getSrc()),
                dest = graph.getNode(e.getDest());

        int x = ratioX(src.getLocation().x()),
                x1 = ratioX(dest.getLocation().x()),
                y = ratioY(src.getLocation().y()),
                y1 = ratioY(dest.getLocation().y());

        g.setColor(new Color(40, 42, 45, 54));
        g.setStroke(new BasicStroke(3));
        g.setFont(new Font("Elephant", Font.PLAIN, 12));
        g.drawLine(x, y, x1, y1);
        g.drawString("  ", (x + 5 * x1) / 4, (y + 5 * y1) / 4);
    }

    /**
     * @param g
     */
    private void drawAgents(Graphics g) {
        if (gameAlgo == null || gameAlgo.getAgents() == null) return;
        Iterator<Agent> itr = gameAlgo.getAgents().iterator();
        while (itr.hasNext()) {
            Agent agent = itr.next();
            if (agent == null) continue;
            g.setColor(new Color(255, 0, 0, 255));
            int x = ratioX(agent.getPos().x()),
                    y = ratioY(agent.getPos().y());
            g.drawString("ID:" + agent.getId() + " , Speed: " + agent.getSpeed(), x - 50, y - 40);
            if (agent.getId() == 0) {
                g.drawImage(this.ash, x - 18, y - 30, 30, 60, null);
            } else if (agent.getId() == 1) {
                g.drawImage(this.misty, x - 18, y - 30, 40, 70, null);
            } else {
                g.drawImage(this.brock, x - 18, y - 30, 40, 70, null);
            }
        }
    }

    /**
     * @param g
     */
    private void drawPokemos(Graphics g) {
        if (gameAlgo == null || gameAlgo.getPokemons() == null) return;
        Iterator<Pokemon> itr = gameAlgo.getPokemons().iterator();
        while (itr.hasNext()) {
            Pokemon pokemon = itr.next();
            if (pokemon.getLocation() == null) continue;
            int x = ratioX(pokemon.getLocation().x()), y = ratioY(pokemon.getLocation().y());

            if (pokemon.getType() < 0) {
                g.setColor(new Color(255, 170, 0));
                if (pokemon.getValue() <= 8)
                    g.drawImage(this.pichu, x - 12, y - 24, 25, 35, null);
                else if (pokemon.getValue() <= 11 && pokemon.getValue() >= 9)
                    g.drawImage(this.pikachu, x - 20, y - 24, 38, 38, null);
                else if (pokemon.getValue() >= 12 && pokemon.getValue() <= 14)
                    g.drawImage(this.raichu, x - 30, y - 33, 58, 58, null);
                else
                    g.drawImage(this.raikou, x - 28, y - 24, 60, 60, null);
            } else {
                g.setColor(new Color(79, 146, 17));
                //   g.drawImage(this.bulbasour, x - 12, y - 18, 30, 30, null);
                if (pokemon.getValue() <= 8)
                    g.drawImage(this.bulbasour, x - 12, y - 18, 30, 30, null);
                else if (pokemon.getValue() <= 11 && pokemon.getValue() >= 9)
                    g.drawImage(this.ivysaur, x - 20, y - 32, 45, 45, null);
                else if (pokemon.getValue() >= 12 && pokemon.getValue() <= 14)
                    g.drawImage(this.venusaur, x - 28, y - 24, 50, 50, null);
                else
                    g.drawImage(this.rayquaza, x - 28, y - 35, 80, 80, null);
            }
            g.drawString("" + pokemon.getValue(), x - 13, y - 40);
        }
    }

    /**
     * @param g
     */
    private void drawInfo(Graphics g) {
        if (gameAlgo == null) return;
        g.setColor(Color.black);
        g.setFont(new Font("Cooper Black", Font.PLAIN, getWidth() / 65));
        if (gameAlgo.getInfo() != null) {
            String info = "";
            info += "Grade: " + gameAlgo.getInfo().getGrade();
            if (gameAlgo.isRunning())
                info += " | Time: " + gameAlgo.getGame().timeToEnd() / 1000;
            info += " | Moves: " + gameAlgo.getInfo().getMoves();
            g.drawString(info, getWidth() / 7, 50);

            g.drawString("Level: " + gameAlgo.getInfo().getGameLevel() + " | Pokemons: " + gameAlgo.getInfo().getPokemons() + " | Agents: "
                    + gameAlgo.getInfo().getAgents(), (getWidth() / 5) * 3, 50);

            if (!gameAlgo.getGame().isRunning()) {
                g.setFont(new Font("Cooper Black", Font.PLAIN, 170));
                g.setColor(Color.red);
                if (gameAlgo.getInfo() != null)
                    g.drawString("Game Over!", (getWidth() / 2) - 475, (getHeight() / 2) + 45);
            }
        }
    }

    /**
     *
     */
    private void menuBar() {
        JMenu menu = new JMenu("Menu");
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(this);
        newGame.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menu.add(newGame);
        JMenuItem stop = new JMenuItem("Stop Game");
        stop.addActionListener(this);
        menu.add(stop);

        JMenuBar fatherMenu = new JMenuBar();
        fatherMenu.add(menu);
        setJMenuBar(fatherMenu);
    }

    /**
     * @param num
     * @param key
     */
    private void initNewGame(int num, int key) {
        Ex2.id = key;
        Ex2.level = num;
        Ex2.main(new String[2]);
    }
}
