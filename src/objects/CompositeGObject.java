package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class CompositeGObject extends GObject {

	private List<GObject> gObjects;

	public CompositeGObject() {
		super(0, 0, 0, 0);
		gObjects = new ArrayList<GObject>();
	}

	public void add(GObject gObject) {
		gObjects.add(gObject);
	}

	public void remove(GObject gObject) {
		gObjects.remove(gObject);
	}

	@Override
	public void move(int dX, int dY) {
		for(GObject o : gObjects){
			o.move(dX, dY);
		}

		x += dX;
		y += dY;
	}
	
	public void recalculateRegion() {
		int x = gObjects.get(0).x;
		int y = gObjects.get(0).y;
		int width = gObjects.get(0).x + gObjects.get(0).width;
		int height = gObjects.get(0).y + gObjects.get(0).height;

		for(GObject o : gObjects){
			if(o.x < x){
				x = o.x;
			}
			if(o.x + o.width > width){
				width = o.x + o.width;
			}
			if(o.y < y){
				y = o.y;
			}
			if(o.y + o.height > height){
				height = o.y + o.height;
			}
		}

		this.x = x;
		this.y = y;
		this.width = width - x;
		this.height = height - y;
	}

	@Override
	public void paintObject(Graphics g) {
		for(GObject o : gObjects){
			o.paintObject(g);
		}
	}

	@Override
	public void paintLabel(Graphics g) {
		for(GObject o : gObjects){
			o.paintLabel(g);
		}

		g.drawString("Group", x, y+height+10);
	}
	
}
