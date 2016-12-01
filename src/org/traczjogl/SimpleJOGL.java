package org.traczjogl;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.Scanner;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * GWatek.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel)
 * <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class SimpleJOGL implements GLEventListener {

    //statyczne pola okre?laj?ce rotacj? wok?? osi X i Y
    private static float xrot = 0.0f, yrot = 0.0f;
    static Koparka koparka;
    static BufferedImage image1 = null, image2 = null;
    static Texture t1 = null, t2 = null;

    public static float ambientLight[] = {0.3f, 0.3f, 0.3f, 1.0f};//swiat?o otaczaj?ce
    public static float diffuseLight[] = {0.7f, 0.7f, 0.7f, 1.0f};//?wiat?o rozproszone
    public static float specular[] = {1.0f, 1.0f, 1.0f, 1.0f}; //?wiat?o odbite
    public static float lightPos[] = {0.0f, 150.0f, 150.0f, 1.0f};//pozycja ?wiat?a
    public static float lightPos2[] = {0.0f, 150.0f, -150.0f, 1.0f};
    public static float direction[] = {0, 0, 0};

    public static void main(String[] args) {
        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();

        canvas.addGLEventListener(new SimpleJOGL());
        frame.add(canvas);
        frame.setSize(640, 480);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });

        //Obs?uga klawiszy strza?ek
        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    xrot -= 3.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    xrot += 3.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    yrot += 3.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    yrot -= 3.0f;
                }
                if (e.getKeyChar() == 'q') {
                    ambientLight = new float[]{ambientLight[0] - 0.1f, ambientLight[0] - 0.1f, ambientLight[0] - 0.1f, 1.0f};
                }
                if (e.getKeyChar() == 'w') {
                    ambientLight = new float[]{ambientLight[0] + 0.1f, ambientLight[0] + 0.1f, ambientLight[0] + 0.1f, 1.0f};
                }
                if (e.getKeyChar() == 'a') {
                    diffuseLight = new float[]{diffuseLight[0] - 0.1f, diffuseLight[0] - 0.1f, diffuseLight[0] - 0.1f, diffuseLight[0] - 0.1f};
                }
                if (e.getKeyChar() == 's') {
                    diffuseLight = new float[]{diffuseLight[0] + 0.1f, diffuseLight[0] + 0.1f, diffuseLight[0] + 0.1f, diffuseLight[0] + 0.1f};
                }
                if (e.getKeyChar() == 'z') {
                    specular = new float[]{specular[0] - 0.1f, specular[0] - 0.1f, specular[0] - 0.1f, specular[0] - 0.1f};
                }
                if (e.getKeyChar() == 'x') {
                    specular = new float[]{specular[0] + 0.1f, specular[0] + 0.1f, specular[0] + 0.1f, specular[0] + 0.1f};
                }
                if (e.getKeyChar() == 'k') {
                    lightPos = new float[]{lightPos[0] - 0.1f, lightPos[0] - 0.1f, lightPos[0] - 0.1f, lightPos[0] - 0.1f};
                }
                if (e.getKeyChar() == 'l') {
                    lightPos = new float[]{lightPos[0] + 0.1f, lightPos[0] + 0.1f, lightPos[0] + 0.1f, lightPos[0] + 0.1f};
                }
                
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }

        });

        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.

        //wy??czenie wewn?trzych stron prymityw?w
        gl.glEnable(GL.GL_CULL_FACE);

        //(czwarty parametr okre?la odleg?o?? ?r?d?a:
        //0.0f-niesko?czona; 1.0f-okre?lona przez pozosta?e parametry)
        gl.glEnable(GL.GL_LIGHTING); //uaktywnienie o?wietlenia
        //ustawienie parametr?w ?r?d?a ?wiat?a nr. 0
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0); //swiat?o otaczaj?ce
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0); //?wiat?o rozproszone
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular, 0); //?wiat?o odbite
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0); //pozycja ?wiat?a
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPOT_DIRECTION, direction, 0);
        gl.glEnable(GL.GL_LIGHT0); //uaktywnienie ?r?d?a ?wiat?a nr. 0

        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, ambientLight, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, diffuseLight, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, specular, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPos2, 0);
        gl.glEnable(GL.GL_LIGHT1);

        gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie ?ledzenia kolor?w
        //kolory b?d? ustalane za pomoc? glColor
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        //Ustawienie jasno?ci i odblaskowo?ci obiekt?w
        float specref[] = {1.0f, 1.0f, 1.0f, 1.0f}; //parametry odblaskowo?ci
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, specref, 0);

        gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 128);

        gl.glEnable(GL.GL_DEPTH_TEST);
        // Setup the drawing area and shading mode
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.

        try {
            image1 = ImageIO.read(getClass().getResourceAsStream("/pokemon.jpg"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/android.jpg"));
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(null, exc.toString());
            return;
        }

        t1 = TextureIO.newTexture(image1, false);
        t2 = TextureIO.newTexture(image2, false);

        gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_BLEND | GL.GL_MODULATE);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
        gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(50.0f, h, 1.0, 200.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

    }

    public void createShape(GL gl, float px, float py, float pz, float size, float odbicie) {
        float kat, x, y;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(px, py, pz); //?rodek
        if (odbicie == -1) {
            for (kat = 0.0f; kat > (-2.0f * Math.PI);
                    kat -= (Math.PI / 32.0f)) {
                x = size * (float) Math.sin(kat);
                y = size * (float) Math.cos(kat);
                gl.glVertex3f(x, py, y); //kolejne punkty
            }
        } else {
            for (kat = 0.0f; kat < (2.0f * Math.PI);
                    kat += (Math.PI / 32.0f)) {
                x = size * (float) Math.sin(kat);
                y = size * (float) Math.cos(kat);
                gl.glNormal3f(x, 0.0f, 0.0f);
                gl.glVertex3f(x, py, y); //kolejne punkty
            }
        }
        gl.glEnd();

    }

    public void createTriangle(GL gl, float x1, float y1, float x2, float y2, float x3, float y3, float z) {
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glVertex3f(x1, y1, z);
        gl.glVertex3f(x2, y2, z);
        gl.glVertex3f(x3, y3, z);
        gl.glEnd();
    }

    public void createCone(GL gl, float px, float py, float pz, float size) {
        float kat, x, y;

        float xx, yy, katt;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glColor3f(1.0f, 0.0f, 0.0f);

        gl.glVertex3f(0.0f, 2.0f, 0.0f); //?rodek
        for (katt = 0.0f; katt < (2.0f * Math.PI);
                katt += (Math.PI / 32.0f)) {
            xx = 1.0f * (float) Math.sin(katt);
            yy = 1.0f * (float) Math.cos(katt);
            gl.glVertex3f(xx, -2.0f, yy); //kolejne punkty
        }
        gl.glEnd();

        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(px, py, pz); //?rodek
        for (kat = 0.0f; kat < (2.0f * Math.PI);
                kat += (Math.PI / 32.0f)) {
            x = size * (float) Math.sin(kat);
            y = size * (float) Math.cos(kat);
            gl.glVertex3f(x, -py, y); //kolejne punkty
        }
        gl.glEnd();
    }

    public void sierpin(GL gl, float x1, float y1, float x2, float y2, float x3, float y3, int depth) {
        float x12 = (x1 + x2) / 2.0f, y12 = (y1 + y2) / 2.0f,
                x13 = (x1 + x3) / 2.0f, y13 = (y1 + y3) / 2.0f,
                x23 = (x2 + x3) / 2.0f, y23 = (y2 + y3) / 2.0f;

        Random rd = new Random();
        float c1 = rd.nextFloat();
        float c2 = rd.nextFloat();
        float c3 = rd.nextFloat();
        if (depth == 1) {
            gl.glBegin(GL.GL_TRIANGLES);
            gl.glColor3f(1 / depth, 1 / depth, 1 / depth);
            gl.glVertex3f(x1, y1, 5.0f);
            gl.glVertex3f(x2, y2, 5.0f);
            gl.glVertex3f(x3, y3, 5.0f);
            gl.glEnd();
        } else {
            sierpin(gl, x1, y1, x12, y12, x13, y13, depth - 1);
        }
    }

    private float[] WyznaczNormalna(float[] punkty, int ind1, int ind2, int ind3) {
        float[] norm = new float[3];
        float[] wektor0 = new float[3];
        float[] wektor1 = new float[3];

        for (int i = 0; i < 3; i++) {
            wektor0[i] = punkty[i + ind1] - punkty[i + ind2];
            wektor1[i] = punkty[i + ind2] - punkty[i + ind3];
        }

        norm[0] = wektor0[1] * wektor1[2] - wektor0[2] * wektor1[1];
        norm[1] = wektor0[2] * wektor1[0] - wektor0[0] * wektor1[2];
        norm[2] = wektor0[0] * wektor1[1] - wektor0[1] * wektor1[0];
        float d
                = (float) Math.sqrt((norm[0] * norm[0]) + (norm[1] * norm[1]) + (norm[2] * norm[2]));
        if (d == 0.0f) {
            d = 1.0f;
        }
        norm[0] /= d;
        norm[1] /= d;
        norm[2] /= d;

        return norm;
    }

     public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuni?cie o 6 jednostek
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wokó? osi X
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wokó? osi Y


        // KO?O 1
        float x, y, x2, y2, kat, kat2, kat3;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //?rodek
        for (kat = 0.0f; kat < (2.0f * Math.PI);
                kat += (Math.PI / 32.0f)) {
            x = 1.0f * (float) Math.sin(kat);
            y = 1.0f * (float) Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f); //kolejne punkty
        }
        gl.glEnd();
        // KO?O 2
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(0.0f, 0.0f, 2.0f); //?rodek
        for (kat2 = (float) (2.0f * Math.PI); kat2 > 0.0f;
                kat2 -= (Math.PI / 32.0f)) {
            x = 1.0f * (float) Math.sin(kat2);
            y = 1.0f * (float) Math.cos(kat2);
            gl.glVertex3f(x, y, 2.0f); //kolejne punkty
            
        }
        gl.glEnd();
        // PROSTOK?TY
        gl.glBegin(GL.GL_QUAD_STRIP);
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        for (kat3 = (float) (2.0f * Math.PI); kat3 > 0.0f;
                kat3 -= (Math.PI / 32.0f)) {
            x = 1.0f * (float) Math.sin(kat3);
            y = 1.0f * (float) Math.cos(kat3);
            gl.glTexCoord2f(kat3/7, 0.0f);
            gl.glVertex3f(x, y, 2.0f);
            gl.glTexCoord2f(kat3/7, 1.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}
