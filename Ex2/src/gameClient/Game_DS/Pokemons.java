package gameClient.Game_DS;

import api.*;
import org.json.*;
import gameClient.util.*;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.Iterator;


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
   JSONArray arrayPokemons=jsonPokemons.getJSONArray("Pokemons");

   for(int i=0;i<size();i++){
       JSONObject jsonPokemon= arrayPokemons.getJSONObject(i).getJSONObject("Pokemon");
       if(pokemons[i]==null)
           pokemons[i]=new Pokemon(jsonPokemon);
       else
           pokemons[i].update(jsonPokemon);
   }
      Arrays.sort(pokemons);

  }
  public int size() throws JSONException {
   JSONObject g=new JSONObject(game.toString());
   JSONObject p=g.getJSONObject("GameServer");
   return p.getInt("pokemons");
  }
  public Iterator<Pokemon> iterator(){
      return Arrays.stream(pokemons).iterator();
  }
   public String toString(){
      return Arrays.toString(pokemons);
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

     public Point3D getLocation() {return pos;}

     public edge_data getEdge() {return edge;}

     public String toString(){
        return "Pokemon:{ val:"+getValue()+",type:"+getType()+",pos:"+getLocation().toString();
       }

     @Override
     public int compareTo(@NotNull Pokemon o) {
      Double v=getValue();
        return -v.compareTo(o.value);
     }
    }

}
