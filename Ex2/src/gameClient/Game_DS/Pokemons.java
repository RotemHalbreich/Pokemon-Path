package gameClient.Game_DS;

import api.edge_data;
import api.game_service;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import gameClient.util.Point3D;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Double.parseDouble;

public class Pokemons {
  private game_service game;
  private Pokemon[] pokemons;

  public Pokemons(game_service game) throws JSONException {
    this.game=game;
    update();
  }
  public synchronized void update() throws JSONException {

   if(pokemons==null||pokemons.length!=size())
    pokemons=new Pokemon[size()];

   JSONObject jsonPokemons=new JSONObject(game.getPokemons());
   int y=0;


  }
  public int size() throws JSONException {
   JSONObject g=new JSONObject(game.toString());
   JSONObject p=g.getJSONObject("GameServer");
   return p.getInt("pokemons");
  }
    private class Pokemon implements Comparable<Pokemon>{
        private double value;
        private int type;
        private Point3D pos;
        private edge_data edge;

       public Pokemon(JSONObject json) throws JSONException {update(json);}
       private void update(JSONObject json) throws JSONException {
        value=json.getDouble("value");
        type=json.getInt("type");
        double[] c=simplifyPos(json.getString("pos"));
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

     @Override
     public int compareTo(@NotNull Pokemon o) {
      Double v=getValue()*-1;
        return v.compareTo(o.value);
     }
    }

}
