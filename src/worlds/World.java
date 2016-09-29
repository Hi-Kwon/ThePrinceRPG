package worlds;

import java.awt.Graphics;

import entities.EntityManager;
import entities.Player;
import gameStates.AtlantisState;
import thePrinceGame.Handler;
import thePrinceMain.Game;
import tile.Tile;
import utils.Utils;

public class World {

	private Handler handler;
	private int width, height;
	private int spawnX,spawnY;
	private int[][] tiles;
	
	//Entities
	private EntityManager entityManager;
	
	public World(Handler handler, String path){
		this.handler = handler;
		spawnX = (int) (Tile.TILEWIDTH * handler.getData().getX_coord());
		spawnY = (int) (Tile.TILEHEIGHT * handler.getData().getY_coord() -1);
		entityManager = new EntityManager(handler, new Player(handler, spawnX, spawnY));
		
		loadWorld(path);
		
	}
	
	public void tick(){
		
		entityManager.tick();
	}
	
	public void render(Graphics g){
		int xStart = (int) Math.max(0,handler.getGameCamera().getxOffset() / Tile.TILEWIDTH);
		int xEnd = (int) Math.min(width, (handler.getGameCamera().getxOffset() + handler.getWidth()) / Tile.TILEWIDTH + 1);
		int yStart = (int) Math.max(0, handler.getGameCamera().getyOffset() / Tile.TILEHEIGHT);
		int yEnd = (int) Math.min(height, (handler.getGameCamera().getyOffset() + handler.getHeight()) / Tile.TILEHEIGHT + 1);
		
		for(int y = yStart; y < yEnd; y++){
			for(int x = xStart; x < xEnd; x++){
				getTile(x,y).render(g, (int) (x*Tile.TILEWIDTH - handler.getGameCamera().getxOffset()), 
						(int) (y*Tile.TILEHEIGHT - handler.getGameCamera().getyOffset()));
			}
		}
		//Render Entities
		entityManager.render(g);
	}
	
	public Tile getTile(int x, int y){
		if (x < 0 || y < 0 || x > width || y >= height)
			return Tile.deepSeaTile;
		
		Tile t = Tile.tiles[tiles[x][y]];
		if(t == null)
			return Tile.grassTile;
		return t;
	}
	
	private void loadWorld(String path){
		String file = Utils.loadFileAsString(path);
		String[] tokens = file.split("\\s+");
		width = Utils.parseInt(tokens[0]);
		height = Utils.parseInt(tokens[1]);
		
		tiles = new int[width][height];
		for (int y = 0; y < height; y++){
			for (int x = 0; x < width; x++){
				
				tiles[x][y] = Utils.parseInt(tokens[(x + y * width)+2]);
			}
		}
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}