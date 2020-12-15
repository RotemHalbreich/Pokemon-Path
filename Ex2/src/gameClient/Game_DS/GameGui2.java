package gameClient.Game_DS;

import javax.swing.*;

import api.*;
import gameClient.Ex2;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;
import static java.lang.Integer.parseInt;

public class GameGui2 extends JFrame implements MouseListener, ActionListener {
    private GameAlgo gameAlgo;
    private directed_weighted_graph graph;
    private double MinX, MinY, MaxX, MaxY;
    private final String id="305496614";


    public GameGui2() {
        super();

        setTitle("Pokemon");
        setSize(1280, 700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setFrameSize();
        menuBar();
        addMouseListener(this);


    }

    public void update(GameAlgo g) {
        this.gameAlgo = g;
        this.graph = gameAlgo.getGraph();
        resizeFrame();


    }

    public void paint(Graphics g2) {
        if (graph == null) super.paint(g2);
        BufferedImage bufferedImage =
                new BufferedImage(
                 getWidth(),
                 getHeight(),
                 BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = bufferedImage.createGraphics();
        super.paintComponents(g);

        g.setBackground(Color.white);

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
            boolean with = true;
            while (with) {
                select = JOptionPane.showInputDialog("Please choose level number (0-23):  ", "0");
                int num = -1;
                try {
                    num = parseInt(select);
                } catch (Exception ex) {
                    continue;
                }
                if (num < 23 || num > 0) {
                    initNewGame(num);
                    with = false;
                }
            }
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

 // we need to complete this function
    private void drawInfo(Graphics g) {
        if (gameAlgo == null) return;
        g.setColor(Color.black);
        g.setFont(new Font("MV Boli", Font.PLAIN, getWidth() /57));
        if (gameAlgo.getInfo() != null) {
            String info = "";
            info += "Grade: " + gameAlgo.getInfo().getGrade();
            if (gameAlgo.isRunning())
                info += " | Time: " + gameAlgo.getGame().timeToEnd() / 1000;
            info += " | Moves: " + gameAlgo.getInfo().getMoves();
            g.drawString(info, getWidth() / 4, 50);

            g.drawString("Level: " + gameAlgo.getInfo().getGameLevel() + " | Pokemons: " + gameAlgo.getInfo().getPokemons() + " | Agents: "
                    + gameAlgo.getInfo().getAgents(), (getWidth() / 3) * 2, 50);

           if(!gameAlgo.getGame().isRunning()){
               g.setFont(new Font("Courier", Font.PLAIN, 100));
               g.setColor(Color.red);
               if (gameAlgo.getInfo() != null)
                   g.drawString("Game Over!",getWidth()/2-300, getHeight()/2);


           }
        }

    }


    ///////// Private Methods //////////
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

    private void drawGraph(Graphics g) {
        if (graph == null) return;
        for (node_data n : graph.getV()) {
            drawNode(g, n);
            for (edge_data e : graph.getE(n.getKey())) {
                drawEdge(g, e);
            }
        }


    }

    private void drawNode(Graphics g, node_data n) {
        int x = ratioX(n.getLocation().x());
        int y = ratioY(n.getLocation().y());
        g.setColor(Color.blue);
        g.fillOval(x - 5, y - 5, 12, 12);
        g.drawString("  " + n.getKey(), x, y + 10);
    }

    private void drawEdge(Graphics g, edge_data e) {
        node_data src = graph.getNode(e.getSrc()),
                dest = graph.getNode(e.getDest());
        double w = (int) e.getWeight();
        int x = ratioX(src.getLocation().x()),
                x1 = ratioX(dest.getLocation().x()),
                y = ratioY(src.getLocation().y()),
                y1 = ratioY(dest.getLocation().y());

        g.setColor(Color.black);
        g.setFont(new Font("MV Boli", Font.BOLD, 12));
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
            g.fillArc(x - 8, y - 8, 28, 28, 300, 300);
            g.drawString("   ID: " + agent.getId() + " S: " + agent.getSpeed(), x, y);
        }
    }

    private void drawPokemos(Graphics g) {
        if (gameAlgo == null || gameAlgo.getPokemons() == null) return;
        Iterator<Pokemon> itr = gameAlgo.getPokemons().iterator();
        while (itr.hasNext()) {
            Pokemon pokemon = itr.next();
            if (pokemon.getLocation() == null) continue;
            int x = ratioX(pokemon.getLocation().x()),
                    y = ratioY(pokemon.getLocation().y());
            g.setColor(Color.green);
            if (pokemon.getType() < 0) g.setColor(Color.ORANGE);
            g.fill3DRect(x - 6, y - 6, 16, 16, true);
            //g.fillOval(x - 6, y - 6, 16, 16);
            g.drawString("  " + pokemon.getValue(), x, y);

        }
    }

    private void menuBar() {
        JMenu menu = new JMenu("Menu");
        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(this);
        newGame.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menu.add(newGame);
        JMenuItem stop = new JMenuItem("Stop Game!");
        stop.addActionListener(this);
        menu.add(stop);

        JMenuBar fatherMenu = new JMenuBar();
        fatherMenu.add(menu);
        setJMenuBar(fatherMenu);

    }

    //we need to fix that
    private void initNewGame(int num) {
        String[] newGame = new String[2];
        newGame[0] = "" + num;
        newGame[1]=id;
        Ex2.main(newGame);


    }

}