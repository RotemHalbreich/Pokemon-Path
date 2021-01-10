package api;

import java.io.FileNotFoundException;
import java.util.Date;

public class main_connencted {
    public static void main(String[] args) throws FileNotFoundException {
        DWGraph_Algo g=new DWGraph_Algo();
        System.out.println("####### Our Java #######");
        System.out.println("<---------------------------------------------------------->");
        long sAll = new Date().getTime();
        g.load("data/G_10_80_0.json");
        System.out.println("Our Java:\nG_10_80_0 coordinates: (0,8)");
        long start = new Date().getTime();
        System.out.println("Connected components: "+g.connectedComponents().size());
        System.out.println("Connected components runtime:"+(new Date().getTime()-start)/1000.0);
        start = new Date().getTime();
        System.out.println("Shortest path route: "+g.shortestPath(0,8));
        System.out.println("Shortest path distance: "+g.shortestPathDist(0,8));
        System.out.println("Shortest path runtime:"+(new Date().getTime()-start)/1000.0);
        start = new Date().getTime();
        System.out.println("Connected component with vertex 0: "+ g.connectedComponent(0).size());
        System.out.println("Connected component runtime:"+(new Date().getTime()-start)/1000.0);

        System.out.println("<---------------------------------------------------------->");
        g.load("data/G_100_800_0.json");
        System.out.println("Our Java:\nG_100_800_0 coordinates: (12,95)");
        start = new Date().getTime();
        System.out.println("Connected components: "+g.connectedComponents().size());
        System.out.println("Connected components runtime:"+(new Date().getTime()-start)/1000.0);
        start = new Date().getTime();
        System.out.println("Shortest path route: "+g.shortestPath(12,95));
        System.out.println("Shortest path distance: "+g.shortestPathDist(12,95));
        System.out.println("Shortest path runtime:"+(new Date().getTime()-start)/1000.0);
        start = new Date().getTime();
        System.out.println("Connected component with vertex 0: "+ g.connectedComponent(0).size());
        System.out.println("Connected component runtime:"+(new Date().getTime()-start)/1000.0);

        System.out.println("<---------------------------------------------------------->");
        g.load("data/G_1000_8000_0.json");
        System.out.println("Our Java:\nG_1000_8000_0 coordinates: (10,850)");
        start = new Date().getTime();
        System.out.println("Connected components: "+g.connectedComponents().size());
        System.out.println("Connected components runtime:"+(new Date().getTime()-start)/1000.0);
        start = new Date().getTime();
        System.out.println("Shortest path route: "+g.shortestPath(10,850));
        System.out.println("Shortest path distance: "+g.shortestPathDist(10,850));
        System.out.println("Shortest path runtime:"+(new Date().getTime()-start)/1000.0);
        start = new Date().getTime();
        System.out.println("Connected component with vertex 0: "+ g.connectedComponent(0).size());
        System.out.println("Connected component runtime:"+(new Date().getTime()-start)/1000.0);

        System.out.println("<---------------------------------------------------------->");
        g.load("data/G_10000_80000_0.json");
        System.out.println("Our Java:\n/G_10000_80000_0 coordinates: (0,9999)");
        start = new Date().getTime();
        System.out.println("Connected components: "+g.connectedComponents().size());
        System.out.println("Connected components runtime:"+(new Date().getTime()-start)/1000.0);
        start = new Date().getTime();
        System.out.println("Shortest path route: "+g.shortestPath(0,9999));
        System.out.println("Shortest path distance: "+g.shortestPathDist(0,9999));
        System.out.println("Shortest path runtime:"+(new Date().getTime()-start)/1000.0);
        start = new Date().getTime();
        System.out.println("Connected component with vertex 0: "+ g.connectedComponent(0).size());
        System.out.println("Connected component runtime:"+(new Date().getTime()-start)/1000.0);

        System.out.println("<---------------------------------------------------------->");
        g.load("data/G_20000_160000_0.json");
        System.out.println("Our Java:\nG_20000_160000_0 coordinates: (0,19999)");
        start = new Date().getTime();
        System.out.println("Connected components: "+g.connectedComponents().size());
        System.out.println("Connected components runtime:"+(new Date().getTime()-start)/1000.0);
        start = new Date().getTime();
        System.out.println("Shortest path route: "+g.shortestPath(0,19999));
        System.out.println("Shortest path distance: "+g.shortestPathDist(0,19999));
        System.out.println("Shortest path runtime:"+(new Date().getTime()-start)/1000.0);
        start = new Date().getTime();
        System.out.println("Connected component with vertex 0: "+ g.connectedComponent(0).size());
        System.out.println("Connected component runtime:"+(new Date().getTime()-start)/1000.0);

        System.out.println("<---------------------------------------------------------->");
        g.load("data/G_30000_240000_0.json");
        System.out.println("Our Java:\nG_30000_240000_0 coordinates: (0,5000)");
        start = new Date().getTime();
        System.out.println("Connected components: "+g.connectedComponents().size());
        System.out.println("Connected components runtime:"+(new Date().getTime()-start)/1000.0);
        start = new Date().getTime();
        System.out.println("Shortest path route: "+g.shortestPath(0,5000));
        System.out.println("Shortest path distance: "+g.shortestPathDist(0,5000));
        System.out.println("Shortest path runtime:"+(new Date().getTime()-start)/1000.0);
        start = new Date().getTime();
        System.out.println("Connected component with vertex 0: "+ g.connectedComponent(0).size());
        System.out.println("Connected component runtime:"+(new Date().getTime()-start)/1000.0);
        System.out.println("<---------------------------------------------------------->");
        System.out.println();
        System.out.println("Our Java implementation: Total time "+(new Date().getTime()-sAll)/1000.0);
    }
}
