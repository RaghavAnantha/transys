package com.transys.core.report;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;

import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import org.apache.commons.lang3.StringUtils;

import net.sf.jasperreports.engine.JRAbstractRenderer;
import net.sf.jasperreports.engine.JRException;

import net.sf.jasperreports.engine.type.ImageTypeEnum;
import net.sf.jasperreports.engine.type.RenderableTypeEnum;

@SuppressWarnings("serial")
public class WaterMarkRenderer extends JRAbstractRenderer {
	public static String PAGE_ORIENTATION_PORTRAIT = "PORTRAIT";
	public static String PAGE_ORIENTATION_LANDSCAPE = "LANDSCAPE";
	
	private boolean display = false;
	private boolean debug = false;
	
	private String pageOrientaion = PAGE_ORIENTATION_PORTRAIT;
	
	private String displayText = StringUtils.EMPTY;

	public WaterMarkRenderer(boolean display) {
		this.display = display;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getPageOrientaion() {
		return pageOrientaion;
	}

	public void setPageOrientaion(String pageOrientaion) {
		this.pageOrientaion = pageOrientaion;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	@Override
	public byte getType() {
		// No idea what this does
		return RenderableTypeEnum.SVG.getValue();
	}

	@Override
	public byte getImageType() {
		// No idea what this does
		return ImageTypeEnum.UNKNOWN.getValue();
	}

	@Override
	public Dimension2D getDimension() throws JRException {
		// This seems to override whatever is configured in Jasperreports studio
		if (StringUtils.equals(PAGE_ORIENTATION_LANDSCAPE, getPageOrientaion())) {
			// Landscape in pixel: 1390x595
			return new Dimension(1390 - (3 * 40), 490);
		} else {
			// A4 in pixel: 595x842
			return new Dimension(595 - (2 * 40), 700);
		}
	}

	@Override
	public byte[] getImageData() throws JRException {
		// No idea what this does
		return new byte[0];
	}

	@Override
	public void render(Graphics2D g2, Rectangle2D rectangle) throws JRException {
		if (!isDisplay()) {
			return;
		}
		
		AffineTransform originalTransform = g2.getTransform();
		if (isDebug()) {
			// Just for debugging
			g2.setColor(Color.BLUE);
			g2.draw(rectangle);
		}

		g2.translate(rectangle.getX() + 100, rectangle.getMaxY());
		g2.rotate(-55 * Math.PI / 180);

		String text = getDisplayText();
		Font font = new Font("Arial", Font.PLAIN, 120);
		Shape shape = font.createGlyphVector(g2.getFontRenderContext(), text).getOutline();
		
		g2.setColor(new Color(255, 0, 0, 100));
		g2.setStroke(new BasicStroke(1));
		g2.draw(shape);

		g2.setTransform(originalTransform);
	}
}