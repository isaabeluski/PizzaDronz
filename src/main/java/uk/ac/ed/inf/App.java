package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Hello world!
 *
 */
 public class App
{
    public static void main( String[] args ) throws MalformedURLException {
        /**
        LngLat appleton = new LngLat(-3.1869,55.9445);
        boolean test = appleton.inCentralArea();
        System.out.println(test);

        System.out.println("-----------------------------------");

        LngLat corner1 = new LngLat(-3.192473, 55.946233);
        boolean testCorner1 = corner1.inCentralArea();
        System.out.println(testCorner1);

        System.out.println("-----------------------------------");

        LngLat inLIne = new LngLat(-3.184319, 55.944425);
        boolean testLine = inLIne.inCentralArea();
        System.out.println(testLine);

        System.out.println("-----------------------------------");

        LngLat point2 = new LngLat(-3.1869,55.9445);
        boolean test2 = point2.inCentralArea();
        System.out.println(test2);
        **/


        /**
        LngLat inside = new LngLat(4,5);
        System.out.println(inside.inCentralArea());

        System.out.println("-----------------------------------");

        LngLat onEdge = new LngLat(5.5,5);
        System.out.println(onEdge.inCentralArea());

        System.out.println("-----------------------------------");

        LngLat insideChocaConEsquina = new LngLat(4,6);
        System.out.println(insideChocaConEsquina.inCentralArea());

        System.out.println("-----------------------------------");

        LngLat esquinaFuera = new LngLat(1,7);
        System.out.println(esquinaFuera.inCentralArea());

        System.out.println("-----------------------------------");

        LngLat esquina = new LngLat(6,6);
        System.out.println(esquina.inCentralArea());

         **/

        //LngLat point = new LngLat(-3.198, 55.946233);
        //System.out.println(point.inCentralArea());

        /**
        var position = new LngLat(11,8);
        //LngLat newPosition = position.nextPosition(null);



        for (int y = 13; y >= -2; y--){
            for (int x = -8; x <= 17 ; x++) {
                String mes;
                if (new LngLat(x,y).inCentralArea()){
                    if (x >= 0 && x < 10) {
                        if (y >= 0 && y < 10) {
                            mes = "0" + x + ",0" + y + "  ";
                        }
                        else mes = "0" + x + "," + y + "  ";
                    }
                    else {
                        if (y >= 0 && y < 10) {
                            mes = x + ",0" + y + "  ";
                        }
                        else mes = x + "," + y + "  ";
                    }
                    System.out.print(mes);
                }
                else {
                    System.out.print("-----  ");
                }
            }
            System.out.println();
        }



        System.out.println(position.inCentralArea());
        **/
        URL baseUrl = new URL("https://ilp-rest.azurewebsites.net/");

        Restaurant[] restaurants = Restaurant.getRestaurantFromRestServer(baseUrl);

        System.out.println(restaurants);

    }
}
