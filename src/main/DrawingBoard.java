package main;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import objects.*;

public class DrawingBoard extends JPanel {

	private MouseAdapter mouseAdapter; 
	private List<GObject> gObjects;
	private GObject target;
	
	private int gridSize = 10;
	
	public DrawingBoard() {
		gObjects = new ArrayList<GObject>();
		mouseAdapter = new MAdapter();
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setPreferredSize(new Dimension(800, 600));
	}
	
	public void addGObject(GObject gObject) {
		gObjects.add(gObject);
		repaint();
	}
	
	public void groupAll() {
		CompositeGObject compo = new CompositeGObject();

		for(GObject o : gObjects){
			compo.add(o);
		}

		compo.recalculateRegion();
		gObjects.clear();
		gObjects.add(compo);
		repaint();
	}

	public void deleteSelected() {
		gObjects.remove(target);
		repaint();
	}
	
	public void clear() {
		gObjects.clear();
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintBackground(g);
		paintGrids(g);
		paintObjects(g);
	}

	private void paintBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	private void paintGrids(Graphics g) {
		g.setColor(Color.lightGray);
		int gridCountX = getWidth() / gridSize;
		int gridCountY = getHeight() / gridSize;
		for (int i = 0; i < gridCountX; i++) {
			g.drawLine(gridSize * i, 0, gridSize * i, getHeight());
		}
		for (int i = 0; i < gridCountY; i++) {
			g.drawLine(0, gridSize * i, getWidth(), gridSize * i);
		}
	}

	private void paintObjects(Graphics g) {
		for (GObject go : gObjects) {
			go.paint(g);
		}
	}

	class MAdapter extends MouseAdapter {

		private int x;
		private int y;

		private void deselectAll() {
			for(GObject o : gObjects){
				o.deselected();
				repaint();
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			boolean se = false;
			x = e.getX();
			y = e.getY();

			for(GObject o : gObjects){
				if(o.pointerHit(x, y)){
					target = o;
					target.selected();
					se = true;
					repaint();
				}
			}

			if(!se) deselectAll();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int lastX, lastY;
			if(target.pointerHit(e.getX(), e.getY())){
				lastX = e.getX();
				lastY = e.getY();
				target.move(lastX - x, lastY - y);
				x = lastX;
				y = lastY;

				repaint();
			}
		}
	}
	
}