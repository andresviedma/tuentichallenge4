package com.andresviedma.tuenticontest2014.challenge16;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;
import com.google.common.hash.PrimitiveSink;


/**
 * Dear Tuenti engineering:
 * 
 * Ouch! I shouldn't have read this challenge... just when I was dreaming on my
 * feet and ready to go to bed, I had the terrible idea of reading it. And...
 * bad idea, really really bad idea, because it's quite easy! Actually, I think it's
 * the easiest challenge!... unless you pretend to process the 8 million points in
 * a reasonable time and there's a hidden trick, but I don't think so (maybe 
 * that's because I'm almost asleep). I'll see it right now. I'm scared. If
 * there's a trick, you are very clever, my friend... hmmm maybe "painting" the
 * points in a grid with the limited 100x100 space, maybe?, and so iterating
 * points in order N -and not N!-, and after that iterating the 100x100 points?...
 * yeeks, I'll better not think on it, it's late!.
 * 
 * I'll try as well to NOT reading the next challenge. Seriously. Promised. Or so.
 * I don't think I have enough time to solve it, anyway...
 * 
 * P.S.: Ooooooooook, I got it! I have and idea, will I correct it in time???
 * 
 * Sincerely,
 * @author andres
 */
public class PointObservations {
    private final static Logger log = LoggerFactory.getLogger(PointObservations.class);
    
    
    public static class Point {
        private int x;
        private int y;
        private int radius;
        
        public Point(int x, int y, int radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }
        
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
        public int getRadius() {
            return radius;
        }
        
        public int getLeft() {
            return y - radius;
        }
        public int getRight() {
            return y + radius;
        }
        public int getTop() {
            return x - radius;
        }
        public int getBottom() {
            return x + radius;
        }

        public boolean collides(Point p) {
            double xDif = this.x - p.x;
            double yDif = this.y - p.y;
            double distanceSquared = (xDif * xDif) + (yDif * yDif);
            boolean collision = distanceSquared < ((this.radius + p.radius) * (this.radius + p.radius));
            return collision;
        }
        
        public Rectangle asRectangle() {
            return new Rectangle(getTop(), getLeft(), 2 * this.radius, 2 * this.radius);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x,y,radius);
        }

        @Override
        public boolean equals(Object obj) {
            if ((obj == null) || !(obj instanceof Point))  return false;
            Point p = (Point) obj;
            return x == p.x && y == p.y && radius == p.radius;
        }
    }
    
    public static class PointCollision {
        public Point p1;
        public Point p2;

        public PointCollision(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public Rectangle asRectangle() {
            return p1.asRectangle().intersection(p2.asRectangle());
        }
        
        @Override
        public int hashCode() {
            return Hashing.goodFastHash(32).hashObject(this, new Funnel<PointCollision>() {

                public void funnel(PointCollision from, PrimitiveSink into) {
                    into
                        .putInt(p1.x + p2.x)
                        .putInt(p1.y + p2.y)
                        .putInt(p1.radius + p2.radius);
                }
                
            }).asInt();
        }

        @Override
        public boolean equals(Object obj) {
            if ((obj == null) || !(obj instanceof PointCollision))  return false;
            PointCollision c = (PointCollision) obj;
            return (c.p1.equals(p1) && c.p2.equals(p2))
                    || (c.p2.equals(p1) && c.p2.equals(p1));
        }
    }
    
    private final static int SPACE_LENGTH = 100000;

    
    public long calculateCollisionDataCount(int from, int size) throws IOException {
        List<Point> data = this.loadPoints(from, size);
        return this.countCollisions(data);
    }

    public long countPartCollisions (List<Point> data, int xBase, int yBase, int length, Set<PointCollision> counted) {
        // Divide in x*x pieces
        int numPieces = 10;
        long count = 0;
        int maxLength = 1000;
        
        if (length <= maxLength) {
            log.debug("Processing quadrant " + xBase + ", " + yBase + " (" + length + "), visited: " + counted.size() + ", mem: " + Runtime.getRuntime().freeMemory());
            
            for (int i=0; i<data.size() -1; i++) {
                Point p1 = data.get(i);
                for (int j=i + 1; j<data.size(); j++) {
                    Point p2 = data.get(j);
                    PointCollision col = new PointCollision(p1, p2);
                    if (p1.collides(p2)) {
                        count++;
                        
                        // If the collision between the points is not fully included in the
                        // quadrant, put it on the map to avoid counting again in other quadant
                        Rectangle colRect = col.asRectangle();
                        if (!colRect.intersection(new Rectangle(xBase, yBase, length, length)).equals(colRect)) {
                            boolean bAdded = counted.add(col);
                            if (!bAdded)  count--;
                        }
                        
                        // If the bottom-right corner of the collision is in this quadrant, remove it from visited
                        // (since we are exploring left to right and top to bottom, it won't be needed anymore)
                        if ((colRect.getMaxX() <= xBase + length) && (colRect.getMaxY() <= yBase + length)) {
                            counted.remove(col);
                        }
                    }
                }
            }
            
        } else {
            List<Point> quadrant = new ArrayList<PointObservations.Point>();
            int size2 = length / numPieces;
            for (int i=0; i<numPieces; i++) {
                int minTop = (i * size2) + xBase;
                int maxTop = (((i+1) * size2) + xBase) - 1;
                for (int j=0; j<numPieces; j++) {
                    int minLeft = (j * size2) + yBase;
                    int maxLeft = (((j+1) * size2) + yBase) - 1;
                    
                    quadrant.clear();
                    for (int k=0; k<data.size(); k++) {
                        Point p = data.get(k);
                        if ((p.getRight() > minLeft) && (p.getBottom() > minTop)
                                && (p.getLeft() < maxLeft) && (p.getTop() < maxTop)) {
                            quadrant.add(p);
                        }
                    }
                    if (quadrant.size() > 1)
                        count += this.countPartCollisions(quadrant, minTop, minLeft, size2, counted);
                }
            }
        }
        return count;
    }
    
    public long countCollisions (List<Point> data) {
        log.info("*** Calculating collisions...");
        long count = this.countPartCollisions(data, 0, 0, SPACE_LENGTH, new HashSet<PointCollision>());
        log.info("* Collisions calculated: " + count);
        return count;
    }
    
    private List<Point> loadPoints(int from, int size) throws IOException {
        log.info("** Loading data...");
        
        File file = new File("src/main/data/challenge16/points");
        InputStream in0 = new FileInputStream(file);
        LineNumberReader in = new LineNumberReader(new InputStreamReader(in0, "UTF-8"));
        
        List<Point> res = new ArrayList<PointObservations.Point>(size);
        int i = 1;
        String line = in.readLine();
        while ((line != null) && (line.length() > 0) && (i - from < size)) {
            if (i % 100000 == 1)  log.info(i + " / " + (from+size-1));
            
            if (i >= from) {
                List<String> pieces = Splitter.on(CharMatcher.BREAKING_WHITESPACE).omitEmptyStrings().splitToList(line.trim());
                Point p = new Point (Integer.parseInt(pieces.get(0)),
                                     Integer.parseInt(pieces.get(1)),
                                     Integer.parseInt(pieces.get(2)));
                res.add(p);
            }
            
            i++;
            line = in.readLine();
        }
        in.close();
        
        assert res.size() == size;
        
        log.info("* Data loaded ok, points: " + res.size());
        return res;
    }
}
