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

/**
 * SimpleJOGL.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel)
 * <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class SimpleJOGL implements GLEventListener {

    //statyczne pola okreœlaj¹ce rotacjê wokó³ osi X i Y
    private static float xrot = 0.0f, yrot = 0.0f;
    static Koparka koparka;

    public static float ambientLight[] = {0.3f, 0.3f, 0.3f, 1.0f};//swiat?o otaczajšce
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

        //Obs³uga klawiszy strza³ek
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
                //ramie1 
                if (e.getKeyChar() == 'b') { //53 gora -45 dol
                    if (koparka.kat1 < 53.0f && koparka.kat1 >= -45.0f) {
                    koparka.kat1 += 0.5f;
                }
                }

                if (e.getKeyChar() == 'n') {
                     if (koparka.kat1 <= 53.0f && koparka.kat1 > -45.0f) {
                    koparka.kat1 -= 0.5f;
                     }
                }
                //ramie2  45 gora 103
                if (e.getKeyChar() == 'g') {
                     if (koparka.kat2 < 45.0f && koparka.kat2 >= -103.0f) {
                    koparka.kat2 += 0.5f;
                     }
                }   
                
                if (e.getKeyChar() == 'h') {
                    if (koparka.kat2 <= 45.0f && koparka.kat2 > -103.0f) {
                    koparka.kat2 -= 0.5f;
                    }
                }
                //lyzka 25 -120
                if (e.getKeyChar() == 't') {
                     if (koparka.kat3 < 25.0f && koparka.kat3 >= -120.0f) {
                    koparka.kat3 += 0.5f;
                     }
                }
                if (e.getKeyChar() == 'y') {
                      if (koparka.kat3 <= 25.0f && koparka.kat3 > -120.0f) {
                    koparka.kat3 -= 0.5f;
                      }
                }
                if (e.getKeyChar() == 'u') {
                    if (koparka.boki < 45.0f && koparka.boki >= -45.0f) {
                        koparka.boki += 0.5f;
                    }
                }
                if (e.getKeyChar() == 'i') {
                    if (koparka.boki <= 45.0f && koparka.boki > -45.0f) {
                        koparka.boki -= 0.5f;
                    }
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

        //wy³¹czenie wewnêtrzych stron prymitywów
        gl.glEnable(GL.GL_CULL_FACE);

        //(czwarty parametr okreœla odleg³oœæ Ÿród³a:
        //0.0f-nieskoñczona; 1.0f-okreœlona przez pozosta³e parametry)
        gl.glEnable(GL.GL_LIGHTING); //uaktywnienie oœwietlenia
        //ustawienie parametrów Ÿród³a œwiat³a nr. 0
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0); //swiat³o otaczaj¹ce
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0); //œwiat³o rozproszone
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular, 0); //œwiat³o odbite
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0); //pozycja œwiat³a
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPOT_DIRECTION, direction, 0);
        gl.glEnable(GL.GL_LIGHT0); //uaktywnienie Ÿród³a œwiat³a nr. 0

        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, ambientLight, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, diffuseLight, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, specular, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPos2, 0);
        gl.glEnable(GL.GL_LIGHT1);

        gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie œledzenia kolorów
        //kolory bêd¹ ustalane za pomoc¹ glColor
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        //Ustawienie jasnoœci i odblaskowoœci obiektów
        float specref[] = {1.0f, 1.0f, 1.0f, 1.0f}; //parametry odblaskowoœci
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, specref, 0);

        gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 128);

        gl.glEnable(GL.GL_DEPTH_TEST);
        // Setup the drawing area and shading mode
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        
        koparka = new Koparka();
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
        glu.gluPerspective(100.0f, h, 1.0, 200.0);
        //gl.glViewport(0, 0, width/2, height/2);
        /*float ilor=0;
        if (width <= height) {
            ilor = height / width;
            gl.glOrtho(-10.0f, 10.0f, -10.0f * ilor, 10.0f * ilor, -10.0f, 10.0f);
        } else {
            ilor = width / height;
            gl.glOrtho(-10.0f * ilor, 10.0f * ilor, -10.0f, 10.0f, -10.0f, 10.0f);
        }*/
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
       

    }

    public void createShape(GL gl, float px, float py, float pz, float size, float odbicie) {
        float kat, x, y;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(px, py, pz); //œrodek
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

    void walec(GL gl) {
//wywo³ujemy automatyczne normalizowanie normalnych
//bo operacja skalowania je zniekszta³ci
        gl.glEnable(GL.GL_NORMALIZE);
        float x, y, kat;
        gl.glBegin(GL.GL_QUAD_STRIP);
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glNormal3f((float) Math.sin(kat), (float) Math.cos(kat), 0.0f);
            gl.glVertex3f(x, y, -1.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        x = y = kat = 0.0f;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, -1.0f); //srodek kola
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glVertex3f(x, y, -1.0f);
        }
        gl.glEnd();
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        x = y = kat = 0.0f;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
        for (kat = 2.0f * (float) Math.PI; kat > 0.0f; kat -= (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
    }

    void stozek(GL gl) {
//wywo³ujemy automatyczne normalizowanie normalnych
        gl.glEnable(GL.GL_NORMALIZE);
        float x, y, kat;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(0.0f, 0.0f, -2.0f); //wierzcholek stozka
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = (float) Math.sin(kat);
            y = (float) Math.cos(kat);
            gl.glNormal3f((float) Math.sin(kat), (float) Math.cos(kat), -2.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
        for (kat = 2.0f * (float) Math.PI; kat > 0.0f; kat -= (Math.PI / 32.0f)) {
            x = (float) Math.sin(kat);
            y = (float) Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
    }

    void choinka(GL gl) {
        gl.glPushMatrix();
        gl.glColor3f(0.139f, 0.061f, 0.019f);
        gl.glScalef(0.4f, 0.4f, 0.4f);
        walec(gl);
        gl.glTranslatef(0.0f, 0.0f, -0.5f);
        gl.glScalef(2.0f, 2.0f, 1.5f);
        gl.glColor3f(0f, 0.102f, 0.051f);
        stozek(gl);
        gl.glTranslatef(0.0f, 0.0f, -1.1f);
        gl.glScalef(0.7f, 0.7f, 0.7f);
        stozek(gl);
        gl.glTranslatef(0.0f, 0.0f, -1.1f);
        gl.glScalef(0.7f, 0.7f, 0.7f);
        stozek(gl);
        gl.glEnd();
        gl.glPopMatrix();
    }

    public void display(GLAutoDrawable drawable) {
//Tworzenie obiektu
        GL gl = drawable.getGL();
//Czyszczenie przestrzeni 3D przed utworzeniem kolejnej klatki
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        //Resetowanie macierzy transformacji
        gl.glLoadIdentity();

        gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuniêcie o 6 jednostek
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wokó³ osi X
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wokó³ osi Y
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0); //swiat³o otaczaj¹ce
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0); //œwiat³o rozproszone
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular, 0); //œwiat³o odbite

        /*float x, y, kat;
         gl.glBegin(GL.GL_QUAD_STRIP);
         //gl.glVertex3f(0.0f,0.0f,-6.0f); //?rodek
         gl.glColor3f(1.0f, 1.0f, 0.0f);
         for (kat = 0.0f; kat < (2.0f * Math.PI);
         kat += (Math.PI / 32.0f)) {
         x = 1.0f * (float) Math.sin(kat);
         y = 1.0f * (float) Math.cos(kat);
         gl.glNormal3f(x, 0.0f, 0.0f);
         gl.glVertex3f(x, 2.0f, y);
         gl.glVertex3f(x, -2.0f, y);//kolejne punkty
         }
         gl.glEnd();
         gl.glBegin(GL.GL_TRIANGLE_FAN);
         gl.glColor3f(1.0f, 0.0f, 0.0f);
         createShape(gl, 0, 2, 0, 1, 1);
         gl.glColor3f(0.0f, 1.0f, 0.0f);
         createShape(gl, 0, -2, 0, 1, -1);
         gl.glBegin(GL.GL_QUADS);
         gl.glColor3f(1.0f, 0.0f, 1.0f);
         gl.glVertex3f(-1.0f, -1.0f, 1.0f);
         gl.glVertex3f(-1.0f, -1.0f, -1.0f);
         gl.glVertex3f(1.0f, -1.0f, -1.0f);
         gl.glVertex3f(1.0f, -1.0f, 1.0f);
         gl.glEnd();
         gl.glTranslatef(0.0f, 0.0f, -3.0f); //przesuniêcie o 6 jednostek
         gl.glBegin(GL.GL_TRIANGLES);
         float[] scianka1 = {-1.0f, -1.0f, 1.0f, //wpó?rz?dne pierwszego punktu
         1.0f, -1.0f, 1.0f, //wspó?rz?dne drugiego punktu
         0.0f, 1.0f, 0.0f}; //wspó?rz?dne trzeciego punktu
         float[] normalna1 = WyznaczNormalna(scianka1, 0, 3, 6);
         gl.glColor3f(1.0f, 0.0f, 1.0f);
         gl.glNormal3fv(normalna1, 0);
         gl.glVertex3fv(scianka1, 0);
         gl.glVertex3fv(scianka1, 3);
         gl.glVertex3fv(scianka1, 6);
         float[] scianka2 = {1.0f, -1.0f, -1.0f, //wpó?rz?dne pierwszego punktu
         -1.0f, -1.0f, -1.0f, //wspó?rz?dne drugiego punktu
         0.0f, 1.0f, 0.0f}; //wspó?rz?dne trzeciego punktu
         float[] normalna2 = WyznaczNormalna(scianka2, 0, 3, 6);
         gl.glNormal3fv(normalna2, 0);
         gl.glVertex3fv(scianka2, 0); //wspó?rz?dne 1-go punktu zaczynaj? si? od indeksu 0
         gl.glVertex3fv(scianka2, 3); //wspó?rz?dne 2-go punktu zaczynaj? si? od indeksu 3
         gl.glVertex3fv(scianka2, 6); //wspó?rz?dne 3-go punktu zaczynaj? si? od indeksu 6
         float[] scianka3 = {-1.0f, -1.0f, -1.0f, //wpó?rz?dne pierwszego punktu
         -1.0f, -1.0f, 1.0f, //wspó?rz?dne drugiego punktu
         0.0f, 1.0f, 0.0f}; //wspó?rz?dne trzeciego punktu
         float[] normalna3 = WyznaczNormalna(scianka3, 0, 3, 6);
         gl.glNormal3fv(normalna3, 0);
         gl.glVertex3fv(scianka3, 0); //wspó?rz?dne 1-go punktu zaczynaj? si? od indeksu 0
         gl.glVertex3fv(scianka3, 3); //wspó?rz?dne 2-go punktu zaczynaj? si? od indeksu 3
         gl.glVertex3fv(scianka3, 6); //wspó?rz?dne 3-go punktu zaczynaj? si? od indeksu 6
         float[] scianka4 = {1.0f, -1.0f, 1.0f,
         1.0f, -1.0f, -1.0f, //wspó?rz?dne drugiego punktu
         0.0f, 1.0f, 0.0f}; //wspó?rz?dne trzeciego punktu
         float[] normalna4 = WyznaczNormalna(scianka4, 0, 3, 6);
         gl.glNormal3fv(normalna4, 0);
         gl.glVertex3fv(scianka4, 0); //wspó?rz?dne 1-go punktu zaczynaj? si? od indeksu 0
         gl.glVertex3fv(scianka4, 3); //wspó?rz?dne 2-go punktu zaczynaj? si? od indeksu 3
         gl.glVertex3fv(scianka4, 6); //wspó?rz?dne 3-go punktu zaczynaj? si? od indeksu 6
        
        
         */
        koparka.Rysuj(gl);
        koparka.KopanieAutomat(0.03f);

        //Wykonanie wszystkich operacji znajduj¹cych siê w buforze
        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}