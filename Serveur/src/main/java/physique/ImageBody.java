package physique; // Assurez-vous que le package correspond

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import physique.framework.SimulationBody;
public class ImageBody extends SimulationBody {
    /** The image to use, if required */
    public BufferedImage image;
    
    /* (non-Javadoc)
     * @see org.dyn4j.samples.SimulationBody#renderFixture(java.awt.Graphics2D, double, org.dyn4j.dynamics.BodyFixture, java.awt.Color)
     */
    @Override
    protected void renderFixture(Graphics2D g, double scale, BodyFixture fixture, Color color) {
        // do we need to render an image?
        if (this.image != null) {
            // get the shape on the fixture
            Convex convex = fixture.getShape();
            // check the shape type
            if (convex instanceof Rectangle) {
                Rectangle r = (Rectangle)convex;
                Vector2 c = r.getCenter();
                double w = r.getWidth();
                double h = r.getHeight();
                g.drawImage(this.image, 
                        (int)Math.ceil((c.x - w / 2.0) * scale),
                        (int)Math.ceil((c.y - h / 2.0) * scale),
                        (int)Math.ceil(w * scale),
                        (int)Math.ceil(h * scale),
                        null);
            } else if (convex instanceof Circle) {
                // cast the shape to get the radius
                Circle c = (Circle) convex;
                double r = c.getRadius();
                Vector2 cc = c.getCenter();
                int x = (int)Math.ceil((cc.x - r) * scale);
                int y = (int)Math.ceil((cc.y - r) * scale);
                int w = (int)Math.ceil(r * 2 * scale);
                // let's use an image instead
                g.drawImage(this.image, x, y, w, w, null);
            }
        } else {
            // default rendering
            super.renderFixture(g, scale, fixture, color);
        }
    }
}
