package gameClient.Game_DS;

import javax.swing.*;
import api.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Iterator;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;

public class GameGui2 extends JFrame  {
    private GameAlgo gameAlgo;
    private directed_weighted_graph graph;
    private double MinX,MinY,MaxX,MaxY;

    public GameGui2(){
        super();
        setSize(1000,700);
        setFrameSize();
    }
    public void update(GameAlgo g){
        this.gameAlgo=g;
        this.graph=gameAlgo.getGraph();
        resizeFrame();
    }

    public void paint(Graphics g){
        if(graph==null)super.paint(g);

        g.clearRect(0,0,getWidth(),getHeight());
        drawGraph(g);
        drawPokemos(g);
        drawAgents(g);
        drawInfo(g);


    }
//// we need to build that actually rotem need build that ////
    private void drawInfo(Graphics g) {

    }





    private void drawPokemos(Graphics g) {
        if(gameAlgo.getPokemons()==null)return;
        Iterator<Pokemon> itr=gameAlgo.getPokemons().iterator();
        while (itr.hasNext()){
            Pokemon pokemon= itr.next();
            if(pokemon.getLocation()==null)continue;
            int x=ratioX(pokemon.getLocation().x()),
                y=ratioY(pokemon.getLocation().y());
            g.setColor(Color.green);
            if(pokemon.getType()<0)g.setColor(Color.ORANGE);
            g.fillOval(x-10, y-10, 20, 20);
            g.drawString(""+pokemon.getValue(),x,y);

        }
    }









    ///////// Private Methods //////////
    private void setFrameSize() {
        MinX=MAX_VALUE;
        MinY=MAX_VALUE;
        MaxX=MIN_VALUE;
        MaxY=MIN_VALUE;
    }
    // we need to fix the duplicate
    private void resizeFrame() {
        if(graph==null)return;
        for(node_data n: graph.getV()){
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
        if(graph==null)return;
        for(node_data n:graph.getV()){
            drawNode(g,n);
            for(edge_data e:graph.getE(n.getKey())){
                drawEdge(g,e);
            }
        }

    }
    private void drawNode(Graphics g,node_data n) {
        int x=ratioX(n.getLocation().x());
        int y=ratioY(n.getLocation().y());
        g.setColor(Color.BLACK);
        g.fillOval(x-5,y-5,10,10);
        g.drawString(""+n.getKey(),x,y+10);
    }

    private void drawEdge(Graphics g, edge_data e) {
        node_data src=graph.getNode(e.getSrc()),
                dest=graph.getNode(e.getDest());
        double w=(int)e.getWeight();
        int x=ratioX(src.getLocation().x()),x1=x,
            y=ratioY(dest.getLocation().y()),y1=y;

        g.setColor(Color.BLACK);
        g.drawLine(x,y,x1,y1);
        g.drawString(""+w,(x + 4 * x1) / 5, (y + 4 * y1) / 5);
    }
    private void drawAgents(Graphics g) {
        if (gameAlgo.getAgents() == null) return;
        Iterator<Agent> itr = gameAlgo.getAgents().iterator();
        while (itr.hasNext()) {
            Agent agent = itr.next();
            if (agent == null) continue;
            g.setColor(Color.RED);
            int x = ratioX(agent.getPos().x()),
                    y = ratioY(agent.getPos().y());
            g.fillOval( x - 8,  y - 8, 16, 16);
            g.drawString("id:" + agent.getId() + "s:" + agent.getSpeed(), x, y);
        }
    }
}
