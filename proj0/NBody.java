public class NBody {
    public static double readRadius(String input) {
        In in = new In(input);
        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String input) {
        In in = new In(input);
        int numberOfPlanets = in.readInt();
        in.readDouble();

        Planet[] planets = new Planet[numberOfPlanets];

        int i = 0;
        while (i < numberOfPlanets) {
            double xxPos = in.readDouble();
            double yyPos = in.readDouble();
            double xxVel = in.readDouble();
            double yyVel = in.readDouble();
            double mass = in.readDouble();
            String image = in.readString();

            planets[i] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, image);
            i++;
        }

        return planets;
    }

    public static void main(String[] args) {

        double T, dt;

        try {
            T = Double.parseDouble(args[0]);
            dt = Double.parseDouble(args[1]);
        } catch (Exception e) {
            throw new NumberFormatException("Unable to parse input to double. Please input valid double");
        }

        String filename = args[2];

        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);

        // double time = 0.0;

        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius, radius);
		
		

        for (double time = 0.0; time < T; time += dt) {
            StdDraw.clear();
            StdDraw.picture(0, 0, "images/starfield.jpg");

            Double[] xForces = new Double[5];
            Double[] yForces = new Double[5];
            
            for (int i = 0; i < planets.length; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }

            for (int i = 0; i < planets.length; i++) {
                planets[i].draw();
                planets[i].update(dt, xForces[i], yForces[i]);
            }
    
            StdDraw.show();
        }

        

        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                        planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                        planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
        }
        
        
    }
}
