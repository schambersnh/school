/**
 * QuadTreeRake.java
 * 
 * When a particle leaves, or expires it is added to the dead list stored here,
 * and then restart and used again. It's to inefficent to deallocate and allocate
 *  objects every time it's needed to do the number involved 
 */

//package FlowQuad;

import java.awt.event.ActionEvent;
import java.util.Random;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Timer;

//this rake is exclusively a quad tree rake, 
//   all non quad tree references have been removed

public class QuadTreeRake 
{
    private Timer ticks;
    private VectorField field;
    private int count = 100;
    private int frequency;
    public  int numParticles = 1000;
    private int lifetime = 100;
    private int minLife  = lifetime / 2;
    public ArrayList<Particle> deadParticles;

    public QuadTreeRake( VectorField parent, int delay, int nParticles ) 
    {
       field = parent;
       frequency = delay;
       numParticles = nParticles;
       deadParticles = new ArrayList<Particle>( numParticles );
       initalParticleBurst( numParticles );
       ticks = new javax.swing.Timer( frequency, 
          new ActionListener() 
          {
             public void actionPerformed( ActionEvent e ) 
             {
                field.newFrame();
                emit();
             }
          } );
       ticks.start();
    }

    //-------------------------- restart() -----------------------------
    /**
     * restart the initial display; called at construction time and
     * whenever a major parameter change takes place (# particles, vectorScale)
     */
    public void restart()
    {
       field.restart();
       deadParticles = new ArrayList<Particle>( numParticles );
       initalParticleBurst( numParticles );
    }
    public void initalParticleBurst( int size ) 
    {
       /*
        * calculate an increasing templife so that particles don't all start
        * spawning and dying at the same time, makes the trace look inaccurate
        * as well as harsh on the system
        */
       for ( int i = 0; i < size; i++ ) 
       {
          int life = (int)( Math.random() * ( lifetime - minLife ) + minLife );
          int tempLife = (int) ( lifetime * ( i / ((double) size )) );
          if ( tempLife < 1 )
             tempLife = 1;
          Point2D.Float pos = VectorField.randomPoint();
          field.addParticle( new Particle( field, pos, tempLife ));
       }
    }
    //------------------------------------ emit ------------------------------
    /**
     * regenerates dead particles
     * 
     * limit particles lives within the world of object reuse, and just
     * respawns every dead particle
     */
    private void emit() 
    {
       Iterator<Particle> iter = deadParticles.iterator();
       Particle particle;
       while ( iter.hasNext()) 
       {
          particle = iter.next();
          particle.setLifetime( lifetime );
          particle.restart();
          Point2D.Float loc = VectorField.randomPoint();
          particle.setPosition( loc );
          particle.setVelocity( VectorField.getVector( loc ));
          field.addParticle( particle );
          iter.remove();
       }
    }

    //------------------- setDelay( int ) ------------------------------
    /**
     * Change the frame delay
     */
    public void setDelay( int delay ) 
    {
        this.frequency = delay;
        ticks.setDelay( delay );
        ticks.restart();
        restart();
    }

    //------------------- setNumParticles( int ) ------------------------------
    /**
     * Change the number of particles
     */
    public void setNumParticles( int num ) 
    {
        this.numParticles = num;
        restart();
    }

    //------------------- addDeadparticle( Particle ) ------------------------------
    /**
     * put a dead particle in to deadParticles list (for later re-use
     */
    public void addDeadParticle( Particle particle ) 
    {
        deadParticles.add( particle );
    }

    //------------------- setLifetime( int ) ------------------------------
    /**
     * Change the maximum lifetime of particles
     */
    public void setLifetime( int newLifetime ) 
    {
        lifetime = newLifetime;
        minLife = lifetime / 2;
        restart();
    }
}
