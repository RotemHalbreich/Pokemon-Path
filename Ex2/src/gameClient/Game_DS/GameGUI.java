package gameClient.Game_DS;

import javax.imageio.ImageIO;
import javax.swing.*;

import api.*;
import gameClient.Ex2;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;
import static java.lang.Integer.parseInt;

public class GameGUI extends JFrame implements MouseListener, ActionListener {
    private GameAlgo gameAlgo;
    private directed_weighted_graph graph;
    private double MinX, MinY, MaxX, MaxY;
    private  String id = "305496614";
    private BufferedImage background;
    private BufferedImage ash;
    private BufferedImage pichu;
    private BufferedImage pikachu;
    private BufferedImage raichu;
    private BufferedImage bulbasour;
    private BufferedImage ivysaur;
    private BufferedImage venusaur;

    private Color g;


    public GameGUI() {

        super();

        setTitle("Pokemon - The Game");
        setSize(1280, 700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setFrameSize();

        menuBar();
        addMouseListener(this);
        setImages();
        initGame("constructor");

    }

    private void setImages() {
        try {
            this.background = ImageIO.read(new File("data/images/pokemon-pc-game.png"));
            this.ash = ImageIO.read(new File("data/images/ash.png"));
            this.pichu = ImageIO.read(new File("data/images/pichu.png"));
            this.pikachu = ImageIO.read(new File("data/images/pikachu.png"));
            this.raichu = ImageIO.read(new File("data/images/raichu.png"));
            this.bulbasour = ImageIO.read(new File("data/images/bulbasaur.png"));
            this.ivysaur = ImageIO.read(new File("data/images/ivysaur.png"));
            this.venusaur = ImageIO.read(new File("data/images/venusaur.png"));



//            bulbasour = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g1 = bulbasour.createGraphics();
//
//            pikachu = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g2 = pikachu.createGraphics();
//
//            ash = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g3 = ash.createGraphics();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void drawBackground(Graphics2D g) {
        g.drawImage(background, 0, 100, getWidth(), getHeight(), null);
//        background = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
//        background.createGraphics();
    }

    public void update(GameAlgo g) {
        this.gameAlgo = g;
        this.graph = gameAlgo.getGraph();

        resizeFrame();

    }

    public void paint(Graphics g2) {
        if (graph == null) {
            super.paint(g2);
            return;
        }

        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        this.paintComponents(g);
       // g.dispose();

        super.paintComponents(g);


        //g.setBackground(Color.white);

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
        // System.out.println("x:"+e.getX()+"\t y:"+e.getY());
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String select = e.getActionCommand();
        System.out.println(select);
        if (select.equals("New Game")) {
            gameAlgo.getGame().stopGame();
            initGame("new");

        } else {
            gameAlgo.getGame().stopGame();
        }
    }


//////// TODO4!!!! ///////

    // we need to think about
    // what we want to do after game
    public void gameOver(Graphics g) {
        ;
    }




    ///////// Private Methods //////////
    public void initGame(String from){
        String select=null,key=null;
        boolean with = true;

        while (with) {
            key = JOptionPane.showInputDialog("Please enter your ID:  ", "311549364");
            select = JOptionPane.showInputDialog("Please choose level number (0-23):  ", "0");
            int num = -1,key_id = -1;
            try {
                num = parseInt(select);
                key_id = parseInt(key);
            } catch (Exception ex) {
                continue;
            }
            if (num < 23 || num > 0) {
              if(from.equals("constructor")){
                  Ex2.level=num;
                  Ex2.id=key_id;
              }
              if(from.equals("new"))
                  initNewGame(num,key_id);

                with = false;
            }
        }

    }
    private void setFrameSize() {
        MinX = MAX_VALUE;
        MinY = MAX_VALUE;
        MaxX = MIN_VALUE;
        MaxY = MIN_VALUE;
    }

    // we need to fix the duplicate
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
    }

    private int ratioY(double y) {
        if (y > MaxY || y < MinY)
            resizeFrame();

        return getHeight() - (int) ((y - MinY) / (MaxY - MinY) * (getHeight() - 100) + 30);
    }

    private int ratioX(double x) {
        if (x > MaxX || x < MinX)
            resizeFrame();
        return (int) ((x - MinX) / (MaxX - MinX) * (getWidth() - 50) + 25);
    }

    private void drawGraph(Graphics2D g) {
        if (graph == null) return;
        for (node_data n : graph.getV()) {
            drawNode(g, n);
            for (edge_data e : graph.getE(n.getKey())) {
                drawEdge(g, e);
            }
        }
    }

    private void drawNode(Graphics2D g, node_data n) {
        int x = ratioX(n.getLocation().x());
        int y = ratioY(n.getLocation().y());
        g.setColor(Color.blue);
        g.setFont(new Font("MV Boli", Font.BOLD, 13));
        g.fillOval(x - 7, y - 7, 18, 18);
        g.drawString("  " + n.getKey(), x, y + 10);
    }

    private void drawEdge(Graphics2D g, edge_data e) {
        node_data src = graph.getNode(e.getSrc()),
                dest = graph.getNode(e.getDest());
        double w = (int) e.getWeight();
        int x = ratioX(src.getLocation().x()),
                x1 = ratioX(dest.getLocation().x()),
                y = ratioY(src.getLocation().y()),
                y1 = ratioY(dest.getLocation().y());

        g.setColor(Color.black);
        g.setStroke(new BasicStroke(3));
        g.setFont(new Font("MV Boli", Font.BOLD, 13));
        g.drawLine(x, y, x1, y1);
        g.drawString("  ", (x + 5 * x1) / 4, (y + 5 * y1) / 4);
    }

    private void drawAgents(Graphics g) {
        if (gameAlgo == null || gameAlgo.getAgents() == null) return;
        Iterator<Agent> itr = gameAlgo.getAgents().iterator();
        while (itr.hasNext()) {
            Agent agent = itr.next();
            if (agent == null) continue;
            g.setColor(Color.RED);
            int x = ratioX(agent.getPos().x()),
                    y = ratioY(agent.getPos().y());
            // g.fillOval(x - 8, y - 8, 24, 24);
            g.drawImage(this.ash, x - 45, y - 45, 90, 90, null);
            // g.fillArc(x - 8, y - 8, 28, 28, 300, 300);
            g.drawString("    ID:" + agent.getId() + " , Speed: " + agent.getSpeed(), x, y);
        }
    }

    private void drawPokemos(Graphics g) {
        if (gameAlgo == null || gameAlgo.getPokemons() == null) return;
        Iterator<Pokemon> itr = gameAlgo.getPokemons().iterator();
        while (itr.hasNext()) {
            Pokemon pokemon = itr.next();
            if (pokemon.getLocation() == null) continue;
            int x = ratioX(pokemon.getLocation().x()), y = ratioY(pokemon.getLocation().y());
            // g.setColor(Color.green);
            if (pokemon.getType() < 0) {
                g.setColor(Color.ORANGE);
                if (pokemon.getValue() <= 8) {
                    g.drawImage(this.pichu, x - 12, y - 24, 25, 35, null);
                }
                if (pokemon.getValue() <= 12 && pokemon.getValue() > 8) {
                    g.drawImage(this.pikachu, x - 20, y - 24, 38, 38, null);
                }
                if (pokemon.getValue() > 12) {
                    g.drawImage(this.raichu, x - 20, y - 33, 55, 55, null);
                }
            } else {
                g.setColor(Color.GREEN);
                if (pokemon.getValue() <= 8) {
                    g.drawImage(this.bulbasour, x - 12, y - 18, 30, 30, null);
                }
                if (pokemon.getValue() <= 12 && pokemon.getValue() > 8) {
                    g.drawImage(this.ivysaur, x - 20, y - 32, 45, 45, null);
                }
                if (pokemon.getValue() > 12) {
                    g.drawImage(this.venusaur, x - 28, y - 24, 50, 50, null);
                }
            }
            // g.fill3DRect(x - 6, y - 6, 16, 16, true);
            //g.fillOval(x - 6, y - 6, 16, 16);
            g.drawString("    " + pokemon.getValue(), x, y);

        }
    }
    private void drawInfo(Graphics g) {
        if (gameAlgo == null) return;
        g.setColor(Color.black);
        g.setFont(new Font("MV Boli", Font.PLAIN, getWidth() / 57));
        if (gameAlgo.getInfo() != null) {
            String info = "";
            info += "Grade: " + gameAlgo.getInfo().getGrade();
            if (gameAlgo.isRunning())
                info += " | Time: " + gameAlgo.getGame().timeToEnd() / 1000;
            info += " | Moves: " + gameAlgo.getInfo().getMoves();
            g.drawString(info, getWidth() / 7, 60);

            g.drawString("Level: " + gameAlgo.getInfo().getGameLevel() + " | Pokemons: " + gameAlgo.getInfo().getPokemons() + " | Agents: "
                    + gameAlgo.getInfo().getAgents(), (getWidth() / 5) * 3, 60);

            if (!gameAlgo.getGame().isRunning()) {
                g.setFont(new Font("MV Boli", Font.PLAIN, 170));
                g.setColor(Color.red);
                if (gameAlgo.getInfo() != null)
                    g.drawString("Game Over!", (getWidth() / 2) - 475, (getHeight() / 2) + 45);


            }
        }

    }


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

    //we need to fix that
    private void initNewGame(int num,int key) {
        String[] newGame = new String[2];
        id=String.valueOf(key);
        newGame[0] = "" + num;
        newGame[1] = id;
        Ex2.main(newGame);


    }

}