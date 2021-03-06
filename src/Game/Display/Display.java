/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game.Display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author vv
 */
public class Display
{
    private final static int CLEAR_COLOR = 0xff2f7828;
    public final static int TRANSPARENT_COLOR = 0xff150f0f;
    
    private int width, height;
    public int[] pixels;
    private Graphics g;
    
    public Display(int width, int height)
    {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }
    
    public boolean onScreen(int x, int y, int width, int height)
    {
        if(x >= Game.Game.mainLevel.xScroll && x - width <= Game.Game.mainLevel.xScroll + Game.Game.display.getWidth())
            if(y >= Game.Game.mainLevel.yScroll && y - height <= Game.Game.mainLevel.yScroll + Game.Game.display.getHeight())
                return true;
        return false;
    }
    
    public Graphics getGraphics()
    {
        return g;
    }
    
    public void setGraphics(Graphics g)
    {
        this.g = g;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public void clear()
    {
        for(int i = 0; i < pixels.length; i++)
            pixels[i] = CLEAR_COLOR; 
    }
    /**
     * Renders a sprite to the screen
     * <p>
     * @param x 
     * @param y 
     * @param sprite 
     */
    public void renderSprite(int x, int y, Sprite sprite)
    {
        for(int yy = y + Game.Game.mainLevel.yScroll, j = 0; j < sprite.getHeight(); yy++, j++)
        {
            if(yy < 0 || yy >= height)continue;
            for(int xx = x + Game.Game.mainLevel.xScroll, i = 0; i < sprite.getWidth(); xx++, i++)
            {
                if(xx < 0 || xx >= width)continue;
                if(sprite.pixels[i + j * sprite.getWidth()] == TRANSPARENT_COLOR)continue;
                pixels[xx + yy * width] = sprite.pixels[i + j * sprite.getWidth()];
            }
        }
    }
    
    public void renderFixedSprite(int x, int y, Sprite sprite)
    {
        for(int yy = y, j = 0; j < sprite.getHeight(); yy++, j++)
        {
            if(yy < 0 || yy >= height)continue;
            for(int xx = x, i = 0; i < sprite.getWidth(); xx++, i++)
            {
                if(xx < 0 || xx >= width)continue;
                if(sprite.pixels[i + j * sprite.getWidth()] == TRANSPARENT_COLOR)continue;
                pixels[xx + yy * width] = sprite.pixels[i + j * sprite.getWidth()];
            }
        }
    }
    
    public void renderFixedBar(int x, int y, int width, int height, int borderSize, int outLineColor, int color1, int percentage)
    {
        //draw outline
        //upper and bottom outline
        for(int yy = y; yy < y + borderSize; yy++)
        {
            if(yy < 0 || yy >= this.height)continue;
            for(int xx = x + borderSize; xx < x + width + borderSize; xx++)
            {
                if(xx < 0 || xx >= this.width)continue;
                pixels[xx + yy * this.width] = outLineColor;
                if(yy + height >= this.height)continue;
                pixels[xx + (yy + height) * this.width] = outLineColor;
            }
        }
        //left and right outline
        for(int xx = x; xx < x + borderSize; xx++)
        {
            if(xx < 0 || xx >= this.width)continue;
            for(int yy = y; yy < y + height + borderSize; yy++)
            {
                if(yy < 0 || yy >= this.height)continue;
                pixels[xx + yy * this.width] = outLineColor;
                if(xx + width + borderSize < 0 || xx + width + borderSize >= this.width)continue;
                pixels[(xx + width + borderSize) + yy * this.width] = outLineColor;
            }
        }
        //draw containment
        for(int yy = y + borderSize; yy < y + height; yy++)
        {
            if(yy < 0 || yy >= this.height)continue;
            for(int xx = x + borderSize; xx < x + width + borderSize; xx++)
            {
                if(xx < 0 || xx >= this.width)continue;
                if(xx - x - borderSize >= Math.round((width * percentage) / 100))
                {
                    pixels[xx + yy * this.width] = outLineColor;
                    continue;
                }
                pixels[xx + yy * this.width] = color1;
            }
        }
    }
    
    public void renderFixedText(int x, int y, String font, String text, int style, int size, Color color)
    {
        Font f = new Font(font, style, size);
        g.setColor(color);
        g.setFont(f);
        g.drawString(text, x, y + size);
    }
    
    public void renderText(int x, int y, String font, String text, int style, int size, Color color)
    {
        int xx = x + Game.Game.mainLevel.xScroll;
        int yy = y + Game.Game.mainLevel.yScroll;
        Font f = new Font(font, style, size);
        g.setColor(color);
        g.setFont(f);
        g.drawString(text, xx, yy + size);
    }
    
    public void renderBox(int x, int y, int width, int height, int borderSize, int borderColor, int insideColor)
    {
        x += Game.Game.mainLevel.xScroll;
        y += Game.Game.mainLevel.yScroll;
        //render outline
        //render lines
        for(int yy = y; yy < y + borderSize; yy++)
        {
            if(yy < 0 || yy >= this.height)continue;
            for(int xx = x; xx < x + width; xx++)
            {
                if(xx < 0 || xx >= this.width)continue;
                pixels[xx + yy * this.width] = borderColor;
                if(yy + height < 0 || yy + height >= this.height)continue;
                pixels[xx + (yy + height) * this.width] = borderColor;
            }
        }
        //render columns
        for(int xx = x; xx < x + borderSize; xx++)
        {
            if(xx < 0 || xx >= this.width)continue;
            for(int yy = y; yy < y + height; yy++)
            {
                if(yy < 0 || yy >= this.height)continue;
                pixels[xx + yy * this.width] = borderColor;
                if(xx + width < 0 || xx + width >= this.width)continue;
                pixels[(xx + width) + yy * this.width] = borderColor;
            }
        }
        //render inside
        for(int yy = y + borderSize; yy < y + height; yy++)
        {
            if(yy < 0 || yy >= this.height)continue;
            for(int xx = x + borderSize; xx < x + width; xx++)
            {
                if(xx < 0 || xx >= this.width)continue;
                pixels[xx + yy * this.width] = insideColor;
            }
        }
    }
    
    public void renderFixedBox(int x, int y, int width, int height, int borderSize, int borderColor, int insideColor)
    {
        //render outline
        //render lines
        for(int yy = y; yy < y + borderSize; yy++)
        {
            if(yy < 0 || yy >= this.height)continue;
            for(int xx = x; xx < x + width; xx++)
            {
                if(xx < 0 || xx >= this.width)continue;
                pixels[xx + yy * this.width] = borderColor;
                if(yy + height < 0 || yy + height >= this.height)continue;
                pixels[xx + (yy + height) * this.width] = borderColor;
            }
        }
        //render columns
        for(int xx = x; xx < x + borderSize; xx++)
        {
            if(xx < 0 || xx >= this.width)continue;
            for(int yy = y; yy < y + height; yy++)
            {
                if(yy < 0 || yy >= this.height)continue;
                pixels[xx + yy * this.width] = borderColor;
                if(xx + width < 0 || xx + width >= this.width)continue;
                pixels[(xx + width) + yy * this.width] = borderColor;
            }
        }
        //render inside
        for(int yy = y + borderSize; yy < y + height; yy++)
        {
            if(yy < 0 || yy >= this.height)continue;
            for(int xx = x + borderSize; xx < x + width; xx++)
            {
                if(xx < 0 || xx >= this.width)continue;
                pixels[xx + yy * this.width] = insideColor;
            }
        }
    }
}
