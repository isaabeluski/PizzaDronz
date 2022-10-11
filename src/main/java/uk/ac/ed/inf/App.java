package uk.ac.ed.inf;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        LngLat point = new LngLat(-3.1869,55.9445);
        boolean test = point.inCentralArea();
        System.out.println(test);
    }
}
