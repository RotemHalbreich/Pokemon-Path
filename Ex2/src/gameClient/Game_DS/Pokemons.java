package gameClient.Game_DS;

import api.edge_data;
import com.google.gson.JsonObject;
import gameClient.util.Point3D;

import static java.lang.Double.parseDouble;

public class Pokemons {


    private class Pokemon {
        private double value;
        private int type;
        private Point3D pos;
        private edge_data edge;

       public Pokemon(JsonObject json){update(json);}
       private void update(JsonObject json){
        value=json.get("value").getAsDouble();
        type=json.get("type").getAsInt();
        double[] c=simplifyPos(json.get("pos").getAsString());
        pos=new Point3D(c[0],c[1],c[2]);
        //edge-> we need think about that
       }
       private double[] simplifyPos(String s){
        double[] ans= new double[3];
        String[] as=s.split(",");
        ans[0]=parseDouble(as[0]);
        ans[1]=parseDouble(as[1]);
        ans[2]=parseDouble(as[2]);
        return ans;
       }

     public double getValue() {return value;}

     public int getType() {return type;}

     public Point3D getPos() {return pos;}

     public edge_data getEdge() {return edge;}

     public String toString(){
        return "Pokemon:{ val:"+getValue()+",type:"+getType()+",pos:"+getPos().toString();
       }
    }

}
