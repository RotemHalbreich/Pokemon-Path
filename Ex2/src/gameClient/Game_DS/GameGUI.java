package gameClient.Game_DS;


import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;

@SuppressWarnings("serial")
public class GameGUI extends JFrame implements ActionListener, MouseListener {

    private game_service game;
    private DWGraph_Algo algo = new DWGraph_Algo();
    private Information info;
    private Pokemons pokemons;
    private Agents agents;


    private final int NODE_SIZE = 10; // need to be even
    private final int ARROW_SIZE = NODE_SIZE - 2;


    private double minX = Double.MAX_VALUE;
    private double maxX = Double.MIN_VALUE;
    private double minY = Double.MAX_VALUE;
    private double maxY = Double.MIN_VALUE;

    private double EPS = 0.00001;

    public GameGUI(game_service game) {
        this.game = game;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 700);
        addMouseListener(this);
        //setPokemons();
        setVisible(true);
        initGame();
    }

    private void initGame() {
        initGraph();

    }

    private void initGraph() {
        directed_weighted_graph graph = algo.readFromJson(game.getGraph());
        algo.init(graph);
        refactorMinMaxXY();
        repaint();
    }

    private void startAutoGame() throws JSONException {
        GameAlgo auto = new GameAlgo(game, algo, pokemons, agents);
        game.startGame();
        auto.start();
    }

    private void refactorMinMaxXY() {
        if (algo == null || algo.getGraph() == null) return;
        for (node_data node : algo.getGraph().getV()) {
            if (node.getLocation().x() < minX)
                minX = node.getLocation().x();

            if (node.getLocation().x() > maxX)
                maxX = node.getLocation().x();

            if (node.getLocation().y() < minY)
                minY = node.getLocation().y();

            if (node.getLocation().y() > maxY)
                maxY = node.getLocation().y();
        }
    }

    /**
     * @param x the x coordinate to convert
     * @return the location on the screen that fit that x
     */
    private int scaleX(double x) {
        if (x > maxX || x < minX)
            refactorMinMaxXY();
        return (int) ((x - minX) / (maxX - minX) * (getWidth() - 50) + 25);
    }

    /**
     * @param y the y coordinate to convert
     * @return the location on the screen that fit that y
     */
    private int scaleY(double y) {
        if (y > maxY || y < minY)
            refactorMinMaxXY();
        return getHeight() - (int) ((y - minY) / (maxY - minY) * (getHeight() - 100) + 30);
    }

    /**
     * update the frame according to the current location of the game objects
     */
    @Override
    public void paint(Graphics g) {

        if (algo == null || algo.getGraph() == null) {
            super.paint(g);
            return;
        }

        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setFont(new Font("Courier", Font.PLAIN, 20));
        super.paintComponents(g2d);

        drawGraph(g2d);

//        drawPokemons(g2d);
//
//        drawAgents(g2d);


        Graphics2D orgGraphic = (Graphics2D) g;
        orgGraphic.drawImage(bufferedImage, null, 0, 0);

    }


//    private void drawAgents(Graphics g) {
//        if (agents == null) return;
//
//
//        for (Agent agent : agents) {
//            if (agent != null) {
//                g.setColor(Color.green);
//                int x = scaleX(agent.getPos().x()) - IMAGE_SIZE / 2;
//                int y = scaleY(agent.getPos().y()) - IMAGE_SIZE / 2;
//                g.drawImage(this.robotIMG, x, y, IMAGE_SIZE, IMAGE_SIZE, null);
//                g.drawString("" + agent.getSpeed(), x, y);
//
//                if (agent.getDest() == -1) {
//                    g.setColor(Color.DARK_GRAY);
//                    g.drawOval(x - IMAGE_SIZE, y - IMAGE_SIZE, 3 * IMAGE_SIZE, 3 * IMAGE_SIZE);
//                }
//            }
//        }
//    }
//
//    private void drawPokemons(Graphics g) {
//        if (pokemons == null) return;
//
//        g.setColor(Color.blue);
//        for (Pokemon pokemon : pokemons) {
//
//            int x = scaleX(pokemon.getLocation().x()) - IMAGE_SIZE / 2;
//            int y = scaleY(pokemon.getLocation().y()) - IMAGE_SIZE / 2;
//            if (pokemon.getType() < 0)
//
//                g.drawImage(bananaIMG, x, y, IMAGE_SIZE, IMAGE_SIZE, null);
//            else
//                g.drawImage(appleIMG, x, y, IMAGE_SIZE, IMAGE_SIZE, null);
//
//            g.drawString(pokemon.getValue() + "", x, y);
//        }
//    }

    /**
     * Draw the graph vertexes and edges in their current place on the screen
     */
    private void drawGraph(Graphics2D g2d) {
        if (algo == null || algo.getGraph() == null)
            return;

        for (node_data n : algo.getGraph().getV()) {
            int x = scaleX(n.getLocation().x());
            int y = scaleY(n.getLocation().y());
            g2d.setColor(Color.black);
            g2d.fillOval(x - NODE_SIZE / 2, y - NODE_SIZE / 2, NODE_SIZE, NODE_SIZE);
            g2d.drawString("" + n.getKey(), x, y + 15);

            for (edge_data e : algo.getGraph().getE(n.getKey())) {
                drawEdge(g2d, e);
            }
        }
    }

    /**
     * Draw a single edge
     */
    private void drawEdge(Graphics g, edge_data e) {
        node_data src = algo.getGraph().getNode(e.getSrc());
        node_data dest = algo.getGraph().getNode(e.getDest());
        int x1 = scaleX(src.getLocation().x());
        int y1 = scaleY(src.getLocation().y());
        int x2 = scaleX(dest.getLocation().x());
        int y2 = scaleY(dest.getLocation().y());

        g.setColor(Color.DARK_GRAY);
        g.drawLine(x1, y1, x2, y2);
        double w = (int) (e.getWeight() * 10);
        w /= 10;
        g.drawString("" + w, (x1 + 4 * x2) / 5, (y1 + 4 * y2) / 5);

        g.setColor(Color.yellow);
        g.fillRect(((x1 + 7 * x2) / 8 - ARROW_SIZE / 2), ((y1 + 7 * y2) / 8 - ARROW_SIZE / 2), ARROW_SIZE, ARROW_SIZE);
    }


    // we need to complete this
    private void setPokemons() {
    }


    @Override
    public void actionPerformed(ActionEvent e) {

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
}